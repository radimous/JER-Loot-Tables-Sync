package com.radimous.jer_loot_tables_sync;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(JERLootTablesSync.MOD_ID)
public class JERLootTablesSync {

    public static final String MOD_ID = "jer_loot_tables_sync";
    public static final Logger LOGGER = LogUtils.getLogger();

    public JERLootTablesSync() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        ModNetwork.init();
    }
}
