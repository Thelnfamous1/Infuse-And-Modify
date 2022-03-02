package com.infamous.simply_harder.datagen.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class MultiConditionShapedRecipeBuilder extends ShapedRecipeBuilder {
    private final Item result;
    private final int count;
    private final List<String> rows = Lists.newArrayList();
    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    @Nullable
    private String group;

    RequirementsStrategy AND_EXCEPT_LAST = (requirementsIn) -> {

        if(requirementsIn.size() <= 1){
            return RequirementsStrategy.OR.createRequirements(requirementsIn);
        }

        String[][] requirements = new String[requirementsIn.size() - 1][];
        int i = 0;

        String last = "";
        for(String requirement : requirementsIn) {
            if(i < requirementsIn.size() - 1){
                requirements[i++] = new String[]{requirement, ""};
            } else{
                last = requirement;
            }
        }

        for(String[] andRequirements : requirements){
            andRequirements[1] = last;
        }

        return requirements;
    };

    public MultiConditionShapedRecipeBuilder(ItemLike p_126114_, int p_126115_) {
        super(p_126114_, p_126115_);
        this.result = p_126114_.asItem();
        this.count = p_126115_;
    }

    public static MultiConditionShapedRecipeBuilder shaped(ItemLike result) {
        return shaped(result, 1);
    }

    public static MultiConditionShapedRecipeBuilder shaped(ItemLike result, int count) {
        return new MultiConditionShapedRecipeBuilder(result, count);
    }

    @Override
    public MultiConditionShapedRecipeBuilder define(Character p_126122_, Tag<Item> p_126123_) {
        return this.define(p_126122_, Ingredient.of(p_126123_));
    }

    @Override
    public MultiConditionShapedRecipeBuilder define(Character p_126128_, ItemLike p_126129_) {
        return this.define(p_126128_, Ingredient.of(p_126129_));
    }

    @Override
    public MultiConditionShapedRecipeBuilder define(Character p_126125_, Ingredient p_126126_) {
        if (this.key.containsKey(p_126125_)) {
            throw new IllegalArgumentException("Symbol '" + p_126125_ + "' is already defined!");
        } else if (p_126125_ == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(p_126125_, p_126126_);
            return this;
        }
    }

    @Override
    public MultiConditionShapedRecipeBuilder pattern(String p_126131_) {
        if (!this.rows.isEmpty() && p_126131_.length() != this.rows.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.rows.add(p_126131_);
            return this;
        }
    }

    @Override
    public MultiConditionShapedRecipeBuilder unlockedBy(String p_126133_, CriterionTriggerInstance p_126134_) {
        this.advancement.addCriterion(p_126133_, p_126134_);
        return this;
    }

    @Override
    public MultiConditionShapedRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    public void save(Consumer<FinishedRecipe> p_126141_, ResourceLocation p_126142_) {
        this.ensureValid(p_126142_);
        this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(p_126142_)).rewards(AdvancementRewards.Builder.recipe(p_126142_)).requirements(AND_EXCEPT_LAST);
        p_126141_.accept(new ShapedRecipeBuilder.Result(p_126142_, this.result, this.count, this.group == null ? "" : this.group, this.rows, this.key, this.advancement, new ResourceLocation(p_126142_.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + p_126142_.getPath())));
    }

    protected void ensureValid(ResourceLocation id) {
        if (this.rows.isEmpty()) {
            throw new IllegalStateException("No pattern is defined for level gated shaped recipe " + id + "!");
        } else {
            Set<Character> set = Sets.newHashSet(this.key.keySet());
            set.remove(' ');

            for(String s : this.rows) {
                for(int i = 0; i < s.length(); ++i) {
                    char c0 = s.charAt(i);
                    if (!this.key.containsKey(c0) && c0 != ' ') {
                        throw new IllegalStateException("Pattern in recipe " + id + " uses undefined symbol '" + c0 + "'");
                    }

                    set.remove(c0);
                }
            }

            if (!set.isEmpty()) {
                throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + id);
            } else if (this.rows.size() == 1 && this.rows.get(0).length() == 1) {
                throw new IllegalStateException("Shaped recipe " + id + " only takes in a single item - should it be a shapeless recipe instead?");
            } else if (this.advancement.getCriteria().isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + id);
            }
        }
    }
}
