package com.infamous.simply_harder.custom;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public record WrappedMasterworkTier(int levelCost, int materialCost,
                                    WrappedAttributeModifierMap modifierMap) {

    public static final String LEVEL_COST = "level_cost";
    public static final String MATERIAL_COST = "material_cost";
    public static final String MODIFIERS = "modifiers";

    public static WrappedMasterworkTier fromNetwork(FriendlyByteBuf byteBuf) {
        int levelCost = byteBuf.readVarInt();
        int materialCost = byteBuf.readVarInt();
        WrappedAttributeModifierMap modifierMap = WrappedAttributeModifierMap.fromNetwork(byteBuf);
        return new WrappedMasterworkTier(levelCost, materialCost, modifierMap);
    }

    public static WrappedMasterworkTier fromJson(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            JsonObject tierObj = jsonElement.getAsJsonObject();
            int levelCost = GsonHelper.getAsInt(tierObj, LEVEL_COST, 1);
            int materialCost = GsonHelper.getAsInt(tierObj, MATERIAL_COST, 1);
            WrappedAttributeModifierMap modifierMap = WrappedAttributeModifierMap.fromJson(tierObj.get(MODIFIERS));
            return new WrappedMasterworkTier(levelCost, materialCost, modifierMap);
        } else {
            throw new JsonParseException("Expected masterwork tiers to be an object");
        }
    }

    public void toNetwork(FriendlyByteBuf byteBuf) {
        byteBuf.writeVarInt(this.levelCost);
        byteBuf.writeVarInt(this.materialCost);
        this.modifierMap.toNetwork(byteBuf);
    }
}
