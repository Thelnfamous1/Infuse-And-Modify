package com.infamous.simply_harder.custom.data;

import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.trading.MerchantOffer;

public record WrappedMerchantOffer(MerchantOffer offer) {

    public static final String BASE_COST_A = "baseCostA";
    public static final String COST_B = "costB";
    public static final String RESULT = "result";
    public static final String USES = "uses";
    public static final String MAX_USES = "maxUses";
    public static final String XP = "xp";
    public static final String PRICE_MULTIPLIER = "priceMultiplier";
    public static final String DEMAND = "demand";

    public static WrappedMerchantOffer fromJson(JsonObject jsonObject) {
        ItemStack baseCostA = ShapedRecipe.itemStackFromJson(jsonObject.getAsJsonObject(BASE_COST_A));
        ItemStack costB;
        if (jsonObject.has(COST_B)) {
            costB = ShapedRecipe.itemStackFromJson(jsonObject.getAsJsonObject(COST_B));
        } else {
            costB = ItemStack.EMPTY;
        }
        ItemStack result = ShapedRecipe.itemStackFromJson(jsonObject.getAsJsonObject(RESULT));
        int uses = GsonHelper.getAsInt(jsonObject, USES, 0);
        int maxUses = GsonHelper.getAsInt(jsonObject, MAX_USES, 4);
        int xp = GsonHelper.getAsInt(jsonObject, XP, 1);
        float priceMultiplier = GsonHelper.getAsFloat(jsonObject, PRICE_MULTIPLIER, 0.0F);
        int demand = GsonHelper.getAsInt(jsonObject, DEMAND, 0);
        MerchantOffer merchantOffer = new MerchantOffer(baseCostA, costB, result, uses, maxUses, xp, priceMultiplier, demand);

        return new WrappedMerchantOffer(merchantOffer);
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(BASE_COST_A, itemStackToJson(this.offer.getBaseCostA()));
        if (!this.offer.getCostB().isEmpty()) jsonObject.add(COST_B, itemStackToJson(this.offer.getCostB()));
        jsonObject.add(RESULT, itemStackToJson(this.offer.getResult()));
        jsonObject.addProperty(USES, this.offer.getUses());
        jsonObject.addProperty(MAX_USES, this.offer.getMaxUses());
        jsonObject.addProperty(XP, this.offer.getXp());
        jsonObject.addProperty(PRICE_MULTIPLIER, this.offer.getPriceMultiplier());
        jsonObject.addProperty(DEMAND, this.offer.getDemand());
        return jsonObject;
    }

    private static JsonObject itemStackToJson(ItemStack stack) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("item", stack.getItem().getRegistryName().toString());
        if (stack.getCount() > 1) {
            jsonObject.addProperty("count", stack.getCount());
        }
        if (stack.hasTag()) {
            jsonObject.addProperty("nbt", stack.getTag().toString());
        }
        return jsonObject;
    }
}
