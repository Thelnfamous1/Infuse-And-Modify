package com.infamous.simply_harder.custom.item;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.registry.SHItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ModificationItem extends Item {

    public static final String NAME = "modification";
    public static final String MODIFICATION = new ResourceLocation(SimplyHarder.MOD_ID, NAME).toString();

    public ModificationItem(Properties properties) {
        super(properties);
    }

    public static boolean isMod(ItemStack itemStack) {
        return itemStack.is(SHItems.MODIFICATION.get());
    }

    public static boolean hasMod(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(MODIFICATION, Tag.TAG_COMPOUND);
    }

    public static boolean isUsableMod(ItemStack itemStack) {
        return isMod(itemStack) && hasMod(itemStack);
    }

    public static boolean isNonUsableMod(ItemStack itemStack) {
        return isMod(itemStack) && !hasMod(itemStack);
    }

    private static CompoundTag getMod(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        if (!tag.contains(MODIFICATION, Tag.TAG_COMPOUND)) {
            tag.put(MODIFICATION, new CompoundTag());
        }
        return tag.getCompound(MODIFICATION);
    }

    public static void clearMod(ItemStack stack){
        if(stack.hasTag()){
            stack.getTag().remove(MODIFICATION);
        }
    }

    public static boolean isValidForMod(ItemStack left, ItemStack right) {
        CompoundTag modifierTag = getMod(right);
        return left.isDamageableItem(); // TODO: Temporary, pending Modifier system impl
    }

    public static void addMod(ItemStack left, ItemStack right) {
        CompoundTag modifierTag = getMod(right);
        // TODO: Modifier system
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
        }
    }
}
