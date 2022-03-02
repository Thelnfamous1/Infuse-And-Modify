package com.infamous.simply_harder.datagen.util;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.custom.ModNBTIngredient;
import com.infamous.simply_harder.custom.item.InfusionCoreItem;
import com.infamous.simply_harder.datagen.ModItemTagsProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.UpgradeRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class InfusionRecipeHelper {
    public static void infusion(Consumer<FinishedRecipe> onFinished, Tag<Item> tag, ArmorMaterial armorMaterial, Item armor) {
        ItemStack armorMaterialInfusion = InfusionCoreItem.buildArmorMaterialInfusion(armorMaterial);

        infusion(onFinished, tag, armorMaterialInfusion, armor);
    }

    public static void infusion(Consumer<FinishedRecipe> onFinished, Tier tier, Item axe, Item hoe, Item pickaxe, Item shovel, Item sword) {
        ItemStack tierInfusion = InfusionCoreItem.buildTierInfusion(tier);

        infusion(onFinished, ModItemTagsProvider.AXES, tierInfusion, axe);
        infusion(onFinished, ModItemTagsProvider.HOES, tierInfusion, hoe);
        infusion(onFinished, ModItemTagsProvider.PICKAXES, tierInfusion, pickaxe);
        infusion(onFinished, ModItemTagsProvider.SHOVELS, tierInfusion, shovel);
        infusion(onFinished, ModItemTagsProvider.SWORDS, tierInfusion, sword);
    }

    public static void infusion(Consumer<FinishedRecipe> onFinished, ArmorMaterial armorMaterial, Item boots, Item chestplate, Item helmets, Item leggings) {
        ItemStack armorMaterialInfusion = InfusionCoreItem.buildArmorMaterialInfusion(armorMaterial);

        infusion(onFinished, ModItemTagsProvider.BOOTS, armorMaterialInfusion, boots);
        infusion(onFinished, ModItemTagsProvider.CHESTPLATES, armorMaterialInfusion, chestplate);
        infusion(onFinished, ModItemTagsProvider.HELMETS, armorMaterialInfusion, helmets);
        infusion(onFinished, ModItemTagsProvider.LEGGINGS, armorMaterialInfusion, leggings);
    }

    private static void infusion(Consumer<FinishedRecipe> onFinished, Tag<Item> base, ItemStack infusion, Item result) {
        UpgradeRecipeBuilder
                .smithing(Ingredient.of(base), new ModNBTIngredient(infusion), result)
                .unlocks("has_infusion_core", RecipeProviderHelper.hasWithNbt(infusion.getItem(), infusion.getTag()))
                .save(onFinished, new ResourceLocation(SimplyHarder.MOD_ID, result.getRegistryName().getPath() + "_infusion"));
    }
}
