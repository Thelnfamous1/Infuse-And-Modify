package com.infamous.simply_harder.custom.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.util.InfuseAndModifyHelper;
import com.infamous.simply_harder.util.TooltipHelper;
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
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ModifierCoreItem extends Item {

    public static final String NAME = "modifier_core";
    public static final String MODIFIER_CORE_TAG = new ResourceLocation(SimplyHarder.MOD_ID, NAME).toString();

    public static final String ATTRIBUTE_MODIFIERS_TAG = "AttributeModifiers";
    public static final String ATTRIBUTE_NAME_TAG = "AttributeName";
    public static final String SLOT_TAG = "Slot";

    public static final String SUBTRACTIVE_MODIFIER_LOCALIZATION = "attribute.modifier.take.";
    public static final String ADDITIVE_MODIFIER_LOCALIZATION = "attribute.modifier.plus.";

    public static final ResourceLocation EMPTY_LABEL_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, NAME + "/empty");

    public ModifierCoreItem(Properties properties) {
        super(properties);
    }

    public static boolean isModifierCore(ItemStack itemStack) {
        return itemStack.getItem() instanceof ModifierCoreItem;
    }

    public static boolean hasModifierCore(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(MODIFIER_CORE_TAG, Tag.TAG_COMPOUND);
    }

    public static boolean isFullCore(ItemStack itemStack) {
        return isModifierCore(itemStack) && hasModifierCore(itemStack);
    }

    public static boolean isEmptyCore(ItemStack itemStack) {
        return isModifierCore(itemStack) && !hasModifierCore(itemStack);
    }

    private static CompoundTag getModifierCore(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        if (!tag.contains(MODIFIER_CORE_TAG, Tag.TAG_COMPOUND)) {
            tag.put(MODIFIER_CORE_TAG, new CompoundTag());
        }
        return tag.getCompound(MODIFIER_CORE_TAG);
    }

    public static void clearModifierCore(ItemStack stack){
        if(stack.hasTag()){
            stack.getTag().remove(MODIFIER_CORE_TAG);
        }
    }

    public static void addModifiersToCore(ItemStack addFrom, ItemStack coreStack) {
        for(EquipmentSlot slot : EquipmentSlot.values()){
            Multimap<Attribute, AttributeModifier> modifiers = addFrom.getAttributeModifiers(slot);
            for(Map.Entry<Attribute, AttributeModifier> entry : modifiers.entries()){
                addModifierToCore(coreStack, entry.getKey(), entry.getValue(), slot);
            }
        }
        InfuseAndModifyHelper.addRequirementsToTag(addFrom, getModifierCore(coreStack));
    }

    // Replicated from ItemStack#addAttributeModifier
    public static void addModifierToCore(ItemStack coreStack, Attribute attribute, AttributeModifier modifier, @Nullable EquipmentSlot slot) {
        CompoundTag modifierCoreTag = getModifierCore(coreStack);

        if (!modifierCoreTag.contains(ATTRIBUTE_MODIFIERS_TAG, Tag.TAG_LIST)) {
            modifierCoreTag.put(ATTRIBUTE_MODIFIERS_TAG, new ListTag());
        }

        ListTag attributeModifiersTag = modifierCoreTag.getList(ATTRIBUTE_MODIFIERS_TAG, Tag.TAG_COMPOUND);
        CompoundTag modifierTag = modifier.save();
        modifierTag.putString(ATTRIBUTE_NAME_TAG, InfuseAndModifyHelper.getAttributeId(attribute).toString());
        if (slot != null) {
            modifierTag.putString(SLOT_TAG, slot.getName());
        }

        attributeModifiersTag.add(modifierTag);
    }

    public static void addModifiersFromCore(ItemStack addTo, ItemStack addFrom) {
        CompoundTag addToTag = addTo.getOrCreateTag();
        addToTag.remove(ATTRIBUTE_MODIFIERS_TAG);
        for(EquipmentSlot slot : EquipmentSlot.values()){
            Multimap<Attribute, AttributeModifier> modifiers = getModifiersFromCore(addFrom, slot);
            for(Map.Entry<Attribute, AttributeModifier> entry : modifiers.entries()){
                addTo.addAttributeModifier(entry.getKey(), entry.getValue(), slot);
            }
        }
    }

    // Replicated from ItemStack#getAttributeModifiers
    public static Multimap<Attribute, AttributeModifier> getModifiersFromCore(ItemStack coreStack, EquipmentSlot slot) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        CompoundTag modifierCore = getModifierCore(coreStack);
        ListTag attributeModifiersTag = modifierCore.getList(ATTRIBUTE_MODIFIERS_TAG, Tag.TAG_COMPOUND);

        for(int i = 0; i < attributeModifiersTag.size(); ++i) {
            CompoundTag modifierTag = attributeModifiersTag.getCompound(i);
            if (!modifierTag.contains(SLOT_TAG, Tag.TAG_STRING) || modifierTag.getString(SLOT_TAG).equals(slot.getName())) {
                Optional<Attribute> attribute = InfuseAndModifyHelper.getAttributeFromTag(modifierTag, ATTRIBUTE_NAME_TAG);
                attribute.ifPresent(a -> {
                    AttributeModifier modifier = AttributeModifier.load(modifierTag);
                    if (modifier != null && modifier.getId().getLeastSignificantBits() != 0L && modifier.getId().getMostSignificantBits() != 0L) {
                        modifiers.put(a, modifier);
                    }
                });
            }
        }
        return modifiers;
    }

    public static Set<ToolAction> getRequiredToolActions(ItemStack itemStack) {
        CompoundTag modifierCoreTag = getModifierCore(itemStack);
        return InfuseAndModifyHelper.getRequiredToolActionsFromTag(modifierCoreTag);
    }

    public static Optional<UseAnim> getRequiredUseAnimation(ItemStack itemStack) {
        CompoundTag modifierCoreTag = getModifierCore(itemStack);
        return InfuseAndModifyHelper.getRequiredUseAnimationFromTag(modifierCoreTag);
    }

    public static Optional<EquipmentSlot> getRequiredEquipmentSlot(ItemStack itemStack) {
        CompoundTag modifierCoreTag = getModifierCore(itemStack);
        return InfuseAndModifyHelper.getRequiredEquipmentSlotFromTag(modifierCoreTag);
    }

    // Replicated from ItemStack#getTooltipLines
    public static void showModifiersFromCore(List<Component> components, ItemStack coreStack){
        for(EquipmentSlot slot : EquipmentSlot.values()) {
            Multimap<Attribute, AttributeModifier> modifiersFromCore = getModifiersFromCore(coreStack, slot);
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

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> toolTip, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, toolTip, tooltipFlag);
        if(hasModifierCore(itemStack)){
            showModifiersFromCore(toolTip, itemStack);
            TooltipHelper.appendRequirements(toolTip, getRequiredToolActions(itemStack), getRequiredUseAnimation(itemStack), getRequiredEquipmentSlot(itemStack));
        } else{
            toolTip.add((new TranslatableComponent(Util.makeDescriptionId("item", EMPTY_LABEL_LOCALIZATION))).withStyle(ChatFormatting.GRAY));
        }
    }
}
