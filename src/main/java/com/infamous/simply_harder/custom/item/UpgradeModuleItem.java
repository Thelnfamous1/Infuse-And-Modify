package com.infamous.simply_harder.custom.item;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.registry.SHItems;
import com.infamous.simply_harder.util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UpgradeModuleItem extends Item {

    public static final String NAME = "upgrade_module";
    public static final String UPGRADE_MODULE_TAG = new ResourceLocation(SimplyHarder.MOD_ID, NAME).toString();

    public static final String INFUSED_ITEM_TAG = "InfusedItem";
    public static final ResourceLocation INFUSED_ITEM_LABEL_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, NAME + "/infused_item");

    public static final ResourceLocation UPGRADE_MODULE_LORE_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, NAME + "/lore");


    public UpgradeModuleItem(Properties properties) {
        super(properties);
    }

    public static boolean isUpgradeModule(ItemStack stack){
        return stack.is(SHItems.UPGRADE_MODULE.get());
    }

    private static boolean hasInfusion(ItemStack stack){
        //noinspection ConstantConditions
        return stack.hasTag() && stack.getTag().contains(UPGRADE_MODULE_TAG, Tag.TAG_COMPOUND);
    }

    public static boolean isInfusedUpgradeModule(ItemStack stack) {
        return isUpgradeModule(stack) && hasInfusedItem(stack);
    }

    public static boolean isNonInfusedUpgradeModule(ItemStack base) {
        return isUpgradeModule(base) && !hasInfusedItem(base);
    }

    public static void clearUpgradeModule(ItemStack stack){
        if(stack.hasTag()){
            stack.getTag().remove(UPGRADE_MODULE_TAG);
        }
    }

    public static void setInfusedItem(ItemStack infuseTo, ItemStack infuseFrom) {
        CompoundTag upgradeModuleTag = getUpgradeModule(infuseTo);
        upgradeModuleTag.put(INFUSED_ITEM_TAG, infuseFrom.save(new CompoundTag()));
    }

    public static boolean hasInfusedItem(ItemStack stack){
        if(!hasInfusion(stack)){
            return false;
        }
        return getUpgradeModule(stack).contains(INFUSED_ITEM_TAG, Tag.TAG_COMPOUND);
    }

    public static ItemStack getInfusedItem(ItemStack stack){
        CompoundTag upgradeModule = getUpgradeModule(stack);
        return ItemStack.of(upgradeModule.getCompound(INFUSED_ITEM_TAG));
    }

    public static Item getInfusedItemType(ItemStack stack){
        CompoundTag upgradeModuleTag = getUpgradeModule(stack);
        CompoundTag infusedItemTag = upgradeModuleTag.getCompound(INFUSED_ITEM_TAG);
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(infusedItemTag.getString("id")));
    }

    private static CompoundTag getUpgradeModule(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        if (!tag.contains(UPGRADE_MODULE_TAG, Tag.TAG_COMPOUND)) {
            tag.put(UPGRADE_MODULE_TAG, new CompoundTag());
        }
        return tag.getCompound(UPGRADE_MODULE_TAG);
    }

    private static void appendInfusedItemText(ItemStack itemStack, List<Component> tooltip, TooltipFlag tooltipFlag) {
        if(tooltipFlag.isAdvanced()){
            ItemStack infusedItem = getInfusedItem(itemStack);
            tooltip.add(
                    (new TranslatableComponent(Util.makeDescriptionId("item", INFUSED_ITEM_LABEL_LOCALIZATION)))
                            .withStyle(ChatFormatting.GRAY)
            );
            List<Component> tooltipLines = infusedItem.getTooltipLines((Player)null, tooltipFlag);
            tooltip.addAll(tooltipLines);
        } else{
            Item infusedItemType = getInfusedItemType(itemStack);
            tooltip.add(
                    (new TranslatableComponent(Util.makeDescriptionId("item", INFUSED_ITEM_LABEL_LOCALIZATION)))
                            .withStyle(ChatFormatting.GRAY)
                            .append(" ")
                            .append(
                                    (new TranslatableComponent(infusedItemType.getDescriptionId()))
                                            .withStyle(ChatFormatting.GREEN)
                            )
            );
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, tooltip, tooltipFlag);

        TooltipHelper.appendLore(tooltip, UPGRADE_MODULE_LORE_LOCALIZATION);

        if(hasInfusedItem(itemStack)){
            tooltip.add(TextComponent.EMPTY);
            appendInfusedItemText(itemStack, tooltip, tooltipFlag);
        }
    }

}
