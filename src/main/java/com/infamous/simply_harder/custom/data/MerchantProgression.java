package com.infamous.simply_harder.custom.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public record MerchantProgression(VillagerProfession profession,
                                  Map<Integer, ResourceLocation> levelUpRewards,
                                  Map<Integer, WrappedMerchantOffers> additionalOffers) {

    public static final MerchantProgression NONE = new MerchantProgression(VillagerProfession.NONE, ImmutableMap.of(), ImmutableMap.of());
    public static final String PROFESSION = "profession";
    public static final String LEVEL_UP_REWARDS = "career_rewards_by_level";
    public static final String ADDITIONAL_OFFERS = "additional_offers_by_level";
    public static final String OFFERS = "offers";

    public static MerchantProgression fromJson(JsonObject topElement) {
        ResourceLocation professionLocation = new ResourceLocation(GsonHelper.getAsString(topElement, PROFESSION));
        VillagerProfession profession = ForgeRegistries.PROFESSIONS.getValue(professionLocation);

        // level up rewards
        JsonArray levelUpRewardsArr = GsonHelper.getAsJsonArray(topElement, LEVEL_UP_REWARDS);
        Map<Integer, ResourceLocation> levelUpRewards = new LinkedHashMap<>();
        for(int i = 0; i < levelUpRewardsArr.size(); i++){
            JsonElement jsonElement = levelUpRewardsArr.get(i);
            ResourceLocation lootTableLocation = new ResourceLocation(jsonElement.getAsString());
            int level = i + 2;
            levelUpRewards.put(level, lootTableLocation);
        }
        // additional offers
        JsonArray additionalOffersArr = GsonHelper.getAsJsonArray(topElement, ADDITIONAL_OFFERS);
        Map<Integer, WrappedMerchantOffers> additionalOffers = new LinkedHashMap<>();
        for(int i = 0; i < additionalOffersArr.size(); i++){
            int currentLevel = i + 1;
            JsonElement jsonElement = additionalOffersArr.get(i);
            JsonObject elemObj = jsonElement.getAsJsonObject();
            WrappedMerchantOffers offers = WrappedMerchantOffers.fromJson(elemObj.get(OFFERS));
            additionalOffers.put(currentLevel, offers);
        }

        return new MerchantProgression(profession, levelUpRewards, additionalOffers);
    }

    public Optional<ResourceLocation> getRewardsForLevel(int level) {
        return Optional.ofNullable(this.levelUpRewards.get(level));
    }

    public Optional<WrappedMerchantOffers> getAdditionalOffersForLevel(int level){
        return Optional.ofNullable(this.additionalOffers.get(level));
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty(PROFESSION, this.profession.getRegistryName().toString());

        JsonArray levelUpRewardsArr = new JsonArray();
        this.levelUpRewards.forEach((level, lootTable) -> levelUpRewardsArr.add(lootTable.toString()));
        jsonObject.add(LEVEL_UP_REWARDS, levelUpRewardsArr);

        JsonArray additionalOffersArr = new JsonArray();
        this.additionalOffers.forEach((level, offers) -> {
            JsonObject offersObj = new JsonObject();
            offersObj.add(OFFERS, offers.toJson());
            additionalOffersArr.add(offersObj);
        });
        jsonObject.add(ADDITIONAL_OFFERS, additionalOffersArr);

        return jsonObject;
    }
}
