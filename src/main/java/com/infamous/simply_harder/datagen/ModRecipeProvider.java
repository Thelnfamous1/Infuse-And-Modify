package com.infamous.simply_harder.datagen;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.custom.item.UpgradeModuleItem;
import com.infamous.simply_harder.custom.recipe.InfuseUpgradeModuleRecipe;
import com.infamous.simply_harder.custom.recipe.ModificationRecipe;
import com.infamous.simply_harder.datagen.builder.DisabledRecipeBuilder;
import com.infamous.simply_harder.datagen.builder.InfusionRecipeBuilder;
import com.infamous.simply_harder.registry.SHItems;
import com.infamous.simply_harder.registry.SHRecipes;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
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

        // ADD IRON -> DIAMOND INFUSION RECIPES
        buildSimpleInfusionRecipe(onFinished, Items.IRON_AXE, Items.DIAMOND_AXE);
        buildSimpleInfusionRecipe(onFinished, Items.IRON_HOE, Items.DIAMOND_HOE);
        buildSimpleInfusionRecipe(onFinished, Items.IRON_PICKAXE, Items.DIAMOND_PICKAXE);
        buildSimpleInfusionRecipe(onFinished, Items.IRON_SHOVEL, Items.DIAMOND_SHOVEL);
        buildSimpleInfusionRecipe(onFinished, Items.IRON_SWORD, Items.DIAMOND_SWORD);

        buildSimpleInfusionRecipe(onFinished, Items.IRON_BOOTS, Items.DIAMOND_BOOTS);
        buildSimpleInfusionRecipe(onFinished, Items.IRON_CHESTPLATE, Items.DIAMOND_CHESTPLATE);
        buildSimpleInfusionRecipe(onFinished, Items.IRON_HELMET, Items.DIAMOND_HELMET);
        buildSimpleInfusionRecipe(onFinished, Items.IRON_LEGGINGS, Items.DIAMOND_LEGGINGS);

        // ADD DIAMOND -> NETHERITE INFUSION RECIPES
        buildSimpleInfusionRecipe(onFinished, Items.DIAMOND_AXE, Items.NETHERITE_AXE);
        buildSimpleInfusionRecipe(onFinished, Items.DIAMOND_HOE, Items.NETHERITE_HOE);
        buildSimpleInfusionRecipe(onFinished, Items.DIAMOND_PICKAXE, Items.NETHERITE_PICKAXE);
        buildSimpleInfusionRecipe(onFinished, Items.DIAMOND_SHOVEL, Items.NETHERITE_SHOVEL);
        buildSimpleInfusionRecipe(onFinished, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD);

        buildSimpleInfusionRecipe(onFinished, Items.DIAMOND_BOOTS, Items.NETHERITE_BOOTS);
        buildSimpleInfusionRecipe(onFinished, Items.DIAMOND_CHESTPLATE, Items.NETHERITE_CHESTPLATE);
        buildSimpleInfusionRecipe(onFinished, Items.DIAMOND_HELMET, Items.NETHERITE_HELMET);
        buildSimpleInfusionRecipe(onFinished, Items.DIAMOND_LEGGINGS, Items.NETHERITE_LEGGINGS);

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

    private void buildSimpleInfusionRecipe(Consumer<FinishedRecipe> onFinished, Item base, Item addition) {
        InfusionRecipeBuilder.infusion(base, addition).unlocks("has_upgrade_module", hasInfusedUpgradeModule(addition.getDefaultInstance())).save(onFinished, base, addition);
    }

    private static ItemStack buildUpgradeModule(ItemStack stack){
        ItemStack upgradeModule = SHItems.UPGRADE_MODULE.get().getDefaultInstance();
        UpgradeModuleItem.setInfusedItem(upgradeModule, stack);
        return upgradeModule;
    }

    private static InventoryChangeTrigger.TriggerInstance hasInfusedUpgradeModule(ItemStack stack){
        ItemStack upgradeModule = buildUpgradeModule(stack);
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
