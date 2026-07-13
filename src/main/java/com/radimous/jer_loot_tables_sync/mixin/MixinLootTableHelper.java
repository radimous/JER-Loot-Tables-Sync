package com.radimous.jer_loot_tables_sync.mixin;

import com.radimous.jer_loot_tables_sync.NetworkDrops;
import jeresources.api.drop.LootDrop;
import jeresources.util.LootTableHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = LootTableHelper.class, remap = false)
public class MixinLootTableHelper  {
    @Inject(method = "toDrops(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
    private static void networkDropsRL(ResourceLocation lootTableID, CallbackInfoReturnable<List<LootDrop>> cir) {
        if (lootTableID != null) {
            var fromNet = NetworkDrops.ID_TO_LOOT.get(lootTableID);
            if (fromNet != null) {
                cir.setReturnValue(fromNet.drops());
            }
        }
    }

    @Inject(method = "toDrops(Lnet/minecraft/world/level/storage/loot/LootTable;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
    private static void networkDropsLT(LootTable table, CallbackInfoReturnable<List<LootDrop>> cir) {
        var lootTableID = table.getLootTableId();
        if (lootTableID != null) {
            var fromNet = NetworkDrops.ID_TO_LOOT.get(lootTableID);
            if (fromNet != null) {
                cir.setReturnValue(fromNet.drops());
            }
        }

    }
}
