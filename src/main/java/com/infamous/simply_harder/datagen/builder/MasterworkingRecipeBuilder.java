package com.infamous.simply_harder.datagen.builder;

import com.google.gson.JsonObject;
import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.custom.recipe.InfusionRecipe;
import com.infamous.simply_harder.custom.recipe.MasterworkingRecipe;
import com.infamous.simply_harder.registry.SHRecipes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class MasterworkingRecipeBuilder {
    private final Ingredient masterworkable;
    private final ResourceLocation progressionId;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();

    public MasterworkingRecipeBuilder(Ingredient masterworkable, ResourceLocation progressionId) {
        this.masterworkable = masterworkable;
        this.progressionId = progressionId;
    }

    public static MasterworkingRecipeBuilder masterworking(ItemLike masterworkable, ResourceLocation progressionId) {
        return masterworking(Ingredient.of(masterworkable), progressionId);
    }

    public static MasterworkingRecipeBuilder masterworking(Tag<Item> masterworkable, ResourceLocation progressionId) {
        return masterworking(Ingredient.of(masterworkable), progressionId);
    }

    public static MasterworkingRecipeBuilder masterworking(Ingredient base, ResourceLocation progressionId) {
        return new MasterworkingRecipeBuilder(base, progressionId);
    }

    public MasterworkingRecipeBuilder unlocks(String name, CriterionTriggerInstance trigger) {
        this.advancement.addCriterion(name, trigger);
        return this;
    }
    
    public void save(Consumer<FinishedRecipe> onFinished, Item base){
        this.save(onFinished, base.getRegistryName().getPath());
    }

    public void save(Consumer<FinishedRecipe> onFinished, String name) {
        this.save(onFinished, new ResourceLocation(SimplyHarder.MOD_ID, name));
    }

    public void save(Consumer<FinishedRecipe> onFinished, ResourceLocation id) {
        this.ensureValid(id);
        this.advancement.parent(new ResourceLocation(id.getNamespace(), "recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
        onFinished.accept(new MasterworkingRecipeBuilder.Result(new ResourceLocation(id.getNamespace(), MasterworkingRecipe.NAME + "/" + id.getPath()), this.masterworkable, this.progressionId, this.advancement, new ResourceLocation(id.getNamespace(), "recipes/" + MasterworkingRecipe.NAME + "/" + id.getPath())));
    }

    private void ensureValid(ResourceLocation id) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient masterworkable;
        private final ResourceLocation progressionId;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, Ingredient masterworkable, ResourceLocation progressionId, Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.masterworkable = masterworkable;
            this.progressionId = progressionId;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        public void serializeRecipeData(JsonObject jsonObject) {
            jsonObject.add(MasterworkingRecipe.Serializer.MASTERWORKABLE, this.masterworkable.toJson());
            jsonObject.addProperty(MasterworkingRecipe.Serializer.PROGRESSION, this.progressionId.toString());
        }

        public ResourceLocation getId() {
            return this.id;
        }

        public RecipeSerializer<?> getType() {
            return SHRecipes.MASTERWORKING.get();
        }

        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
