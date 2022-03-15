package com.infamous.simply_harder;

import com.infamous.simply_harder.custom.recipe.ModRecipeTypes;
import com.infamous.simply_harder.datagen.provider.MerchantProgressionProvider;
import com.infamous.simply_harder.datagen.provider.*;
import com.infamous.simply_harder.network.SHNetwork;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = SimplyHarder.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SHModEvents {

    @SubscribeEvent
    static void onCommonSetup(FMLCommonSetupEvent event){
        event.enqueueWork(SHNetwork::register);
        event.enqueueWork(() -> ModRecipeTypes.MODIFICATION.register(RecipeType.register(new ResourceLocation(SimplyHarder.MOD_ID, "modification").toString())));
        event.enqueueWork(() -> ModRecipeTypes.MASTERWORKING.register(RecipeType.register(new ResourceLocation(SimplyHarder.MOD_ID, "masterworking").toString())));
    }

    @SubscribeEvent
    static void onGatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        if(event.includeServer()){
            // tags
            ModBlockTagsProvider modBlockTagsProvider = new ModBlockTagsProvider(generator, existingFileHelper);
            generator.addProvider(new ModItemTagsProvider(generator, modBlockTagsProvider, existingFileHelper));
            // recipes
            generator.addProvider(new ModRecipeProvider(generator));
            // gear mods
            generator.addProvider(new GearModProvider(generator));
            // masterwork progressions
            generator.addProvider(new MasterworkProgressionProvider(generator));
            // loot tables
            generator.addProvider(new ModLootTableProvider(generator));
            // merchant progressions
            generator.addProvider(new MerchantProgressionProvider(generator));
        }
    }
}
