package com.infamous.simply_harder;

import com.infamous.simply_harder.custom.GearModManager;
import com.infamous.simply_harder.custom.MasterworkProgressionManager;
import com.infamous.simply_harder.custom.MerchantProgressionManager;
import com.infamous.simply_harder.registry.SHAttributes;
import com.infamous.simply_harder.registry.SHItems;
import com.infamous.simply_harder.registry.SHRecipes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SimplyHarder.MOD_ID)
public class SimplyHarder
{
    public static final String MOD_ID = "simply_harder";
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public static final GearModManager GEAR_MOD_MANAGER = new GearModManager();
    public static final MasterworkProgressionManager MASTERWORK_PROGRESSION_MANAGER = new MasterworkProgressionManager();
    public static final MerchantProgressionManager MERCHANT_PROGRESSION_MANAGER = new MerchantProgressionManager(); // server-side only

    public SimplyHarder() {
        // Register the setup method for modloading
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        SHItems.register(modEventBus);
        SHRecipes.register(modEventBus);
        SHAttributes.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

}
