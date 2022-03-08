package com.infamous.simply_harder.custom.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.infamous.simply_harder.custom.WrappedMasterworkTier;
import com.infamous.simply_harder.custom.container.SimpleAnvilContainer;
import com.infamous.simply_harder.custom.item.EnhancementCoreItem;
import com.infamous.simply_harder.registry.SHRecipes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.LinkedHashMap;
import java.util.Map;

public class MasterworkingRecipe extends ForgingRecipe {

    public static final String NAME = "masterworking";
    private final Ingredient base;
    private final Map<Integer, WrappedMasterworkTier> tiers;

    public MasterworkingRecipe(ResourceLocation id, Ingredient base, Map<Integer, WrappedMasterworkTier> tiers){
        super(id);
        this.base = base;
        this.tiers = tiers;
    }

    @Override
    public boolean matches(SimpleAnvilContainer container, Level level) {
        ItemStack left = ForgingRecipe.getLeft(container);
        ItemStack right = ForgingRecipe.getRight(container);
        return this.base.test(left) && this.inTierRange(EnhancementCoreItem.getTierCheckTag(left))
                && EnhancementCoreItem.isEnhancementCore(right);
    }

    private boolean inTierRange(int tier) {
        return Math.max(0, tier) == Math.min(tier, this.tiers.size());
    }

    @Override
    public ItemStack assemble(SimpleAnvilContainer container) {
        ItemStack left = ForgingRecipe.getLeft(container);
        ItemStack result = left.copy();
        int newTier = EnhancementCoreItem.getTierCheckTag(left) + 1;
        EnhancementCoreItem.setTier(result, newTier);
        EnhancementCoreItem.clearModifiers(result);
        EnhancementCoreItem.addModifiers(result, this.getTier(newTier).modifierMap());
        return result;
    }

    @Override
    public int calculateLevelCost(SimpleAnvilContainer container) {
        ItemStack left = ForgingRecipe.getLeft(container);
        return this.getTier(EnhancementCoreItem.getTierCheckTag(left) + 1).levelCost();
    }

    @Override
    public int calculateMaterialCost(SimpleAnvilContainer container) {
        ItemStack left = ForgingRecipe.getLeft(container);
        return this.getTier(EnhancementCoreItem.getTierCheckTag(left) + 1).materialCost();
    }

    protected WrappedMasterworkTier getTier(int tier) {
        return this.tiers.get(tier);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SHRecipes.MASTERWORKING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.MASTERWORKING.get();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<MasterworkingRecipe> {

        public static final String BASE = "base";
        public static final String TIERS = "tiers";

        @Override
        public MasterworkingRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
            Ingredient base = Ingredient.fromJson(jsonObject.get(BASE));

            JsonArray tiersArr = GsonHelper.getAsJsonArray(jsonObject, TIERS);
            int size = tiersArr.size();
            Map<Integer, WrappedMasterworkTier> tiers = new LinkedHashMap<>(size);
            for(int i = 0; i < size; i++){
                int currentTier = i + 1;
                tiers.put(currentTier, WrappedMasterworkTier.fromJson(tiersArr.get(i)));
            }

            return new MasterworkingRecipe(id, base, tiers);
        }

        @Override
        public MasterworkingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf byteBuf) {
            Ingredient base = Ingredient.fromNetwork(byteBuf);

            int size = byteBuf.readVarInt();
            Map<Integer, WrappedMasterworkTier> tiers = new LinkedHashMap<>(size);
            for(int i = 0; i < size; i++){
                int currentTier = i + 1;
                tiers.put(currentTier, WrappedMasterworkTier.fromNetwork(byteBuf));
            }

            return new MasterworkingRecipe(id, base, tiers);
        }

        @Override
        public void toNetwork(FriendlyByteBuf byteBuf, MasterworkingRecipe recipe) {
            recipe.base.toNetwork(byteBuf);

            int size = recipe.tiers.size();
            byteBuf.writeVarInt(size);
            for(int i = 0; i < size; i++){
                int currentTier = i + 1;
                recipe.tiers.get(currentTier).toNetwork(byteBuf);
            }
        }
    }
}
