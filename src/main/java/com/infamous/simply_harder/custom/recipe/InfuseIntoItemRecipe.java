package com.infamous.simply_harder.custom.recipe;

import com.infamous.simply_harder.custom.item.InfusionCatalystItem;
import com.infamous.simply_harder.custom.item.ModifierCoreItem;
import com.infamous.simply_harder.registry.SHRecipes;
import com.infamous.simply_harder.util.InfuseAndModifyHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class InfuseIntoItemRecipe extends InfusionRecipe{
    public static final String NAME = "infuse_into_item";

    public InfuseIntoItemRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean isAdditionIngredient(ItemStack stack) {
        return InfusionCatalystItem.isActiveCatalyst(stack);
    }

    @Override
    public boolean matches(Container container, Level level) {
        ItemStack base = InfusionRecipe.getBase(container);
        ItemStack addition = InfusionRecipe.getAddition(container);
        return !base.isEmpty() && !ModifierCoreItem.isModifierCore(base) && InfuseAndModifyHelper.meetsRequirements(base, addition);
    }

    @Override
    public ItemStack assemble(Container container) {
        ItemStack base = InfusionRecipe.getBase(container);
        ItemStack infusionCore = InfusionRecipe.getAddition(container);

        ItemStack infusedItem = InfusionCatalystItem.getInfusedItem(infusionCore).copy();
        CompoundTag baseTag = base.getTag();
        if (baseTag != null) {
            infusedItem.setTag(baseTag.copy());
        }

        return infusedItem;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SHRecipes.INFUSE_INTO_ITEM.get();
    }
}
