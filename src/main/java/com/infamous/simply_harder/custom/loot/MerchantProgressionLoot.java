package com.infamous.simply_harder.custom.loot;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.infamous.simply_harder.registry.SHItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class MerchantProgressionLoot implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {
   public void accept(BiConsumer<ResourceLocation, LootTable.Builder> lootTableBuilder) {
       buildLootFor(lootTableBuilder, SHBuiltInLootTables.ARMORER_PROGRESSION_REWARDS);
       buildLootFor(lootTableBuilder, SHBuiltInLootTables.LEATHERWORKER_PROGRESSION_REWARDS);
       buildLootFor(lootTableBuilder, SHBuiltInLootTables.TOOLSMITH_PROGRESSION_REWARDS);
       buildLootFor(lootTableBuilder, SHBuiltInLootTables.WEAPONSMITH_PROGRESSION_REWARDS);
   }

    private void buildLootFor(BiConsumer<ResourceLocation, LootTable.Builder> lootTableBuilder, List<ResourceLocation> rewardsLocation) {
        for(int i = 0; i < rewardsLocation.size(); i++){
            ResourceLocation location = rewardsLocation.get(i);
            switch (i){
                case 0 -> buildApprenticeReward(lootTableBuilder, location);
                case 1 -> buildJourneymanReward(lootTableBuilder, location);
                case 2 -> buildExpertReward(lootTableBuilder, location);
                case 3 -> buildMasterReward(lootTableBuilder, location);
            }
        }
    }

    private void buildApprenticeReward(BiConsumer<ResourceLocation, LootTable.Builder> lootTableBuilder, ResourceLocation location) {
      lootTableBuilder.accept(location,
              LootTable.lootTable()
                      .withPool(LootPool.lootPool()
                              .setRolls(ConstantValue.exactly(1.0F))
                              .add(
                                      LootItem.lootTableItem(SHItems.ENHANCEMENT_CORE.get())
                                              .apply(SetItemCountFunction.setCount(
                                                      ConstantValue.exactly(1.0F))
                                              )
                              )
              )
      );
   }

   private void buildJourneymanReward(BiConsumer<ResourceLocation, LootTable.Builder> lootTableBuilder, ResourceLocation location) {
      lootTableBuilder.accept(location,
              LootTable.lootTable()
                      .withPool(LootPool.lootPool()
                              .setRolls(ConstantValue.exactly(1.0F))
                              .add(
                                      LootItem.lootTableItem(SHItems.UPGRADE_MODULE.get())
                                              .apply(SetItemCountFunction.setCount(
                                                      ConstantValue.exactly(1.0F))
                                              )
                              )
                      )
      );
   }

   private void buildExpertReward(BiConsumer<ResourceLocation, LootTable.Builder> lootTableBuilder, ResourceLocation location) {
      lootTableBuilder.accept(location,
              LootTable.lootTable()
                      .withPool(LootPool.lootPool()
                              .setRolls(ConstantValue.exactly(1.0F))
                              .add(
                                      LootItem.lootTableItem(SHItems.ENHANCEMENT_CORE.get())
                                              .apply(SetItemCountFunction.setCount(
                                                      ConstantValue.exactly(1.0F))
                                              )
                              )
                      )
      );
   }

   private void buildMasterReward(BiConsumer<ResourceLocation, LootTable.Builder> lootTableBuilder, ResourceLocation location) {
      lootTableBuilder.accept(location,
              LootTable.lootTable()
                      .withPool(LootPool.lootPool()
                              .setRolls(ConstantValue.exactly(1.0F))
                              .add(
                                      LootItem.lootTableItem(SHItems.UPGRADE_MODULE.get())
                                              .apply(SetItemCountFunction.setCount(
                                                      ConstantValue.exactly(1.0F))
                                              )
                              )
                      )
      );
   }
}