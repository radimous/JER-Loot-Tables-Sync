package com.radimous.jer_loot_tables_sync;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    private static int id = 0;
    private static final String PROTOCOL_VERSION = "1.0.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
        .named(ResourceLocation.fromNamespaceAndPath(JERLootTablesSync.MOD_ID, "s2c_loot_table"))
        .clientAcceptedVersions(ver -> true)
        .serverAcceptedVersions(ver -> true)
        .networkProtocolVersion(() -> PROTOCOL_VERSION)
        .simpleChannel();

    public static void init() {
        CHANNEL.registerMessage(id++, S2CLoottableMessage.class, S2CLoottableMessage::encode, S2CLoottableMessage::decode, S2CLoottableMessage::handle);
    }

    public static <T> void sendToClient(T message, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
