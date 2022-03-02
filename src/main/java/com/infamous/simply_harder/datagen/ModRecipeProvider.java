package com.infamous.simply_harder.datagen;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.datagen.builder.MultiConditionShapedRecipeBuilder;
import com.infamous.simply_harder.datagen.builder.DisabledRecipeBuilder;
import com.infamous.simply_harder.custom.critera.ExperienceChangeTrigger;
import com.infamous.simply_harder.datagen.util.InfusionRecipeHelper;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> onFinished) {
        InfusionRecipeHelper.infusion(onFinished, ArmorMaterials.LEATHER, Items.LEATHER_BOOTS, Items.LEATHER_CHESTPLATE, Items.LEATHER_HELMET, Items.LEATHER_LEGGINGS);
        InfusionRecipeHelper.infusion(onFinished, ArmorMaterials.CHAIN, Items.CHAINMAIL_BOOTS, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_HELMET, Items.CHAINMAIL_LEGGINGS);
        InfusionRecipeHelper.infusion(onFinished, ArmorMaterials.IRON, Items.IRON_BOOTS, Items.IRON_CHESTPLATE, Items.IRON_HELMET, Items.IRON_LEGGINGS);
        InfusionRecipeHelper.infusion(onFinished, ArmorMaterials.GOLD, Items.GOLDEN_BOOTS, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_HELMET, Items.GOLDEN_LEGGINGS);
        InfusionRecipeHelper.infusion(onFinished, ArmorMaterials.DIAMOND, Items.DIAMOND_BOOTS, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_HELMET, Items.DIAMOND_LEGGINGS);
        InfusionRecipeHelper.infusion(onFinished, ModItemTagsProvider.HELMETS, ArmorMaterials.TURTLE, Items.TURTLE_HELMET); // Special case: Turtle tier only used for 1 item in vanilla, Turtle Helmet
        InfusionRecipeHelper.infusion(onFinished, ArmorMaterials.NETHERITE, Items.NETHERITE_BOOTS, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_HELMET, Items.NETHERITE_LEGGINGS);

        InfusionRecipeHelper.infusion(onFinished, Tiers.WOOD, Items.WOODEN_AXE, Items.WOODEN_HOE, Items.WOODEN_PICKAXE, Items.WOODEN_SHOVEL, Items.WOODEN_SWORD);
        InfusionRecipeHelper.infusion(onFinished, Tiers.STONE, Items.STONE_AXE, Items.STONE_HOE, Items.STONE_PICKAXE, Items.STONE_SHOVEL, Items.STONE_SWORD);
        InfusionRecipeHelper.infusion(onFinished, Tiers.IRON, Items.IRON_AXE, Items.IRON_HOE, Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_SWORD);
        InfusionRecipeHelper.infusion(onFinished, Tiers.DIAMOND, Items.DIAMOND_AXE, Items.DIAMOND_HOE, Items.DIAMOND_PICKAXE, Items.DIAMOND_SHOVEL, Items.DIAMOND_SWORD);
        InfusionRecipeHelper.infusion(onFinished, Tiers.GOLD, Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_SWORD);
        InfusionRecipeHelper.infusion(onFinished, Tiers.NETHERITE, Items.NETHERITE_AXE, Items.NETHERITE_HOE, Items.NETHERITE_PICKAXE, Items.NETHERITE_SHOVEL, Items.NETHERITE_SWORD);

        // REPLACE STONE TOOLS VANILLA RECIPES, REQUIRE LEVEL 10 to unlock
        MultiConditionShapedRecipeBuilder.shaped(Items.STONE_AXE).define('#', Items.STICK).define('X', ItemTags.STONE_TOOL_MATERIALS).pattern("XX").pattern("X#").pattern(" #").unlockedBy("has_cobblestone", has(ItemTags.STONE_TOOL_MATERIALS)).unlockedBy("has_experience", hasExperienceLevel(10)).save(onFinished);
        MultiConditionShapedRecipeBuilder.shaped(Items.STONE_HOE).define('#', Items.STICK).define('X', ItemTags.STONE_TOOL_MATERIALS).pattern("XX").pattern(" #").pattern(" #").unlockedBy("has_cobblestone", has(ItemTags.STONE_TOOL_MATERIALS)).unlockedBy("has_experience", hasExperienceLevel(10)).save(onFinished);
        MultiConditionShapedRecipeBuilder.shaped(Items.STONE_PICKAXE).define('#', Items.STICK).define('X', ItemTags.STONE_TOOL_MATERIALS).pattern("XXX").pattern(" # ").pattern(" # ").unlockedBy("has_cobblestone", has(ItemTags.STONE_TOOL_MATERIALS)).unlockedBy("has_experience", hasExperienceLevel(10)).save(onFinished);
        MultiConditionShapedRecipeBuilder.shaped(Items.STONE_SHOVEL).define('#', Items.STICK).define('X', ItemTags.STONE_TOOL_MATERIALS).pattern("X").pattern("#").pattern("#").unlockedBy("has_cobblestone", has(ItemTags.STONE_TOOL_MATERIALS)).unlockedBy("has_experience", hasExperienceLevel(10)).save(onFinished);
        MultiConditionShapedRecipeBuilder.shaped(Items.STONE_SWORD).define('#', Items.STICK).define('X', ItemTags.STONE_TOOL_MATERIALS).pattern("X").pattern("X").pattern("#").unlockedBy("has_cobblestone", has(ItemTags.STONE_TOOL_MATERIALS)).unlockedBy("has_experience", hasExperienceLevel(10)).save(onFinished);

        // ADD CHAINMAIL ARMOR RECIPES, REQUIRE LEVEL 10 to unlock
        MultiConditionShapedRecipeBuilder.shaped(Items.CHAINMAIL_BOOTS).define('X', Items.CHAIN).define('#', Items.IRON_NUGGET).pattern("# #").pattern("X X").unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)).unlockedBy("has_experience", hasExperienceLevel(10)).save(onFinished, new ResourceLocation(SimplyHarder.MOD_ID, "chainmail_boots"));
        MultiConditionShapedRecipeBuilder.shaped(Items.CHAINMAIL_CHESTPLATE).define('X', Items.CHAIN).define('#', Items.IRON_NUGGET).pattern("X X").pattern("#X#").pattern("X#X").unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)).unlockedBy("has_experience", hasExperienceLevel(10)).save(onFinished, new ResourceLocation(SimplyHarder.MOD_ID, "chainmail_chestplate"));
        MultiConditionShapedRecipeBuilder.shaped(Items.CHAINMAIL_HELMET).define('X', Items.CHAIN).define('#', Items.IRON_NUGGET).pattern("#X#").pattern("X X").unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)).unlockedBy("has_experience", hasExperienceLevel(10)).save(onFinished, new ResourceLocation(SimplyHarder.MOD_ID, "chainmail_helmet"));
        MultiConditionShapedRecipeBuilder.shaped(Items.CHAINMAIL_LEGGINGS).define('X', Items.CHAIN).define('#', Items.IRON_NUGGET).pattern("X#X").pattern("# #").pattern("X X").unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)).unlockedBy("has_experience", hasExperienceLevel(10)).save(onFinished, new ResourceLocation(SimplyHarder.MOD_ID, "chainmail_leggings"));

        // REPLACE IRON TOOLS VANILLA RECIPES, REQUIRE LEVEL 20 to unlock
        MultiConditionShapedRecipeBuilder.shaped(Items.IRON_AXE).define('#', Items.STICK).define('X', Items.IRON_INGOT).pattern("XX").pattern("X#").pattern(" #").unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)).unlockedBy("has_experience", hasExperienceLevel(20)).save(onFinished);
        MultiConditionShapedRecipeBuilder.shaped(Items.IRON_HOE).define('#', Items.STICK).define('X', Items.IRON_INGOT).pattern("XX").pattern(" #").pattern(" #").unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)).unlockedBy("has_experience", hasExperienceLevel(20)).save(onFinished);
        MultiConditionShapedRecipeBuilder.shaped(Items.IRON_PICKAXE).define('#', Items.STICK).define('X', Items.IRON_INGOT).pattern("XXX").pattern(" # ").pattern(" # ").unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)).unlockedBy("has_experience", hasExperienceLevel(20)).save(onFinished);
        MultiConditionShapedRecipeBuilder.shaped(Items.IRON_SHOVEL).define('#', Items.STICK).define('X', Items.IRON_INGOT).pattern("X").pattern("#").pattern("#").unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)).unlockedBy("has_experience", hasExperienceLevel(20)).save(onFinished);
        MultiConditionShapedRecipeBuilder.shaped(Items.IRON_SWORD).define('#', Items.STICK).define('X', Items.IRON_INGOT).pattern("X").pattern("X").pattern("#").unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)).unlockedBy("has_experience", hasExperienceLevel(20)).save(onFinished);

        // REPLACE IRON ARMOR VANILLA RECIPES, REQUIRE LEVEL 20 to unlock
        MultiConditionShapedRecipeBuilder.shaped(Items.IRON_BOOTS).define('X', Items.IRON_INGOT).pattern("X X").pattern("X X").unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)).unlockedBy("has_experience", hasExperienceLevel(20)).save(onFinished);
        MultiConditionShapedRecipeBuilder.shaped(Items.IRON_CHESTPLATE).define('X', Items.IRON_INGOT).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)).unlockedBy("has_experience", hasExperienceLevel(20)).save(onFinished);
        MultiConditionShapedRecipeBuilder.shaped(Items.IRON_HELMET).define('X', Items.IRON_INGOT).pattern("XXX").pattern("X X").unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)).unlockedBy("has_experience", hasExperienceLevel(20)).save(onFinished);
        MultiConditionShapedRecipeBuilder.shaped(Items.IRON_LEGGINGS).define('X', Items.IRON_INGOT).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)).unlockedBy("has_experience", hasExperienceLevel(20)).save(onFinished);

        // ADD NETHERITE TOOLS RECIPES
        ShapedRecipeBuilder.shaped(Items.NETHERITE_AXE).define('#', Items.STICK).define('X', Items.NETHERITE_INGOT).pattern("XX").pattern("X#").pattern(" #").unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT)).save(onFinished, new ResourceLocation(SimplyHarder.MOD_ID, "netherite_axe"));
        ShapedRecipeBuilder.shaped(Items.NETHERITE_HOE).define('#', Items.STICK).define('X', Items.NETHERITE_INGOT).pattern("XX").pattern(" #").pattern(" #").unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT)).save(onFinished, new ResourceLocation(SimplyHarder.MOD_ID, "netherite_hoe"));
        ShapedRecipeBuilder.shaped(Items.NETHERITE_PICKAXE).define('#', Items.STICK).define('X', Items.NETHERITE_INGOT).pattern("XXX").pattern(" # ").pattern(" # ").unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT)).save(onFinished, new ResourceLocation(SimplyHarder.MOD_ID, "netherite_pickaxe"));
        ShapedRecipeBuilder.shaped(Items.NETHERITE_SHOVEL).define('#', Items.STICK).define('X', Items.NETHERITE_INGOT).pattern("X").pattern("#").pattern("#").unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT)).save(onFinished, new ResourceLocation(SimplyHarder.MOD_ID, "netherite_shovel"));
        ShapedRecipeBuilder.shaped(Items.NETHERITE_SWORD).define('#', Items.STICK).define('X', Items.NETHERITE_INGOT).pattern("X").pattern("X").pattern("#").unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT)).save(onFinished, new ResourceLocation(SimplyHarder.MOD_ID, "netherite_sword"));

        // ADD NETHERITE ARMOR RECIPES
        ShapedRecipeBuilder.shaped(Items.NETHERITE_BOOTS).define('X', Items.NETHERITE_INGOT).pattern("X X").pattern("X X").unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT)).save(onFinished, new ResourceLocation(SimplyHarder.MOD_ID, "netherite_boots"));
        ShapedRecipeBuilder.shaped(Items.NETHERITE_CHESTPLATE).define('X', Items.NETHERITE_INGOT).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT)).save(onFinished, new ResourceLocation(SimplyHarder.MOD_ID, "netherite_chestplate"));
        ShapedRecipeBuilder.shaped(Items.NETHERITE_HELMET).define('X', Items.NETHERITE_INGOT).pattern("XXX").pattern("X X").unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT)).save(onFinished, new ResourceLocation(SimplyHarder.MOD_ID, "netherite_helmet"));
        ShapedRecipeBuilder.shaped(Items.NETHERITE_LEGGINGS).define('X', Items.NETHERITE_INGOT).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT)).save(onFinished, new ResourceLocation(SimplyHarder.MOD_ID, "netherite_leggings"));


        // REPLACE NETHERITE INGOT VANILLA RECIPE
        ShapelessRecipeBuilder.shapeless(Items.NETHERITE_INGOT).requires(Items.NETHERITE_SCRAP, 1).requires(Items.GOLD_INGOT, 1).group("netherite_ingot").unlockedBy("has_netherite_scrap", has(Items.NETHERITE_SCRAP)).save(onFinished);

        // DISABLE NETHERITE ARMOR VANILLA RECIPES
        DisabledRecipeBuilder.disabled(Items.NETHERITE_BOOTS).save(onFinished, new ResourceLocation("netherite_boots_smithing"));
        DisabledRecipeBuilder.disabled(Items.NETHERITE_CHESTPLATE).save(onFinished, new ResourceLocation("netherite_chestplate_smithing"));
        DisabledRecipeBuilder.disabled(Items.NETHERITE_HELMET).save(onFinished, new ResourceLocation("netherite_helmet_smithing"));
        DisabledRecipeBuilder.disabled(Items.NETHERITE_LEGGINGS).save(onFinished, new ResourceLocation("netherite_leggings_smithing"));

        // DISABLE NETHERITE TOOL VANILLA RECIPES
        DisabledRecipeBuilder.disabled(Items.NETHERITE_AXE).save(onFinished, new ResourceLocation("netherite_axe_smithing"));
        DisabledRecipeBuilder.disabled(Items.NETHERITE_HOE).save(onFinished, new ResourceLocation("netherite_hoe_smithing"));
        DisabledRecipeBuilder.disabled(Items.NETHERITE_PICKAXE).save(onFinished, new ResourceLocation("netherite_pickaxe_smithing"));
        DisabledRecipeBuilder.disabled(Items.NETHERITE_SHOVEL).save(onFinished, new ResourceLocation("netherite_shovel_smithing"));
        DisabledRecipeBuilder.disabled(Items.NETHERITE_SWORD).save(onFinished, new ResourceLocation("netherite_sword_smithing"));
    }

    private ExperienceChangeTrigger.TriggerInstance hasExperienceLevel(int experienceLevel) {
        return ExperienceChangeTrigger.TriggerInstance.hasLevel(experienceLevel);
    }

}
