package com.infamous.simply_harder.util;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class MerchantHelper {

    public static Vec3 getThrowTargetPos(PathfinderMob merchant, @Nullable Player lastTradedPlayer) {
        return lastTradedPlayer != null ? lastTradedPlayer.position() : getRandomNearbyPos(merchant);
    }

    public static Vec3 getRandomNearbyPos(PathfinderMob pathfinderMob) {
        Vec3 landRandomPos = LandRandomPos.getPos(pathfinderMob, 4, 2);
        return landRandomPos == null ? pathfinderMob.position() : landRandomPos;
    }


    public static boolean hasRankUpRewards(VillagerProfession profession) {
        return profession == VillagerProfession.WEAPONSMITH
                || profession == VillagerProfession.TOOLSMITH
                || profession == VillagerProfession.ARMORER
                || profession == VillagerProfession.LEATHERWORKER;
    }
}
