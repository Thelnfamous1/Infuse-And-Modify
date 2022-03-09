package com.infamous.simply_harder.custom.data;

import com.google.common.collect.*;
import com.google.gson.*;
import com.infamous.simply_harder.util.AttributeHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;


public class WrappedAttributeModifierMap {

    private final Multimap<Attribute, WrappedAttributeModifier> attributeModifiers;

    public static final WrappedAttributeModifierMap EMPTY = new WrappedAttributeModifierMap(ImmutableMultimap.of());

    public WrappedAttributeModifierMap(
            Multimap<Attribute, WrappedAttributeModifier> attributeModifiers){
        this.attributeModifiers = attributeModifiers;
    }

    public static final String ATTRIBUTE = "attribute";
    private final Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> cachedForSlot = Maps.newHashMap();

    public Multimap<Attribute, AttributeModifier> forSlot(EquipmentSlot slot){
        return this.cachedForSlot.computeIfAbsent(slot, k -> {
            Multimap<Attribute, WrappedAttributeModifier> filterBySlot = Multimaps.filterValues(this.attributeModifiers, input -> input.slot() == null || input.slot() == slot);
            return Multimaps.transformValues(filterBySlot, WrappedAttributeModifier::attributeModifier);
        });
    }

    public JsonArray toJson() {
        JsonArray jsonarray = new JsonArray();

        for (Map.Entry<Attribute, WrappedAttributeModifier> entry : this.attributeModifiers.entries()) {
            jsonarray.add(entry.getValue().toJson(entry.getKey()));
        }

        return jsonarray;
    }

    public static WrappedAttributeModifierMap fromJson(JsonElement jsonElement) {
        Multimap<Attribute, WrappedAttributeModifier> attributeModifiers = ArrayListMultimap.create();
        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            jsonArray.forEach(je -> {
                JsonObject jsonObject = je.getAsJsonObject();
                ResourceLocation attributeName = ResourceLocation.tryParse(GsonHelper.getAsString(jsonObject, ATTRIBUTE));
                Optional<Attribute> attribute = AttributeHelper.getAttribute(attributeName);
                attribute.ifPresentOrElse(a -> attributeModifiers.put(a, WrappedAttributeModifier.fromJson(jsonObject)), () -> {
                    throw new JsonParseException("Invalid attribute name: " + attributeName);
                });
            });
            return new WrappedAttributeModifierMap(attributeModifiers);
        } else {
            throw new JsonParseException("Expected " + MasterworkTier.ATTRIBUTE_MODIFIERS + " to be an array");
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

    public Multimap<Attribute, WrappedAttributeModifier> getAttributeModifiers() {
        return this.attributeModifiers;
    }

    public static class Builder{
        private final ImmutableMultimap.Builder<Attribute, WrappedAttributeModifier> attributeModifiers = ImmutableMultimap.builder();

        public Builder(){
        }

        public static Builder builder(){
            return new Builder();
        }

        public Builder addAttributeModifier(Attribute attribute, @Nullable EquipmentSlot slot, AttributeModifier modifier){
            this.attributeModifiers.put(attribute, new WrappedAttributeModifier(slot, modifier));
            return this;
        }

        public WrappedAttributeModifierMap build(){
            return new WrappedAttributeModifierMap(this.attributeModifiers.build());
        }

    }
}
