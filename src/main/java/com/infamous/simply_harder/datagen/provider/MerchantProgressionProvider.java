package com.infamous.simply_harder.datagen.provider;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.infamous.simply_harder.custom.MerchantProgressionManager;
import com.infamous.simply_harder.custom.data.GearMod;
import com.infamous.simply_harder.custom.data.MerchantProgression;
import com.infamous.simply_harder.custom.item.GearModItem;
import com.infamous.simply_harder.custom.loot.SHBuiltInGearMods;
import com.infamous.simply_harder.custom.loot.SHBuiltInLootTables;
import com.infamous.simply_harder.datagen.builder.MerchantProgressionBuilder;
import com.infamous.simply_harder.registry.SHItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public class MerchantProgressionProvider implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    public static final String MERCHANT_PROGRESSIONS_NAME = "Merchant Progressions";
    protected final DataGenerator generator;

    public MerchantProgressionProvider(DataGenerator dataGenerator) {
        this.generator = dataGenerator;
    }

    @Override
    public void run(HashCache hashCache) throws IOException {
        Path path = this.generator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();
        buildMerchantProgressions((mp) -> {
            ResourceLocation professionLocation = mp.profession().getRegistryName();
            if (!set.add(professionLocation)) {
                throw new IllegalStateException("Duplicate " + MerchantProgressionManager.MERCHANT_PROGRESSION + " " + professionLocation);
            } else {
                saveMerchantProgressions(hashCache, mp.toJson(), path.resolve("data/" + professionLocation.getNamespace() + "/" + MerchantProgressionManager.MERCHANT_PROGRESSIONS_PATH + "/" + professionLocation.getPath() + ".json"));
            }
        });
    }

    @Override
    public String getName() {
        return MERCHANT_PROGRESSIONS_NAME;
    }

    protected void buildMerchantProgressions(Consumer<MerchantProgression> onFinished) {
        buildProgressionFor(onFinished, VillagerProfession.ARMORER, SHBuiltInLootTables.ARMORER_PROGRESSION_REWARDS);
        buildProgressionFor(onFinished, VillagerProfession.LEATHERWORKER, SHBuiltInLootTables.LEATHERWORKER_PROGRESSION_REWARDS);
        buildProgressionFor(onFinished, VillagerProfession.WEAPONSMITH, SHBuiltInLootTables.WEAPONSMITH_PROGRESSION_REWARDS);
        buildProgressionFor(onFinished, VillagerProfession.TOOLSMITH, SHBuiltInLootTables.TOOLSMITH_PROGRESSION_REWARDS);
    }

    private void buildProgressionFor(Consumer<MerchantProgression> onFinished, VillagerProfession profession, List<ResourceLocation> rewardLocations) {
        MerchantProgressionBuilder progression = MerchantProgressionBuilder.progression(profession);
        for (ResourceLocation rewardLocation : rewardLocations) {
            progression.addReward(rewardLocation);
        }
        for(int level = VillagerData.MIN_VILLAGER_LEVEL; level <= VillagerData.MAX_VILLAGER_LEVEL; level++){
            MerchantOffers offers = new MerchantOffers();
            buildSpecialOffers(profession, offers, level);
            progression.addOffers(offers);
        }
        progression.save(onFinished);
    }

    private void buildSpecialOffers(VillagerProfession profession, MerchantOffers offers, int level) {
        switch (level){
            case 1 -> {
                offers.add(coreForEmeralds());
                offers.add(modForEmeralds(getRandomGearModBySlot(slotForProfession(profession, EquipmentSlot.LEGS))));
            }
            case 2 -> {
                offers.add(modForEmeralds(getRandomGearModBySlot(slotForProfession(profession, EquipmentSlot.FEET))));
            }
            case 3 -> {
                offers.add(modForEmeralds(getRandomGearModBySlot(slotForProfession(profession, EquipmentSlot.HEAD))));
            }
            case 4 -> {
                offers.add(modForEmeralds(getRandomGearModBySlot(slotForProfession(profession, EquipmentSlot.CHEST))));
            }
            case 5 -> {
                offers.add(moduleForEmeraldsAndCore());
            }
        }

    }

    private EquipmentSlot slotForProfession(VillagerProfession profession, EquipmentSlot armorSlot) {
        return profession == VillagerProfession.ARMORER || profession == VillagerProfession.LEATHERWORKER ?
                armorSlot :
                EquipmentSlot.MAINHAND;
    }

    private GearMod getRandomGearModBySlot(EquipmentSlot slot) {
        switch (slot){
            case FEET -> {
                return getRandomGearMod(SHBuiltInGearMods.boots());
            }
            case LEGS -> {
                return getRandomGearMod(SHBuiltInGearMods.leggings());
            }
            case CHEST -> {
                return getRandomGearMod(SHBuiltInGearMods.chestplate());
            }
            case HEAD -> {
                return getRandomGearMod(SHBuiltInGearMods.helmet());
            }
            case MAINHAND -> {
                return getRandomGearMod(SHBuiltInGearMods.weapon());
            }
            default -> {
                return GearMod.UNKNOWN;
            }
        }
    }

    private GearMod getRandomGearMod(Collection<GearMod> mods) {
        return mods
                .stream()
                .skip(new Random().nextInt(mods.size()))
                .findFirst()
                .orElse(GearMod.UNKNOWN);
    }

    private MerchantOffer modForEmeralds(GearMod gearMod) {
        return new MerchantOffer(
                new ItemStack(Items.EMERALD, 40),
                ItemStack.EMPTY,
                GearModItem.createGearMod(gearMod),
                0,
                12,
                40,
                0.05F,
                0);
    }

    private MerchantOffer moduleForEmeraldsAndCore() {
        return new MerchantOffer(
                new ItemStack(Items.EMERALD, 20),
                new ItemStack(SHItems.ENHANCEMENT_CORE.get()),
                new ItemStack(SHItems.UPGRADE_MODULE.get()),
                0,
                12,
                20,
                0.05F,
                0);
    }

    private MerchantOffer coreForEmeralds() {
        return new MerchantOffer(
                new ItemStack(Items.EMERALD),
                ItemStack.EMPTY,
                new ItemStack(SHItems.ENHANCEMENT_CORE.get()),
                0,
                12,
                1,
                0.05F,
                0);
    }

    private static void saveMerchantProgressions(HashCache hashCache, JsonObject serialized, Path path) {
        try {
            String s = GSON.toJson((JsonElement)serialized);
            String s1 = SHA1.hashUnencodedChars(s).toString();
            if (!Objects.equals(hashCache.getHash(path), s1) || !Files.exists(path)) {
                Files.createDirectories(path.getParent());
                BufferedWriter bufferedwriter = Files.newBufferedWriter(path);

                try {
                    bufferedwriter.write(s);
                } catch (Throwable throwable1) {
                    if (bufferedwriter != null) {
                        try {
                            bufferedwriter.close();
                        } catch (Throwable throwable) {
                            throwable1.addSuppressed(throwable);
                        }
                    }

                    throw throwable1;
                }

                if (bufferedwriter != null) {
                    bufferedwriter.close();
                }
            }

            hashCache.putNew(path, s1);
        } catch (IOException ioexception) {
            LOGGER.error("Couldn't save " + MerchantProgressionManager.MERCHANT_PROGRESSION + " {}", path, ioexception);
        }

    }
}
