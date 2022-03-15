package com.infamous.simply_harder.custom.loot;

import com.google.common.collect.Sets;
import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.custom.data.GearMod;
import com.infamous.simply_harder.custom.data.WrappedAttributeModifierMap;
import com.infamous.simply_harder.datagen.builder.GearModBuilder;
import com.infamous.simply_harder.datagen.provider.ModItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.*;

public class SHBuiltInGearMods {

    private static final Set<GearMod> GEAR_MODS = Sets.newHashSet();
    private static final Set<GearMod> IMMUTABLE_GEAR_MODS = Collections.unmodifiableSet(GEAR_MODS);

    private static final Map<ResourceLocation, GearMod> BOOTS_MODS = buildBootsMods();
    private static final Map<ResourceLocation, GearMod> LEGGINGS_MODS = buildLeggingsMods();
    private static final Map<ResourceLocation, GearMod> CHESTPLATE_MODS = buildChestplateMods();
    private static final Map<ResourceLocation, GearMod> HELMET_MODS = buildHelmetMods();
    private static final Map<ResourceLocation, GearMod> WEAPON_MODS = buildWeaponMods();

    public static final String ATTACK_DAMAGE_PREFIX = "strong";
    public static final String ATTACK_SPEED_PREFIX = "hasty";
    public static final String ATTACK_KNOCKBACK_PREFIX = "knocking";
    public static final String ARMOR_PREFIX = "fortified";
    public static final String ARMOR_TOUGHNESS_PREFIX = "rigid";
    public static final String KNOCKBACK_RESISTANCE_PREFIX = "resolute";
    public static final String MOVEMENT_SPEED_PREFIX = "swift";
    public static final String MAX_HEALTH_PREFIX = "healthy";
    public static final String LUCK_PREFIX = "lucky";

    public static final String BOOTS_SUFFIX = "foot_guards";
    public static final String LEGGINGS_SUFFIX = "leg_guards";
    public static final String CHESTPLATE_SUFFIX = "chest_guard";
    public static final String HELMET_SUFFIX = "head_guard";
    public static final String WEAPON_SUFFIX = "blade";

    public static final UUID BOOTS_UUID = UUID.fromString("37823925-51dc-408f-954f-3e577d7c0a2e");
    public static final UUID LEGGINGS_UUID = UUID.fromString("afd64ff9-2e2e-4b14-a594-21117631bb70");
    public static final UUID CHESTPLATE_UUID = UUID.fromString("1600b3eb-c37d-440b-bfed-fd3484e2d0ae");
    public static final UUID HELMET_UUID = UUID.fromString("2c968932-08a4-4ecc-abbe-50a7371a4cdb");
    public static final UUID WEAPON_UUID = UUID.fromString("9b0f12ac-5f12-4dd8-8eb3-2cd2a088440b");

    private static Map<ResourceLocation, GearMod> buildBootsMods() {
        Map<ResourceLocation, GearMod> mods = new HashMap<>();
        buildDefaultGearModsForSlot(mods, BOOTS_SUFFIX, BOOTS_UUID, EquipmentSlot.FEET, ModItemTagsProvider.BOOTS);
        return mods;
    }

    private static Map<ResourceLocation, GearMod> buildLeggingsMods() {
        Map<ResourceLocation, GearMod> mods = new HashMap<>();
        buildDefaultGearModsForSlot(mods, LEGGINGS_SUFFIX, LEGGINGS_UUID, EquipmentSlot.LEGS, ModItemTagsProvider.LEGGINGS);
        return mods;
    }

    private static Map<ResourceLocation, GearMod> buildChestplateMods() {
        Map<ResourceLocation, GearMod> mods = new HashMap<>();
        buildDefaultGearModsForSlot(mods, CHESTPLATE_SUFFIX, CHESTPLATE_UUID, EquipmentSlot.CHEST, ModItemTagsProvider.CHESTPLATES);
        return mods;
    }

    private static Map<ResourceLocation, GearMod> buildHelmetMods() {
        Map<ResourceLocation, GearMod> mods = new HashMap<>();
        buildDefaultGearModsForSlot(mods, HELMET_SUFFIX, HELMET_UUID, EquipmentSlot.HEAD, ModItemTagsProvider.HELMETS);
        return mods;
    }

    private static Map<ResourceLocation, GearMod> buildWeaponMods() {
        Map<ResourceLocation, GearMod> mods = new HashMap<>();
        buildDefaultGearModsForSlot(mods, WEAPON_SUFFIX, WEAPON_UUID, EquipmentSlot.MAINHAND, ModItemTagsProvider.WEAPONS);
        return mods;
    }

