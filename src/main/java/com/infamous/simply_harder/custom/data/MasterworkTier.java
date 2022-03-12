package com.infamous.simply_harder.custom.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public record MasterworkTier(int levelCost, int materialCost, float levelRefund, float materialRefund,
                             WrappedAttributeModifierMap wrappedAttributeModifiers) {

    public static final String LEVEL_COST = "level_cost";
    public static final String MATERIAL_COST = "material_cost";
    public static final String LEVEL_REFUND = "level_refund";
    public static final String MATERIAL_REFUND = "material_refund";
    public static final String ATTRIBUTE_MODIFIERS = "attribute_modifiers";

    public static final MasterworkTier EMPTY = new MasterworkTier(0, 0, 0, 0, WrappedAttributeModifierMap.EMPTY);

    public static MasterworkTier fromNetwork(FriendlyByteBuf byteBuf) {
        int levelCost = byteBuf.readVarInt();
        int materialCost = byteBuf.readVarInt();
        float levelRefund = byteBuf.readFloat();
        float materialRefund = byteBuf.readFloat();
        WrappedAttributeModifierMap wrappedAttributeModifiers = WrappedAttributeModifierMap.fromNetwork(byteBuf);
        return new MasterworkTier(levelCost, materialCost, levelRefund, materialRefund, wrappedAttributeModifiers);
    }

    public static MasterworkTier fromJson(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            JsonObject tierObj = jsonElement.getAsJsonObject();
            int levelCost = GsonHelper.getAsInt(tierObj, LEVEL_COST, 1);
            int materialCost = GsonHelper.getAsInt(tierObj, MATERIAL_COST, 1);
            float levelRefund = GsonHelper.getAsFloat(tierObj, LEVEL_REFUND, 0);
            float materialRefund = GsonHelper.getAsFloat(tierObj, MATERIAL_REFUND, 0);
            WrappedAttributeModifierMap wrappedAttributeModifiers = WrappedAttributeModifierMap.fromJson(tierObj.get(ATTRIBUTE_MODIFIERS));
            return new MasterworkTier(levelCost, materialCost, levelRefund, materialRefund, wrappedAttributeModifiers);
        } else {
            throw new JsonParseException("Expected " + MasterworkProgression.TIERS + " to be an object");
        }
    }

    public static void toNetwork(FriendlyByteBuf byteBuf, MasterworkTier masterworkTier) {
        byteBuf.writeVarInt(masterworkTier.levelCost);
        byteBuf.writeVarInt(masterworkTier.materialCost);
        byteBuf.writeFloat(masterworkTier.levelRefund);
        byteBuf.writeFloat(masterworkTier.materialRefund);
        masterworkTier.wrappedAttributeModifiers.toNetwork(byteBuf);
    }

    public static JsonObject toJson(MasterworkTier masterworkTier) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(LEVEL_COST, masterworkTier.levelCost);
        jsonObject.addProperty(MATERIAL_COST, masterworkTier.materialCost);
        jsonObject.addProperty(LEVEL_REFUND, masterworkTier.levelRefund);
        jsonObject.addProperty(MATERIAL_REFUND, masterworkTier.materialRefund);
        jsonObject.add(ATTRIBUTE_MODIFIERS, masterworkTier.wrappedAttributeModifiers.toJson());
        return jsonObject;
    }

    public static class Builder{
        private int levelCost;
        private int materialCost;
        private float levelRefund;
        private float materialRefund;
        private WrappedAttributeModifierMap wrappedAttributeModifiers = WrappedAttributeModifierMap.EMPTY;

        public Builder(){

        }

        public static Builder builder(){
            return new Builder();
        }

        public Builder levelCost(int levelCost){
            this.levelCost = levelCost;
            return this;
        }

        public Builder materialCost(int materialCost){
            this.materialCost = materialCost;
            return this;
        }

        public Builder levelRefund(float levelRefund){
            this.levelRefund = levelRefund;
            return this;
        }

        public Builder materialRefund(float materialRefund){
            this.materialRefund = materialRefund;
            return this;
        }

        public Builder wrappedAttributeModifiers(WrappedAttributeModifierMap wrappedAttributeModifiers){
            this.wrappedAttributeModifiers = wrappedAttributeModifiers;
            return this;
        }

        public MasterworkTier build(){
            return new MasterworkTier(this.levelCost, this.materialCost, this.levelRefund, this.materialRefund, this.wrappedAttributeModifiers);
        }

    }
}
