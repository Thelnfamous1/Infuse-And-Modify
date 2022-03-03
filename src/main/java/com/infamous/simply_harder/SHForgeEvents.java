package com.infamous.simply_harder;

import com.infamous.simply_harder.custom.ModRecipeTypes;
import com.infamous.simply_harder.custom.critera.ModCriteriaTriggers;
import com.infamous.simply_harder.custom.recipe.ModificationRecipe;
import com.infamous.simply_harder.registry.SHAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = SimplyHarder.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SHForgeEvents {
    private static final UUID[] EXHAUSTION_MODIFIER_UUID_PER_SLOT = new UUID[]{
            UUID.fromString("2c139584-08d5-4ac4-b332-289e7cf75a00"),
            UUID.fromString("769b1502-35e9-44c3-a289-c560c0cd75d8"),
            UUID.fromString("430274fc-33a6-488e-89ec-447a187f01cb"),
            UUID.fromString("980a4aa7-e457-4f33-8f98-4c567b8489cc")};

    private static final String EXHAUSTION_MODIFIER_NAME = new ResourceLocation(SimplyHarder.MOD_ID, "exhaustion").toString();

    @SubscribeEvent
    static void onItemAttributeEvent(ItemAttributeModifierEvent event){
        ItemStack itemStack = event.getItemStack();
        EquipmentSlot slotType = event.getSlotType();
        handleExhaustion(event, itemStack, slotType);
    }

    private static void handleExhaustion(ItemAttributeModifierEvent event, ItemStack itemStack, EquipmentSlot slotType) {
        ResourceLocation location = itemStack.getItem().getRegistryName();
        double itemExhaustionModifier = SimplyHarder.getArmorManager().getExhaustionModifier(location);
        if(itemExhaustionModifier != 0.0D && slotType == Mob.getEquipmentSlotForItem(itemStack)){
            UUID uuidForSlot = EXHAUSTION_MODIFIER_UUID_PER_SLOT[slotType.getIndex()];
            AttributeModifier exhaustionModifier = new AttributeModifier(uuidForSlot, EXHAUSTION_MODIFIER_NAME, itemExhaustionModifier, AttributeModifier.Operation.MULTIPLY_BASE);
            event.addModifier(SHAttributes.EXHAUSTION.get(), exhaustionModifier);
        }
    }

    @SubscribeEvent
    static void onAnvilUpdate(AnvilUpdateEvent event){
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        Player player = event.getPlayer();
        Container container = ModificationRecipe.buildContainer(left, right);
        player.level.getRecipeManager()
                .getRecipeFor(ModRecipeTypes.MODIFICATION.get(), container, player.level)
                .ifPresent(mr -> {
                    event.setOutput(mr.assemble(container));
                    event.setCost(mr.calculateLevelCost(container));
                    event.setMaterialCost(mr.calculateMaterialCost(container));
                });
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void onXpChange(PlayerXpEvent.XpChange event){
        if(event.isCanceled()) return;

        Player player = event.getPlayer();
        if(player instanceof ServerPlayer serverPlayer){
            int futureTotal = Mth.clamp(serverPlayer.totalExperience + event.getAmount(), 0, Integer.MAX_VALUE); // mimic logic in Player
            /*
            PeakExperienceAttacher.getPeakExperience(serverPlayer).ifPresent(pe -> {
                if(futureTotal > pe.getPeakTotalExperience()){
                    pe.setPeakTotalExperience(futureTotal, true);
                }
            });
             */
            ModCriteriaTriggers.EXPERIENCE_CHANGED.get().triggerTotalChange(serverPlayer, futureTotal);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void onLevelChange(PlayerXpEvent.LevelChange event){
        if(event.isCanceled()) return;

        Player player = event.getPlayer();
        if(player instanceof ServerPlayer serverPlayer){
            int futureLevels = serverPlayer.experienceLevel + event.getLevels();
            /*
            PeakExperienceAttacher.getPeakExperience(serverPlayer).ifPresent(pe -> {
                if(futureLevels > pe.getPeakExperienceLevel()){
                    pe.setPeakExperienceLevel(futureLevels, true);
                }
            });
             */
            ModCriteriaTriggers.EXPERIENCE_CHANGED.get().triggerLevelChange(serverPlayer, futureLevels);
        }
    }


    @SubscribeEvent
    static void onAddReloadListener(AddReloadListenerEvent event){
        event.addListener(SimplyHarder.getArmorManager());
    }
}
