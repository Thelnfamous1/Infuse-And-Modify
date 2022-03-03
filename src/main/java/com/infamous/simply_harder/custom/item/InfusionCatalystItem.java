package com.infamous.simply_harder.custom.item;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.util.InfuseAndModifyHelper;
import com.infamous.simply_harder.util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class InfusionCatalystItem extends Item {

    public static final String NAME = "infusion_catalyst";
    public static final String INFUSION_CATALYST_TAG = new ResourceLocation(SimplyHarder.MOD_ID, NAME).toString();

    public static final String INFUSED_ITEM_TAG = "InfusedItem";
    public static final ResourceLocation INFUSED_ITEM_LABEL_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, NAME + "/infused_item");

    public static final ResourceLocation INACTIVE_LABEL_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, NAME + "/inactive");

    public InfusionCatalystItem(Properties properties) {
        super(properties);
    }

    public static boolean isInfusionCatalyst(ItemStack stack){
        return stack.getItem() instanceof InfusionCatalystItem;
    }

    private static boolean hasInfusionCatalyst(ItemStack stack){
        //noinspection ConstantConditions
        return stack.hasTag() && stack.getTag().contains(INFUSION_CATALYST_TAG, Tag.TAG_COMPOUND);
    }

    public static boolean isActiveCatalyst(ItemStack stack) {
        return isInfusionCatalyst(stack) && hasInfusedItem(stack);
    }

    public static boolean isInactiveCatalyst(ItemStack base) {
        return isInfusionCatalyst(base) && !hasInfusedItem(base);
    }

    public static void clearInfusionCatalyst(ItemStack stack){
        if(stack.hasTag()){
            stack.getTag().remove(INFUSION_CATALYST_TAG);
        }
    }

    public static void setInfusedItem(ItemStack infuseTo, ItemStack infuseFrom) {
        CompoundTag infusionCoreTag = getInfusionCatalyst(infuseTo);
        infusionCoreTag.put(INFUSED_ITEM_TAG, infuseFrom.save(new CompoundTag()));
        InfuseAndModifyHelper.addRequirementsToTag(infuseFrom, infusionCoreTag);
    }

    public static boolean hasInfusedItem(ItemStack stack){
        if(!hasInfusionCatalyst(stack)){
            return false;
        }
        return getInfusionCatalyst(stack).contains(INFUSED_ITEM_TAG, Tag.TAG_COMPOUND);
    }

    public static ItemStack getInfusedItem(ItemStack stack){
        CompoundTag infusionCoreTag = getInfusionCatalyst(stack);
        return ItemStack.of(infusionCoreTag.getCompound(INFUSED_ITEM_TAG));
    }

    private static CompoundTag getInfusionCatalyst(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        if (!tag.contains(INFUSION_CATALYST_TAG, Tag.TAG_COMPOUND)) {
            tag.put(INFUSION_CATALYST_TAG, new CompoundTag());
        }
        return tag.getCompound(INFUSION_CATALYST_TAG);
    }

    public static Set<ToolAction> getRequiredToolActions(ItemStack itemStack) {
        CompoundTag infusionCatalystTag = getInfusionCatalyst(itemStack);
        return InfuseAndModifyHelper.getRequiredToolActionsFromTag(infusionCatalystTag);
    }

    public static Optional<UseAnim> getRequiredUseAnimation(ItemStack itemStack) {
        CompoundTag infusionCatalystTag = getInfusionCatalyst(itemStack);
        return InfuseAndModifyHelper.getRequiredUseAnimationFromTag(infusionCatalystTag);
    }

    public static Optional<EquipmentSlot> getRequiredEquipmentSlot(ItemStack itemStack) {
        CompoundTag infusionCatalystTag = getInfusionCatalyst(itemStack);
        return InfuseAndModifyHelper.getRequiredEquipmentSlotFromTag(infusionCatalystTag);
    }

    private static void appendInfusedItemText(ItemStack itemStack, List<Component> components) {
        ItemStack infusedItem = getInfusedItem(itemStack);
        components.add(
                (new TranslatableComponent(Util.makeDescriptionId("item", INFUSED_ITEM_LABEL_LOCALIZATION)))
                        .withStyle(ChatFormatting.GRAY)
                        .append(" ")
                        .append(
                                (new TranslatableComponent(infusedItem.getDescriptionId()))
                                        .withStyle(ChatFormatting.GREEN)
                        )
        );
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> toolTip, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, toolTip, tooltipFlag);
        if(hasInfusedItem(itemStack)){
            appendInfusedItemText(itemStack, toolTip);
            TooltipHelper.appendRequirements(toolTip, getRequiredToolActions(itemStack), getRequiredUseAnimation(itemStack), getRequiredEquipmentSlot(itemStack));
        } else{
            toolTip.add((new TranslatableComponent(Util.makeDescriptionId("item", INACTIVE_LABEL_LOCALIZATION))).withStyle(ChatFormatting.GRAY));
        }
    }

}
