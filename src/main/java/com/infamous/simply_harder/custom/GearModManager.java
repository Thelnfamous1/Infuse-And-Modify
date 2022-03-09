package com.infamous.simply_harder.custom;

import com.google.common.collect.Maps;
import com.google.gson.*;
import com.infamous.simply_harder.custom.data.GearMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


public class GearModManager extends SimpleJsonResourceReloadListener {

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    private static final String PARSING_ERROR_MESSAGE = "Parsing error loading gear mod {}";
    private static final String LOADED_GEAR_MODS_MESSAGE = "Loaded {} gear mods";
    private static final Logger LOGGER = LogManager.getLogger();

    private Map<ResourceLocation, GearMod> gearMods = new HashMap<>();

    public GearModManager() {
        super(GSON, "gear_mods");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> loader, ResourceManager manager, ProfilerFiller profiler) {
        Map<ResourceLocation, GearMod> readGearMods = Maps.newHashMap();

        for (Map.Entry<ResourceLocation, JsonElement> entry : loader.entrySet()) {
            ResourceLocation identifier = entry.getKey();

            try {
                JsonObject topElement = GsonHelper.convertToJsonObject(entry.getValue(), "top element");
                GearMod gearMod = GearMod.fromJson(topElement);
                readGearMods.put(gearMod.id(), gearMod);
            } catch (IllegalArgumentException | JsonParseException exception) {
                LOGGER.error(PARSING_ERROR_MESSAGE, identifier, exception);
            }
        }

        this.gearMods = readGearMods;
        LOGGER.info(LOADED_GEAR_MODS_MESSAGE, readGearMods.size());
    }
    
    public Optional<GearMod> getGearMod(ResourceLocation id) {
        return Optional.ofNullable(this.gearMods.get(id));
    }

    public Map<ResourceLocation, GearMod> collectGearMods(){
        return Collections.unmodifiableMap(this.gearMods);
    }

    public void replaceGearMods(Map<ResourceLocation, GearMod> replacement) {
        this.gearMods = replacement;
    }
}