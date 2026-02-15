package com.radimous.jer_loot_tables_sync.mixin;

import com.radimous.jer_loot_tables_sync.NetworkDrops;
import jeresources.api.drop.LootDrop;
import jeresources.entry.DungeonEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(value = DungeonEntry.class,remap = false)
public class MixinDungeonEntry {
    @Shadow private Set<LootDrop> drops;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void fillPartialIfEmpty(String name, LootTable lootTable, CallbackInfo ci){
        if (this.drops.isEmpty()) {
            this.drops.addAll(NetworkDrops.ID_TO_LOOT.get(ResourceLocation.parse(name)));
        }
    }
}
