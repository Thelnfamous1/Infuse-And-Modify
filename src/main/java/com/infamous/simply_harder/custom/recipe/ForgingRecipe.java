package com.infamous.simply_harder.custom.recipe;

import com.infamous.simply_harder.custom.container.SimpleAnvilContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Blocks;

public abstract class ForgingRecipe implements Recipe<SimpleAnvilContainer> {
    private final ResourceLocation id;

    public ForgingRecipe(ResourceLocation id) {
        this.id = id;
    }

    public static SimpleAnvilContainer buildAnvilContainer(AnvilMenu anvilMenu, ItemStack left, ItemStack right){
        return new SimpleAnvilContainer(anvilMenu, left, right);
    }

    protected static ItemStack getLeft(SimpleAnvilContainer container) {
        return container.getItem(AnvilMenu.INPUT_SLOT);
    }

    protected static ItemStack getRight(SimpleAnvilContainer container) {
        return container.getItem(AnvilMenu.ADDITIONAL_SLOT);
    }

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

    public abstract int calculateLevelCost(SimpleAnvilContainer container);

    public abstract int calculateMaterialCost(SimpleAnvilContainer container);
}
