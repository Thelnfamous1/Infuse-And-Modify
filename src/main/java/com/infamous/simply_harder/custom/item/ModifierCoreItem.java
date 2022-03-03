package com.infamous.simply_harder.custom.item;

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
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModifierCoreItem extends Item {

    public static final String NAME = "modifier_core";
    public static final String MODIFIER_CORE_TAG = new ResourceLocation(SimplyHarder.MOD_ID, NAME).toString();
    public static final String SLOT_TAG = "Slot";
    public static final String ATTRIBUTE_NAME_TAG = "AttributeName";
    public static final String SUBTRACTIVE_MODIFIER_LOCALIZATION = "attribute.modifier.take.";
    public static final String ADDITIVE_MODIFIER_LOCALIZATION = "attribute.modifier.plus.";

    public ModifierCoreItem(Properties properties) {
        super(properties);
    }

    public static void addModifierToCore(ItemStack stack, Attribute attribute, AttributeModifier modifier, @Nullable EquipmentSlot slot) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(MODIFIER_CORE_TAG, Tag.TAG_LIST)) {
            tag.put(MODIFIER_CORE_TAG, new ListTag());
        }

        ListTag modifierCoreTag = tag.getList(MODIFIER_CORE_TAG, Tag.TAG_COMPOUND);
        CompoundTag modifierTag = modifier.save();
        modifierTag.putString(ATTRIBUTE_NAME_TAG, getAttributeResource(attribute).toString());
        if (slot != null) {
            modifierTag.putString(SLOT_TAG, slot.getName());
        }

        modifierCoreTag.add(modifierTag);
    }

    private static ResourceLocation getAttributeResource(Attribute attribute) {
        return ForgeRegistries.ATTRIBUTES.getKey(attribute);
    }

    public static boolean hasModifierCore(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(MODIFIER_CORE_TAG, Tag.TAG_LIST);
    }

    public static Multimap<Attribute, AttributeModifier> getModifiersFromCore(ItemStack stack, EquipmentSlot slot) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        ListTag modifierCoreTag = stack.getTag().getList(MODIFIER_CORE_TAG, Tag.TAG_COMPOUND);

        for(int i = 0; i < modifierCoreTag.size(); ++i) {
            CompoundTag modifierTag = modifierCoreTag.getCompound(i);
            if (!modifierTag.contains(SLOT_TAG, Tag.TAG_STRING) || modifierTag.getString(SLOT_TAG).equals(slot.getName())) {
                Optional<Attribute> attribute = getAttribute(modifierTag);
                if (attribute.isPresent()) {
                    AttributeModifier modifier = AttributeModifier.load(modifierTag);
                    if (modifier != null && modifier.getId().getLeastSignificantBits() != 0L && modifier.getId().getMostSignificantBits() != 0L) {
                        modifiers.put(attribute.get(), modifier);
                    }
                }
            }
        }
        return modifiers;
    }

    private static Optional<Attribute> getAttribute(CompoundTag modifierTag) {
        ResourceLocation attributeName = ResourceLocation.tryParse(modifierTag.getString(ATTRIBUTE_NAME_TAG));
        return Optional.ofNullable(ForgeRegistries.ATTRIBUTES.containsKey(attributeName) ?
                ForgeRegistries.ATTRIBUTES.getValue(attributeName) :
                null);
    }

    public static void showModifiersFromCore(List<Component> components, ItemStack stack){
        for(EquipmentSlot slot : EquipmentSlot.values()) {
            Multimap<Attribute, AttributeModifier> modifiersFromCore = getModifiersFromCore(stack, slot);
            if (!modifiersFromCore.isEmpty()) {
                components.add(TextComponent.EMPTY);
                components.add((new TranslatableComponent(Util.makeDescriptionId("item", buildModifiersLocalization(slot)))).withStyle(ChatFormatting.GRAY));

                for(Map.Entry<Attribute, AttributeModifier> entry : modifiersFromCore.entries()) {
                    AttributeModifier attributemodifier = entry.getValue();
                    double modifierAmount = attributemodifier.getAmount();

                    double displayedAmount;
                    if (attributemodifier.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
                        if (entry.getKey().equals(Attributes.KNOCKBACK_RESISTANCE)) {
                            displayedAmount = modifierAmount * 10.0D;
                        } else {
                            displayedAmount = modifierAmount;
                        }
                    } else {
                        displayedAmount = modifierAmount * 100.0D;
                    }

                    if (modifierAmount > 0.0D) {
                        components.add((new TranslatableComponent(buildAdditiveModifierLocalization(attributemodifier), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(displayedAmount), new TranslatableComponent(entry.getKey().getDescriptionId()))).withStyle(ChatFormatting.BLUE));
                    } else if (modifierAmount < 0.0D) {
                        displayedAmount *= -1.0D;
                        components.add((new TranslatableComponent(buildSubtractiveModifierLocalization(attributemodifier), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(displayedAmount), new TranslatableComponent(entry.getKey().getDescriptionId()))).withStyle(ChatFormatting.RED));
                    }
                }
            }
        }
    }

    private static String buildSubtractiveModifierLocalization(AttributeModifier attributemodifier) {
        return SUBTRACTIVE_MODIFIER_LOCALIZATION + attributemodifier.getOperation().toValue();
    }

    private static String buildAdditiveModifierLocalization(AttributeModifier attributemodifier) {
        return ADDITIVE_MODIFIER_LOCALIZATION + attributemodifier.getOperation().toValue();
    }

    private static ResourceLocation buildModifiersLocalization(EquipmentSlot equipmentslot) {
        return new ResourceLocation(SimplyHarder.MOD_ID, NAME + "/modifiers/" + equipmentslot.getName());
    }

    public static boolean isModifierCore(ItemStack itemStack) {
        return itemStack.getItem() instanceof ModifierCoreItem;
    }

    public static boolean isEmptyModifierCore(ItemStack itemStack) {
        return isModifierCore(itemStack) && !hasModifierCore(itemStack);
    }

    public static boolean isFullModifierCore(ItemStack itemStack) {
        return isModifierCore(itemStack) && hasModifierCore(itemStack);
    }

    public static boolean isNonCoreWithModifierCore(ItemStack itemStack) {
        return !isModifierCore(itemStack) && hasModifierCore(itemStack);
    }

    public static boolean canModifyCore(ItemStack left, ItemStack right) {
        return isEmptyModifierCore(left)
                && isNonCoreWithModifierCore(right);
    }

    public static boolean canModifyUsingCore(ItemStack left, ItemStack right) {
        return left.isDamageableItem() && !hasModifierCore(left)
                && isFullModifierCore(right);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, components, tooltipFlag);
        if(hasModifierCore(itemStack)){
            showModifiersFromCore(components, itemStack);
        }

    }
}
