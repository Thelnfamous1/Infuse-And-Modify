package com.infamous.simply_harder.registry;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.custom.recipe.InfusionRecipe;
import com.infamous.simply_harder.custom.recipe.InfuseUpgradeModuleRecipe;
import com.infamous.simply_harder.custom.recipe.MasterworkingRecipe;
import com.infamous.simply_harder.custom.recipe.ModificationRecipe;
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

    public static final RegistryObject<InfusionRecipe.Serializer> INFUSION =
            register(InfusionRecipe.NAME, InfusionRecipe.Serializer::new);

    public static final RegistryObject<SimpleRecipeSerializer<InfuseUpgradeModuleRecipe>> INFUSE_UPGRADE_MODULE =
            register(InfuseUpgradeModuleRecipe.NAME, () -> new SimpleRecipeSerializer<>(InfuseUpgradeModuleRecipe::new));

    public static final RegistryObject<SimpleRecipeSerializer<ModificationRecipe>> MODIFICATION =
            register(ModificationRecipe.NAME, () -> new SimpleRecipeSerializer<>(ModificationRecipe::new));

    public static final RegistryObject<MasterworkingRecipe.Serializer> MASTERWORKING =
            register(MasterworkingRecipe.NAME, MasterworkingRecipe.Serializer::new);

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> RegistryObject<S> register(String name, Supplier<S> supplier){
        return RECIPES.register(name, supplier);
    }

    public static void register(IEventBus modEventBus){
        RECIPES.register(modEventBus);
    }
}
