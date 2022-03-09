package com.infamous.simply_harder.custom.data;

import com.google.common.collect.ImmutableMultimap;
import com.google.gson.JsonObject;
import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.custom.item.GearModItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;

public record GearMod(ResourceLocation id,
                      Ingredient installable,
                      int levelCost,
                      WrappedAttributeModifierMap wrappedAttributeModifiers) {

    public static final String ID = "id";
    public static final String INSTALLABLE = "installable";
    public static final String LEVEL_COST = "level_cost";
    public static final String ATTRIBUTE_MODIFIERS = "attribute_modifiers";
    public static final ResourceLocation UNKNOWN_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, "unknown");
    public static final GearMod UNKNOWN = new GearMod(UNKNOWN_LOCALIZATION, Ingredient.EMPTY, 1, new WrappedAttributeModifierMap(ImmutableMultimap.of()));

    public static GearMod fromJson(JsonObject jsonObject) {
        ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(jsonObject, ID));
        Ingredient installable = Ingredient.fromJson(jsonObject.get(INSTALLABLE));
        int levelCost = GsonHelper.getAsInt(jsonObject, LEVEL_COST, 1);
        WrappedAttributeModifierMap attributeModifiers = WrappedAttributeModifierMap.fromJson(jsonObject.get(ATTRIBUTE_MODIFIERS));
        return new GearMod(id, installable, levelCost, attributeModifiers);
    }

    public static GearMod fromNetwork(FriendlyByteBuf byteBuf) {
        ResourceLocation id = new ResourceLocation(byteBuf.readUtf());
        Ingredient installable = Ingredient.fromNetwork(byteBuf);
        int levelCost = byteBuf.readVarInt();
        WrappedAttributeModifierMap attributeModifiers = WrappedAttributeModifierMap.fromNetwork(byteBuf);
        return new GearMod(id, installable, levelCost, attributeModifiers);
    }

    public static void toNetwork(FriendlyByteBuf byteBuf, GearMod gearMod) {
        byteBuf.writeResourceLocation(gearMod.id);
        gearMod.installable.toNetwork(byteBuf);
        byteBuf.writeVarInt(gearMod.levelCost);
        gearMod.wrappedAttributeModifiers.toNetwork(byteBuf);
    }

    public CompoundTag save(){
        CompoundTag compoundtag = new CompoundTag();
        compoundtag.putString(GearModItem.GEAR_MOD_NAME, this.id.toString());
        return compoundtag;
    }

}
