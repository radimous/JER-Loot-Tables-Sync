package com.radimous.jer_loot_tables_sync;

import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;

@Mod.EventBusSubscriber
public class EventHandler {
    @SubscribeEvent
    public static void onOnDatapackSync(OnDatapackSyncEvent event) {
        var plauerList = event.getPlayer() == null ? event.getPlayerList().getPlayers() : Collections.singletonList(event.getPlayer());
        S2CLoottableMessage.sendLootToPlayers(event.getPlayerList().getServer(), plauerList);
    }

}
