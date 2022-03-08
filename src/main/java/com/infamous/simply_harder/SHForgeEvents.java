package com.infamous.simply_harder;

import com.google.common.collect.Multimap;
import com.infamous.simply_harder.custom.container.SimpleAnvilContainer;
import com.infamous.simply_harder.custom.item.EnhancementCoreItem;
import com.infamous.simply_harder.custom.item.GearModItem;
import com.infamous.simply_harder.custom.recipe.ForgingRecipe;
import com.infamous.simply_harder.custom.recipe.ModRecipeTypes;
import com.infamous.simply_harder.util.AttributeHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid = SimplyHarder.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SHForgeEvents {

    @SubscribeEvent
    static void onItemAttributeEvent(ItemAttributeModifierEvent event){
        ItemStack itemStack = event.getItemStack();
        EquipmentSlot slotType = event.getSlotType();
        if(EnhancementCoreItem.hasMasterwork(itemStack)){
            Multimap<Attribute, AttributeModifier> modifiers = AttributeHelper.getAttributeModifiers(EnhancementCoreItem.getMasterwork(itemStack), slotType);
            for(Map.Entry<Attribute, AttributeModifier> entry : modifiers.entries()){
                event.addModifier(entry.getKey(), entry.getValue());
            }
        }
        if(GearModItem.hasMod(itemStack)){
            Multimap<Attribute, AttributeModifier> modifiers = AttributeHelper.getAttributeModifiers(GearModItem.getMod(itemStack), slotType);
            for(Map.Entry<Attribute, AttributeModifier> entry : modifiers.entries()){
                event.addModifier(entry.getKey(), entry.getValue());
            }
        }
    }

    @SubscribeEvent
    static void onAnvilUpdate(AnvilUpdateEvent event){
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        Player player = event.getPlayer();
        SimpleAnvilContainer container = ForgingRecipe.buildAnvilContainer((AnvilMenu) player.containerMenu, player, left, right);
        player.level.getRecipeManager()
                .getRecipeFor(ModRecipeTypes.MODIFICATION.get(), container, player.level)
                .ifPresentOrElse(mr -> {
                    event.setOutput(mr.assemble(container));
                    event.setCost(mr.calculateLevelCost(container));
                    event.setMaterialCost(mr.calculateMaterialCost(container));
                }, () -> handleMasterworkingRecipe(event, player, container));
    }

    private static void handleMasterworkingRecipe(AnvilUpdateEvent event, Player player, SimpleAnvilContainer container) {
        player.level.getRecipeManager()
                .getRecipeFor(ModRecipeTypes.MASTERWORKING.get(), container, player.level)
                .ifPresent(mwr -> {
                    event.setOutput(mwr.assemble(container));
                    event.setCost(mwr.calculateLevelCost(container));
                    event.setMaterialCost(mwr.calculateMaterialCost(container));
                });
    }
}
