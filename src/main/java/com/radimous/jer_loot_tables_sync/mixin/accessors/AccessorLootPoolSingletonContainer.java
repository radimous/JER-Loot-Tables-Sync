package com.radimous.jer_loot_tables_sync.mixin.accessors;

import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootPoolSingletonContainer.class)
public interface AccessorLootPoolSingletonContainer {
    @Accessor
    int getWeight();

    @Accessor
    LootItemFunction[] getFunctions();
}
