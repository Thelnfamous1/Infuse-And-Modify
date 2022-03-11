package com.infamous.simply_harder.util;

import com.google.common.collect.Multimap;
import com.infamous.simply_harder.custom.data.GearMod;
import com.infamous.simply_harder.custom.data.MasterworkProgression;
import com.infamous.simply_harder.custom.data.MasterworkTier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

public class AttributeHelper {

    public static final String ADDITIIVE_MODIFIER_LOCALIZATION = "attribute.modifier.plus.";
    public static final String SUBTRACTIVE_MODIFIER_LOCALIZATION = "attribute.modifier.take.";

    public static Optional<Attribute> getAttribute(ResourceLocation attributeName) {
        return Optional.ofNullable(ForgeRegistries.ATTRIBUTES.containsKey(attributeName) ? ForgeRegistries.ATTRIBUTES.getValue(attributeName) : null);
    }

    public static Multimap<Attribute, AttributeModifier> getAttributeModifiersFromMasterworkProgression(int tier, MasterworkProgression masterworkProgression, EquipmentSlot slot) {
        return masterworkProgression.getTier(tier).orElse(MasterworkTier.EMPTY).wrappedAttributeModifiers().forSlot(slot);
    }

    public static Multimap<Attribute, AttributeModifier> getAttributeModifiersFromGearMod(GearMod gearMod, EquipmentSlot slot) {
        return gearMod.wrappedAttributeModifiers().forSlot(slot);
    }
}
