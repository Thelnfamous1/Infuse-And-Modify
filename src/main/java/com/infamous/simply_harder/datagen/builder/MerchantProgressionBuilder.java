package com.infamous.simply_harder.datagen.builder;


import com.infamous.simply_harder.custom.data.MerchantProgression;
import com.infamous.simply_harder.custom.data.WrappedMerchantOffers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MerchantProgressionBuilder {

    private final VillagerProfession profession;
    private final Map<Integer, ResourceLocation> levelUpRewards = new LinkedHashMap<>();
    private final Map<Integer, MerchantOffers> additionalOffers = new LinkedHashMap<>();
    private int levelUpCounter = 2;
    private int levelCounter = 1;

    public MerchantProgressionBuilder(VillagerProfession profession) {
        this.profession = profession;
    }

    public static MerchantProgressionBuilder progression(VillagerProfession profession){
        return new MerchantProgressionBuilder(profession);
    }

    public MerchantProgressionBuilder addReward(ResourceLocation location){
        this.levelUpRewards.put(this.levelUpCounter++, location);
        return this;
    }

    public MerchantProgressionBuilder addOffers(MerchantOffers offers){
        this.additionalOffers.put(this.levelCounter++, offers);
        return this;
    }

    public MerchantProgressionBuilder addOffer(int level, MerchantOffer offer){
        this.additionalOffers.computeIfAbsent(level, k -> new MerchantOffers()).add(offer);
        return this;
    }

    public MerchantProgression build(){
        Map<Integer, MerchantOffers> additionalOffers = this.additionalOffers;
        Map<Integer, WrappedMerchantOffers> wrappedAdditionalOffers = new LinkedHashMap<>();
        additionalOffers.forEach((level, offers) -> wrappedAdditionalOffers.put(level, WrappedMerchantOffers.wrap(offers)));
        return new MerchantProgression(this.profession, this.levelUpRewards, wrappedAdditionalOffers);
    }

    public void save(Consumer<MerchantProgression> onFinished) {
        onFinished.accept(this.build());
    }
}
