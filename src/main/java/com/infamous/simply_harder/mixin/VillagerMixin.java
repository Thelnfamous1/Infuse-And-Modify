package com.infamous.simply_harder.mixin;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.registry.SHItems;
import com.infamous.simply_harder.util.MerchantHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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
        if(!MerchantHelper.hasRankUpRewards(profession)) return;

        int newLevel = this.getVillagerData().getLevel();
        ItemStack reward = ItemStack.EMPTY;
        switch (newLevel) {
            case 2 -> reward = new ItemStack(SHItems.UPGRADE_MODULE.get(), 3);
            case 3 -> reward = new ItemStack(SHItems.ENHANCEMENT_CORE.get(), 3);
            case 4 -> reward = new ItemStack(SHItems.UPGRADE_MODULE.get(), 6);
            case 5 -> reward = new ItemStack(SHItems.ENHANCEMENT_CORE.get(), 6);
            default -> SimplyHarder.LOGGER.info("Unexpected new merchant career level for villager {}: {}", this, newLevel);
        }
        if(!reward.isEmpty()){
            Player lastTradedPlayer = this.lastTradedPlayer;
            Vec3 throwTargetPos = MerchantHelper.getThrowTargetPos(this, lastTradedPlayer);
            BehaviorUtils.throwItem(this, reward, throwTargetPos.add(0, 1.0D, 0));
        }
    }

}
