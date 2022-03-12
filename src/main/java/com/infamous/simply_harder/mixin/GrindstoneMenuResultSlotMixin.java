package com.infamous.simply_harder.mixin;

import com.infamous.simply_harder.custom.item.EnhancementCoreItem;
import com.infamous.simply_harder.custom.item.GearModItem;
import com.infamous.simply_harder.util.GrindstoneHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/world/inventory/GrindstoneMenu$4")
public class GrindstoneMenuResultSlotMixin {

    @Inject(method = "onTake", at = @At(value = "HEAD"), cancellable = true)
    private void handleOnTake(Player player, ItemStack toTake, CallbackInfo ci) {
        if(player.containerMenu instanceof GrindstoneMenu menu){
            GrindstoneMenuAccessor menuAccessor = (GrindstoneMenuAccessor) menu;
            ItemStack top = menuAccessor.getRepairSlots().getItem(GrindstoneMenu.INPUT_SLOT);
            ItemStack bottom = menuAccessor.getRepairSlots().getItem(GrindstoneMenu.ADDITIONAL_SLOT);
            boolean anyPresent = !top.isEmpty() || !bottom.isEmpty();
            boolean bothPresent = !top.isEmpty() && !bottom.isEmpty();
            if(!anyPresent || bothPresent) return; // TODO: Two items in input slot do nothing for now

            ItemStack input = !top.isEmpty() ? top : bottom;
            menuAccessor.getAccess().execute((level, blockPos) -> {
                if(level instanceof ServerLevel serverLevel){
                    Vec3 position = Vec3.atCenterOf(blockPos);
                    if (GearModItem.hasMod(input)) {
                        GrindstoneHelper.refundGearMod(input, serverLevel, position);
                    } else if(EnhancementCoreItem.hasMasterwork(input)){
                        GrindstoneHelper.refundMasterworkProgression(input, serverLevel, position);
                    }
                }
                level.levelEvent(GrindstoneHelper.GRINDSTONE_USE_EVENT_ID, blockPos, 0);
            });
            menuAccessor.getRepairSlots().setItem(GrindstoneMenu.INPUT_SLOT, ItemStack.EMPTY);
            menuAccessor.getRepairSlots().setItem(GrindstoneMenu.ADDITIONAL_SLOT, ItemStack.EMPTY);
            ci.cancel();
        }
    }

}
