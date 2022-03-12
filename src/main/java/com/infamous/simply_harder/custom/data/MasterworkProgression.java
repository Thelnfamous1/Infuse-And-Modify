package com.infamous.simply_harder.custom.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.infamous.simply_harder.SimplyHarder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class MasterworkProgression {

    public static final String TIERS = "tiers";
    public static final String ID = "id";
    public static final int STARTING_TIER = 1;
    private final ResourceLocation id;
    private final Map<Integer, MasterworkTier> tiers;

    public static final ResourceLocation NONE_LOCALIZATION = new ResourceLocation(SimplyHarder.MOD_ID, "none");
    public static final MasterworkProgression NONE = new MasterworkProgression(NONE_LOCALIZATION, ImmutableMap.of());

    public MasterworkProgression(ResourceLocation id, Map<Integer, MasterworkTier> tiers){
        this.id = id;
        this.tiers = tiers;
    }

    public Map<Integer, MasterworkTier> getTiers() {
        return this.tiers;
    }

    public static MasterworkProgression fromJson(JsonObject jsonObject){
        ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(jsonObject, ID));
        JsonArray tiersArr = GsonHelper.getAsJsonArray(jsonObject, TIERS);

        int size = tiersArr.size();
        Map<Integer, MasterworkTier> tiers = new LinkedHashMap<>(size);
        for(int i = 0; i < size; i++){
            int currentTier = i + 1;
            tiers.put(currentTier, MasterworkTier.fromJson(tiersArr.get(i)));
        }

        return new MasterworkProgression(id, tiers);
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ID, this.id.toString());
        JsonArray tiersArr = new JsonArray();
        int size = this.tiers.size();
        for(int i = 0; i < size; i++){
            int currentTier = i + 1;
            tiersArr.add(MasterworkTier.toJson(this.tiers.get(currentTier)));
        }
        jsonObject.add(TIERS, tiersArr);
        return jsonObject;
    }

    public static MasterworkProgression fromNetwork(FriendlyByteBuf byteBuf){
        ResourceLocation id = byteBuf.readResourceLocation();
        Map<Integer, MasterworkTier> tiers = byteBuf.readMap(FriendlyByteBuf::readInt, MasterworkTier::fromNetwork);

        return new MasterworkProgression(id, tiers);
    }

    public static void toNetwork(FriendlyByteBuf byteBuf, MasterworkProgression progression){
        byteBuf.writeResourceLocation(progression.id);
        byteBuf.writeMap(progression.tiers, FriendlyByteBuf::writeVarInt, MasterworkTier::toNetwork);
    }

    public boolean inTierRange(int tier) {
        return Math.max(0, tier) == Math.min(tier, this.tiers.size() - 1);
    }

    public Optional<MasterworkTier> getTier(int tier) {
        return Optional.ofNullable(this.tiers.get(tier));
    }

    public boolean matches(MasterworkProgression progression){
        return this.id.equals(progression.id);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public boolean isNone() {
        return this == NONE || this.tiers.isEmpty();
    }
}
