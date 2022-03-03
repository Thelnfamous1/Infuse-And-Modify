package com.infamous.simply_harder.custom.recipe;

import com.infamous.simply_harder.custom.item.InfusionCatalystItem;
import com.infamous.simply_harder.registry.SHRecipes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;

import java.util.Optional;
import java.util.Set;

public class InfuseIntoItemRecipe extends InfusionRecipe{
    public InfuseIntoItemRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean isAdditionIngredient(ItemStack stack) {
        return InfusionCatalystItem.isActiveCatalyst(stack);
    }

    @Override
    public boolean matches(Container container, Level level) {
        ItemStack base = InfusionRecipe.getBase(container);
        ItemStack addition = InfusionRecipe.getAddition(container);
        if(InfusionCatalystItem.isActiveCatalyst(addition)){
            Set<ToolAction> toolActions = InfusionCatalystItem.getRequiredToolActions(addition);
            for(ToolAction toolAction : toolActions){
                if(!base.canPerformAction(toolAction)) return false;
            }
            Optional<EquipmentSlot> requiredEquipmentSlot = InfusionCatalystItem.getRequiredEquipmentSlot(addition);
            return requiredEquipmentSlot.map(es -> LivingEntity.getEquipmentSlotForItem(base) == es)
                    .orElse(true);
        }
        return false;
    }

    @Override
    public ItemStack assemble(Container container) {
        ItemStack base = InfusionRecipe.getBase(container);
        ItemStack infusionCore = InfusionRecipe.getAddition(container);

        ItemStack infusedItem = InfusionCatalystItem.getInfusedItem(infusionCore).copy();
        CompoundTag baseTag = base.getTag();
        if (baseTag != null) {
            infusedItem.setTag(baseTag.copy());
        }

        return infusedItem;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SHRecipes.INFUSE_INTO_ITEM.get();
    }
}
