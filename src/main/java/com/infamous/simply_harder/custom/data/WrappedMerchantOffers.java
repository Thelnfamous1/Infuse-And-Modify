package com.infamous.simply_harder.custom.data;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.world.item.trading.MerchantOffers;

import java.util.ArrayList;
import java.util.List;

public record WrappedMerchantOffers(
        List<WrappedMerchantOffer> offers) {

    public static final WrappedMerchantOffers EMPTY = new WrappedMerchantOffers(ImmutableList.of());
    private static final String OFFERS = "offers";

    public static WrappedMerchantOffers fromJson(JsonElement jsonElement) {
        List<WrappedMerchantOffer> offers = new ArrayList<>();
        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            jsonArray.forEach(je -> {
                JsonObject jsonObject = je.getAsJsonObject();
                WrappedMerchantOffer offer = WrappedMerchantOffer.fromJson(jsonObject);
                offers.add(offer);
            });
            return new WrappedMerchantOffers(offers);
        } else {
            throw new JsonParseException("Expected " + WrappedMerchantOffers.OFFERS + " to be an array");
        }
    }

    public JsonArray toJson() {
        JsonArray jsonarray = new JsonArray();

        for (WrappedMerchantOffer offer : this.offers) {
            jsonarray.add(offer.toJson());
        }

        return jsonarray;
    }

    public static WrappedMerchantOffers wrap(MerchantOffers offers) {
        ArrayList<WrappedMerchantOffer> offersToWrap = new ArrayList<>();
        offers.forEach(offer -> offersToWrap.add(new WrappedMerchantOffer(offer)));
        return new WrappedMerchantOffers(offersToWrap);
    }

    public MerchantOffers unwrap() {
        MerchantOffers unwrappedOffers = new MerchantOffers();
        for (WrappedMerchantOffer wrappedOffer : this.offers) {
            unwrappedOffers.add(wrappedOffer.offer());
        }
        return unwrappedOffers;
    }
}
