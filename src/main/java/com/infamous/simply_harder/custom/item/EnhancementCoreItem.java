package com.infamous.simply_harder.custom.item;

import com.infamous.simply_harder.SimplyHarder;
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


    public EnhancementCoreItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);

        TooltipHelper.appendLore(p_41423_, ENHANCEMENT_CORE_LORE_LOCALIZATION);
    }
}
