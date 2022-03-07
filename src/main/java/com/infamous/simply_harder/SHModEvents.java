package com.infamous.simply_harder;

import com.infamous.simply_harder.custom.ModRecipeTypes;
import com.infamous.simply_harder.datagen.ModRecipeProvider;
import com.infamous.simply_harder.network.ModNetwork;
import com.infamous.simply_harder.registry.SHAttributes;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = SimplyHarder.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SHModEvents {

    @SubscribeEvent
    static void onAttributeModification(EntityAttributeModificationEvent event){
    }

    @SubscribeEvent
    static void onCommonSetup(FMLCommonSetupEvent event){
        event.enqueueWork(ModNetwork::register);
        //event.enqueueWork(PeakExperienceAttacher::register);
        //event.enqueueWork(() -> ModCriteriaTriggers.EXPERIENCE_CHANGED.register(CriteriaTriggers.register(new ExperienceChangeTrigger())));
        event.enqueueWork(() -> ModRecipeTypes.MODIFICATION.register(RecipeType.register(new ResourceLocation(SimplyHarder.MOD_ID, "modification").toString())));
    }

    @SubscribeEvent
    static void onGatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        if(event.includeServer()){
            //ModBlockTagsProvider modBlockTagsProvider = new ModBlockTagsProvider(generator, existingFileHelper);
            //generator.addProvider(new ModItemTagsProvider(generator, modBlockTagsProvider, existingFileHelper));
            generator.addProvider(new ModRecipeProvider(generator));
        }
    }
}
