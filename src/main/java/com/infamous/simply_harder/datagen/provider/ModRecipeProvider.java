package com.infamous.simply_harder.datagen.provider;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.custom.item.UpgradeModuleItem;
import com.infamous.simply_harder.custom.recipe.InfuseUpgradeModuleRecipe;
import com.infamous.simply_harder.custom.recipe.ModificationRecipe;
import com.infamous.simply_harder.datagen.builder.DisabledRecipeBuilder;
import com.infamous.simply_harder.datagen.builder.InfusionRecipeBuilder;
import com.infamous.simply_harder.datagen.builder.MasterworkingRecipeBuilder;
import com.infamous.simply_harder.datagen.provider.ModItemTagsProvider;
import com.infamous.simply_harder.registry.SHItems;
import com.infamous.simply_harder.registry.SHRecipes;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> onFinished) {
        SpecialRecipeBuilder.special(SHRecipes.INFUSE_UPGRADE_MODULE.get()).save(onFinished, new ResourceLocation(SimplyHarder.MOD_ID, InfuseUpgradeModuleRecipe.NAME).toString());
        SpecialRecipeBuilder.special(SHRecipes.MODIFICATION.get()).save(onFinished, new ResourceLocation(SimplyHarder.MOD_ID, ModificationRecipe.NAME).toString());

        this.buildSimpleMasterworkingRecipe(onFinished, ModItemTagsProvider.WEAPONS, new ResourceLocation(SimplyHarder.MOD_ID, "weapon"), CreativeModeTab.TAB_COMBAT);
        this.buildSimpleMasterworkingRecipe(onFinished, ModItemTagsProvider.BOOTS, new ResourceLocation(SimplyHarder.MOD_ID, "boots"), CreativeModeTab.TAB_COMBAT);
        this.buildSimpleMasterworkingRecipe(onFinished, ModItemTagsProvider.CHESTPLATES, new ResourceLocation(SimplyHarder.MOD_ID, "chestplate"), CreativeModeTab.TAB_COMBAT);
        this.buildSimpleMasterworkingRecipe(onFinished, ModItemTagsProvider.HELMETS, new ResourceLocation(SimplyHarder.MOD_ID, "helmet"), CreativeModeTab.TAB_COMBAT);
        this.buildSimpleMasterworkingRecipe(onFinished, ModItemTagsProvider.LEGGINGS, new ResourceLocation(SimplyHarder.MOD_ID, "leggings"), CreativeModeTab.TAB_COMBAT);

        this.buildInfusionRecipes(onFinished);

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

    private void buildInfusionRecipes(Consumer<FinishedRecipe> onFinished) {
        // ADD WOOD TOOL -> STONE TOOL INFUSION RECIPES
        this.buildSimpleInfusionRecipe(onFinished, Items.WOODEN_AXE, Items.STONE_AXE, CreativeModeTab.TAB_TOOLS);
        this.buildSimpleInfusionRecipe(onFinished, Items.WOODEN_HOE, Items.STONE_HOE, CreativeModeTab.TAB_TOOLS);
        this.buildSimpleInfusionRecipe(onFinished, Items.WOODEN_PICKAXE, Items.STONE_PICKAXE, CreativeModeTab.TAB_TOOLS);
        this.buildSimpleInfusionRecipe(onFinished, Items.WOODEN_SHOVEL, Items.STONE_SHOVEL, CreativeModeTab.TAB_TOOLS);
        this.buildSimpleInfusionRecipe(onFinished, Items.WOODEN_SWORD, Items.STONE_SWORD, CreativeModeTab.TAB_COMBAT);

        // ADD LEATHER ARMOR -> CHAINMAIL ARMOR INFUSION RECIPES
        this.buildSimpleInfusionRecipe(onFinished, Items.LEATHER_BOOTS, Items.CHAINMAIL_BOOTS, CreativeModeTab.TAB_COMBAT);
        this.buildSimpleInfusionRecipe(onFinished, Items.LEATHER_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, CreativeModeTab.TAB_COMBAT);
        this.buildSimpleInfusionRecipe(onFinished, Items.LEATHER_HELMET, Items.CHAINMAIL_HELMET, CreativeModeTab.TAB_COMBAT);
        this.buildSimpleInfusionRecipe(onFinished, Items.LEATHER_LEGGINGS, Items.CHAINMAIL_LEGGINGS, CreativeModeTab.TAB_COMBAT);

        // ADD GOLD TOOL -> STONE TOOL INFUSION RECIPES
        this.buildSimpleInfusionRecipe(onFinished, Items.GOLDEN_AXE, Items.STONE_AXE, CreativeModeTab.TAB_TOOLS);
        this.buildSimpleInfusionRecipe(onFinished, Items.GOLDEN_HOE, Items.STONE_HOE, CreativeModeTab.TAB_TOOLS);
        this.buildSimpleInfusionRecipe(onFinished, Items.GOLDEN_PICKAXE, Items.STONE_PICKAXE, CreativeModeTab.TAB_TOOLS);
        this.buildSimpleInfusionRecipe(onFinished, Items.GOLDEN_SHOVEL, Items.STONE_SHOVEL, CreativeModeTab.TAB_TOOLS);
        this.buildSimpleInfusionRecipe(onFinished, Items.GOLDEN_SWORD, Items.STONE_SWORD, CreativeModeTab.TAB_COMBAT);

        // ADD GOLD ARMOR -> CHAINMAIL ARMOR INFUSION RECIPES
        this.buildSimpleInfusionRecipe(onFinished, Items.GOLDEN_BOOTS, Items.CHAINMAIL_BOOTS, CreativeModeTab.TAB_COMBAT);
        this.buildSimpleInfusionRecipe(onFinished, Items.GOLDEN_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, CreativeModeTab.TAB_COMBAT);
        this.buildSimpleInfusionRecipe(onFinished, Items.GOLDEN_HELMET, Items.CHAINMAIL_HELMET, CreativeModeTab.TAB_COMBAT);
        this.buildSimpleInfusionRecipe(onFinished, Items.GOLDEN_LEGGINGS, Items.CHAINMAIL_LEGGINGS, CreativeModeTab.TAB_COMBAT);

        // ADD STONE TOOL -> IRON TOOL INFUSION RECIPES
        this.buildSimpleInfusionRecipe(onFinished, Items.STONE_AXE, Items.IRON_AXE, CreativeModeTab.TAB_TOOLS);
        this.buildSimpleInfusionRecipe(onFinished, Items.STONE_HOE, Items.IRON_HOE, CreativeModeTab.TAB_TOOLS);
        this.buildSimpleInfusionRecipe(onFinished, Items.STONE_PICKAXE, Items.IRON_PICKAXE, CreativeModeTab.TAB_TOOLS);
        this.buildSimpleInfusionRecipe(onFinished, Items.STONE_SHOVEL, Items.IRON_SHOVEL, CreativeModeTab.TAB_TOOLS);
        this.buildSimpleInfusionRecipe(onFinished, Items.STONE_SWORD, Items.IRON_SWORD, CreativeModeTab.TAB_COMBAT);

        // ADD CHAINMAIL ARMOR -> IRON ARMOR INFUSION RECIPES
        this.buildSimpleInfusionRecipe(onFinished, Items.CHAINMAIL_BOOTS, Items.IRON_BOOTS, CreativeModeTab.TAB_COMBAT);
        this.buildSimpleInfusionRecipe(onFinished, Items.CHAINMAIL_CHESTPLATE, Items.IRON_CHESTPLATE, CreativeModeTab.TAB_COMBAT);
        this.buildSimpleInfusionRecipe(onFinished, Items.CHAINMAIL_HELMET, Items.IRON_HELMET, CreativeModeTab.TAB_COMBAT);
        this.buildSimpleInfusionRecipe(onFinished, Items.CHAINMAIL_LEGGINGS, Items.IRON_LEGGINGS, CreativeModeTab.TAB_COMBAT);

        // ADD IRON -> DIAMOND INFUSION RECIPES
        this.buildSimpleInfusionRecipe(onFinished, Items.IRON_AXE, Items.DIAMOND_AXE, CreativeModeTab.TAB_TOOLS);
        this.buildSimpleInfusionRecipe(onFinished, Items.IRON_HOE, Items.DIAMOND_HOE, CreativeModeTab.TAB_TOOLS);
        this.buildSimpleInfusionRecipe(onFinished, Items.IRON_PICKAXE, Items.DIAMOND_PICKAXE, CreativeModeTab.TAB_TOOLS);
        this.buildSimpleInfusionRecipe(onFinished, Items.IRON_SHOVEL, Items.DIAMOND_SHOVEL, CreativeModeTab.TAB_TOOLS);
        this.buildSimpleInfusionRecipe(onFinished, Items.IRON_SWORD, Items.DIAMOND_SWORD, CreativeModeTab.TAB_COMBAT);

        this.buildSimpleInfusionRecipe(onFinished, Items.IRON_BOOTS, Items.DIAMOND_BOOTS, CreativeModeTab.TAB_COMBAT);
        this.buildSimpleInfusionRecipe(onFinished, Items.IRON_CHESTPLATE, Items.DIAMOND_CHESTPLATE, CreativeModeTab.TAB_COMBAT);
        this.buildSimpleInfusionRecipe(onFinished, Items.IRON_HELMET, Items.DIAMOND_HELMET, CreativeModeTab.TAB_COMBAT);
        this.buildSimpleInfusionRecipe(onFinished, Items.IRON_LEGGINGS, Items.DIAMOND_LEGGINGS, CreativeModeTab.TAB_COMBAT);

        // ADD TURTLE -> DIAMOND INFUSION RECIPE
        this.buildSimpleInfusionRecipe(onFinished, Items.TURTLE_HELMET, Items.DIAMOND_HELMET, CreativeModeTab.TAB_COMBAT);

        // ADD DIAMOND -> NETHERITE INFUSION RECIPES
        this.buildSimpleInfusionRecipe(onFinished, Items.DIAMOND_AXE, Items.NETHERITE_AXE, CreativeModeTab.TAB_TOOLS);
        this.buildSimpleInfusionRecipe(onFinished, Items.DIAMOND_HOE, Items.NETHERITE_HOE, CreativeModeTab.TAB_TOOLS);
        this.buildSimpleInfusionRecipe(onFinished, Items.DIAMOND_PICKAXE, Items.NETHERITE_PICKAXE, CreativeModeTab.TAB_TOOLS);
        this.buildSimpleInfusionRecipe(onFinished, Items.DIAMOND_SHOVEL, Items.NETHERITE_SHOVEL, CreativeModeTab.TAB_TOOLS);
        this.buildSimpleInfusionRecipe(onFinished, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD, CreativeModeTab.TAB_COMBAT);

        this.buildSimpleInfusionRecipe(onFinished, Items.DIAMOND_BOOTS, Items.NETHERITE_BOOTS, CreativeModeTab.TAB_COMBAT);
        this.buildSimpleInfusionRecipe(onFinished, Items.DIAMOND_CHESTPLATE, Items.NETHERITE_CHESTPLATE, CreativeModeTab.TAB_COMBAT);
        this.buildSimpleInfusionRecipe(onFinished, Items.DIAMOND_HELMET, Items.NETHERITE_HELMET, CreativeModeTab.TAB_COMBAT);
        this.buildSimpleInfusionRecipe(onFinished, Items.DIAMOND_LEGGINGS, Items.NETHERITE_LEGGINGS, CreativeModeTab.TAB_COMBAT);
    }

    private void buildSimpleInfusionRecipe(Consumer<FinishedRecipe> onFinished, Item base, Item addition, CreativeModeTab creativeTab) {
        InfusionRecipeBuilder.infusion(base, addition).unlocks("has_upgrade_module", hasInfusedUpgradeModule(addition.getDefaultInstance())).save(onFinished, base, addition, creativeTab);
    }

    private void buildSimpleMasterworkingRecipe(Consumer<FinishedRecipe> onFinished, Tag<Item> masterworkable, ResourceLocation progressionId, CreativeModeTab creativeTab) {
        MasterworkingRecipeBuilder.masterworking(masterworkable, progressionId).unlocks("has_enhancement_core", has(SHItems.ENHANCEMENT_CORE.get())).save(onFinished, progressionId, creativeTab);
    }

    private static InventoryChangeTrigger.TriggerInstance hasInfusedUpgradeModule(ItemStack stack){
        ItemStack upgradeModule = UpgradeModuleItem.createInfusedUpgradeModule(stack);
        return hasItemWithNbt(upgradeModule);
    }

    private static InventoryChangeTrigger.TriggerInstance hasItemWithNbt(ItemStack stack) {
        ItemPredicate.Builder builder = ItemPredicate.Builder.item().of(stack.getItem());
        if(stack.hasTag()){
            builder.hasNbt(stack.getTag());
        }
        return inventoryTrigger(builder.build());
    }

}
