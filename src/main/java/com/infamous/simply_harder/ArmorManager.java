package com.infamous.simply_harder;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.text2speech.Narrator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;

import java.util.Map;

public class ArmorManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private Map<ResourceLocation, ArmorContainer> armorContainers = ImmutableMap.of();

    public ArmorManager() {
        super(GSON, SimplyHarder.MOD_ID + "/armor");
    }

    public double getExhaustionModifier(ResourceLocation location){
        return this.getArmorContainer(location).exhaustionModifier;
    }

    public boolean hasArmorContainer(ResourceLocation location){
        return this.armorContainers.containsKey(location);
    }

    private ArmorContainer getArmorContainer(ResourceLocation location) {
        return armorContainers.getOrDefault(location, ArmorContainer.EMPTY);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> locationToJson, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        ImmutableMap.Builder<ResourceLocation, ArmorContainer> armorContainerBuilder = ImmutableMap.builder();
        locationToJson.forEach((location, jsonElement) -> {
            JsonObject topElement = GsonHelper.convertToJsonObject(jsonElement, "top element");
            ArmorContainer armorContainer = ArmorContainer.fromJson(topElement);
            armorContainerBuilder.put(location, armorContainer);
        });
        this.armorContainers = armorContainerBuilder.build();
    }

    private static class ArmorContainer {
        public static final ArmorContainer EMPTY = new ArmorContainer();
        private double exhaustionModifier = 0.0D;

        private ArmorContainer(){}

        private static ArmorContainer fromJson(JsonObject jsonObject){
            ArmorContainer armorContainer = new ArmorContainer();
            armorContainer.exhaustionModifier = GsonHelper.getAsDouble(jsonObject, "exhaustion_modifier", 0.0D);
            return armorContainer;
        }
    }
}
