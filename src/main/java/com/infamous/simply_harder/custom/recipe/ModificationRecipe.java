package com.infamous.simply_harder.custom.recipe;

import com.infamous.simply_harder.custom.ModRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;

public abstract class ModificationRecipe implements Recipe<Container> {
    private final ResourceLocation id;

    public ModificationRecipe(ResourceLocation id) {
        this.id = id;
    }

    public static Container buildContainer(ItemStack left, ItemStack right){
        return new SimpleContainer(left, right);
    }

    protected static ItemStack getLeft(Container container) {
        return container.getItem(AnvilMenu.INPUT_SLOT);
    }

    protected static ItemStack getRight(Container container) {
        return container.getItem(AnvilMenu.ADDITIONAL_SLOT);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public boolean isSpecial() {
        return true;
    }

    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    public boolean canCraftInDimensions(int gridWidth, int gridHeight) {
        return gridWidth * gridHeight >= 2;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(Blocks.ANVIL);
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.MODIFICATION.get();
    }

    public abstract int calculateLevelCost(Container container);

    public int calculateMaterialCost(Container container) {
        return 1;
    }
}
