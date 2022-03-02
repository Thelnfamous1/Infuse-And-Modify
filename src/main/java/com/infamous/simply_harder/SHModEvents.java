package com.infamous.simply_harder;

import com.infamous.simply_harder.custom.critera.ExperienceChangeTrigger;
import com.infamous.simply_harder.custom.critera.ModCriteriaTriggers;
import com.infamous.simply_harder.datagen.ModBlockTagsProvider;
import com.infamous.simply_harder.datagen.ModItemTagsProvider;
import com.infamous.simply_harder.datagen.ModRecipeProvider;
import com.infamous.simply_harder.network.ModNetwork;
import com.infamous.simply_harder.registry.SHAttributes;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.EntityType;
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
        event.add(EntityType.PLAYER, SHAttributes.EXHAUSTION.get());
    }

    @SubscribeEvent
    static void onCommonSetup(FMLCommonSetupEvent event){
        event.enqueueWork(ModNetwork::register);
        //event.enqueueWork(PeakExperienceAttacher::register);
        event.enqueueWork(() -> ModCriteriaTriggers.EXPERIENCE_CHANGED.register(CriteriaTriggers.register(new ExperienceChangeTrigger())));
    }

    @SubscribeEvent
    static void onGatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        if(event.includeServer()){
            ModBlockTagsProvider modBlockTagsProvider = new ModBlockTagsProvider(generator, existingFileHelper);
            generator.addProvider(new ModItemTagsProvider(generator, modBlockTagsProvider, existingFileHelper));
            generator.addProvider(new ModRecipeProvider(generator));
        }
    }
}
