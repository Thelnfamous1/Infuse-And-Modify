package com.infamous.simply_harder.custom.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public record MasterworkTier(int levelCost, int materialCost,
                             WrappedAttributeModifierMap wrappedAttributeModifiers) {

    public static final String LEVEL_COST = "level_cost";
    public static final String MATERIAL_COST = "material_cost";
    public static final String ATTRIBUTE_MODIFIERS = "attribute_modifiers";

    public static final MasterworkTier EMPTY = new MasterworkTier(0, 0, WrappedAttributeModifierMap.EMPTY);

    public static MasterworkTier fromNetwork(FriendlyByteBuf byteBuf) {
        int levelCost = byteBuf.readVarInt();
        int materialCost = byteBuf.readVarInt();
        WrappedAttributeModifierMap wrappedAttributeModifiers = WrappedAttributeModifierMap.fromNetwork(byteBuf);
        return new MasterworkTier(levelCost, materialCost, wrappedAttributeModifiers);
    }

    public static MasterworkTier fromJson(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            JsonObject tierObj = jsonElement.getAsJsonObject();
            int levelCost = GsonHelper.getAsInt(tierObj, LEVEL_COST, 1);
            int materialCost = GsonHelper.getAsInt(tierObj, MATERIAL_COST, 1);
            WrappedAttributeModifierMap wrappedAttributeModifiers = WrappedAttributeModifierMap.fromJson(tierObj.get(ATTRIBUTE_MODIFIERS));
            return new MasterworkTier(levelCost, materialCost, wrappedAttributeModifiers);
        } else {
            throw new JsonParseException("Expected " + MasterworkProgression.TIERS + " to be an object");
        }
    }

    public static void toNetwork(FriendlyByteBuf byteBuf, MasterworkTier masterworkTier) {
        byteBuf.writeVarInt(masterworkTier.levelCost);
        byteBuf.writeVarInt(masterworkTier.materialCost);
        masterworkTier.wrappedAttributeModifiers.toNetwork(byteBuf);
    }
}
