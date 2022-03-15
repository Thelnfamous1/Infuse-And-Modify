package com.infamous.simply_harder.custom;

import com.google.common.collect.Maps;
import com.google.gson.*;
import com.infamous.simply_harder.custom.data.MerchantProgression;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.npc.VillagerProfession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class MerchantProgressionManager extends SimpleJsonResourceReloadListener {

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public static final String MERCHANT_PROGRESSION = "merchant progression";
    private static final String PARSING_ERROR_MESSAGE = "Parsing error loading " + MERCHANT_PROGRESSION + " {}";
    private static final String LOADED_MERCHANT_PROGRESSIONS_MESSAGE = "Loaded {} " + MERCHANT_PROGRESSION + "s";
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MERCHANT_PROGRESSIONS_PATH = "merchant_progressions";

    private Map<VillagerProfession, MerchantProgression> merchantProgressions = new HashMap<>();

    public MerchantProgressionManager() {
        super(GSON, MERCHANT_PROGRESSIONS_PATH);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> loader, ResourceManager manager, ProfilerFiller profiler) {
        Map<VillagerProfession, MerchantProgression> readMerchantProgressions = Maps.newHashMap();

        for (Map.Entry<ResourceLocation, JsonElement> entry : loader.entrySet()) {
            ResourceLocation identifier = entry.getKey();

            try {
                JsonObject topElement = GsonHelper.convertToJsonObject(entry.getValue(), "top element");
                MerchantProgression progression = MerchantProgression.fromJson(topElement);
                readMerchantProgressions.put(progression.profession(), progression);
            } catch (IllegalArgumentException | JsonParseException exception) {
                LOGGER.error(PARSING_ERROR_MESSAGE, identifier, exception);
            }
        }

        this.merchantProgressions = readMerchantProgressions;
        LOGGER.info(LOADED_MERCHANT_PROGRESSIONS_MESSAGE, readMerchantProgressions.size());
    }
    
    public Optional<MerchantProgression> getMerchantProgression(VillagerProfession profession) {
        return Optional.ofNullable(this.merchantProgressions.get(profession));
    }

}