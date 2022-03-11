package com.infamous.simply_harder.datagen;

import com.infamous.simply_harder.SimplyHarder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ModItemTagsProvider extends ItemTagsProvider {

    public static final Tags.IOptionalNamedTag<Item> AXES = buildProprietaryTag("axes");
    public static final Tags.IOptionalNamedTag<Item> HOES = buildProprietaryTag("hoes");
    public static final Tags.IOptionalNamedTag<Item> PICKAXES = buildProprietaryTag("pickaxes");
    public static final Tags.IOptionalNamedTag<Item> SHOVELS = buildProprietaryTag("shovels");
    public static final Tags.IOptionalNamedTag<Item> TOOLS = buildProprietaryTag("tools");

    public static final Tags.IOptionalNamedTag<Item> SWORDS = buildProprietaryTag("swords");
    public static final Tags.IOptionalNamedTag<Item> TRIDENTS = buildProprietaryTag("tridents");
    public static final Tags.IOptionalNamedTag<Item> WEAPONS = buildProprietaryTag("weapons");

    public static final Tags.IOptionalNamedTag<Item> BOWS = buildProprietaryTag("bows");
    public static final Tags.IOptionalNamedTag<Item> CROSSBOWS = buildProprietaryTag("crossbows");
    public static final Tags.IOptionalNamedTag<Item> PROJECTILE_WEAPONS = buildProprietaryTag("projectile_weapons");

    public static final Tags.IOptionalNamedTag<Item> SHIELDS = buildProprietaryTag("shields");

    public static final Tags.IOptionalNamedTag<Item> BOOTS = buildProprietaryTag("boots");
    public static final Tags.IOptionalNamedTag<Item> CHESTPLATES = buildProprietaryTag("chestplates");
    public static final Tags.IOptionalNamedTag<Item> HELMETS = buildProprietaryTag("helmets");
    public static final Tags.IOptionalNamedTag<Item> LEGGINGS = buildProprietaryTag("leggings");
    public static final Tags.IOptionalNamedTag<Item> ARMORS = buildProprietaryTag("armors");

    public ModItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, SimplyHarder.MOD_ID, existingFileHelper);
    }

    private static Tags.IOptionalNamedTag<Item> buildProprietaryTag(String path) {
        return ItemTags.createOptional(new ResourceLocation(SimplyHarder.MOD_ID, path));
    }

    @Override
    protected void addTags() {
        this.axes();
        this.hoes();
        this.pickaxes();
        this.shovels();
        this.tools();

        this.swords();
        this.tridents();
        this.weapons();

        this.bows();
        this.crossbows();
        this.projectileWeapons();

        this.shields();

        this.boots();
        this.chestplates();
        this.helmets();
        this.leggings();
        this.armors();
    }

    private void projectileWeapons() {
        this.tag(PROJECTILE_WEAPONS).addTag(BOWS);
        this.tag(PROJECTILE_WEAPONS).addTag(CROSSBOWS);
    }

    private void crossbows() {
        this.tag(CROSSBOWS).add(Items.CROSSBOW);
    }

    private void bows() {
        this.tag(BOWS).add(Items.BOW);
    }

    private void armors() {
        this.tag(ARMORS).addTag(BOOTS);
        this.tag(ARMORS).addTag(CHESTPLATES);
        this.tag(ARMORS).addTag(HELMETS);
        this.tag(ARMORS).addTag(LEGGINGS);
    }

    private void tools(){
        this.tag(TOOLS).addTag(AXES);
        this.tag(TOOLS).addTag(HOES);
        this.tag(TOOLS).addTag(PICKAXES);
        this.tag(TOOLS).addTag(SHOVELS);
    }

    private void weapons() {
        this.tag(WEAPONS).addTag(SWORDS);
        this.tag(WEAPONS).addTag(TRIDENTS);
        this.tag(WEAPONS).addTag(TOOLS);
    }

    private void shields(){
        this.tag(SHIELDS).add(Items.SHIELD);
    }

    private void tridents(){
        this.tag(TRIDENTS).add(Items.TRIDENT);
    }

    private void axes() {
        this.tag(AXES).add(Items.WOODEN_AXE);
        this.tag(AXES).add(Items.GOLDEN_AXE);
        this.tag(AXES).add(Items.STONE_AXE);
        this.tag(AXES).add(Items.IRON_AXE);
        this.tag(AXES).add(Items.DIAMOND_AXE);
        this.tag(AXES).add(Items.NETHERITE_AXE);
    }

    private void hoes() {
        this.tag(HOES).add(Items.WOODEN_HOE);
        this.tag(HOES).add(Items.GOLDEN_HOE);
        this.tag(HOES).add(Items.STONE_HOE);
        this.tag(HOES).add(Items.IRON_HOE);
        this.tag(HOES).add(Items.DIAMOND_HOE);
        this.tag(HOES).add(Items.NETHERITE_HOE);
    }

    private void pickaxes() {
        this.tag(PICKAXES).add(Items.WOODEN_PICKAXE);
        this.tag(PICKAXES).add(Items.GOLDEN_PICKAXE);
        this.tag(PICKAXES).add(Items.STONE_PICKAXE);
        this.tag(PICKAXES).add(Items.IRON_PICKAXE);
        this.tag(PICKAXES).add(Items.DIAMOND_PICKAXE);
        this.tag(PICKAXES).add(Items.NETHERITE_PICKAXE);
    }

    private void swords() {
        this.tag(SWORDS).add(Items.WOODEN_SWORD);
        this.tag(SWORDS).add(Items.GOLDEN_SWORD);
        this.tag(SWORDS).add(Items.STONE_SWORD);
        this.tag(SWORDS).add(Items.IRON_SWORD);
        this.tag(SWORDS).add(Items.DIAMOND_SWORD);
        this.tag(SWORDS).add(Items.NETHERITE_SWORD);
    }

    private void shovels() {
        this.tag(SHOVELS).add(Items.WOODEN_SHOVEL);
        this.tag(SHOVELS).add(Items.GOLDEN_SHOVEL);
        this.tag(SHOVELS).add(Items.STONE_SHOVEL);
        this.tag(SHOVELS).add(Items.IRON_SHOVEL);
        this.tag(SHOVELS).add(Items.DIAMOND_SHOVEL);
        this.tag(SHOVELS).add(Items.NETHERITE_SHOVEL);
    }

    private void helmets() {
        this.tag(HELMETS).add(Items.LEATHER_HELMET);
        this.tag(HELMETS).add(Items.CHAINMAIL_HELMET);
        this.tag(HELMETS).add(Items.IRON_HELMET);
        this.tag(HELMETS).add(Items.GOLDEN_HELMET);
        this.tag(HELMETS).add(Items.DIAMOND_HELMET);
        this.tag(HELMETS).add(Items.TURTLE_HELMET); // special case
        this.tag(HELMETS).add(Items.NETHERITE_HELMET);
    }

    private void boots() {
        this.tag(BOOTS).add(Items.LEATHER_BOOTS);
        this.tag(BOOTS).add(Items.CHAINMAIL_BOOTS);
        this.tag(BOOTS).add(Items.IRON_BOOTS);
        this.tag(BOOTS).add(Items.GOLDEN_BOOTS);
        this.tag(BOOTS).add(Items.DIAMOND_BOOTS);
        this.tag(BOOTS).add(Items.NETHERITE_BOOTS);
    }

    private void chestplates() {
        this.tag(CHESTPLATES).add(Items.LEATHER_CHESTPLATE);
        this.tag(CHESTPLATES).add(Items.CHAINMAIL_CHESTPLATE);
        this.tag(CHESTPLATES).add(Items.IRON_CHESTPLATE);
        this.tag(CHESTPLATES).add(Items.GOLDEN_CHESTPLATE);
        this.tag(CHESTPLATES).add(Items.DIAMOND_CHESTPLATE);
        this.tag(CHESTPLATES).add(Items.NETHERITE_CHESTPLATE);
    }

    private void leggings() {
        this.tag(LEGGINGS).add(Items.LEATHER_LEGGINGS);
        this.tag(LEGGINGS).add(Items.CHAINMAIL_LEGGINGS);
        this.tag(LEGGINGS).add(Items.IRON_LEGGINGS);
        this.tag(LEGGINGS).add(Items.GOLDEN_LEGGINGS);
        this.tag(LEGGINGS).add(Items.DIAMOND_LEGGINGS);
        this.tag(LEGGINGS).add(Items.NETHERITE_LEGGINGS);
    }
}