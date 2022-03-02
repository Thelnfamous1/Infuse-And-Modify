package com.infamous.simply_harder.datagen.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class DisabledRecipeBuilder implements RecipeBuilder {

    private final Item result;

    public DisabledRecipeBuilder(ItemLike result){
        this.result = result.asItem();
    }

    public static DisabledRecipeBuilder disabled(ItemLike result){
        return new DisabledRecipeBuilder(result);
    }

    @Override
    public RecipeBuilder unlockedBy(String p_176496_, CriterionTriggerInstance p_176497_) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String p_176495_) {
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> onFinished, ResourceLocation id) {
        onFinished.accept(new Result(id, this.result, new ResourceLocation(id.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + id.getPath())));

    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Advancement.Builder advancement = Advancement.Builder.advancement()
                .addCriterion("impossible", impossible())
                .parent(new ResourceLocation("recipes/root"));
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, Item result, ResourceLocation advancementId) {
            this.id = id;
            this.advancementId = advancementId;
        }

        private static ImpossibleTrigger.TriggerInstance impossible() {
            return new ImpossibleTrigger.TriggerInstance();
        }

        @Override
        public JsonObject serializeRecipe() {
            JsonObject jsonobject = new JsonObject();
            this.serializeRecipeData(jsonobject);
            return jsonobject;
        }

        @Override
        public void serializeRecipeData(JsonObject jsonObject) {
            JsonArray conditionsArr = new JsonArray();
            JsonObject elemObj = new JsonObject();

            elemObj.addProperty("type", "forge:false");
            conditionsArr.add(elemObj);

            jsonObject.add("conditions", conditionsArr);
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return null;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
