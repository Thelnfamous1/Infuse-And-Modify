package com.infamous.simply_harder.datagen.provider;

import com.google.common.base.Equivalence;
import com.infamous.simply_harder.SimplyHarder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
    public static final Tags.IOptionalNamedTag<Item> INFUSIONS = buildProprietaryTag("infusions");

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

        this.infusions();
    }

    private void infusions() {
        this.tag(INFUSIONS).addTag(ARMORS).addTag(WEAPONS);
    }

    private void projectileWeapons() {
        this.tag(PROJECTILE_WEAPONS).addTag(BOWS).addTag(CROSSBOWS);
    }

    private void crossbows() {
        this.tag(CROSSBOWS).add(Items.CROSSBOW);
    }

    private void bows() {
        this.tag(BOWS).add(Items.BOW);
    }

    private void armors() {
        this.tag(ARMORS).addTag(BOOTS).addTag(CHESTPLATES).addTag(HELMETS).addTag(LEGGINGS);
    }

    private void tools(){
        this.tag(TOOLS).addTag(AXES).addTag(HOES).addTag(PICKAXES).addTag(SHOVELS);
    }

    private void weapons() {
        this.tag(WEAPONS).addTag(SWORDS).addTag(TRIDENTS).addTag(TOOLS);
    }

    private void shields(){
        this.tag(SHIELDS).add(Items.SHIELD);
    }

    private void tridents(){
        this.tag(TRIDENTS).add(Items.TRIDENT);
    }

    private void axes() {
        this.tag(AXES).add(Items.WOODEN_AXE)
                .add(Items.GOLDEN_AXE)
                .add(Items.STONE_AXE)
                .add(Items.IRON_AXE)
                .add(Items.DIAMOND_AXE)
                .add(Items.NETHERITE_AXE);
    }

    private void hoes() {
        this.tag(HOES)
                .add(Items.WOODEN_HOE)
                .add(Items.GOLDEN_HOE)
                .add(Items.STONE_HOE)
                .add(Items.IRON_HOE)
                .add(Items.DIAMOND_HOE)
                .add(Items.NETHERITE_HOE);
    }

    private void pickaxes() {
        this.tag(PICKAXES)
                .add(Items.WOODEN_PICKAXE)
                .add(Items.GOLDEN_PICKAXE)
                .add(Items.STONE_PICKAXE)
                .add(Items.IRON_PICKAXE)
                .add(Items.DIAMOND_PICKAXE)
                .add(Items.NETHERITE_PICKAXE);
    }

    private void swords() {
        this.tag(SWORDS)
                .add(Items.WOODEN_SWORD)
                .add(Items.GOLDEN_SWORD)
                .add(Items.STONE_SWORD)
                .add(Items.IRON_SWORD)
                .add(Items.DIAMOND_SWORD)
                .add(Items.NETHERITE_SWORD);
    }

    private void shovels() {
        this.tag(SHOVELS)
                .add(Items.WOODEN_SHOVEL)
                .add(Items.GOLDEN_SHOVEL)
                .add(Items.STONE_SHOVEL)
                .add(Items.IRON_SHOVEL)
                .add(Items.DIAMOND_SHOVEL)
                .add(Items.NETHERITE_SHOVEL);
    }

    private void helmets() {
        this.tag(HELMETS)
                .add(Items.LEATHER_HELMET)
                .add(Items.CHAINMAIL_HELMET)
                .add(Items.IRON_HELMET)
                .add(Items.GOLDEN_HELMET)
                .add(Items.DIAMOND_HELMET)
                .add(Items.TURTLE_HELMET)
                .add(Items.NETHERITE_HELMET);
    }

    private void boots() {
        this.tag(BOOTS)
                .add(Items.LEATHER_BOOTS)
                .add(Items.CHAINMAIL_BOOTS)
                .add(Items.IRON_BOOTS)
                .add(Items.GOLDEN_BOOTS)
                .add(Items.DIAMOND_BOOTS)
                .add(Items.NETHERITE_BOOTS);
    }

    private void chestplates() {
        this.tag(CHESTPLATES)
                .add(Items.LEATHER_CHESTPLATE)
                .add(Items.CHAINMAIL_CHESTPLATE)
                .add(Items.IRON_CHESTPLATE)
                .add(Items.GOLDEN_CHESTPLATE)
                .add(Items.DIAMOND_CHESTPLATE)
                .add(Items.NETHERITE_CHESTPLATE);
    }

    private void leggings() {
        this.tag(LEGGINGS)
                .add(Items.LEATHER_LEGGINGS)
                .add(Items.CHAINMAIL_LEGGINGS)
                .add(Items.IRON_LEGGINGS)
                .add(Items.GOLDEN_LEGGINGS)
                .add(Items.DIAMOND_LEGGINGS)
                .add(Items.NETHERITE_LEGGINGS);
    }
}