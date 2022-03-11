package com.infamous.simply_harder.datagen.provider;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.custom.data.GearMod;
import com.infamous.simply_harder.custom.data.WrappedAttributeModifierMap;
import com.infamous.simply_harder.datagen.ModItemTagsProvider;
import com.infamous.simply_harder.datagen.builder.GearModBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class GearModProvider implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    protected final DataGenerator generator;

    public GearModProvider(DataGenerator dataGenerator) {
        this.generator = dataGenerator;
    }

    @Override
    public void run(HashCache hashCache) throws IOException {
        Path path = this.generator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();
        buildGearMods((gm) -> {
            ResourceLocation id = gm.id();
            if (!set.add(id)) {
                throw new IllegalStateException("Duplicate gear mod " + id);
            } else {
                saveGearMod(hashCache, gm.toJson(), path.resolve("data/" + id.getNamespace() + "/gear_mods/" + id.getPath() + ".json"));
            }
        });
    }

    private static void saveGearMod(HashCache hashCache, JsonObject serialized, Path path) {
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
            LOGGER.error("Couldn't save gear mod {}", path, ioexception);
        }

    }

    protected void buildGearMods(Consumer<GearMod> onFinished) {
        String weaponSuffix = "blade";
        UUID weaponUUID = UUID.fromString("9b0f12ac-5f12-4dd8-8eb3-2cd2a088440b");
        this.buildDefaultGearModsForSlot(onFinished, weaponSuffix, weaponUUID, EquipmentSlot.MAINHAND, ModItemTagsProvider.WEAPONS);

        String helmetSuffix = "head_guard";
        UUID helmetUUID = UUID.fromString("2c968932-08a4-4ecc-abbe-50a7371a4cdb");
        this.buildDefaultGearModsForSlot(onFinished, helmetSuffix, helmetUUID, EquipmentSlot.HEAD, ModItemTagsProvider.HELMETS);

        String chestplateSuffix = "chest_guard";
        UUID chestplateUUID = UUID.fromString("1600b3eb-c37d-440b-bfed-fd3484e2d0ae");
        this.buildDefaultGearModsForSlot(onFinished, chestplateSuffix, chestplateUUID, EquipmentSlot.CHEST, ModItemTagsProvider.CHESTPLATES);

        String leggingsSuffix = "leg_guards";
        UUID leggingsUUID = UUID.fromString("afd64ff9-2e2e-4b14-a594-21117631bb70");
        this.buildDefaultGearModsForSlot(onFinished, leggingsSuffix, leggingsUUID, EquipmentSlot.LEGS, ModItemTagsProvider.LEGGINGS);

        String bootsSuffix = "foot_guards";
        UUID bootsUUID = UUID.fromString("37823925-51dc-408f-954f-3e577d7c0a2e");
        this.buildDefaultGearModsForSlot(onFinished, bootsSuffix, bootsUUID, EquipmentSlot.FEET, ModItemTagsProvider.BOOTS);
    }

    private void buildDefaultGearModsForSlot(Consumer<GearMod> onFinished, String suffix, UUID uuid, EquipmentSlot slot, Tag<Item> tag) {
        this.buildGearMod(onFinished, uuid, new ResourceLocation(SimplyHarder.MOD_ID, "strong_" + suffix), Attributes.ATTACK_DAMAGE, 1.0D, AttributeModifier.Operation.ADDITION, slot, tag);
        this.buildGearMod(onFinished, uuid, new ResourceLocation(SimplyHarder.MOD_ID, "hasty_" + suffix), Attributes.ATTACK_SPEED, 0.4D, AttributeModifier.Operation.ADDITION, slot, tag);
        this.buildGearMod(onFinished, uuid, new ResourceLocation(SimplyHarder.MOD_ID, "knocking_" + suffix), Attributes.ATTACK_KNOCKBACK, 0.1D, AttributeModifier.Operation.ADDITION, slot, tag);
        this.buildGearMod(onFinished, uuid, new ResourceLocation(SimplyHarder.MOD_ID, "fortified_" + suffix), Attributes.ARMOR, 2.0D, AttributeModifier.Operation.ADDITION, slot, tag);
        this.buildGearMod(onFinished, uuid, new ResourceLocation(SimplyHarder.MOD_ID, "rigid_" + suffix), Attributes.ARMOR_TOUGHNESS, 1.0D, AttributeModifier.Operation.ADDITION, slot, tag);
        this.buildGearMod(onFinished, uuid, new ResourceLocation(SimplyHarder.MOD_ID, "resolute_" + suffix), Attributes.KNOCKBACK_RESISTANCE, 0.1D, AttributeModifier.Operation.ADDITION, slot, tag);
        this.buildGearMod(onFinished, uuid, new ResourceLocation(SimplyHarder.MOD_ID, "swift_" + suffix), Attributes.MOVEMENT_SPEED, 0.01D, AttributeModifier.Operation.ADDITION, slot, tag);
        this.buildGearMod(onFinished, uuid, new ResourceLocation(SimplyHarder.MOD_ID, "healthy_" + suffix), Attributes.MAX_HEALTH, 2.0D, AttributeModifier.Operation.ADDITION, slot, tag);
        this.buildGearMod(onFinished, uuid, new ResourceLocation(SimplyHarder.MOD_ID, "lucky_" + suffix), Attributes.LUCK, 1.0D, AttributeModifier.Operation.ADDITION, slot, tag);
    }

    private void buildGearMod(Consumer<GearMod> onFinished, UUID uuid, ResourceLocation name, Attribute attribute, double amount, AttributeModifier.Operation operation, EquipmentSlot slot, Tag<Item> tag) {
        WrappedAttributeModifierMap.Builder wrappedAttributeModifiersBuilder = WrappedAttributeModifierMap.Builder.builder();
        wrappedAttributeModifiersBuilder.addAttributeModifier(
                            attribute,
                            slot,
                            new AttributeModifier(uuid, name.toString(), amount, operation));
            GearModBuilder.gearMod(name)
                    .installable(Ingredient.of(tag))
                    .levelCost(1)
                    .wrappedAttributeModifiers(wrappedAttributeModifiersBuilder.build())
                    .save(onFinished);
    }

    @Override
    public String getName() {
        return "Gear Mods";
    }
}
