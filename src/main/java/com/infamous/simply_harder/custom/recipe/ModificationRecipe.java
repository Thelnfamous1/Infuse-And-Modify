package com.infamous.simply_harder.custom.recipe;

import com.infamous.simply_harder.custom.container.SimpleAnvilContainer;
import com.infamous.simply_harder.custom.data.GearMod;
import com.infamous.simply_harder.custom.item.GearModItem;
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
    protected int calculateLevelCost(SimpleAnvilContainer container) {
        return GearModItem.getLevelCostCheckTag(container.getRight());
    }

    @Override
    protected int calculateMaterialCost(SimpleAnvilContainer container) {
        return 1;
    }

    @Override
    protected boolean simpleMatches(SimpleAnvilContainer container, Level level) {
        ItemStack left = container.getLeft();
        ItemStack right = container.getRight();
        return GearModItem.hasInternal(right)
                && this.canInstall(left, GearModItem.getInternalModCheckTag(right));
    }

    private boolean canInstall(ItemStack left, GearMod mod) {
        return mod.installable().test(left) && !GearModItem.hasMod(left);
    }

    @Override
    public ItemStack assemble(SimpleAnvilContainer container) {
        ItemStack left = container.getLeft();
        ItemStack right = container.getRight();

        ItemStack result = left.copy();
        GearModItem.setMod(result, GearModItem.getInternalModCheckTag(right));
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
