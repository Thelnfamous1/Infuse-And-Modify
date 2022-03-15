package com.infamous.simply_harder.datagen.provider;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.infamous.simply_harder.custom.loot.MerchantProgressionLoot;
import com.infamous.simply_harder.custom.loot.SHBuiltInLootTables;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModLootTableProvider extends LootTableProvider {
    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> subProviders =
            ImmutableList.of(
                    Pair.of(MerchantProgressionLoot::new, LootContextParamSets.ADVANCEMENT_ENTITY)
            );

    public ModLootTableProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return this.subProviders;
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
        for(ResourceLocation location : Sets.difference(SHBuiltInLootTables.all(), map.keySet())) {
            validationtracker.reportProblem("Missing built-in table: " + location);
        }

        map.forEach((location, lootTable) -> {
            LootTables.validate(validationtracker, location, lootTable);
        });
    }
}
