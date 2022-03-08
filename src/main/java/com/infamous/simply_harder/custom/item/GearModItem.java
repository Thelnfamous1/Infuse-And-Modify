package com.infamous.simply_harder.custom.item;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.registry.SHItems;
import com.infamous.simply_harder.util.TooltipHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class GearModItem extends Item {

    public static final String NAME = "gear_mod";
    public static final String GEAR_MOD_TAG = new ResourceLocation(SimplyHarder.MOD_ID, NAME).toString();

    public static final ResourceLocation INSTALLED_MOD_LABEL_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, NAME + "/installed_mod");

    public GearModItem(Properties properties) {
        super(properties);
    }

    public static boolean isMod(ItemStack itemStack) {
        return itemStack.is(SHItems.GEAR_MOD.get());
    }

    public static boolean hasMod(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(GEAR_MOD_TAG, Tag.TAG_COMPOUND);
    }

    public static boolean isUsableMod(ItemStack itemStack) {
        return isMod(itemStack) && hasMod(itemStack);
    }

    public static boolean isNonUsableMod(ItemStack itemStack) {
        return isMod(itemStack) && !hasMod(itemStack);
    }

    public static CompoundTag getMod(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        if (!tag.contains(GEAR_MOD_TAG, Tag.TAG_COMPOUND)) {
            tag.put(GEAR_MOD_TAG, new CompoundTag());
        }
        return tag.getCompound(GEAR_MOD_TAG);
    }

    public static void clearMod(ItemStack stack){
        if(stack.hasTag()){
            stack.getTag().remove(GEAR_MOD_TAG);
        }
    }

    public static boolean isValidForMod(ItemStack left, ItemStack right) {
        CompoundTag modifierTag = getMod(right);
        return left.isDamageableItem(); // TODO: Temporary, pending Gear Mod system impl
    }

    public static void addMod(ItemStack left, ItemStack right) {
        CompoundTag modifierTag = getMod(right);
        // TODO: Gear Mod system
    }

    public static String getModName(ItemStack stack) {
        CompoundTag modifierTag = getMod(stack);
        return "";
        // TODO: Gear Mod system
    }

    public static int getLevelCost(ItemStack right) {
        return 0;
    }

    public static int getMaterialCost(ItemStack right) {
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> toolTip, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, toolTip, tooltipFlag);

        if(hasMod(itemStack)){
            TooltipHelper.appendModificationLines(toolTip, itemStack);
        }
    }
}
