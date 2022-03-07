package com.infamous.simply_harder.util;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.custom.item.EnhancementCoreItem;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class TooltipHelper {

    public static final String MASTERWORK_TIER_LOCALIZATION_STRING = "masterwork/tier/";
    public static final ResourceLocation MASTERWORK_LEVEL_COST_LOCALIZATION= new ResourceLocation(SimplyHarder.MOD_ID, "masterwork/level_cost");
    public static final ResourceLocation MASTERWORK_MATERIAL_COST_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, "masterwork/material_cost");

    public static ResourceLocation buildMasterworkTierLocalization(int tier){
        return new ResourceLocation(SimplyHarder.MOD_ID, MASTERWORK_TIER_LOCALIZATION_STRING + tier);
    }

    public static void appendLore(List<Component> toolTip, ResourceLocation localization) {
        // add lore
        toolTip.add(
                (new TranslatableComponent(Util.makeDescriptionId("item", localization)))
                    .withStyle(ChatFormatting.WHITE)
                    .withStyle(ChatFormatting.ITALIC)
        );
    }

    public static void appendMasterworkLines(List<Component> toolTip, ItemStack itemStack) {
        int tier = EnhancementCoreItem.getTier(itemStack);
        toolTip.add(
                (new TranslatableComponent(Util.makeDescriptionId("item", buildMasterworkTierLocalization(tier))))
                        .withStyle(ChatFormatting.YELLOW)
        );
        if(EnhancementCoreItem.isMaxTier(itemStack)){
            int levelCost = EnhancementCoreItem.getLevelCost(itemStack);
            int materialCost = EnhancementCoreItem.getMaterialCost(itemStack);
            toolTip.add(
                    (new TranslatableComponent(Util.makeDescriptionId("item", MASTERWORK_LEVEL_COST_LOCALIZATION), levelCost))
                            .withStyle(ChatFormatting.YELLOW)
            );
            toolTip.add(
                    (new TranslatableComponent(Util.makeDescriptionId("item", MASTERWORK_MATERIAL_COST_LOCALIZATION), materialCost))
                            .withStyle(ChatFormatting.YELLOW)
            );
        }
    }
}
