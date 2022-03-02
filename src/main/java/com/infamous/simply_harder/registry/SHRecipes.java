package com.infamous.simply_harder.registry;

import com.infamous.simply_harder.SimplyHarder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SHRecipes {

    private static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SimplyHarder.MOD_ID);

    public static void register(IEventBus modEventBus){
        RECIPES.register(modEventBus);
    }
}
