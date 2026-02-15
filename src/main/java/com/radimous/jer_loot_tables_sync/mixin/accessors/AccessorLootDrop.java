package com.radimous.jer_loot_tables_sync.mixin.accessors;

import jeresources.api.conditionals.Conditional;
import jeresources.api.drop.LootDrop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(value = LootDrop.class, remap = false)
public interface AccessorLootDrop {
    @Accessor
    Set<Conditional> getConditionals();
}
