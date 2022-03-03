package com.infamous.simply_harder.client;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.custom.item.ModifierCoreItem;
import com.infamous.simply_harder.util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = SimplyHarder.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class SHForgeClientEvents {

    @SubscribeEvent
    static void onItemTooltip(ItemTooltipEvent event){
        TooltipFlag flags = event.getFlags();
        ItemStack itemStack = event.getItemStack();
        List<Component> toolTip = event.getToolTip();

        if(flags.isAdvanced()){
            TooltipHelper.appendToolActionsText(itemStack, toolTip);
            TooltipHelper.appendUseAnimationText(itemStack, toolTip);
            TooltipHelper.appendEquipmentSlotText(itemStack, toolTip);
        }
    }

}
