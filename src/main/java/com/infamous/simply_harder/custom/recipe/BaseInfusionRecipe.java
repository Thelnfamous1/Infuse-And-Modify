package com.infamous.simply_harder.custom.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.UpgradeRecipe;
import net.minecraft.world.level.Level;

public abstract class BaseInfusionRecipe extends UpgradeRecipe {
    protected final Ingredient infusionBase;
    protected final Ingredient infusionAddition;

    public BaseInfusionRecipe(ResourceLocation id, Ingredient infusionBase, Ingredient infusionAddition) {
        super(id, infusionBase, infusionAddition, ItemStack.EMPTY);
        this.infusionBase = infusionBase;
        this.infusionAddition = infusionAddition;
    }

    protected static ItemStack getBase(Container container) {
        return container.getItem(SmithingMenu.INPUT_SLOT);
    }

    protected static ItemStack getAddition(Container container) {
        return container.getItem(SmithingMenu.ADDITIONAL_SLOT);
    }

    public abstract boolean isBaseIngredient(ItemStack stack);

    @Override
    public abstract boolean isAdditionIngredient(ItemStack stack);

    @Override
    public boolean matches(Container container, Level level) {
        ItemStack base = BaseInfusionRecipe.getBase(container);
        ItemStack addition = BaseInfusionRecipe.getAddition(container);
        return this.isBaseIngredient(base) && this.isAdditionIngredient(addition);
    }

    @Override
    public abstract ItemStack assemble(Container container);

    @Override
    public abstract RecipeSerializer<?> getSerializer();
}
