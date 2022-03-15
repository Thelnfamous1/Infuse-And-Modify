package com.infamous.simply_harder.util;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.custom.data.MerchantProgression;
import com.infamous.simply_harder.custom.data.WrappedMerchantOffers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class MerchantHelper {

    private static Vec3 getThrowTargetPos(PathfinderMob merchant, @Nullable Player lastTradedPlayer) {
        return lastTradedPlayer != null ? lastTradedPlayer.position() : getRandomNearbyPos(merchant);
    }

    private static Vec3 getRandomNearbyPos(PathfinderMob pathfinderMob) {
        Vec3 landRandomPos = LandRandomPos.getPos(pathfinderMob, 4, 2);
        return landRandomPos == null ? pathfinderMob.position() : landRandomPos;
    }

    private static List<ItemStack> getLevelUpRewards(LivingEntity merchant, VillagerProfession profession, int level) {
        ResourceLocation levelUpRewards = SimplyHarder.MERCHANT_PROGRESSION_MANAGER
                .getMerchantProgression(profession)
                .orElse(MerchantProgression.NONE)
                .getRewardsForLevel(level)
                .orElse(BuiltInLootTables.EMPTY);

        LootContext lootContext = (new LootContext.Builder((ServerLevel) merchant.level))
                .withParameter(LootContextParams.THIS_ENTITY, merchant)
                .withParameter(LootContextParams.ORIGIN, merchant.position())
                .withRandom(merchant.level.random)
                .create(LootContextParamSets.ADVANCEMENT_ENTITY);

        return merchant.level.getServer()
                .getLootTables()
                .get(levelUpRewards)
                .getRandomItems(lootContext);
    }

    public static void generateAndGiveRewards(PathfinderMob merchant, VillagerProfession profession, int newLevel, @Nullable Player lastTradedPlayer) {
        List<ItemStack> rankUpRewards = getLevelUpRewards(merchant, profession, newLevel);
        if(!rankUpRewards.isEmpty()){
            Vec3 throwTargetPos = getThrowTargetPos(merchant, lastTradedPlayer);
            for(ItemStack reward : rankUpRewards){
                BehaviorUtils.throwItem(merchant, reward, throwTargetPos.add(0, 1.0D, 0));
            }
        }
    }

    public static void addAdditionalOffers(VillagerProfession profession, int currentLevel, MerchantOffers offers) {
        Optional<MerchantProgression> merchantProgression = SimplyHarder.MERCHANT_PROGRESSION_MANAGER.getMerchantProgression(profession);
        merchantProgression.ifPresent(mp -> offers.addAll(mp.getAdditionalOffersForLevel(currentLevel).orElse(WrappedMerchantOffers.EMPTY).unwrap()));
    }
}
