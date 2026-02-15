package com.radimous.jer_loot_tables_sync.mixin.accessors;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootTableReference.class)
public interface AccessorLootTableReference {
    @Accessor
    ResourceLocation getName();
}
