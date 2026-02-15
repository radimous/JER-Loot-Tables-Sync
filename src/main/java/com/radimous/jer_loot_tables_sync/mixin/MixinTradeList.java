package com.radimous.jer_loot_tables_sync.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import jeresources.collection.TradeList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Random;

@Mixin(value = TradeList.class, remap = false)
public class MixinTradeList {
    @WrapOperation(method = "addMerchantRecipe", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;getOffer(Lnet/minecraft/world/entity/Entity;Ljava/util/Random;)Lnet/minecraft/world/item/trading/MerchantOffer;",remap = true))
    private MerchantOffer fixNPE(VillagerTrades.ItemListing instance, Entity entity, Random random, Operation<MerchantOffer> original){
        try {
            return original.call(instance, entity, random);
        } catch (NullPointerException npe) {
            return null;
        }
    }
}
