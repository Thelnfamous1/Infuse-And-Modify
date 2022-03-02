package com.infamous.simply_harder.custom.critera;

import com.google.gson.JsonObject;
import com.infamous.simply_harder.SimplyHarder;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

public class ExperienceChangeTrigger extends SimpleCriterionTrigger<ExperienceChangeTrigger.TriggerInstance> {
    private static final ResourceLocation ID = new ResourceLocation(SimplyHarder.MOD_ID, "experience_changed");

    public ResourceLocation getId() {
        return ID;
    }

    public ExperienceChangeTrigger.TriggerInstance createInstance(JsonObject jsonObject, EntityPredicate.Composite composite, DeserializationContext deserializationContext) {
        JsonObject experienceObj = GsonHelper.getAsJsonObject(jsonObject, "experience", new JsonObject());
        MinMaxBounds.Ints experienceLevel = MinMaxBounds.Ints.fromJson(experienceObj.get("experience_level"));
        MinMaxBounds.Ints totalExperience = MinMaxBounds.Ints.fromJson(experienceObj.get("total_experience"));
        return new ExperienceChangeTrigger.TriggerInstance(composite, experienceLevel, totalExperience);
    }

    public void triggerLevelChange(ServerPlayer serverPlayer, int experienceLevel) {
        this.trigger(serverPlayer, experienceLevel, serverPlayer.totalExperience);
    }

    public void triggerTotalChange(ServerPlayer serverPlayer, int totalExperience) {
        this.trigger(serverPlayer, serverPlayer.experienceLevel, totalExperience);
    }

    public void trigger(ServerPlayer serverPlayer, int experienceLevel, int totalExperience) {
        this.trigger(serverPlayer, (triggerInstance) -> triggerInstance.matches(experienceLevel, totalExperience));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final MinMaxBounds.Ints hasExperienceLevel;
        private final MinMaxBounds.Ints hasTotalExperience;

        public TriggerInstance(EntityPredicate.Composite composite, MinMaxBounds.Ints hasExperienceLevel, MinMaxBounds.Ints hasTotalExperience) {
            super(ExperienceChangeTrigger.ID, composite);
            this.hasExperienceLevel = hasExperienceLevel;
            this.hasTotalExperience = hasTotalExperience;
        }

        public static ExperienceChangeTrigger.TriggerInstance hasLevel(int experienceLevel) {
            return new ExperienceChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.atLeast(experienceLevel), MinMaxBounds.Ints.ANY);
        }

        public static ExperienceChangeTrigger.TriggerInstance hasTotal(int totalExperience) {
            return new ExperienceChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.atLeast(totalExperience));
        }

        public static ExperienceChangeTrigger.TriggerInstance hasExperience(int experienceLevel, int totalExperience) {
            return new ExperienceChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.atLeast(experienceLevel), MinMaxBounds.Ints.atLeast(totalExperience));
        }

        public JsonObject serializeToJson(SerializationContext p_43196_) {
            JsonObject mainObj = super.serializeToJson(p_43196_);
            if (!this.hasExperienceLevel.isAny() || !this.hasTotalExperience.isAny()) {
                JsonObject experienceObj = new JsonObject();
                experienceObj.add("experience_level", this.hasExperienceLevel.serializeToJson());
                experienceObj.add("total_experience", this.hasTotalExperience.serializeToJson());
                mainObj.add("experience", experienceObj);
            }

            return mainObj;
        }

        public boolean matches(int experienceLevel, int totalExperience) {
            return this.hasExperienceLevel.matches(experienceLevel) &&
                    this.hasTotalExperience.matches(totalExperience);
        }
    }
}
