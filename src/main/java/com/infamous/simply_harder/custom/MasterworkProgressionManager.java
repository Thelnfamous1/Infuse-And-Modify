package com.infamous.simply_harder.custom;

import com.google.common.collect.Maps;
import com.google.gson.*;
import com.infamous.simply_harder.custom.data.MasterworkProgression;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class MasterworkProgressionManager extends SimpleJsonResourceReloadListener {

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    private static final String PARSING_ERROR_MESSAGE = "Parsing error loading masterwork progression {}";
    private static final String LOADED_GEAR_MODS_MESSAGE = "Loaded {} masterwork progressions";
    private static final Logger LOGGER = LogManager.getLogger();

    private Map<ResourceLocation, MasterworkProgression> masterworkProgressions = new HashMap<>();

    public MasterworkProgressionManager() {
        super(GSON, "masterwork_progressions");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> loader, ResourceManager manager, ProfilerFiller profiler) {
        Map<ResourceLocation, MasterworkProgression> readGearMods = Maps.newHashMap();

        for (Map.Entry<ResourceLocation, JsonElement> entry : loader.entrySet()) {
            ResourceLocation identifier = entry.getKey();

            try {
                JsonObject topElement = GsonHelper.convertToJsonObject(entry.getValue(), "top element");
                MasterworkProgression gearMod = MasterworkProgression.fromJson(topElement);
                readGearMods.put(gearMod.getId(), gearMod);
            } catch (IllegalArgumentException | JsonParseException exception) {
                LOGGER.error(PARSING_ERROR_MESSAGE, identifier, exception);
            }
        }

        this.masterworkProgressions = readGearMods;
        LOGGER.info(LOADED_GEAR_MODS_MESSAGE, readGearMods.size());
    }
    
    public Optional<MasterworkProgression> getProgression(ResourceLocation id) {
        return Optional.ofNullable(this.masterworkProgressions.get(id));
    }

    public Map<ResourceLocation, MasterworkProgression> collectProgressions(){
        return Collections.unmodifiableMap(this.masterworkProgressions);
    }

    public void replaceProgressions(Map<ResourceLocation, MasterworkProgression> replacement) {
        this.masterworkProgressions = replacement;
    }
}