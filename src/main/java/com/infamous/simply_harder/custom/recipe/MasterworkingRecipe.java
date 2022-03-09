package com.infamous.simply_harder.custom.recipe;

import com.google.gson.JsonObject;
import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.custom.container.SimpleAnvilContainer;
import com.infamous.simply_harder.custom.data.MasterworkProgression;
import com.infamous.simply_harder.custom.data.MasterworkTier;
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

public class MasterworkingRecipe extends ForgingRecipe {

    public static final String NAME = "masterworking";
    private final Ingredient masterworkable;
    private final ResourceLocation progressionId;

    public MasterworkingRecipe(ResourceLocation id, Ingredient masterworkable, ResourceLocation progressionId){
        super(id);
        this.masterworkable = masterworkable;
        this.progressionId = progressionId;
    }

    private MasterworkProgression getProgression(){
        return SimplyHarder.MASTERWORK_PROGRESSION_MANAGER.getProgression(this.progressionId).orElse(MasterworkProgression.NONE);
    }

    @Override
    protected boolean simpleMatches(SimpleAnvilContainer container, Level level) {
        ItemStack left = container.getLeft();
        ItemStack right = container.getRight();
        return this.canMasterwork(left)
                && EnhancementCoreItem.isCore(right);
    }

    private boolean canMasterwork(ItemStack stack) {
        MasterworkProgression progression = this.getProgression();
        if(progression.isNone()){
            return false;
        }

        return this.masterworkable.test(stack)
                && this.canProgress(progression, stack)
                && progression.inTierRange(EnhancementCoreItem.getTierCheckTag(stack));
    }

    private boolean canProgress(MasterworkProgression progression, ItemStack stack) {
        MasterworkProgression storedProgression = EnhancementCoreItem.getProgressionCheckTag(stack);
        return storedProgression.isNone() || progression.matches(storedProgression);
    }

    @Override
    public ItemStack assemble(SimpleAnvilContainer container) {
        ItemStack left = container.getLeft();
        ItemStack result = left.copy();
        int nextTier = this.getNextTier(left);
        EnhancementCoreItem.setProgression(result, this.progressionId);
        EnhancementCoreItem.setTier(result, nextTier);
        return result;
    }

    protected int getNextTier(ItemStack left) {
        return EnhancementCoreItem.getTierCheckTag(left) + 1;
    }

    @Override
    protected int calculateLevelCost(SimpleAnvilContainer container) {
        ItemStack left = container.getLeft();
        return this.getNextTierInProgression(left).levelCost();
    }

    @Override
    protected int calculateMaterialCost(SimpleAnvilContainer container) {
        ItemStack left = container.getLeft();
        return this.getNextTierInProgression(left).materialCost();
    }

    protected MasterworkTier getNextTierInProgression(ItemStack stack) {
        int nextTier = this.getNextTier(stack);
        return this.getProgression().getTier(nextTier).orElse(MasterworkTier.EMPTY);
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

        public static final String MASTERWORKABLE = "masterworkable";
        public static final String PROGRESSION = "progression";

        @Override
        public MasterworkingRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
            Ingredient masterworkable = Ingredient.fromJson(jsonObject.get(MASTERWORKABLE));

            ResourceLocation progressionId = new ResourceLocation(GsonHelper.getAsString(jsonObject, PROGRESSION));

            return new MasterworkingRecipe(id, masterworkable, progressionId);
        }

        @Override
        public MasterworkingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf byteBuf) {
            Ingredient masterworkable = Ingredient.fromNetwork(byteBuf);
            ResourceLocation progressionId = byteBuf.readResourceLocation();

            return new MasterworkingRecipe(id, masterworkable, progressionId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf byteBuf, MasterworkingRecipe recipe) {
            recipe.masterworkable.toNetwork(byteBuf);
            byteBuf.writeResourceLocation(recipe.progressionId);
        }
    }
}
