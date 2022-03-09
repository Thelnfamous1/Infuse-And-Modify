package com.infamous.simply_harder.custom.recipe;

import com.infamous.simply_harder.custom.container.SimpleAnvilContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public abstract class ForgingRecipe implements Recipe<SimpleAnvilContainer> {
    private final ResourceLocation id;

    public ForgingRecipe(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public boolean matches(SimpleAnvilContainer container, Level level) {
        return this.calculateMaterialCost(container) <= container.getRight().getCount()
                && this.simpleMatches(container, level);
    }

    protected abstract boolean simpleMatches(SimpleAnvilContainer container, Level level);

    public ResourceLocation getId() {
        return this.id;
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

    public int calculateTotalLevelCost(SimpleAnvilContainer container){
        return this.calculateLevelCost(container) * container.getLeft().getCount();
    }
    public int calculateTotalMaterialCost(SimpleAnvilContainer container){
        return this.calculateMaterialCost(container) * container.getLeft().getCount();
    }

    protected abstract int calculateLevelCost(SimpleAnvilContainer container);

    protected abstract int calculateMaterialCost(SimpleAnvilContainer container);
}
