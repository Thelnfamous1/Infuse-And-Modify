package com.infamous.simply_harder.mixin;

import com.infamous.simply_harder.util.GrindstoneHelper;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = {"net/minecraft/world/inventory/GrindstoneMenu$2",
        "net/minecraft/world/inventory/GrindstoneMenu$3"})
public class GrindstoneMenuRepairSlotsMixin {

    @Inject(method = "mayPlace", at = @At(value = "RETURN"), cancellable = true)
    private void handleMayPlace(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if(GrindstoneHelper.hasSpecialNBT(stack)) cir.setReturnValue(true);
    }
}
