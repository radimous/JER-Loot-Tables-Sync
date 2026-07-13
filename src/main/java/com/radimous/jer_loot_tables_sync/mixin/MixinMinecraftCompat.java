package com.radimous.jer_loot_tables_sync.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.radimous.jer_loot_tables_sync.NetworkDrops;
import com.radimous.jer_loot_tables_sync.mixin.accessors.AccessorMobEntry;
import jeresources.compatibility.minecraft.MinecraftCompat;
import jeresources.entry.MobEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;
import java.util.function.Supplier;

@Mixin(value = MinecraftCompat.class, remap = false)
public class MixinMinecraftCompat {
    @WrapOperation(method = "lambda$registerVanillaMobs$0", at = @At(value = "INVOKE", target = "Ljeresources/entry/MobEntry;create(Ljava/util/function/Supplier;Lnet/minecraft/world/level/storage/loot/LootTable;)Ljeresources/entry/MobEntry;"))
    private static MobEntry fillPartialIfEmpty(Supplier<LivingEntity> entity, LootTable lootTable, Operation<MobEntry> original, @Local(argsOnly = true) Map.Entry<ResourceLocation, ?> entry) {
        var loottableRL = entry.getKey();
        var origRv = original.call(entity, lootTable);
        if (lootTable.getLootTableId() == null && loottableRL != null && origRv.getDrops().isEmpty()) {
            var netDrops = NetworkDrops.ID_TO_LOOT.get(loottableRL);
            if (netDrops != null) {
                return AccessorMobEntry.createMobEntry(entity, null, null, AccessorMobEntry.getANY_BIOMES(), netDrops.drops());
            }
        }

        return origRv;
    }

}
