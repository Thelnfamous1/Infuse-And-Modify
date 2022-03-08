package com.infamous.simply_harder.util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.infamous.simply_harder.SimplyHarder;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AttributeHelper {

    public static final String ATTRIBUTE_MODIFIERS_TAG = "AttributeModifiers";
    public static final String SLOT_TAG = "Slot";
    public static final String ATTRIBUTE_NAME_TAG = "AttributeName";
    public static final String ADDITIIVE_MODIFIER_LOCALIZATION = "attribute.modifier.plus.";
    public static final String SUBTRACTIVE_MODIFIER_LOCALIZATION = "attribute.modifier.take.";

    public static void addAttributeModifier(CompoundTag tag, Attribute attribute, AttributeModifier modifier, @Nullable EquipmentSlot slot) {
        if (!tag.contains(ATTRIBUTE_MODIFIERS_TAG, Tag.TAG_LIST)) {
            tag.put(ATTRIBUTE_MODIFIERS_TAG, new ListTag());
        }

        ListTag modifierTags = tag.getList(ATTRIBUTE_MODIFIERS_TAG, Tag.TAG_COMPOUND);
        CompoundTag modifierTag = modifier.save();
        modifierTag.putString(ATTRIBUTE_NAME_TAG, ForgeRegistries.ATTRIBUTES.getKey(attribute).toString());
        if (slot != null) {
            modifierTag.putString(SLOT_TAG, slot.getName());
        }

        modifierTags.add(modifierTag);
    }

    public static Multimap<Attribute, AttributeModifier> getAttributeModifiers(CompoundTag tag, EquipmentSlot slot) {
        Multimap<Attribute, AttributeModifier> multimap = ArrayListMultimap.create();
        if (tag.contains(ATTRIBUTE_MODIFIERS_TAG, Tag.TAG_LIST)) {
            multimap = HashMultimap.create();
            ListTag modifierTags = tag.getList(ATTRIBUTE_MODIFIERS_TAG, Tag.TAG_COMPOUND);

            for(int i = 0; i < modifierTags.size(); ++i) {
                CompoundTag modifierTag = modifierTags.getCompound(i);
                if (!modifierTag.contains(SLOT_TAG, Tag.TAG_STRING) || modifierTag.getString(SLOT_TAG).equals(slot.getName())) {
                    ResourceLocation attributeName = ResourceLocation.tryParse(modifierTag.getString(ATTRIBUTE_NAME_TAG));
                    Optional<Attribute> optional = getAttribute(attributeName);
                    if (optional.isPresent()) {
                        AttributeModifier modifier = AttributeModifier.load(modifierTag);
                        if (modifier != null && modifier.getId().getLeastSignificantBits() != 0L && modifier.getId().getMostSignificantBits() != 0L) {
                            multimap.put(optional.get(), modifier);
                        }
                    }
                }
            }
        }
        return multimap;
    }

    public static Optional<Attribute> getAttribute(ResourceLocation attributeName) {
        return Optional.ofNullable(ForgeRegistries.ATTRIBUTES.containsKey(attributeName) ? ForgeRegistries.ATTRIBUTES.getValue(attributeName) : null);
    }

}
