package com.infamous.simply_harder.custom.recipe;

import com.infamous.simply_harder.custom.container.SimpleAnvilContainer;
import com.infamous.simply_harder.custom.item.EnhancementCoreItem;
import com.infamous.simply_harder.registry.SHRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class MasterworkingRecipe extends ForgingRecipe {

    public static final String NAME = "masterworking";

    public MasterworkingRecipe(ResourceLocation id){
        super(id);
    }

    @Override
    public boolean matches(SimpleAnvilContainer container, Level level) {
        ItemStack left = ForgingRecipe.getLeft(container);
        ItemStack right = ForgingRecipe.getRight(container);
        return this.isValidItemForMasterworking(left, container)
                && !EnhancementCoreItem.isMaxTier(left)
                && EnhancementCoreItem.isEnhancementCore(right);
    }

    private boolean isValidItemForMasterworking(ItemStack left, SimpleAnvilContainer container) {
        return !left.isEmpty() && (left.isDamageableItem() || container.getPlayer().getAbilities().instabuild);
    }

    @Override
    public ItemStack assemble(SimpleAnvilContainer container) {
        ItemStack left = ForgingRecipe.getLeft(container);
        ItemStack result = left.copy();
        EnhancementCoreItem.setTier(result, EnhancementCoreItem.getTier(left) + 1);
        return result;
    }

    @Override
    public int calculateLevelCost(SimpleAnvilContainer container) {
        ItemStack left = ForgingRecipe.getLeft(container);
        return EnhancementCoreItem.getLevelCost(left);
    }

    @Override
    public int calculateMaterialCost(SimpleAnvilContainer container) {
        ItemStack left = ForgingRecipe.getLeft(container);
        return EnhancementCoreItem.getMaterialCost(left);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SHRecipes.MASTERWORKING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.MASTERWORKING.get();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
