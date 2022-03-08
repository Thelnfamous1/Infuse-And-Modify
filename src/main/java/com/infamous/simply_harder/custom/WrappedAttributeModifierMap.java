package com.infamous.simply_harder.custom;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.*;
import com.infamous.simply_harder.util.AttributeHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.Map;
import java.util.Optional;


public record WrappedAttributeModifierMap(
        Multimap<Attribute, WrappedAttributeModifier> modifiers) {

    public static final String ATTRIBUTE = "attribute";

    public JsonArray toJson() {
        JsonArray jsonarray = new JsonArray();

        for (Map.Entry<Attribute, WrappedAttributeModifier> entry : this.modifiers.entries()) {
            jsonarray.add(entry.getValue().toJson(entry.getKey()));
        }

        return jsonarray;
    }

    public static WrappedAttributeModifierMap fromJson(JsonElement jsonElement) {
        Multimap<Attribute, WrappedAttributeModifier> modifiers = ArrayListMultimap.create();
        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            jsonArray.forEach(je -> {
                JsonObject jsonObject = je.getAsJsonObject();
                ResourceLocation attributeName = ResourceLocation.tryParse(GsonHelper.getAsString(jsonObject, ATTRIBUTE));
                Optional<Attribute> attribute = AttributeHelper.getAttribute(attributeName);
                attribute.ifPresentOrElse(a -> modifiers.put(a, WrappedAttributeModifier.fromJson(jsonObject)), () -> {
                    throw new JsonParseException("Invalid attribute name: " + attributeName);
                });
            });
            return new WrappedAttributeModifierMap(modifiers);
        } else {
            throw new JsonParseException("Expected " + WrappedMasterworkTier.MODIFIERS + " to be an array");
        }
    }

    public void toNetwork(FriendlyByteBuf friendlyByteBuf) {
        String jsonString = this.toJson().toString();
        friendlyByteBuf.writeUtf(jsonString);
    }

    public static WrappedAttributeModifierMap fromNetwork(FriendlyByteBuf byteBuf) {
        String jsonString = byteBuf.readUtf();
        return fromJson(JsonParser.parseString(jsonString));
    }
}
