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
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class InfusionRecipeBuilder {
    private final Ingredient base;
    private final Ingredient addition;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();

    public InfusionRecipeBuilder(Ingredient base, Ingredient addition) {
        this.base = base;
        this.addition = addition;
    }

    public static InfusionRecipeBuilder infusion(ItemLike base, ItemLike addition) {
        return infusion(Ingredient.of(base), Ingredient.of(addition));
    }

    public static InfusionRecipeBuilder infusion(Ingredient base, Ingredient addition) {
        return new InfusionRecipeBuilder(base, addition);
    }

    public InfusionRecipeBuilder unlocks(String name, CriterionTriggerInstance trigger) {
        this.advancement.addCriterion(name, trigger);
        return this;
    }
    
    public void save(Consumer<FinishedRecipe> onFinished, Item base, Item addition, CreativeModeTab creativeTab){
        this.save(onFinished, addition.getRegistryName().getPath() + "_from_" + base.getRegistryName().getPath(), creativeTab);
    }

    public void save(Consumer<FinishedRecipe> onFinished, String name, CreativeModeTab creativeTab) {
        this.save(onFinished, new ResourceLocation(SimplyHarder.MOD_ID, name), creativeTab);
    }

    public void save(Consumer<FinishedRecipe> onFinished, ResourceLocation id, CreativeModeTab creativeTab) {
        this.ensureValid(id);
        this.advancement.parent(new ResourceLocation(id.getNamespace(), "recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
        onFinished.accept(new InfusionRecipeBuilder.Result(new ResourceLocation(id.getNamespace(), InfusionRecipe.NAME + "/" + id.getPath()), this.base, this.addition, this.advancement, new ResourceLocation(id.getNamespace(), "recipes/" + creativeTab.getRecipeFolderName() + "/" + id.getPath())));
    }

    private void ensureValid(ResourceLocation id) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient base;
        private final Ingredient addition;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, Ingredient base, Ingredient addition, Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.base = base;
            this.addition = addition;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        public void serializeRecipeData(JsonObject jsonObject) {
            jsonObject.add(InfusionRecipe.BASE_NAME, this.base.toJson());
            jsonObject.add(InfusionRecipe.ADDITION_NAME, this.addition.toJson());
        }

        public ResourceLocation getId() {
            return this.id;
        }

        public RecipeSerializer<?> getType() {
            return SHRecipes.INFUSION.get();
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
