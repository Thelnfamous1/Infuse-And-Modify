package com.infamous.simply_harder.custom.item;

import com.infamous.simply_harder.custom.data.GearMod;
import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.registry.SHItems;
import com.infamous.simply_harder.util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class GearModItem extends Item {

    public static final String NAME = "gear_mod";
    public static final String GEAR_MOD_TAG = new ResourceLocation(SimplyHarder.MOD_ID, NAME).toString();
    public static final String INTERNAL_NAME = NAME + "/internal";
    public static final String INTERNAL_TAG = new ResourceLocation(SimplyHarder.MOD_ID, INTERNAL_NAME).toString();

    public static final String INSTALLED_LOCALIZATION_STRING = NAME + "/installed";
    public static final ResourceLocation INSTALLED_MOD_LABEL_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, INSTALLED_LOCALIZATION_STRING);
    public static final ResourceLocation NAME_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, NAME + "/name");
    public static final String GEAR_MOD_NAME = "GearModName";

    public GearModItem(Properties properties) {
        super(properties);
    }

    public static boolean isMod(ItemStack itemStack) {
        return itemStack.is(SHItems.GEAR_MOD.get());
    }

    public static boolean hasInternal(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(INTERNAL_TAG, Tag.TAG_COMPOUND);
    }

    public static boolean hasMod(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(GEAR_MOD_TAG, Tag.TAG_COMPOUND);
    }

    private static CompoundTag getInternalTag(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        if (!tag.contains(INTERNAL_TAG, Tag.TAG_COMPOUND)) {
            tag.put(INTERNAL_TAG, new CompoundTag());
        }
        return tag.getCompound(INTERNAL_TAG);
    }

    public static CompoundTag getModTag(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        if (!tag.contains(GEAR_MOD_TAG, Tag.TAG_COMPOUND)) {
            tag.put(GEAR_MOD_TAG, new CompoundTag());
        }
        return tag.getCompound(GEAR_MOD_TAG);
    }

    public static void clearModTag(ItemStack stack){
        if(stack.hasTag()){
            stack.getTag().remove(GEAR_MOD_TAG);
        }
    }

    private static String getInternalModName(ItemStack stack) {
        CompoundTag gearModTag = getInternalTag(stack);
        return gearModTag.getString(GEAR_MOD_NAME);
    }

    private static String getModName(ItemStack stack) {
        CompoundTag gearModTag = getModTag(stack);
        return gearModTag.getString(GEAR_MOD_NAME);
    }

    public static String getInternalModNameCheckTag(ItemStack stack){
        return hasInternal(stack) ? getInternalModName(stack) : "";
    }

    public static String getModNameCheckTag(ItemStack stack){
        return hasMod(stack) ? getModName(stack) : "";
    }

    public static GearMod getInternalModCheckTag(ItemStack stack){
        String gearModName = getInternalModNameCheckTag(stack);
        ResourceLocation gearModId = new ResourceLocation(gearModName);
        return SimplyHarder.GEAR_MOD_MANAGER.getGearMod(gearModId).orElse(GearMod.UNKNOWN);
    }

    public static GearMod getModCheckTag(ItemStack stack){
        String gearModName = getModNameCheckTag(stack);
        ResourceLocation gearModId = new ResourceLocation(gearModName);
        return SimplyHarder.GEAR_MOD_MANAGER.getGearMod(gearModId).orElse(GearMod.UNKNOWN);
    }

    public static void setInternal(ItemStack stack, GearMod gearMod) {
        CompoundTag internalTag = getInternalTag(stack);
        internalTag.putString(GEAR_MOD_NAME, gearMod.id().toString());
    }

    public static void setMod(ItemStack left, GearMod gearMod) {
        CompoundTag gearModTag = getModTag(left);
        gearModTag.putString(GEAR_MOD_NAME, gearMod.id().toString());
    }

    public static int getLevelCostCheckTag(ItemStack right) {
        GearMod gearMod = getInternalModCheckTag(right);

        return gearMod.levelCost();
    }

    public static void appendInternalModLines(List<Component> toolTip, ItemStack itemStack) {
        GearMod gearMod = getInternalModCheckTag(itemStack);
        toolTip.add(
                (new TranslatableComponent(Util.makeDescriptionId(TooltipHelper.ITEM, NAME_LOCALIZATION)))
                        .withStyle(ChatFormatting.GRAY)
                        .append(" ")
                        .append(
                                (new TranslatableComponent(Util.makeDescriptionId(NAME, gearMod.id())))
                                        .withStyle(ChatFormatting.GREEN)
                        )
        );
    }

    public static ItemStack createGearMod(GearMod gearMod) {
        ItemStack gearModItem = SHItems.GEAR_MOD.get().getDefaultInstance();
        setInternal(gearModItem, gearMod);
        return gearModItem;
    }

    public static void spawnGearMod(Level level, Vec3 position, GearMod gearMod) {
        ItemStack gearModItem = createGearMod(gearMod);
        level.addFreshEntity(new ItemEntity(level, position.x, position.y, position.z, gearModItem));
    }

    public static void spawnLevelRefund(ServerLevel level, Vec3 position, GearMod gearMod){
        ExperienceOrb.award(level, position, gearMod.levelRefund());
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> toolTip, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, toolTip, tooltipFlag);

        if(hasInternal(itemStack)){
            appendInternalModLines(toolTip, itemStack);
            TooltipHelper.appendAttributeModifiersLinesFromGearMod(getInternalModCheckTag(itemStack), toolTip, GearModItem.NAME);
        }
    }
}