    private static void buildDefaultGearModsForSlot(Map<ResourceLocation, GearMod> mods, String suffix, UUID uuid, EquipmentSlot slot, Tag<Item> tag) {
        buildDefaultGearMod(mods, uuid, new ResourceLocation(SimplyHarder.MOD_ID, ATTACK_DAMAGE_PREFIX + "_" + suffix), Attributes.ATTACK_DAMAGE, 1.0D, AttributeModifier.Operation.ADDITION, slot, tag);
        buildDefaultGearMod(mods, uuid, new ResourceLocation(SimplyHarder.MOD_ID, ATTACK_SPEED_PREFIX + "_" + suffix), Attributes.ATTACK_SPEED, 0.4D, AttributeModifier.Operation.ADDITION, slot, tag);
        buildDefaultGearMod(mods, uuid, new ResourceLocation(SimplyHarder.MOD_ID, ATTACK_KNOCKBACK_PREFIX + "_" + suffix), Attributes.ATTACK_KNOCKBACK, 0.1D, AttributeModifier.Operation.ADDITION, slot, tag);
        buildDefaultGearMod(mods, uuid, new ResourceLocation(SimplyHarder.MOD_ID, ARMOR_PREFIX + "_" + suffix), Attributes.ARMOR, 2.0D, AttributeModifier.Operation.ADDITION, slot, tag);
        buildDefaultGearMod(mods, uuid, new ResourceLocation(SimplyHarder.MOD_ID, ARMOR_TOUGHNESS_PREFIX + "_" + suffix), Attributes.ARMOR_TOUGHNESS, 1.0D, AttributeModifier.Operation.ADDITION, slot, tag);
        buildDefaultGearMod(mods, uuid, new ResourceLocation(SimplyHarder.MOD_ID, KNOCKBACK_RESISTANCE_PREFIX + "_" + suffix), Attributes.KNOCKBACK_RESISTANCE, 0.1D, AttributeModifier.Operation.ADDITION, slot, tag);
        buildDefaultGearMod(mods, uuid, new ResourceLocation(SimplyHarder.MOD_ID, MOVEMENT_SPEED_PREFIX + "_" + suffix), Attributes.MOVEMENT_SPEED, 0.01D, AttributeModifier.Operation.ADDITION, slot, tag);
        buildDefaultGearMod(mods, uuid, new ResourceLocation(SimplyHarder.MOD_ID, MAX_HEALTH_PREFIX + "_" + suffix), Attributes.MAX_HEALTH, 2.0D, AttributeModifier.Operation.ADDITION, slot, tag);
        buildDefaultGearMod(mods, uuid, new ResourceLocation(SimplyHarder.MOD_ID, LUCK_PREFIX + "_" + suffix), Attributes.LUCK, 1.0D, AttributeModifier.Operation.ADDITION, slot, tag);
    }

    private static GearMod buildDefaultGearMod(Map<ResourceLocation, GearMod> mods, UUID uuid, ResourceLocation name, Attribute attribute, double amount, AttributeModifier.Operation operation, EquipmentSlot slot, Tag<Item> tag) {
        WrappedAttributeModifierMap.Builder wrappedAttributeModifiersBuilder = WrappedAttributeModifierMap.Builder.builder();
        wrappedAttributeModifiersBuilder.addAttributeModifier(
                attribute,
                slot,
                new AttributeModifier(uuid, name.toString(), amount, operation));
        GearMod gearMod = GearModBuilder.gearMod(name)
                .installable(Ingredient.of(tag))
                .levelCost(1)
                .levelRefund(1)
                .wrappedAttributeModifiers(wrappedAttributeModifiersBuilder.build())
                .build();
        mods.put(gearMod.id(), gearMod);
        if(GEAR_MODS.add(gearMod)){
            return gearMod;
        } else{
            throw new IllegalArgumentException(gearMod.id() + " is already a registered built-in gear mod");
        }
    }

    public static Set<GearMod> all() {
        return IMMUTABLE_GEAR_MODS;
    }

    public static GearMod getBoots(ResourceLocation location) {
        return BOOTS_MODS.getOrDefault(location, GearMod.UNKNOWN);
    }

    public static GearMod getLeggings(ResourceLocation location) {
        return LEGGINGS_MODS.getOrDefault(location, GearMod.UNKNOWN);
    }

    public static GearMod getChestplate(ResourceLocation location) {
        return CHESTPLATE_MODS.getOrDefault(location, GearMod.UNKNOWN);
    }

    public static GearMod getHelmet(ResourceLocation location) {
        return HELMET_MODS.getOrDefault(location, GearMod.UNKNOWN);
    }

    public static GearMod getWeapon(ResourceLocation location) {
        return WEAPON_MODS.getOrDefault(location, GearMod.UNKNOWN);
    }

    public static Collection<GearMod> boots() {
        return BOOTS_MODS.values();
    }

    public static Collection<GearMod> leggings() {
        return LEGGINGS_MODS.values();
    }

    public static Collection<GearMod> chestplate() {
        return CHESTPLATE_MODS.values();
    }

    public static Collection<GearMod> helmet() {
        return HELMET_MODS.values();
    }

    public static Collection<GearMod> weapon() {
        return WEAPON_MODS.values();
    }
}
