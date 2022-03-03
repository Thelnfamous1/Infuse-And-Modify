package com.infamous.simply_harder.util;

import com.infamous.simply_harder.SimplyHarder;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.common.ToolAction;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class TooltipHelper {
    public static final ResourceLocation REQUIRED_TOOL_ACTIONS_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, "generic/required_tool_actions");
    public static final ResourceLocation REQUIRED_EQUIPMENT_SLOT_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, "generic/required_equipment_slot");
    public static final ResourceLocation REQUIRED_USE_ANIM_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, "generic/required_use_animation");
    public static final ResourceLocation EQUIPMENT_SLOT_LABEL_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, "generic/equipment_slot");
    public static final ResourceLocation TOOL_ACTIONS_LABEL_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, "generic/tool_actions");
    public static final ResourceLocation USE_ANIMATION_LABEL_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, "generic/use_animation");

    public static ResourceLocation buildToolActionLocalization(ToolAction toolAction) {
        return new ResourceLocation(SimplyHarder.MOD_ID, "generic/tool_action/" + toolAction.name());
    }

    public static ResourceLocation buildEquipmentSlotLocalization(EquipmentSlot slot) {
        return new ResourceLocation(SimplyHarder.MOD_ID, "generic/equipment_slot/" + slot.getName());
    }

    public static ResourceLocation buildUseAnimationLocalization(UseAnim slot) {
        return new ResourceLocation(SimplyHarder.MOD_ID, "generic/use_animation/" + slot.name().toLowerCase(Locale.ROOT));
    }

    public static void appendEquipmentSlotText(ItemStack itemStack, List<Component> toolTip) {
        EquipmentSlot slot = LivingEntity.getEquipmentSlotForItem(itemStack);
        toolTip.add(TextComponent.EMPTY);
        toolTip.add(
                (new TranslatableComponent(Util.makeDescriptionId("item", EQUIPMENT_SLOT_LABEL_LOCALIZATION)))
                        .withStyle(ChatFormatting.GRAY)
                        .append(" ")
                        .append(
                                (new TranslatableComponent(Util.makeDescriptionId("item", buildEquipmentSlotLocalization(slot))))
                                        .withStyle(ChatFormatting.GREEN)
                        )
        );
    }

    public static void appendToolActionsText(ItemStack itemStack, List<Component> toolTip) {
        boolean addedLabel = false;
        for(ToolAction toolAction : ToolAction.getActions()){ // not ideal, but ItemStacks don't store their performable ToolActions
            if(itemStack.canPerformAction(toolAction)){
                if(!addedLabel){
                    toolTip.add(TextComponent.EMPTY);
                    toolTip.add((new TranslatableComponent(Util.makeDescriptionId("item", TOOL_ACTIONS_LABEL_LOCALIZATION)))
                            .withStyle(ChatFormatting.GRAY));
                    addedLabel = true;
                }
                toolTip.add((new TranslatableComponent(Util.makeDescriptionId("item", buildToolActionLocalization(toolAction))))
                        .withStyle(ChatFormatting.GREEN));
            }
        }
    }

    public static void appendRequiredToolActionsText(List<Component> components, Set<ToolAction> requiredToolActions1) {
        if(!requiredToolActions1.isEmpty()){
            components.add(TextComponent.EMPTY);
            components.add((new TranslatableComponent(Util.makeDescriptionId("item", REQUIRED_TOOL_ACTIONS_LOCALIZATION)))
                    .withStyle(ChatFormatting.GRAY));
            for(ToolAction toolAction : requiredToolActions1){
                components.add(
                        (new TranslatableComponent(Util.makeDescriptionId("item", buildToolActionLocalization(toolAction))))
                                .withStyle(ChatFormatting.GREEN)
                );
            }
        }
    }

    public static void appendRequiredUseAnimationText(List<Component> components, Optional<UseAnim> requiredUseAnim1) {
        requiredUseAnim1.ifPresent(ua -> {
                    components.add(TextComponent.EMPTY);
                    components.add(
                            (new TranslatableComponent(Util.makeDescriptionId("item", REQUIRED_USE_ANIM_LOCALIZATION)))
                                    .withStyle(ChatFormatting.GRAY)
                                    .append(" ")
                                    .append(
                                            (new TranslatableComponent(Util.makeDescriptionId("item", buildUseAnimationLocalization(ua))))
                                                    .withStyle(ChatFormatting.GREEN)
                                    )
                    );
                }
        );
    }

    public static void appendRequiredEquipmentSlotText(List<Component> components, Optional<EquipmentSlot> requiredEquipmentSlot1) {
        requiredEquipmentSlot1.ifPresent(es -> {
                components.add(TextComponent.EMPTY);
                components.add(
                (new TranslatableComponent(Util.makeDescriptionId("item", REQUIRED_EQUIPMENT_SLOT_LOCALIZATION)))
                        .withStyle(ChatFormatting.GRAY)
                        .append(" ")
                        .append(
                                (new TranslatableComponent(Util.makeDescriptionId("item", buildEquipmentSlotLocalization(es))))
                                        .withStyle(ChatFormatting.GREEN)
                        )
                );
        }
        );
    }

    public static void appendUseAnimationText(ItemStack itemStack, List<Component> toolTip) {
        UseAnim useAnimation = itemStack.getUseAnimation();
        toolTip.add(TextComponent.EMPTY);
        toolTip.add(
                (new TranslatableComponent(Util.makeDescriptionId("item", USE_ANIMATION_LABEL_LOCALIZATION)))
                        .withStyle(ChatFormatting.GRAY)
                        .append(" ")
                        .append(
                                (new TranslatableComponent(Util.makeDescriptionId("item", buildUseAnimationLocalization(useAnimation))))
                                        .withStyle(ChatFormatting.GREEN)
                        )
        );
    }

    public static void appendRequirements(List<Component> components, Set<ToolAction> requiredToolActions, Optional<UseAnim> requiredUseAnimation, Optional<EquipmentSlot> requiredEquipmentSlot) {
        appendRequiredToolActionsText(components, requiredToolActions);
        appendRequiredUseAnimationText(components, requiredUseAnimation);
        appendRequiredEquipmentSlotText(components, requiredEquipmentSlot);
    }
}
