package com.infamous.simply_harder.custom;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import javax.annotation.Nullable;
import java.util.UUID;

public record WrappedAttributeModifier(EquipmentSlot slot,
                                       AttributeModifier modifier) {

    public static final String SLOT = "slot";
    public static final String UUID = "uuid";
    public static final String NAME = "name";
    public static final String AMOUNT = "amount";
    public static final String OPERATION = "operation";

    public WrappedAttributeModifier(@Nullable EquipmentSlot slot, AttributeModifier modifier) {
        this.slot = slot;
        this.modifier = modifier;
    }

    public static WrappedAttributeModifier fromJson(JsonElement jsonElement) {
        EquipmentSlot slot;
        AttributeModifier modifier;
        if (jsonElement.isJsonObject()) {
            JsonObject modifierObj = jsonElement.getAsJsonObject();

            slot = modifierObj.has(SLOT) ? EquipmentSlot.byName(modifierObj.get(SLOT).getAsString()) : null;

            UUID uuid = java.util.UUID.fromString(GsonHelper.getAsString(modifierObj, UUID));
            String name = GsonHelper.getAsString(modifierObj, NAME);
            double amount = GsonHelper.getAsDouble(modifierObj, AMOUNT);
            AttributeModifier.Operation operation = AttributeModifier.Operation.fromValue(GsonHelper.getAsInt(modifierObj, OPERATION));

            modifier = new AttributeModifier(uuid, name, amount, operation);
            return new WrappedAttributeModifier(slot, modifier);
        } else {
            throw new JsonParseException("Expected " + WrappedMasterworkTier.MODIFIERS + " entry to be an object");
        }
    }

    public JsonObject toJson(Attribute attribute) {
        JsonObject obj = new JsonObject();
        obj.addProperty(WrappedAttributeModifierMap.ATTRIBUTE, attribute.getRegistryName().toString());
        if (this.slot != null) obj.addProperty(SLOT, this.slot.getName());
        obj.addProperty(UUID, this.modifier.getId().toString());
        obj.addProperty(NAME, this.modifier.getName());
        obj.addProperty(AMOUNT, this.modifier.getAmount());
        obj.addProperty(OPERATION, this.modifier.getOperation().toValue());
        return obj;
    }
}
