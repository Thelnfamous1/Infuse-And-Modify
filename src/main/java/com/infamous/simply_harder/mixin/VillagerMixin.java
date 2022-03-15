package com.infamous.simply_harder.mixin;

import com.infamous.simply_harder.util.MerchantHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Villager.class)
public abstract class VillagerMixin extends AbstractVillager {

    public VillagerMixin(EntityType<? extends AbstractVillager> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow public abstract VillagerData getVillagerData();

    @Shadow @Nullable private Player lastTradedPlayer;

    @Inject(method = "increaseMerchantCareer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/Villager;setVillagerData(Lnet/minecraft/world/entity/npc/VillagerData;)V", shift = At.Shift.AFTER))
    private void handleIncreaseMerchantCareer(CallbackInfo ci){
        if(this.level.isClientSide){
            return;
        }
        VillagerProfession profession = this.getVillagerData().getProfession();
        int newLevel = this.getVillagerData().getLevel();
        MerchantHelper.generateAndGiveRewards(this, profession, newLevel, this.lastTradedPlayer);
    }

    @Inject(method = "updateTrades", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/Villager;addOffersFromItemListings(Lnet/minecraft/world/item/trading/MerchantOffers;[Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;I)V", shift = At.Shift.AFTER))
    private void handleUpdateTrades(CallbackInfo ci){
        if(this.level.isClientSide){
            return;
        }
        VillagerProfession profession = this.getVillagerData().getProfession();
        int currentLevel = this.getVillagerData().getLevel();
        MerchantOffers offers = this.getOffers();
        MerchantHelper.addAdditionalOffers(profession, currentLevel, offers);
    }

}
