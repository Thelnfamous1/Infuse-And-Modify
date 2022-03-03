package com.infamous.simply_harder.custom.recipe;

import com.infamous.simply_harder.custom.item.InfusionCatalystItem;
import com.infamous.simply_harder.registry.SHRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class InfuseItemIntoRecipe extends InfusionRecipe{

    public InfuseItemIntoRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean isAdditionIngredient(ItemStack stack) {
        return !InfusionCatalystItem.isInfusionCatalyst(stack);
    }

    @Override
    public boolean matches(Container container, Level level) {
        ItemStack base = InfusionRecipe.getBase(container);
        ItemStack addition = InfusionRecipe.getAddition(container);
        return InfusionCatalystItem.isInactiveCatalyst(base) && !addition.isEmpty();
    }

    @Override
    public ItemStack assemble(Container container) {
        ItemStack base = InfusionRecipe.getBase(container);
        ItemStack addition = InfusionRecipe.getAddition(container);

        ItemStack infusedCore = base.copy();
        InfusionCatalystItem.setInfusedItem(infusedCore, addition);
        return infusedCore;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SHRecipes.INFUSE_ITEM_INTO.get();
    }
}
