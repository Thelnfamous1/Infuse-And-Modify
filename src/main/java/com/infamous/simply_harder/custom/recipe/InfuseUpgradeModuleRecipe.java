package com.infamous.simply_harder.custom.recipe;

import com.infamous.simply_harder.custom.item.UpgradeModuleItem;
import com.infamous.simply_harder.datagen.provider.ModItemTagsProvider;
import com.infamous.simply_harder.registry.SHRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class InfuseUpgradeModuleRecipe extends BaseInfusionRecipe {

    public static final String NAME = "infuse_upgrade_module";

    public InfuseUpgradeModuleRecipe(ResourceLocation id) {
        super(id, Ingredient.EMPTY, Ingredient.EMPTY);
    }

    @Override
    public boolean isBaseIngredient(ItemStack stack) {
        return UpgradeModuleItem.isNonInfusedUpgradeModule(stack);
    }

    @Override
    public boolean isAdditionIngredient(ItemStack stack) {
        return stack.is(ModItemTagsProvider.INFUSIONS);
    }

    @Override
    public ItemStack assemble(Container container) {
        ItemStack upgradeModule = BaseInfusionRecipe.getBase(container);
        ItemStack addition = BaseInfusionRecipe.getAddition(container);

        ItemStack infusedUpgradeModule = upgradeModule.copy();
        infusedUpgradeModule.setCount(1); // only create a single infused upgrade module from the stack
        UpgradeModuleItem.setInfusedItem(infusedUpgradeModule, addition);
        return infusedUpgradeModule;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SHRecipes.INFUSE_UPGRADE_MODULE.get();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
