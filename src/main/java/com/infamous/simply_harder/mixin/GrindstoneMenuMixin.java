package com.infamous.simply_harder.mixin;

import com.infamous.simply_harder.custom.item.EnhancementCoreItem;
import com.infamous.simply_harder.custom.item.GearModItem;
import com.infamous.simply_harder.custom.item.UpgradeModuleItem;
import com.infamous.simply_harder.util.GrindstoneHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(GrindstoneMenu.class)
public abstract class GrindstoneMenuMixin extends AbstractContainerMenu {

    @Shadow @Final Container repairSlots;
    @Shadow @Final private Container resultSlots;

    protected GrindstoneMenuMixin(@Nullable MenuType<?> menuType, int networkId) {
        super(menuType, networkId);
    }

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void handleCreateResult(CallbackInfo ci){
        ItemStack top = this.repairSlots.getItem(GrindstoneMenu.INPUT_SLOT);
        ItemStack bottom = this.repairSlots.getItem(GrindstoneMenu.ADDITIONAL_SLOT);
        boolean checkGearMod = GearModItem.hasMod(top) || GearModItem.hasMod(bottom);
        boolean checkMasterwork = EnhancementCoreItem.hasMasterwork(top) || EnhancementCoreItem.hasMasterwork(bottom);
        boolean checkInfusion = UpgradeModuleItem.hasInfusedItem(top) || UpgradeModuleItem.hasInfusedItem(bottom);
        boolean createCustomResult = checkGearMod || checkMasterwork || checkInfusion;
        if(createCustomResult){
            Function<ItemStack, ItemStack> tagRemovalFunction = checkGearMod ?
                    GrindstoneHelper::removeGearMod :
                    checkMasterwork ?
                            GrindstoneHelper::removeMasterwork :
                            GrindstoneHelper::removeInfusion;
            ItemStack customResult = GrindstoneHelper.createCustomResult(top, bottom, tagRemovalFunction);
            this.resultSlots.setItem(0, customResult);
            this.broadcastChanges();
            ci.cancel();
        }
    }
}
