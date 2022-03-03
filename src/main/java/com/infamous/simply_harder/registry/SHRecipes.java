package com.infamous.simply_harder.registry;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.custom.recipe.InfuseIntoItemRecipe;
import com.infamous.simply_harder.custom.recipe.InfuseItemIntoRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class SHRecipes {

    private static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SimplyHarder.MOD_ID);

    public static final RegistryObject<SimpleRecipeSerializer<InfuseIntoItemRecipe>> INFUSE_INTO_ITEM =
            register("infuse_into_item", () -> new SimpleRecipeSerializer<>(InfuseIntoItemRecipe::new));

    public static final RegistryObject<SimpleRecipeSerializer<InfuseItemIntoRecipe>> INFUSE_ITEM_INTO =
            register("infuse_item_into", () -> new SimpleRecipeSerializer<>(InfuseItemIntoRecipe::new));

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> RegistryObject<S> register(String name, Supplier<S> supplier){
        return RECIPES.register(name, supplier);
    }

    public static void register(IEventBus modEventBus){
        RECIPES.register(modEventBus);
    }
}
