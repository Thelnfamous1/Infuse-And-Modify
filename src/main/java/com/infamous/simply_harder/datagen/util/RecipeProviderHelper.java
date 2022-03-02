package com.infamous.simply_harder.datagen.util;

import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public class RecipeProviderHelper {

    public static InventoryChangeTrigger.TriggerInstance has(ItemLike itemLike) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(itemLike).build());
    }

    public static InventoryChangeTrigger.TriggerInstance hasWithNbt(ItemLike itemLike, CompoundTag nbt) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(itemLike).hasNbt(nbt).build());
    }

    public static InventoryChangeTrigger.TriggerInstance has(Tag<Item> itemTag) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(itemTag).build());
    }

    public static InventoryChangeTrigger.TriggerInstance hasWithNbt(Tag<Item> itemTag, CompoundTag nbt) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(itemTag).hasNbt(nbt).build());
    }

    public static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... itemPredicates) {
        return new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, itemPredicates);
    }
}
