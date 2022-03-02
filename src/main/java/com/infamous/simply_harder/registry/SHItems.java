package com.infamous.simply_harder.registry;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.custom.item.ModifierCoreItem;
import com.infamous.simply_harder.custom.item.InfusionCoreItem;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class SHItems {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SimplyHarder.MOD_ID);

    public static final RegistryObject<Item> INFUSION_CORE = register("infusion_core", buildInfusionCore());

    public static final RegistryObject<Item> MODIFIER_CORE = register("modifier_core", buildModifierCore());

    private static Supplier<Item> buildSimpleMaterial() {
        return () -> new Item(((new Item.Properties()).tab(CreativeModeTab.TAB_MATERIALS)));
    }

    private static Supplier<Item> buildInfusionCore() {
        return () -> new InfusionCoreItem(((new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));
    }

    private static Supplier<Item> buildModifierCore() {
        return () -> new ModifierCoreItem(((new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));
    }

    private static RegistryObject<Item> register(String id, Supplier<Item> builder) {
        return ITEMS.register(id, builder);
    }

    public static void register(IEventBus modEventBus){
        ITEMS.register(modEventBus);
    }
}
