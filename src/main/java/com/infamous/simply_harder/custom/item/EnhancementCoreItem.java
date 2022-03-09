package com.infamous.simply_harder.custom.item;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.custom.data.MasterworkProgression;
import com.infamous.simply_harder.custom.data.WrappedAttributeModifier;
import com.infamous.simply_harder.custom.data.WrappedAttributeModifierMap;
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
    public static final String MASTERWORK_PROGRESSION_TAG = "MasterworkProgression";

    public EnhancementCoreItem(Properties properties) {
        super(properties);
    }

    public static boolean isCore(ItemStack right) {
        return right.is(SHItems.ENHANCEMENT_CORE.get());
    }

    public static boolean hasMasterwork(ItemStack itemStack) {
        return itemStack.hasTag() && itemStack.getTag().contains(MASTERWORK_TAG, Tag.TAG_COMPOUND);
    }

    private static CompoundTag getMasterwork(ItemStack stack){
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(MASTERWORK_TAG, Tag.TAG_COMPOUND)) {
            tag.put(MASTERWORK_TAG, new CompoundTag());
        }
        return tag.getCompound(MASTERWORK_TAG);
    }

    private static int getTier(ItemStack itemStack) {
        CompoundTag masterworkTag = getMasterwork(itemStack);

        return masterworkTag.getInt(TIER_TAG);
    }

    public static int getTierCheckTag(ItemStack stack){
        return hasMasterwork(stack) ? getTier(stack) : 0;
    }

    private static String getProgressionName(ItemStack stack) {
        CompoundTag masterworkTag = getMasterwork(stack);
        return masterworkTag.getString(MASTERWORK_PROGRESSION_TAG);
    }

    public static String getProgressionNameCheckTag(ItemStack stack) {
        return hasMasterwork(stack) ? getProgressionName(stack) : MasterworkProgression.NONE.getId().toString();
    }

    private static MasterworkProgression getProgression(ItemStack stack) {
        String progressionName = getProgressionNameCheckTag(stack);
        ResourceLocation progressionId = new ResourceLocation(progressionName);

        return SimplyHarder.MASTERWORK_PROGRESSION_MANAGER.getProgression(progressionId).orElse(MasterworkProgression.NONE);
    }

    public static MasterworkProgression getProgressionCheckTag(ItemStack stack){
        return hasMasterwork(stack) ? getProgression(stack) : MasterworkProgression.NONE;
    }

    public static void setTier(ItemStack left, int tier) {
        CompoundTag masterworkTag = getMasterwork(left);
        masterworkTag.putInt(TIER_TAG, tier);
    }

    public static void setProgression(ItemStack result, ResourceLocation progressionId) {
        CompoundTag masterworkTag = getMasterwork(result);
        masterworkTag.putString(MASTERWORK_PROGRESSION_TAG, progressionId.toString());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltip, tooltipFlag);

        TooltipHelper.appendLore(tooltip, ENHANCEMENT_CORE_LORE_LOCALIZATION);
    }
}
