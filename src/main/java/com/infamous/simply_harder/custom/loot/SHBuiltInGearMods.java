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

    private static Map<ResourceLocation, GearMod> buildBootsMods() {
        Map<ResourceLocation, GearMod> mods = new HashMap<>();
        String bootsSuffix = "foot_guards";
        UUID bootsUUID = UUID.fromString("37823925-51dc-408f-954f-3e577d7c0a2e");
        buildDefaultGearModsForSlot(mods, bootsSuffix, bootsUUID, EquipmentSlot.FEET, ModItemTagsProvider.BOOTS);
        return mods;
    }

    private static Map<ResourceLocation, GearMod> buildLeggingsMods() {
        Map<ResourceLocation, GearMod> mods = new HashMap<>();
        String leggingsSuffix = "leg_guards";
        UUID leggingsUUID = UUID.fromString("afd64ff9-2e2e-4b14-a594-21117631bb70");
        buildDefaultGearModsForSlot(mods, leggingsSuffix, leggingsUUID, EquipmentSlot.LEGS, ModItemTagsProvider.LEGGINGS);
        return mods;
    }

    private static Map<ResourceLocation, GearMod> buildChestplateMods() {
        Map<ResourceLocation, GearMod> mods = new HashMap<>();
        String chestplateSuffix = "chest_guard";
        UUID chestplateUUID = UUID.fromString("1600b3eb-c37d-440b-bfed-fd3484e2d0ae");
        buildDefaultGearModsForSlot(mods, chestplateSuffix, chestplateUUID, EquipmentSlot.CHEST, ModItemTagsProvider.CHESTPLATES);
        return mods;
    }

    private static Map<ResourceLocation, GearMod> buildHelmetMods() {
        Map<ResourceLocation, GearMod> mods = new HashMap<>();
        String helmetSuffix = "head_guard";
        UUID helmetUUID = UUID.fromString("2c968932-08a4-4ecc-abbe-50a7371a4cdb");
        buildDefaultGearModsForSlot(mods, helmetSuffix, helmetUUID, EquipmentSlot.HEAD, ModItemTagsProvider.HELMETS);
        return mods;
    }

    private static Map<ResourceLocation, GearMod> buildWeaponMods() {
        Map<ResourceLocation, GearMod> mods = new HashMap<>();
        String weaponSuffix = "blade";
        UUID weaponUUID = UUID.fromString("9b0f12ac-5f12-4dd8-8eb3-2cd2a088440b");
        buildDefaultGearModsForSlot(mods, weaponSuffix, weaponUUID, EquipmentSlot.MAINHAND, ModItemTagsProvider.WEAPONS);
        return mods;
    }

    private static void buildDefaultGearModsForSlot(Map<ResourceLocation, GearMod> mods, String suffix, UUID uuid, EquipmentSlot slot, Tag<Item> tag) {
        buildDefaultGearMod(mods, uuid, new ResourceLocation(SimplyHarder.MOD_ID, "strong_" + suffix), Attributes.ATTACK_DAMAGE, 1.0D, AttributeModifier.Operation.ADDITION, slot, tag);
        buildDefaultGearMod(mods, uuid, new ResourceLocation(SimplyHarder.MOD_ID, "hasty_" + suffix), Attributes.ATTACK_SPEED, 0.4D, AttributeModifier.Operation.ADDITION, slot, tag);
        buildDefaultGearMod(mods, uuid, new ResourceLocation(SimplyHarder.MOD_ID, "knocking_" + suffix), Attributes.ATTACK_KNOCKBACK, 0.1D, AttributeModifier.Operation.ADDITION, slot, tag);
        buildDefaultGearMod(mods, uuid, new ResourceLocation(SimplyHarder.MOD_ID, "fortified_" + suffix), Attributes.ARMOR, 2.0D, AttributeModifier.Operation.ADDITION, slot, tag);
        buildDefaultGearMod(mods, uuid, new ResourceLocation(SimplyHarder.MOD_ID, "rigid_" + suffix), Attributes.ARMOR_TOUGHNESS, 1.0D, AttributeModifier.Operation.ADDITION, slot, tag);
        buildDefaultGearMod(mods, uuid, new ResourceLocation(SimplyHarder.MOD_ID, "resolute_" + suffix), Attributes.KNOCKBACK_RESISTANCE, 0.1D, AttributeModifier.Operation.ADDITION, slot, tag);
        buildDefaultGearMod(mods, uuid, new ResourceLocation(SimplyHarder.MOD_ID, "swift_" + suffix), Attributes.MOVEMENT_SPEED, 0.01D, AttributeModifier.Operation.ADDITION, slot, tag);
        buildDefaultGearMod(mods, uuid, new ResourceLocation(SimplyHarder.MOD_ID, "healthy_" + suffix), Attributes.MAX_HEALTH, 2.0D, AttributeModifier.Operation.ADDITION, slot, tag);
        buildDefaultGearMod(mods, uuid, new ResourceLocation(SimplyHarder.MOD_ID, "lucky_" + suffix), Attributes.LUCK, 1.0D, AttributeModifier.Operation.ADDITION, slot, tag);
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
