package com.infamous.simply_harder.custom.item;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.registry.SHItems;
import com.infamous.simply_harder.util.TooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnhancementCoreItem extends Item {
    public static final String NAME = "enhancement_core";

    public static final ResourceLocation ENHANCEMENT_CORE_LORE_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, NAME + "/lore");
    public static final int MAX_TIER = 10;

    public EnhancementCoreItem(Properties properties) {
        super(properties);
    }

    public static boolean hasMasterwork(ItemStack itemStack) {
        // TODO
        return false;
    }

    public static int getTier(ItemStack itemStack) {
        // TODO
        return 0;
    }

    public static boolean isMaxTier(ItemStack itemStack) {
        return getTier(itemStack) >= MAX_TIER;
    }

    public static int getLevelCost(ItemStack itemStack) {
        // TODO
        return 0;
    }

    public static int getMaterialCost(ItemStack itemStack) {
        // TODO
        return 0;
    }

    public static boolean isEnhancementCore(ItemStack right) {
        return right.is(SHItems.ENHANCEMENT_CORE.get());
    }

    public static void setTier(ItemStack left, int tier) {
        // TODO

        if(!isMaxTier(left)){
            setLevelCost(left, calculateCostForTier(tier));
            setMaterialCost(left, calculateCostForTier(tier));
        }
    }

    private static void setMaterialCost(ItemStack left, int materialCost) {
        // TODO
    }

    private static void setLevelCost(ItemStack left, int levelCost) {
        // TODO
    }

    private static int calculateCostForTier(int tier) {
        return tier / 2 + tier % 2 == 0 ? 0 : 1;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltip, tooltipFlag);

        TooltipHelper.appendLore(tooltip, ENHANCEMENT_CORE_LORE_LOCALIZATION);
    }
}
