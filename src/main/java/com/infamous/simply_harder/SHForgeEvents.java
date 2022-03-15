package com.infamous.simply_harder;

import com.google.common.collect.Multimap;
import com.infamous.simply_harder.custom.container.SimpleAnvilContainer;
import com.infamous.simply_harder.custom.data.GearMod;
import com.infamous.simply_harder.custom.data.MasterworkProgression;
import com.infamous.simply_harder.custom.item.EnhancementCoreItem;
import com.infamous.simply_harder.custom.item.GearModItem;
import com.infamous.simply_harder.custom.recipe.ForgingRecipe;
import com.infamous.simply_harder.custom.recipe.ModRecipeTypes;
import com.infamous.simply_harder.network.ClientboundUpdateGearModsPacket;
import com.infamous.simply_harder.network.ClientboundUpdateMasterworkProgressionsPacket;
import com.infamous.simply_harder.network.SHNetwork;
import com.infamous.simply_harder.util.AttributeHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = SimplyHarder.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SHForgeEvents {

    @SubscribeEvent
    static void onItemAttributeEvent(ItemAttributeModifierEvent event){
        ItemStack itemStack = event.getItemStack();
        EquipmentSlot slotType = event.getSlotType();

        if(EnhancementCoreItem.hasMasterwork(itemStack)){
            Multimap<Attribute, AttributeModifier> modifiers = AttributeHelper.getAttributeModifiersFromMasterworkProgression(EnhancementCoreItem.getTierCheckTag(itemStack), EnhancementCoreItem.getProgressionCheckTag(itemStack), slotType);
            for(Map.Entry<Attribute, AttributeModifier> entry : modifiers.entries()){
                event.addModifier(entry.getKey(), entry.getValue());
            }
        }
        if(GearModItem.hasMod(itemStack)){
            Multimap<Attribute, AttributeModifier> modifiers = AttributeHelper.getAttributeModifiersFromGearMod(GearModItem.getModCheckTag(itemStack), slotType);
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
        SimpleAnvilContainer container = new SimpleAnvilContainer((AnvilMenu) player.containerMenu, player, left, right);
        player.level.getRecipeManager()
                .getRecipeFor(ModRecipeTypes.MODIFICATION.get(), container, player.level)
                .ifPresentOrElse(updateAnvil(event, container), () ->
                        player.level.getRecipeManager()
                                .getRecipeFor(ModRecipeTypes.MASTERWORKING.get(), container, player.level)
                                .ifPresent(updateAnvil(event, container))
                );
    }

    private static Consumer<ForgingRecipe> updateAnvil(AnvilUpdateEvent event, SimpleAnvilContainer container) {
        return fr -> {
            event.setOutput(fr.assemble(container));
            event.setCost(fr.calculateTotalLevelCost(container));
            event.setMaterialCost(fr.calculateTotalMaterialCost(container));
        };
    }

    @SubscribeEvent
    static void onAddReloadableResource(AddReloadListenerEvent event){
        event.addListener(SimplyHarder.GEAR_MOD_MANAGER);
        event.addListener(SimplyHarder.MASTERWORK_PROGRESSION_MANAGER);
        event.addListener(SimplyHarder.MERCHANT_PROGRESSION_MANAGER);
    }

    @SubscribeEvent
    static void onDatapackSync(OnDatapackSyncEvent event){
        ServerPlayer joinedPlayer = event.getPlayer();
        if(joinedPlayer != null){
            syncCustomData(joinedPlayer);
        }
        for(ServerPlayer serverPlayer : event.getPlayerList().getPlayers()){
            syncCustomData(serverPlayer);
        }
    }

    private static void syncCustomData(ServerPlayer serverPlayer) {
        Map<ResourceLocation, GearMod> gearMods = SimplyHarder.GEAR_MOD_MANAGER.collectGearMods();
        SHNetwork.syncToPlayer(serverPlayer, new ClientboundUpdateGearModsPacket(gearMods));

        Map<ResourceLocation, MasterworkProgression> progressions = SimplyHarder.MASTERWORK_PROGRESSION_MANAGER.collectProgressions();
        SHNetwork.syncToPlayer(serverPlayer, new ClientboundUpdateMasterworkProgressionsPacket(progressions));
    }

}
