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
import net.minecraftforge.common.ToolAction;

import java.util.List;

public class TooltipHelper {
    public static ResourceLocation buildToolActionLocalization(ToolAction toolAction) {
        return new ResourceLocation(SimplyHarder.MOD_ID, "tool_action/" + toolAction.name());
    }

    public static ResourceLocation buildEquipmentSlotLocalization(EquipmentSlot slot) {
        return new ResourceLocation(SimplyHarder.MOD_ID, "equipment_slot/" + slot.getName());
    }

    public static void appendEquipmentSlotText(ItemStack itemStack, List<Component> toolTip) {
        EquipmentSlot slot = LivingEntity.getEquipmentSlotForItem(itemStack);
        toolTip.add(TextComponent.EMPTY);
        toolTip.add(
                (new TranslatableComponent(Util.makeDescriptionId("item", new ResourceLocation(SimplyHarder.MOD_ID, "equipment_slot"))))
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
                    toolTip.add((new TranslatableComponent(Util.makeDescriptionId("item", new ResourceLocation(SimplyHarder.MOD_ID, "tool_actions"))))
                            .withStyle(ChatFormatting.GRAY));
                    addedLabel = true;
                }
                toolTip.add((new TranslatableComponent(Util.makeDescriptionId("item", buildToolActionLocalization(toolAction))))
                        .withStyle(ChatFormatting.GREEN));
            }
        }
    }
}
