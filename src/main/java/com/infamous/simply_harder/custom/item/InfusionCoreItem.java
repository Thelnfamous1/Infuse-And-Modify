package com.infamous.simply_harder.custom.item;

import com.infamous.simply_harder.registry.SHItems;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.TierSortingRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfusionCoreItem extends Item {

    public static final String TIER_TAG = "Tier";
    public static final String TIER_LABEL_LOCALIZATION = "item.simply_harder.infusion_core.tier";

    public static final String ARMOR_MATERIAL_TAG = "ArmorMaterial";
    public static final String ARMOR_MATERIAL_LABEL_LOCALIZATION = "item.simply_harder.infusion_core.armor_material";

    public InfusionCoreItem(Properties properties) {
        super(properties);
    }

    public static ItemStack setTierInfusion(ItemStack itemStack, Tier tier){
        if(TierSortingRegistry.isTierSorted(tier)){
            itemStack.getOrCreateTag().putString(TIER_TAG, String.valueOf(TierSortingRegistry.getName(tier)));
        }
        return itemStack;
    }

    public static boolean hasTierInfusion(ItemStack itemStack){
        return itemStack.hasTag() && itemStack.getTag().contains(TIER_TAG);
    }

    public static ResourceLocation getTierInfusion(ItemStack itemStack) {
        return ResourceLocation.tryParse(itemStack.getTag().getString(TIER_TAG));
    }

    public static ItemStack buildTierInfusion(Tier tier) {
        return setTierInfusion(new ItemStack(SHItems.INFUSION_CORE.get()), tier);
    }

    public static ItemStack setArmorMaterialInfusion(ItemStack itemStack, ArmorMaterial armorMaterial){
        itemStack.getOrCreateTag().putString(ARMOR_MATERIAL_TAG, String.valueOf(ResourceLocation.tryParse(armorMaterial.getName())));
        return itemStack;
    }

    public static boolean hasArmorMaterialInfusion(ItemStack itemStack){
        return itemStack.hasTag() && itemStack.getTag().contains(ARMOR_MATERIAL_TAG);
    }

    private static ResourceLocation getArmorMaterialInfusion(ItemStack itemStack) {
        String armorMaterial = itemStack.getTag().getString(ARMOR_MATERIAL_TAG);
        return ResourceLocation.tryParse(armorMaterial);
    }

    public static ItemStack buildArmorMaterialInfusion(ArmorMaterial armorMaterial) {
        return setArmorMaterialInfusion(new ItemStack(SHItems.INFUSION_CORE.get()), armorMaterial);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, components, tooltipFlag);
        if(hasTierInfusion(itemStack)){
            ResourceLocation tierInfusion = getTierInfusion(itemStack);
            components.add((new TranslatableComponent(TIER_LABEL_LOCALIZATION))
                    .append(" ")
                    .append(new TranslatableComponent(buildTierLocalization(tierInfusion))));
        }
        if(hasArmorMaterialInfusion(itemStack)){
            ResourceLocation armorMaterialInfusion = getArmorMaterialInfusion(itemStack);
            components.add((new TranslatableComponent(ARMOR_MATERIAL_LABEL_LOCALIZATION)
                    .append(" ")
                    .append(new TranslatableComponent(buildArmorMaterialLocalization(armorMaterialInfusion)))));
        }
    }

    private static String buildTierLocalization(ResourceLocation location) {
        String nameSpace = location.getNamespace();
        String path = location.getPath();
        return TIER_LABEL_LOCALIZATION + "." + nameSpace + "." + path;
    }

    private static String buildArmorMaterialLocalization(ResourceLocation location) {
        String nameSpace = location.getNamespace();
        String path = location.getPath();
        return ARMOR_MATERIAL_LABEL_LOCALIZATION + "." + nameSpace + "." + path;
    }
}
