package com.infamous.simply_harder.registry;

import com.infamous.simply_harder.SimplyHarder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class SHAttributes {

    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, SimplyHarder.MOD_ID);

    public static final RegistryObject<Attribute> EXHAUSTION = register("exhaustion", buildSimpleAttribute("attribute.name.player.exhaustion", 1.0D, 0.0D, 32.0D));

    private static RegistryObject<Attribute> register(String name, Supplier<Attribute> builder) {
        return ATTRIBUTES.register(name, builder);
    }

    private static Supplier<Attribute> buildSimpleAttribute(String descriptionId, double defaultValue, double minValue, double maxValue) {
        return () -> new RangedAttribute(descriptionId, defaultValue, minValue, maxValue);
    }

    public static void register(IEventBus modEventBus){
        ATTRIBUTES.register(modEventBus);
    }
}
