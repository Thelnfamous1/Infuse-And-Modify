package com.infamous.simply_harder.custom.recipe;

import com.google.common.collect.Multimap;
import com.infamous.simply_harder.custom.item.ModifierCoreItem;
import com.infamous.simply_harder.registry.SHRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.Map;


public class ModifyUsingItemRecipe extends ModificationRecipe{
    public static final String NAME = "modify_using_item";

    public ModifyUsingItemRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public int calculateLevelCost(Container container) {
        ItemStack left = getLeft(container);
        ItemStack right = getRight(container);
        int cost = 0;
        for(EquipmentSlot slot : EquipmentSlot.values()){
            Multimap<Attribute, AttributeModifier> modifiers = right.getAttributeModifiers(slot);
            for(Map.Entry<Attribute, AttributeModifier> ignored : modifiers.entries()){
                cost++;
            }
        }
        return cost;
    }

    @Override
    public boolean matches(Container container, Level level) {
        ItemStack left = getLeft(container);
        ItemStack right = getRight(container);
        return ModifierCoreItem.isEmptyCore(left) && !right.isEmpty();
    }

    @Override
    public ItemStack assemble(Container container) {
        ItemStack left = getLeft(container);
        ItemStack right = getRight(container);
        ItemStack result = left.copy();
        ModifierCoreItem.addModifiersToCore(right, result);
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SHRecipes.MODIFY_USING_ITEM.get();
    }
}
