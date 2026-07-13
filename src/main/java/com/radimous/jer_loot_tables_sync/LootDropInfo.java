package com.radimous.jer_loot_tables_sync;

import jeresources.api.drop.LootDrop;

import java.util.List;

public record LootDropInfo(List<LootDrop> drops, int minStacks, int maxStacks) {
}
