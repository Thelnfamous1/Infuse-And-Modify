package com.infamous.simply_harder.util;

import com.google.common.collect.Multimap;
import com.infamous.simply_harder.custom.data.GearMod;
import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.custom.data.MasterworkProgression;
import com.infamous.simply_harder.custom.item.EnhancementCoreItem;
import com.infamous.simply_harder.custom.item.GearModItem;
import com.infamous.simply_harder.custom.item.UpgradeModuleItem;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Map;

public class TooltipHelper {

    public static final String MASTERWORK_TIER_LOCALIZATION_STRING = "masterwork/tier";
    public static final String MASTERWORK_PROGRESSION_LOCALIZATION_STRING = "masterwork/progression";
    public static final ResourceLocation MASTERWORK_TIER_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, MASTERWORK_TIER_LOCALIZATION_STRING);
    public static final ResourceLocation MASTERWORK_PROGRESSION_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, MASTERWORK_PROGRESSION_LOCALIZATION_STRING);
    public static final String ITEM = "item";
    public static final String MASTERWORK_PROGRESSION = "masterwork_progression";

    public static void appendLore(List<Component> toolTip, ResourceLocation localization) {
        toolTip.add(
                (new TranslatableComponent(Util.makeDescriptionId(ITEM, localization)))
                    .withStyle(ChatFormatting.WHITE)
                    .withStyle(ChatFormatting.ITALIC)
        );
    }

    public static void appendInfusedItemText(ItemStack itemStack, List<Component> tooltip, TooltipFlag tooltipFlag) {
        if(tooltipFlag.isAdvanced()){
            ItemStack infusedItem = UpgradeModuleItem.getInfusedItem(itemStack);
            tooltip.add(
                    (new TranslatableComponent(Util.makeDescriptionId(ITEM, UpgradeModuleItem.INFUSED_ITEM_LABEL_LOCALIZATION)))
                            .withStyle(ChatFormatting.GRAY)
            );
            List<Component> tooltipLines = infusedItem.getTooltipLines((Player)null, tooltipFlag);
            tooltip.addAll(tooltipLines);
        } else{
            Item infusedItemType = UpgradeModuleItem.getInfusedItemType(itemStack);
            tooltip.add(
                    (new TranslatableComponent(Util.makeDescriptionId(ITEM, UpgradeModuleItem.INFUSED_ITEM_LABEL_LOCALIZATION)))
                            .withStyle(ChatFormatting.GRAY)
                            .append(" ")
                            .append(
                                    (new TranslatableComponent(infusedItemType.getDescriptionId()))
                                            .withStyle(ChatFormatting.AQUA)
                            )
            );
        }
    }

    public static void appendMasterworkLines(List<Component> toolTip, ItemStack itemStack) {
        ResourceLocation progressionId = new ResourceLocation(EnhancementCoreItem.getProgressionNameCheckTag(itemStack));

        toolTip.add(
                (new TranslatableComponent(Util.makeDescriptionId(ITEM, MASTERWORK_PROGRESSION_LOCALIZATION)))
                        .withStyle(ChatFormatting.GRAY)
                        .append(" ")
                        .append(
                                (new TranslatableComponent(Util.makeDescriptionId(MASTERWORK_PROGRESSION, progressionId)))
                                        .withStyle(ChatFormatting.YELLOW)
                        )
        );

        int tier = EnhancementCoreItem.getTierCheckTag(itemStack);

        toolTip.add(
                (new TranslatableComponent(Util.makeDescriptionId(ITEM, MASTERWORK_TIER_LOCALIZATION)))
                        .withStyle(ChatFormatting.GRAY)
                        .append(" ")
                        .append(
                                (new TranslatableComponent(Util.makeDescriptionId(ITEM, buildMasterworkTierLocalization(tier))))
                                        .withStyle(ChatFormatting.YELLOW)
                        )
        );
    }

    public static void appendInstalledModLines(List<Component> toolTip, ItemStack itemStack) {
        GearMod gearMod = GearModItem.getModCheckTag(itemStack);
        toolTip.add(
                (new TranslatableComponent(Util.makeDescriptionId(ITEM, GearModItem.INSTALLED_MOD_LABEL_LOCALIZATION)))
                        .withStyle(ChatFormatting.GRAY)
                        .append(" ")
                        .append(
                                (new TranslatableComponent(Util.makeDescriptionId(GearModItem.NAME, gearMod.id())))
                                        .withStyle(ChatFormatting.GREEN)
                        )
        );
    }

    public static void appendAttributeModifiersLinesFromMasterworkProgression(int tier, MasterworkProgression masterworkProgression, List<Component> tooltip, String prefix){
        for(EquipmentSlot slot : EquipmentSlot.values()) {
            appendAttributeModifiersForSlot(tooltip, prefix, slot, AttributeHelper.getAttributeModifiersFromMasterworkProgression(tier, masterworkProgression, slot));
        }
    }

    public static void appendAttributeModifiersLinesFromGearMod(GearMod gearMod, List<Component> tooltip, String prefix){
        for(EquipmentSlot slot : EquipmentSlot.values()) {
            appendAttributeModifiersForSlot(tooltip, prefix, slot, AttributeHelper.getAttributeModifiersFromGearMod(gearMod, slot));
        }
    }

    private static void appendAttributeModifiersForSlot(List<Component> tooltip, String prefix, EquipmentSlot slot, Multimap<Attribute, AttributeModifier> attributeModifiers) {
        if (!attributeModifiers.isEmpty()) {
            tooltip.add(TextComponent.EMPTY);
            tooltip.add((new TranslatableComponent(Util.makeDescriptionId(ITEM, buildSlotModifiersLocalization(prefix, slot))))
                    .withStyle(ChatFormatting.GRAY));

            for(Map.Entry<Attribute, AttributeModifier> entry : attributeModifiers.entries()) {
                AttributeModifier modifier = entry.getValue();
                double amount = modifier.getAmount();

                double displayedAmount;
                if (modifier.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && modifier.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
                    if (entry.getKey().equals(Attributes.KNOCKBACK_RESISTANCE)) {
                        displayedAmount = amount * 10.0D;
                    } else {
                        displayedAmount = amount;
                    }
                } else {
                    displayedAmount = amount * 100.0D;
                }

                if (amount > 0.0D) {
                    tooltip.add((new TranslatableComponent(AttributeHelper.ADDITIIVE_MODIFIER_LOCALIZATION + modifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(displayedAmount), new TranslatableComponent(entry.getKey().getDescriptionId())))
                            .withStyle(ChatFormatting.BLUE));
                } else if (amount < 0.0D) {
                    displayedAmount *= -1.0D;
                    tooltip.add((new TranslatableComponent(AttributeHelper.SUBTRACTIVE_MODIFIER_LOCALIZATION + modifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(displayedAmount), new TranslatableComponent(entry.getKey().getDescriptionId())))
                            .withStyle(ChatFormatting.RED));
                }
            }
        }
    }

    private static ResourceLocation buildSlotModifiersLocalization(String prefix, EquipmentSlot slot) {
        return new ResourceLocation(SimplyHarder.MOD_ID, prefix + "/modifiers/" + slot.getName());
    }

    private static ResourceLocation buildMasterworkTierLocalization(int tier){
        return new ResourceLocation(SimplyHarder.MOD_ID, MASTERWORK_TIER_LOCALIZATION_STRING + "/" + tier);
    }

}
