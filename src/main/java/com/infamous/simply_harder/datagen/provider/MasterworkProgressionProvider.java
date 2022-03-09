package com.infamous.simply_harder.datagen.provider;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.custom.data.MasterworkProgression;
import com.infamous.simply_harder.datagen.ModItemTagsProvider;
import com.infamous.simply_harder.datagen.builder.MasterworkProgressionBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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

public class MasterworkProgressionProvider implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    public static final ResourceLocation BOOTS = new ResourceLocation(SimplyHarder.MOD_ID, "boots");
    public static final ResourceLocation CHESTPLATE = new ResourceLocation(SimplyHarder.MOD_ID, "chestplate");
    public static final ResourceLocation HELMET = new ResourceLocation(SimplyHarder.MOD_ID, "helmet");
    public static final ResourceLocation LEGGINGS = new ResourceLocation(SimplyHarder.MOD_ID, "leggings");
    public static final ResourceLocation MELEE_WEAPON = new ResourceLocation(SimplyHarder.MOD_ID, "melee_weapon");
    protected final DataGenerator generator;

    public MasterworkProgressionProvider(DataGenerator dataGenerator) {
        this.generator = dataGenerator;
    }

    @Override
    public void run(HashCache hashCache) throws IOException {
        Path path = this.generator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();
        buildMasterworkProgressions((mwp) -> {
            ResourceLocation id = mwp.getId();
            if (!set.add(id)) {
                throw new IllegalStateException("Duplicate masterwork progression " + id);
            } else {
                saveMasterworkProgression(hashCache, mwp.toJson(), path.resolve("data/" + id.getNamespace() + "/masterwork_progressions/" + id.getPath() + ".json"));
            }
        });
    }

    private static void saveMasterworkProgression(HashCache hashCache, JsonObject serialized, Path path) {
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
            LOGGER.error("Couldn't save masterwork progression {}", path, ioexception);
        }

    }

    protected void buildMasterworkProgressions(Consumer<MasterworkProgression> onFinished) {
        AttributeModifier.Operation[] operations = {AttributeModifier.Operation.ADDITION, AttributeModifier.Operation.ADDITION};

        this.buildMeleeWeapon(onFinished, operations);

        Attribute[] armorAttributes = {Attributes.ARMOR, Attributes.ARMOR_TOUGHNESS};
        double[] armorAmounts = {0.2D, 0.1D};

        this.buildBoots(onFinished, operations, armorAttributes, armorAmounts);
        this.buildChestplate(onFinished, operations, armorAttributes, armorAmounts);
        this.buildHelmet(onFinished, operations, armorAttributes, armorAmounts);
        this.buildLeggings(onFinished, operations, armorAttributes, armorAmounts);
    }

    private void buildLeggings(Consumer<MasterworkProgression> onFinished, AttributeModifier.Operation[] operations, Attribute[] armorAttributes, double[] armorAmounts) {
        double leggingsFactor = 1.0D;
        double[] leggingsAmounts = {armorAmounts[0] * leggingsFactor, armorAmounts[1] * leggingsFactor};
        UUID leggingsUUID = UUID.fromString("c09c05bf-a7f4-4cbd-ade8-ea57a547b6be");
        MasterworkProgressionBuilder.progression(LEGGINGS)
                .addDefaultTiers(
                        new EquipmentSlot[]{EquipmentSlot.LEGS},
                        armorAttributes,
                        new UUID[]{leggingsUUID, leggingsUUID},
                        leggingsAmounts,
                        operations)
                .save(onFinished);
    }

    private void buildHelmet(Consumer<MasterworkProgression> onFinished, AttributeModifier.Operation[] operations, Attribute[] armorAttributes, double[] armorAmounts) {
        double helmetFactor = 0.5D;
        double[] helmetAmounts = {armorAmounts[0] * helmetFactor, armorAmounts[1] * helmetFactor};
        UUID helmetUUID = UUID.fromString("57aa580c-6db3-473f-8b17-6197f7419268");
        MasterworkProgressionBuilder.progression(HELMET)
                .addDefaultTiers(
                        new EquipmentSlot[]{EquipmentSlot.HEAD},
                        armorAttributes,
                        new UUID[]{helmetUUID, helmetUUID},
                        helmetAmounts,
                        operations)
                .save(onFinished);
    }

    private void buildChestplate(Consumer<MasterworkProgression> onFinished, AttributeModifier.Operation[] operations, Attribute[] armorAttributes, double[] armorAmounts) {
        double chestplateFactor = 1.0D;
        double[] chestplateAmounts = {armorAmounts[0] * chestplateFactor, armorAmounts[1] * chestplateFactor};
        UUID chestplateUUID = UUID.fromString("dfb333b7-884d-4496-8aa7-71965116bd96");
        MasterworkProgressionBuilder.progression(CHESTPLATE)
                .addDefaultTiers(
                        new EquipmentSlot[]{EquipmentSlot.CHEST},
                        armorAttributes,
                        new UUID[]{chestplateUUID, chestplateUUID},
                        chestplateAmounts,
                        operations)
                .save(onFinished);
    }

    private void buildBoots(Consumer<MasterworkProgression> onFinished, AttributeModifier.Operation[] operations, Attribute[] armorAttributes, double[] armorAmounts) {
        double bootsFactor = 0.5D;
        double[] bootsAmounts = {armorAmounts[0] * bootsFactor, armorAmounts[1] * bootsFactor};
        UUID bootsUUID = UUID.fromString("38aa5cf6-5cd7-4dc9-be3e-0bfdf7c9cf8c");
        MasterworkProgressionBuilder.progression(BOOTS)
                .addDefaultTiers(
                        new EquipmentSlot[]{EquipmentSlot.FEET},
                        armorAttributes,
                        new UUID[]{bootsUUID, bootsUUID},
                        bootsAmounts,
                        operations)
                .save(onFinished);
    }

    private void buildMeleeWeapon(Consumer<MasterworkProgression> onFinished, AttributeModifier.Operation[] operations) {
        double[] meleeWeaponAmounts = {0.2D, 0.05D};
        UUID meleeWeaponsUUID = UUID.fromString("a2ad69fe-46b6-4d36-8c32-26ca1c3b10cd");
        MasterworkProgressionBuilder.progression(MELEE_WEAPON)
                .addDefaultTiers(
                        new EquipmentSlot[]{EquipmentSlot.MAINHAND},
                        new Attribute[]{Attributes.ATTACK_DAMAGE, Attributes.ATTACK_SPEED},
                        new UUID[]{meleeWeaponsUUID, meleeWeaponsUUID},
                        meleeWeaponAmounts,
                        operations)
                .save(onFinished);
    }

    @Override
    public String getName() {
        return "Masterwork Progressions";
    }
}
