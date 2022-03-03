package com.infamous.simply_harder.custom.item;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.mixin.ToolActionAccessor;
import com.infamous.simply_harder.util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class InfusionCatalystItem extends Item {

    public static final String NAME = "infusion_catalyst";
    public static final String INFUSION_CATALYST_TAG = new ResourceLocation(SimplyHarder.MOD_ID, NAME).toString();

    public static final String INFUSED_ITEM_TAG = "InfusedItem";
    public static final ResourceLocation INFUSED_ITEM_LABEL_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, NAME + "/infused_item");

    public static final String REQUIRED_TOOL_ACTIONS_TAG = "RequiredToolActions";
    public static final ResourceLocation REQUIRED_TOOL_ACTIONS_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, NAME + "/required_tool_actions");

    public static final String REQUIRED_EQUIPMENT_SLOT_TAG = "RequiredEquipmentSlot";
    public static final ResourceLocation REQUIRED_EQUIPMENT_SLOT_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, NAME + "/required_equipment_slot");

    public static final ResourceLocation INACTIVE_LABEL_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, NAME + "/inactive");

    public InfusionCatalystItem(Properties properties) {
        super(properties);
    }

    public static boolean isInfusionCatalyst(ItemStack stack){
        return stack.getItem() instanceof InfusionCatalystItem;
    }

    private static boolean hasInfusion(ItemStack stack){
        //noinspection ConstantConditions
        return stack.hasTag() && stack.getTag().contains(INFUSION_CATALYST_TAG, Tag.TAG_COMPOUND);
    }

    public static void clearInfusion(ItemStack stack){
        if(stack.hasTag()){
            stack.getTag().remove(INFUSION_CATALYST_TAG);
        }
    }

    public static void setInfusedItem(ItemStack infuseTo, ItemStack infuseFrom) {
        CompoundTag infusionCoreTag = getInfusion(infuseTo);
        infusionCoreTag.put(INFUSED_ITEM_TAG, infuseFrom.save(new CompoundTag()));
        ListTag toolActionsTag = new ListTag();
        for(ToolAction toolAction : ToolAction.getActions()){ // not ideal, but ItemStacks don't store their performable ToolActions
            if(infuseFrom.canPerformAction(toolAction)){
                toolActionsTag.add(StringTag.valueOf(toolAction.name()));
            }
        }
        if(!toolActionsTag.isEmpty()){
            infusionCoreTag.put(REQUIRED_TOOL_ACTIONS_TAG, toolActionsTag);
        }
        EquipmentSlot slot = LivingEntity.getEquipmentSlotForItem(infuseFrom);
        infusionCoreTag.put(REQUIRED_EQUIPMENT_SLOT_TAG, StringTag.valueOf(slot.getName()));
    }

    public static boolean hasInfusedItem(ItemStack stack){
        if(!hasInfusion(stack)){
            return false;
        }
        return getInfusion(stack).contains(INFUSED_ITEM_TAG, Tag.TAG_COMPOUND);
    }

    public static ItemStack getInfusedItem(ItemStack stack){
        CompoundTag infusionCoreTag = getInfusion(stack);
        return ItemStack.of(infusionCoreTag.getCompound(INFUSED_ITEM_TAG));
    }

    private static CompoundTag getInfusion(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        if (!tag.contains(INFUSION_CATALYST_TAG, Tag.TAG_COMPOUND)) {
            tag.put(INFUSION_CATALYST_TAG, new CompoundTag());
        }
        return tag.getCompound(INFUSION_CATALYST_TAG);
    }

    public static Set<ToolAction> getRequiredToolActions(ItemStack itemStack) {
        CompoundTag infusionCoreTag = getInfusion(itemStack);

        Set<ToolAction> toolActions = new HashSet<>();
        if(infusionCoreTag.contains(REQUIRED_TOOL_ACTIONS_TAG, Tag.TAG_LIST)){
            ListTag toolActionsTag = infusionCoreTag.getList(REQUIRED_TOOL_ACTIONS_TAG, Tag.TAG_STRING);
            for(Tag tag : toolActionsTag){
                Optional<ToolAction> toolAction = getToolAction(tag.getAsString());
                toolAction.ifPresent(toolActions::add);
            }
        }
        return toolActions;
    }

    private static Optional<ToolAction> getToolAction(String name) {
        return Optional.ofNullable(ToolActionAccessor.getActions().get(name));
    }

    public static Optional<EquipmentSlot> getRequiredEquipmentSlot(ItemStack itemStack) {
        CompoundTag infusionCoreTag = getInfusion(itemStack);
        if(infusionCoreTag.contains(REQUIRED_EQUIPMENT_SLOT_TAG, Tag.TAG_STRING)){
            String slotName = infusionCoreTag.getString(REQUIRED_EQUIPMENT_SLOT_TAG);
            try{
                return Optional.of(EquipmentSlot.byName(slotName));
            } catch (IllegalArgumentException ignored){
            }
        }
        return Optional.empty();
    }

    public static boolean isActiveCatalyst(ItemStack stack) {
        return isInfusionCatalyst(stack) && hasInfusedItem(stack);
    }

    public static boolean isInactiveCatalyst(ItemStack base) {
        return isInfusionCatalyst(base) && !hasInfusedItem(base);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, components, tooltipFlag);
        if(hasInfusedItem(itemStack)){
            appendInfusedItemText(itemStack, components);
            appendRequiredToolActionsText(itemStack, components);
            appendRequiredEquipmentSlotText(itemStack, components);
        } else{
            components.add((new TranslatableComponent(Util.makeDescriptionId("item", INACTIVE_LABEL_LOCALIZATION))).withStyle(ChatFormatting.GRAY));
        }
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

    private static void appendRequiredToolActionsText(ItemStack itemStack, List<Component> components) {
        Set<ToolAction> requiredToolActions = getRequiredToolActions(itemStack);
        if(!requiredToolActions.isEmpty()){
            components.add(TextComponent.EMPTY);
            components.add((new TranslatableComponent(Util.makeDescriptionId("item", REQUIRED_TOOL_ACTIONS_LOCALIZATION)))
                    .withStyle(ChatFormatting.GRAY));
            for(ToolAction toolAction : requiredToolActions){
                components.add(
                        (new TranslatableComponent(Util.makeDescriptionId("item", TooltipHelper.buildToolActionLocalization(toolAction))))
                                .withStyle(ChatFormatting.GREEN)
                );
            }
        }
    }

    private static void appendRequiredEquipmentSlotText(ItemStack itemStack, List<Component> components) {
        Optional<EquipmentSlot> requiredEquipmentSlot = getRequiredEquipmentSlot(itemStack);
        requiredEquipmentSlot.ifPresent(es -> {
                components.add(TextComponent.EMPTY);
                components.add(
                (new TranslatableComponent(Util.makeDescriptionId("item", REQUIRED_EQUIPMENT_SLOT_LOCALIZATION)))
                        .withStyle(ChatFormatting.GRAY)
                        .append(" ")
                        .append(
                                (new TranslatableComponent(Util.makeDescriptionId("item", TooltipHelper.buildEquipmentSlotLocalization(es))))
                                        .withStyle(ChatFormatting.GREEN)
                        )
                );
        }
        );
    }

}
