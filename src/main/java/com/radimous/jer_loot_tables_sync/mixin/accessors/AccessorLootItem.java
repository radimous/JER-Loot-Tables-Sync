package com.radimous.jer_loot_tables_sync.mixin.accessors;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootItem.class)
public interface AccessorLootItem {
    @Accessor
    Item getItem();
}
