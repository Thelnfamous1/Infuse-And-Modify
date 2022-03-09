package com.infamous.simply_harder.datagen.builder;

import com.google.gson.JsonObject;
import com.infamous.simply_harder.custom.data.MasterworkProgression;
import com.infamous.simply_harder.custom.data.MasterworkTier;
import com.infamous.simply_harder.custom.data.WrappedAttributeModifierMap;
import com.infamous.simply_harder.datagen.provider.FinishedMasterworkProgression;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class MasterworkProgressionBuilder {
    private final ResourceLocation progressionId;
    private final Map<Integer, MasterworkTier> tiers = new HashMap<>();
    private int counter = 0;

    public MasterworkProgressionBuilder(ResourceLocation progressionId) {
        this.progressionId = progressionId;
    }

    public static MasterworkProgressionBuilder progression(ResourceLocation progressionId){
        return new MasterworkProgressionBuilder(progressionId);
    }

    public MasterworkProgressionBuilder addTier(MasterworkTier tier){
        this.tiers.put(++this.counter, tier);
        return this;
    }

    private static void validateArgs(EquipmentSlot[] slots, Attribute[] attributes, UUID[] uuids, double[] amounts, AttributeModifier.Operation[] operations) {
        for(EquipmentSlot slot : EquipmentSlot.values()){
            int seenCount = 0;
            for(EquipmentSlot inputSlot : slots){
                if(inputSlot == slot){
                    seenCount++;
                }
            }
            if(seenCount > 1){
                throw new IllegalArgumentException("The EquipmentSlot array must not have duplicate values!");
            }
        }

        if(attributes.length == 0){
            throw new IllegalArgumentException("Do not use an empty Attribute array!");
        }

        if(uuids.length != attributes.length){
            throw new IllegalArgumentException("The UUID array must have same size as the Attribute array!");
        }
        if(amounts.length != attributes.length){
            throw new IllegalArgumentException("The Double array must have same size as the Attribute array!");
        }
        if(operations.length != attributes.length){
            throw new IllegalArgumentException("The Operation array must have same size as the Attribute array!");
        }
    }

    private static int calculateDefaultCostForTier(int tier) {
        return tier % 2 == 0 ? tier / 2 : tier / 2 + 1;
    }

    public MasterworkProgressionBuilder addDefaultTiers(EquipmentSlot[] slots, Attribute[] attributes, UUID[] uuids, double[] amounts, AttributeModifier.Operation[] operations) {
        validateArgs(slots, attributes, uuids, amounts, operations);

        for(int tier = 1; tier <= 10; tier++){
            WrappedAttributeModifierMap.Builder wrappedAttributeModifiersBuilder = WrappedAttributeModifierMap.Builder.builder();

            for(EquipmentSlot slot : slots){
                for(int i = 0; i < attributes.length; i++){
                    wrappedAttributeModifiersBuilder.addAttributeModifier(attributes[i], slot, new AttributeModifier(uuids[slot.getIndex()], getId().toString(), amounts[i] * tier, operations[i]));
                }
            }
            this.addTier(MasterworkTier.Builder.builder()
                    .levelCost(calculateDefaultCostForTier(tier))
                    .materialCost(calculateDefaultCostForTier(tier))
                    .wrappedAttributeModifiers(wrappedAttributeModifiersBuilder.build())
                    .build()
            );
        }
        return this;
    }

    public MasterworkProgression build(){
        return new MasterworkProgression(this.progressionId, this.tiers);
    }

    public void save(Consumer<FinishedMasterworkProgression> onFinished) {
        onFinished.accept(new MasterworkProgressionBuilder.Result(this.progressionId, this.tiers));
    }

    public ResourceLocation getId() {
        return this.progressionId;
    }

    public static class Result implements FinishedMasterworkProgression {
        private final MasterworkProgression progression;

        public Result(ResourceLocation id, Map<Integer, MasterworkTier> tiers) {
            this.progression = new MasterworkProgression(id, tiers);
        }

        public JsonObject serializeMasterworkProgression() {
            return this.progression.toJson();
        }

        public ResourceLocation getId() {
            return this.progression.getId();
        }
    }
}
