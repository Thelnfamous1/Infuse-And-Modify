package com.infamous.simply_harder.custom.recipe;

import com.infamous.simply_harder.custom.container.SimpleAnvilContainer;
import com.infamous.simply_harder.custom.item.ModificationItem;
import com.infamous.simply_harder.registry.SHRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class ModificationRecipe extends ForgingRecipe {
    public static final String NAME = "modification";

    public ModificationRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public int calculateLevelCost(SimpleAnvilContainer container) {
        return ModificationItem.getLevelCost(ForgingRecipe.getRight(container));
    }

    @Override
    public int calculateMaterialCost(SimpleAnvilContainer container) {
        return ModificationItem.getMaterialCost(ForgingRecipe.getRight(container));
    }

    @Override
    public boolean matches(SimpleAnvilContainer container, Level level) {
        ItemStack left = getLeft(container);
        ItemStack right = getRight(container);
        return ModificationItem.isUsableMod(right)
                && ModificationItem.isValidForMod(left, right);
    }

    @Override
    public ItemStack assemble(SimpleAnvilContainer container) {
        ItemStack left = getLeft(container);
        ItemStack right = getRight(container);

        ItemStack result = left.copy();
        ModificationItem.addMod(result, right);
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SHRecipes.MODIFICATION.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.MODIFICATION.get();
    }

    public boolean isSpecial() {
        return true;
    }
}
