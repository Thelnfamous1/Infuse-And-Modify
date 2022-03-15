package com.infamous.simply_harder.custom.recipe;

import com.google.gson.JsonObject;
import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.custom.item.UpgradeModuleItem;
import com.infamous.simply_harder.registry.SHRecipes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class InfusionRecipe extends BaseInfusionRecipe {
    public static final String NAME = "infusion";
    public static final String BASE_NAME = "base";
    public static final String ADDITION_NAME = "infusion";

    public InfusionRecipe(ResourceLocation id, Ingredient base, Ingredient addition) {
        super(id, base, addition);
    }

    @Override
    public boolean isBaseIngredient(ItemStack stack) {
        return this.infusionBase.test(stack) && stack.hasTag();
    }

    @Override
    public boolean isAdditionIngredient(ItemStack stack) {
        return UpgradeModuleItem.hasInfusedItem(stack)
                && this.infusionAddition.test(UpgradeModuleItem.getInfusedItem(stack));
    }

    @Override
    public ItemStack assemble(Container container) {
        ItemStack upgradeBase = BaseInfusionRecipe.getBase(container);
        ItemStack upgradeModule = BaseInfusionRecipe.getAddition(container);

        ItemStack infusedItem = UpgradeModuleItem.getInfusedItem(upgradeModule);
        CompoundTag baseTag = upgradeBase.getTag();
        if (baseTag != null) {
            infusedItem.setTag(baseTag.copy());
        } else{
            SimplyHarder.LOGGER.info("The upgrade base {} does not have any NBT for {} recipe {}", upgradeBase, NAME, this.getId());
        }

        return infusedItem;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SHRecipes.INFUSION.get();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<InfusionRecipe> {

        public InfusionRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
            Ingredient base = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, BASE_NAME));
            Ingredient addition = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, ADDITION_NAME));
            return new InfusionRecipe(id, base, addition);
        }

        public InfusionRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf byteBuf) {
            Ingredient base = Ingredient.fromNetwork(byteBuf);
            Ingredient addition = Ingredient.fromNetwork(byteBuf);
            return new InfusionRecipe(id, base, addition);
        }

        public void toNetwork(FriendlyByteBuf byteBuf, InfusionRecipe recipe) {
            recipe.infusionBase.toNetwork(byteBuf);
            recipe.infusionAddition.toNetwork(byteBuf);
        }
    }
}
