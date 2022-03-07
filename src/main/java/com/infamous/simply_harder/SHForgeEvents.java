package com.infamous.simply_harder;

import com.infamous.simply_harder.custom.container.SimpleAnvilContainer;
import com.infamous.simply_harder.custom.recipe.ModRecipeTypes;
import com.infamous.simply_harder.custom.recipe.ForgingRecipe;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SimplyHarder.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SHForgeEvents {

    @SubscribeEvent
    static void onItemAttributeEvent(ItemAttributeModifierEvent event){
        ItemStack itemStack = event.getItemStack();
        EquipmentSlot slotType = event.getSlotType();
    }

    @SubscribeEvent
    static void onAnvilUpdate(AnvilUpdateEvent event){
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        Player player = event.getPlayer();
        SimpleAnvilContainer container = ForgingRecipe.buildAnvilContainer((AnvilMenu) player.containerMenu, left, right);
        player.level.getRecipeManager()
                .getRecipeFor(ModRecipeTypes.MODIFICATION.get(), container, player.level)
                .ifPresentOrElse(mr -> {
                    event.setOutput(mr.assemble(container));
                    event.setCost(mr.calculateLevelCost(container));
                    event.setMaterialCost(mr.calculateMaterialCost(container));
                }, () -> handleMasterworkRecipe(event, player, container));
    }

    private static void handleMasterworkRecipe(AnvilUpdateEvent event, Player player, SimpleAnvilContainer container) {
        player.level.getRecipeManager()
                .getRecipeFor(ModRecipeTypes.MASTERWORKING.get(), container, player.level)
                .ifPresent(mwr -> {
                    event.setOutput(mwr.assemble(container));
                    event.setCost(mwr.calculateLevelCost(container));
                    event.setMaterialCost(mwr.calculateMaterialCost(container));
                });
    }
}
