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
        this.buildMeleeWeaponMod(onFinished, UUID.fromString("9b0f12ac-5f12-4dd8-8eb3-2cd2a088440b"), new ResourceLocation(SimplyHarder.MOD_ID, "mighty_edge"), Attributes.ATTACK_DAMAGE, 1, AttributeModifier.Operation.ADDITION);
        this.buildMeleeWeaponMod(onFinished, UUID.fromString("4ba87d0a-18cf-47b0-b4fb-6a4b893bdc5f"), new ResourceLocation(SimplyHarder.MOD_ID, "dexterous_grip"), Attributes.ATTACK_SPEED, -0.4, AttributeModifier.Operation.ADDITION);
        this.buildMeleeWeaponMod(onFinished, UUID.fromString("9715a94d-a2d1-477b-bb56-dbd216ff11fc"), new ResourceLocation(SimplyHarder.MOD_ID, "impactful_body"), Attributes.ATTACK_KNOCKBACK, 0.1, AttributeModifier.Operation.ADDITION);


        this.buildArmorMod(onFinished, UUID.fromString("90485c2e-8a10-4be0-9b82-4ac69461ab27"), new ResourceLocation(SimplyHarder.MOD_ID, "resilient_plate"), Attributes.ARMOR, 2, AttributeModifier.Operation.ADDITION);
        this.buildArmorMod(onFinished, UUID.fromString("5cc258f7-b0c8-499f-a407-1bc94e83986b"), new ResourceLocation(SimplyHarder.MOD_ID, "mitigating_scale"), Attributes.ARMOR_TOUGHNESS, 1, AttributeModifier.Operation.ADDITION);
        this.buildArmorMod(onFinished, UUID.fromString("294cd28b-db30-461b-9e02-c2d7f1f41976"), new ResourceLocation(SimplyHarder.MOD_ID, "dense_frame"), Attributes.KNOCKBACK_RESISTANCE, 0.1, AttributeModifier.Operation.ADDITION);
        this.buildArmorMod(onFinished, UUID.fromString("529b0aa7-f572-4480-89d0-ac3f6d42adae"), new ResourceLocation(SimplyHarder.MOD_ID, "mobile_amplifier"), Attributes.MOVEMENT_SPEED, 0.1, AttributeModifier.Operation.MULTIPLY_BASE);
        this.buildArmorMod(onFinished, UUID.fromString("e98eafd4-2de3-4844-819c-77f3ac5e4ac7"), new ResourceLocation(SimplyHarder.MOD_ID, "resolute_augment"), Attributes.MAX_HEALTH, 0.1, AttributeModifier.Operation.ADDITION);
        this.buildArmorMod(onFinished, UUID.fromString("717968b7-14fb-46fd-935f-8d783271a4fd"), new ResourceLocation(SimplyHarder.MOD_ID, "fortunate_weighting"), Attributes.LUCK, 1, AttributeModifier.Operation.ADDITION);
    }

    private void buildGearMod(Consumer<GearMod> onFinished, UUID uuid, ResourceLocation name, Attribute attribute, double amount, AttributeModifier.Operation operation, EquipmentSlot[] slots, Tags.IOptionalNamedTag<Item> tag) {
        WrappedAttributeModifierMap.Builder wrappedAttributeModifiersBuilder = WrappedAttributeModifierMap.Builder.builder();
        for(EquipmentSlot slot : slots){
            wrappedAttributeModifiersBuilder.addAttributeModifier(
                            attribute,
                            slot,
                            new AttributeModifier(uuid, name.toString(), amount, operation));
        }
            GearModBuilder.gearMod(name)
                    .installable(Ingredient.of(tag))
                    .levelCost(1)
                    .wrappedAttributeModifiers(wrappedAttributeModifiersBuilder.build())
                    .save(onFinished);
    }

    private void buildMeleeWeaponMod(Consumer<GearMod> onFinished, UUID uuid, ResourceLocation name, Attribute attribute, double amount, AttributeModifier.Operation operation) {
        this.buildGearMod(onFinished, uuid, name, attribute, amount, operation, new EquipmentSlot[]{EquipmentSlot.MAINHAND}, ModItemTagsProvider.MELEE_WEAPONS);
    }

    private void buildArmorMod(Consumer<GearMod> onFinished, UUID uuid, ResourceLocation name, Attribute attribute, double amount, AttributeModifier.Operation operation) {
        this.buildGearMod(onFinished, uuid, name, attribute, amount, operation, new EquipmentSlot[]{EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD}, ModItemTagsProvider.ARMORS);
    }

    @Override
    public String getName() {
        return "Gear Mods";
    }
}
