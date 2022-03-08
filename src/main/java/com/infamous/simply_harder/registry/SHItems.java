package com.infamous.simply_harder.registry;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.custom.item.EnhancementCoreItem;
import com.infamous.simply_harder.custom.item.GearModItem;
import com.infamous.simply_harder.custom.item.UpgradeModuleItem;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class SHItems {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SimplyHarder.MOD_ID);

    public static final RegistryObject<Item> UPGRADE_MODULE = register(UpgradeModuleItem.NAME, buildUpgradeModule());

    public static final RegistryObject<Item> GEAR_MOD = register(GearModItem.NAME, buildModification());

    public static final RegistryObject<Item> ENHANCEMENT_CORE = register(EnhancementCoreItem.NAME, buildEnhancementCore());

    private static Supplier<Item> buildSimpleMaterial() {
        return () -> new Item(((new Item.Properties()).tab(CreativeModeTab.TAB_MATERIALS)));
    }

    private static Supplier<Item> buildUpgradeModule() {
        return () -> new UpgradeModuleItem(((new Item.Properties()).rarity(Rarity.EPIC).tab(CreativeModeTab.TAB_MISC)));
    }

    private static Supplier<Item> buildModification() {
        return () -> new GearModItem(((new Item.Properties()).rarity(Rarity.EPIC).tab(CreativeModeTab.TAB_MISC)));
    }

    private static Supplier<Item> buildEnhancementCore() {
        return () -> new EnhancementCoreItem(((new Item.Properties()).rarity(Rarity.EPIC).tab(CreativeModeTab.TAB_MISC)));
    }

    private static RegistryObject<Item> register(String id, Supplier<Item> builder) {
        return ITEMS.register(id, builder);
    }

    public static void register(IEventBus modEventBus){
        ITEMS.register(modEventBus);
    }
}
