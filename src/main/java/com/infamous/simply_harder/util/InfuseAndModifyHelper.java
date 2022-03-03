package com.infamous.simply_harder.util;

import com.infamous.simply_harder.custom.item.InfusionCatalystItem;
import com.infamous.simply_harder.custom.item.ModifierCoreItem;
import com.infamous.simply_harder.mixin.ToolActionAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class InfuseAndModifyHelper {
    public static final String REQUIRED_TOOL_ACTIONS_TAG = "RequiredToolActions";
    public static final String REQUIRED_EQUIPMENT_SLOT_TAG = "RequiredEquipmentSlot";
    private static final String REQUIRED_USE_ANIM_TAG = "RequiredUseAnimation";

    public static boolean meetsRequirements(ItemStack base, ItemStack addition) {
        Set<ToolAction> toolActions;
        Optional<UseAnim> requiredUseAnim;
        Optional<EquipmentSlot> requiredEquipmentSlot;

        if(InfusionCatalystItem.isActiveCatalyst(addition)){
            toolActions = InfusionCatalystItem.getRequiredToolActions(addition);
            requiredUseAnim = InfusionCatalystItem.getRequiredUseAnimation(addition);
            requiredEquipmentSlot = InfusionCatalystItem.getRequiredEquipmentSlot(addition);
        } else if(ModifierCoreItem.isFullCore(addition)){
            toolActions = ModifierCoreItem.getRequiredToolActions(addition);
            requiredUseAnim = ModifierCoreItem.getRequiredUseAnimation(addition);
            requiredEquipmentSlot = ModifierCoreItem.getRequiredEquipmentSlot(addition);
        } else{
            return false;
        }

        for(ToolAction toolAction : toolActions){
            if(!base.canPerformAction(toolAction)) return false;
        }
        return requiredUseAnim.map(ua -> base.getUseAnimation() == ua).orElse(true) &&
                requiredEquipmentSlot.map(es -> LivingEntity.getEquipmentSlotForItem(base) == es).orElse(true);
    }

    public static Optional<ToolAction> getToolAction(String name) {
        return Optional.ofNullable(ToolActionAccessor.getActions().get(name));
    }

    public static Set<ToolAction> getRequiredToolActionsFromTag(CompoundTag infusionCoreTag) {
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

    public static void addRequirementsToTag(ItemStack infuseFrom, CompoundTag tag) {
        ListTag toolActionsTag = new ListTag();
        for(ToolAction toolAction : ToolAction.getActions()){ // not ideal, but ItemStacks don't store their performable ToolActions
            if(infuseFrom.canPerformAction(toolAction)){
                toolActionsTag.add(StringTag.valueOf(toolAction.name()));
            }
        }
        if(!toolActionsTag.isEmpty()){
            tag.put(REQUIRED_TOOL_ACTIONS_TAG, toolActionsTag);
        }

        UseAnim useAnim = infuseFrom.getUseAnimation();
        tag.put(REQUIRED_USE_ANIM_TAG, StringTag.valueOf(useAnim.name()));

        EquipmentSlot slot = LivingEntity.getEquipmentSlotForItem(infuseFrom);
        tag.put(REQUIRED_EQUIPMENT_SLOT_TAG, StringTag.valueOf(slot.getName()));
    }

    public static Optional<EquipmentSlot> getRequiredEquipmentSlotFromTag(CompoundTag tag) {
        if(tag.contains(REQUIRED_EQUIPMENT_SLOT_TAG, Tag.TAG_STRING)){
            String slotName = tag.getString(REQUIRED_EQUIPMENT_SLOT_TAG);
            try{
                return Optional.of(EquipmentSlot.byName(slotName));
            } catch (IllegalArgumentException ignored){
            }
        }
        return Optional.empty();
    }

    public static Optional<UseAnim> getRequiredUseAnimationFromTag(CompoundTag tag) {
        if(tag.contains(REQUIRED_USE_ANIM_TAG, Tag.TAG_STRING)){
            String useAnimName = tag.getString(REQUIRED_USE_ANIM_TAG);
            try{
                return Optional.of(UseAnim.valueOf(useAnimName));
            } catch (IllegalArgumentException ignored){
            }
        }
        return Optional.empty();
    }

    public static Optional<Attribute> getAttributeFromTag(CompoundTag tag, String attributeNameTag) {
        ResourceLocation attributeName = ResourceLocation.tryParse(tag.getString(attributeNameTag));
        return Optional.ofNullable(ForgeRegistries.ATTRIBUTES.containsKey(attributeName) ?
                ForgeRegistries.ATTRIBUTES.getValue(attributeName) :
                null);
    }

    @Nullable
    public static ResourceLocation getAttributeId(Attribute attribute) {
        return attribute.getRegistryName();
    }

}
