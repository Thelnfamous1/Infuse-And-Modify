package com.infamous.simply_harder.datagen.provider;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.infamous.simply_harder.custom.MerchantProgressionManager;
import com.infamous.simply_harder.custom.data.MerchantProgression;
import com.infamous.simply_harder.custom.loot.SHBuiltInLootTables;
import com.infamous.simply_harder.datagen.builder.MerchantProgressionBuilder;
import com.infamous.simply_harder.registry.SHItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
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
            buildSpecialOffers(offers, profession, level);
            progression.addOffers(offers);
        }
        progression.save(onFinished);
    }

    private void buildSpecialOffers(MerchantOffers offers, VillagerProfession profession, int level) {
        switch (level){
            case 1, 2, 3, 4 -> {
                offers.add(coreForEmeraldsAndItems(getBountyCost(level), level));
                offers.add(modForEmeralds());
            }
            case 5 -> {
                offers.add(moduleForEmeraldsAndCore());
            }
        }
    }

    private ItemStack getBountyCost(int level) {
        switch (level){
            case 1 -> {
                return new ItemStack(Items.ROTTEN_FLESH, 10);
            }
            case 2 -> {
                return new ItemStack(Items.GUNPOWDER, 10);
            }
            case 3 -> {
                return new ItemStack(Items.ENDER_PEARL, 10);
            }
            case 4 -> {
                return new ItemStack(Items.BLAZE_ROD, 10);
            }
            case 5 -> {
                return new ItemStack(Items.GHAST_TEAR, 10);
            }
        }
        return ItemStack.EMPTY;
    }

    private MerchantOffer modForEmeralds() {
        return new MerchantOffer(
                new ItemStack(Items.EMERALD, 40),
                ItemStack.EMPTY,
                new ItemStack(SHItems.GEAR_MOD.get()),
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

    private MerchantOffer coreForEmeraldsAndItems(ItemStack costB, int level) {
        return new MerchantOffer(
                new ItemStack(Items.EMERALD),
                costB,
                new ItemStack(SHItems.ENHANCEMENT_CORE.get()),
                0,
                12,
                level * 4,
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
