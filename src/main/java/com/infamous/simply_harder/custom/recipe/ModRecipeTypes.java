package com.infamous.simply_harder.custom.recipe;

import com.infamous.simply_harder.custom.SimpleRegistryObjectHolder;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipeTypes {
    public static final SimpleRegistryObjectHolder<RecipeType<ModificationRecipe>> MODIFICATION = new SimpleRegistryObjectHolder<>();


    public static final SimpleRegistryObjectHolder<RecipeType<MasterworkingRecipe>> MASTERWORKING = new SimpleRegistryObjectHolder<>();
}
