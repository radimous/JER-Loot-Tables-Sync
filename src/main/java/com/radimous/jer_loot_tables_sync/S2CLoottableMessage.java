package com.radimous.jer_loot_tables_sync;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.radimous.jer_loot_tables_sync.mixin.accessors.AccessorConditional;
import com.radimous.jer_loot_tables_sync.mixin.accessors.AccessorLootDrop;
import jeresources.api.conditionals.Conditional;
import jeresources.api.drop.LootDrop;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record S2CLoottableMessage(
    List<Pair<ResourceLocation, List<LootDrop>>> msgValue
) {
    public static void encode(S2CLoottableMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.msgValue.size());
        for (var pair : message.msgValue) {
            buffer.writeResourceLocation(pair.getFirst());
            writeLootDrops(buffer, pair.getSecond());
        }
    }

    public static S2CLoottableMessage decode(FriendlyByteBuf buffer) {
        int size = buffer.readInt();
        ArrayList<Pair<ResourceLocation, List<LootDrop>>> arrList = new ArrayList<>(size);
        JERLootTablesSync.LOGGER.debug("Received {} Loot Tables", size);
        for (int i = 0; i < size; i++) {
            ResourceLocation identifier = buffer.readResourceLocation();
            List<LootDrop> json = readLootDrops(buffer);
            arrList.add(new Pair<>(identifier, json));
        }
        return new S2CLoottableMessage(arrList);
    }


    public static void handle(S2CLoottableMessage message, Supplier<NetworkEvent.Context> contextSupplier) {

        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            for (var table : message.msgValue()) {
                NetworkDrops.ID_TO_LOOT.put(table.getFirst(), table.getSecond());
            }
        });
        context.setPacketHandled(true);
    }


    private static void writeLootDrops(FriendlyByteBuf buffer, List<LootDrop> lootDrops) {
        buffer.writeVarInt(lootDrops.size());

        for (LootDrop drop : lootDrops) {
            buffer.writeVarInt(drop.minDrop);
            buffer.writeVarInt(drop.maxDrop);

            buffer.writeItem(drop.item);
            buffer.writeItem(drop.smeltedItem == null ? ItemStack.EMPTY : drop.smeltedItem);

            buffer.writeFloat(drop.chance);

            buffer.writeVarInt(drop.fortuneLevel);
            buffer.writeBoolean(drop.enchanted);

            if (((AccessorLootDrop)drop).getConditionals() != null) {
                buffer.writeVarInt(((AccessorLootDrop)drop).getConditionals().size());
                for (Conditional cond : ((AccessorLootDrop)drop).getConditionals()) {
                    writeConditional(buffer, cond);
                }
            } else {
                buffer.writeVarInt(0);
            }
        }
    }

    private static List<LootDrop> readLootDrops(FriendlyByteBuf buffer) {
        int size = buffer.readVarInt();
        List<LootDrop> drops = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {


            int minDrop = buffer.readVarInt();
            int maxDrop = buffer.readVarInt();

            ItemStack item = buffer.readItem();
            ItemStack smelted = buffer.readItem();
            ItemStack smeltedItem = smelted.isEmpty() ? null : smelted;

            float chance = buffer.readFloat();

            int fortuneLevel = buffer.readVarInt();
            boolean enchanted = buffer.readBoolean();

            int condSize = buffer.readVarInt();
            ArrayList<Conditional> conditionals = new ArrayList<>();
            if (condSize > 0) {
                for (int j = 0; j < condSize; j++) {
                    conditionals.add(readConditional(buffer));
                }
            }
            LootDrop drop = new LootDrop(
                item,
                minDrop,
                maxDrop,
                chance,
                fortuneLevel
            );
            drop.enchanted = enchanted;
            drop.smeltedItem = smeltedItem;
            drop.addConditionals(conditionals);
            drops.add(drop);
        }

        return drops;
    }

    private static void writeConditional(FriendlyByteBuf buffer, Conditional conditional) {
        buffer.writeUtf(((AccessorConditional)conditional).getText());
        buffer.writeUtf(((AccessorConditional)conditional).getColour());
    }

    private static Conditional readConditional(FriendlyByteBuf buffer) {
        var text = buffer.readUtf(32767);
        var color = buffer.readUtf(256);
        var cond =  new Conditional(text);
        ((AccessorConditional)cond).setColour(color);
        return cond;
    }

    public static void sendLootToPlayers(MinecraftServer server, List<ServerPlayer> players) {
        LootTables lootManager = server.getLootTables();
        List<ResourceLocation> names = Lists.newArrayList(lootManager.getIds());

        int size = 50;
        for (int i = 0; i < names.size(); i += size) {
            int end = Math.min(names.size(), i + size);
            List<Pair<ResourceLocation, List<LootDrop>>> arrList = new ArrayList<>(end - i);
            for (int j = i; j < end; j++) {
                ResourceLocation location = names.get(j);
                arrList.add(new Pair<>(location, NetworkDrops.toDrops(lootManager, location)));
            }
            var msg = new S2CLoottableMessage(arrList);
            for (ServerPlayer player : players) {
                ModNetwork.sendToClient(msg, player);
            }
        }
    }

}
