package com.infamous.simply_harder.util;

import com.infamous.simply_harder.custom.data.GearMod;
import com.infamous.simply_harder.custom.data.MasterworkProgression;
import com.infamous.simply_harder.custom.item.EnhancementCoreItem;
import com.infamous.simply_harder.custom.item.GearModItem;
import com.infamous.simply_harder.custom.item.UpgradeModuleItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class GrindstoneHelper {

    public static final int GRINDSTONE_USE_EVENT_ID = 1042;

    public static ItemStack createCustomResult(ItemStack top, ItemStack bottom, Function<ItemStack, ItemStack> tagRemovalFunction) {
        boolean anyPresent = !top.isEmpty() || !bottom.isEmpty();
        boolean bothPresent = !top.isEmpty() && !bottom.isEmpty();
        if (!anyPresent|| bothPresent) { // TODO: Two items in input slot do nothing for now
                return ItemStack.EMPTY;
        } else {
            ItemStack input;
            boolean useTop = !top.isEmpty();
            input = useTop ? top : bottom;
            return tagRemovalFunction.apply(input);
        }
    }

    public static ItemStack removeGearMod(ItemStack input){
        ItemStack result = input.copy();
        result.removeTagKey(GearModItem.GEAR_MOD_TAG);
        return result;
    }

    public static ItemStack removeMasterwork(ItemStack input) {
        ItemStack result = input.copy();
        result.removeTagKey(EnhancementCoreItem.MASTERWORK_TAG);
        return result;
    }

    public static ItemStack removeInfusion(ItemStack input) {
        ItemStack result = input.copy();
        result.removeTagKey(UpgradeModuleItem.UPGRADE_MODULE_TAG);
        return result;
    }

    public static boolean hasSpecialNBT(ItemStack stack) {
        return GearModItem.hasMod(stack)
                || EnhancementCoreItem.hasMasterwork(stack)
                || UpgradeModuleItem.hasInfusedItem(stack);
    }

    public static void refundMasterworkProgression(ItemStack input, ServerLevel serverLevel, Vec3 position) {
        MasterworkProgression progression = EnhancementCoreItem.getProgressionCheckTag(input);
        int highestTier = EnhancementCoreItem.getTierCheckTag(input);
        EnhancementCoreItem.spawnLevelRefund(serverLevel, position, progression, highestTier);
        EnhancementCoreItem.spawnMaterialRefund(serverLevel, position, progression, highestTier);
    }

    public static void refundGearMod(ItemStack input, ServerLevel serverLevel, Vec3 position) {
        GearMod gearMod = GearModItem.getModCheckTag(input);
        GearModItem.spawnGearMod(serverLevel, position, gearMod);
        GearModItem.spawnLevelRefund(serverLevel, position, gearMod);
    }

    public static BiConsumer<Level, BlockPos> spawnDrops(ItemStack input) {
        return (level, blockPos) -> {
            if (level instanceof ServerLevel serverLevel) {
                Vec3 position = Vec3.atCenterOf(blockPos);
                if (GearModItem.hasMod(input)) {
                    refundGearMod(input, serverLevel, position);
                } else if (EnhancementCoreItem.hasMasterwork(input)) {
                    refundMasterworkProgression(input, serverLevel, position);
                } else if (UpgradeModuleItem.hasInfusedItem(input)) {
                    UpgradeModuleItem.spawnInfusedItem(input, serverLevel, position);
                }
            }
            level.levelEvent(GRINDSTONE_USE_EVENT_ID, blockPos, 0);
        };
    }
}
