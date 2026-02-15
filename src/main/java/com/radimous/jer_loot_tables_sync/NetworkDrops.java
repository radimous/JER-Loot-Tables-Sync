package com.radimous.jer_loot_tables_sync;

import com.radimous.jer_loot_tables_sync.mixin.accessors.AccessorLootItem;
import com.radimous.jer_loot_tables_sync.mixin.accessors.AccessorLootPoolEntryContainer;
import com.radimous.jer_loot_tables_sync.mixin.accessors.AccessorLootPoolSingletonContainer;
import com.radimous.jer_loot_tables_sync.mixin.accessors.AccessorLootTableReference;
import jeresources.api.drop.LootDrop;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static jeresources.util.LootTableHelper.getLootConditions;
import static jeresources.util.LootTableHelper.getLootEntries;
import static jeresources.util.LootTableHelper.getPools;

public class NetworkDrops {
    public static final Map<ResourceLocation, List<LootDrop>> ID_TO_LOOT = new ConcurrentHashMap<>();



    public static List<LootDrop> toDrops(LootTables lootTables, LootTable table) {
        List<LootDrop> drops = new ArrayList<>();

        getPools(table).forEach(
            pool -> {
                final float totalWeight = getLootEntries(pool).stream()
                    .filter(entry -> entry instanceof LootPoolSingletonContainer).map(entry -> (LootPoolSingletonContainer) entry)
                    .mapToInt(entry -> ((AccessorLootPoolSingletonContainer)entry).getWeight()).sum();
                final List<LootItemCondition> poolConditions = getLootConditions(pool);
                getLootEntries(pool).stream()
                    .filter(entry -> entry instanceof LootItem).map(entry -> (LootItem) entry)
                    .map(entry -> new LootDrop(
                        ((AccessorLootItem)entry).getItem(),
                        ((AccessorLootPoolSingletonContainer)entry).getWeight() / totalWeight,
                        ((AccessorLootPoolEntryContainer)entry).getConditions(),
                        ((AccessorLootPoolSingletonContainer)entry).getFunctions()))
                    .map(drop -> drop.addLootConditions(poolConditions))
                    .forEach(drops::add);

                getLootEntries(pool).stream()
                    .filter(entry -> entry instanceof LootTableReference).map(entry -> (LootTableReference) entry)
                    .map(entry -> toDrops(lootTables, lootTables.get(((AccessorLootTableReference)entry).getName()))).forEach(drops::addAll);
            }
        );

        drops.removeIf(Objects::isNull);
        return drops;
    }

    public static List<LootDrop> toDrops(LootTables lootTables, ResourceLocation lootTable) {
        return toDrops(lootTables, lootTables.get(lootTable));
    }
}
