package com.infamous.simply_harder.custom.item;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.custom.WrappedAttributeModifier;
import com.infamous.simply_harder.custom.WrappedAttributeModifierMap;
import com.infamous.simply_harder.registry.SHItems;
import com.infamous.simply_harder.util.AttributeHelper;
import com.infamous.simply_harder.util.TooltipHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class EnhancementCoreItem extends Item {
    public static final String NAME = "enhancement_core";
    public static final String MASTERWORK_NAME = "masterwork";
    public static final String MASTERWORK_TAG = new ResourceLocation(SimplyHarder.MOD_ID, MASTERWORK_NAME).toString();
    public static final String TIER_TAG = "Tier";

    public static final ResourceLocation ENHANCEMENT_CORE_LORE_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, NAME + "/lore");

    public EnhancementCoreItem(Properties properties) {
        super(properties);
    }

    public static boolean hasMasterwork(ItemStack itemStack) {
        return itemStack.hasTag() && itemStack.getTag().contains(MASTERWORK_TAG, Tag.TAG_COMPOUND);
    }

    public static CompoundTag getMasterwork(ItemStack stack){
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(MASTERWORK_TAG, Tag.TAG_COMPOUND)) {
            tag.put(MASTERWORK_TAG, new CompoundTag());
        }
        return tag.getCompound(MASTERWORK_TAG);
    }

    public static int getTierCheckTag(ItemStack stack){
        return hasMasterwork(stack) ? getTier(stack) : 0;
    }

    public static int getTier(ItemStack itemStack) {
        CompoundTag masterworkTag = getMasterwork(itemStack);

        return masterworkTag.getInt(TIER_TAG);
    }

    public static boolean isEnhancementCore(ItemStack right) {
        return right.is(SHItems.ENHANCEMENT_CORE.get());
    }

    public static void setTier(ItemStack left, int tier) {
        CompoundTag masterworkTag = getMasterwork(left);
        masterworkTag.putInt(TIER_TAG, tier);
    }

    public static void addModifiers(ItemStack result, WrappedAttributeModifierMap modifierMap) {
        CompoundTag masterworkTag = getMasterwork(result);

        for(Map.Entry<Attribute, WrappedAttributeModifier> entry : modifierMap.modifiers().entries()){
            WrappedAttributeModifier value = entry.getValue();
            AttributeHelper.addAttributeModifier(masterworkTag, entry.getKey(), value.modifier(), value.slot());
        }
    }

    public static void clearModifiers(ItemStack result) {
        CompoundTag masterworkTag = getMasterwork(result);

        masterworkTag.remove(AttributeHelper.ATTRIBUTE_MODIFIERS_TAG);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltip, tooltipFlag);

        TooltipHelper.appendLore(tooltip, ENHANCEMENT_CORE_LORE_LOCALIZATION);
    }
}
