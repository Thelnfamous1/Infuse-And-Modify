package com.infamous.simply_harder.custom.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.UpgradeRecipe;
import net.minecraft.world.level.Level;

public abstract class InfusionRecipe extends UpgradeRecipe {

    public InfusionRecipe(ResourceLocation id) {
        super(id, Ingredient.EMPTY, Ingredient.EMPTY, ItemStack.EMPTY);
    }

    protected static ItemStack getBase(Container container) {
        return container.getItem(SmithingMenu.INPUT_SLOT);
    }

    protected static ItemStack getAddition(Container container) {
        return container.getItem(SmithingMenu.ADDITIONAL_SLOT);
    }

    @Override
    public abstract boolean isAdditionIngredient(ItemStack stack);

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public abstract boolean matches(Container container, Level level);

    @Override
    public abstract ItemStack assemble(Container container);

    @Override
    public abstract RecipeSerializer<?> getSerializer();
}
