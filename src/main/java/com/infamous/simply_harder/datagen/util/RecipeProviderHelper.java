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

    protected static InventoryChangeTrigger.TriggerInstance hasWithNbt(ItemLike itemLike, CompoundTag nbt) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(itemLike).hasNbt(nbt).build());
    }

    protected static InventoryChangeTrigger.TriggerInstance hasWithNbt(Tag<Item> itemTag, CompoundTag nbt) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(itemTag).hasNbt(nbt).build());
    }

    protected static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... itemPredicates) {
        return new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, itemPredicates);
    }
}
