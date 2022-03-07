package com.infamous.simply_harder.util;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class TooltipHelper {

    public static void appendLore(List<Component> toolTip, ResourceLocation localization) {
        // add lore
        toolTip.add(
                (new TranslatableComponent(Util.makeDescriptionId("item", localization)))
                    .withStyle(ChatFormatting.WHITE)
                    .withStyle(ChatFormatting.ITALIC)
        );
    }
}
