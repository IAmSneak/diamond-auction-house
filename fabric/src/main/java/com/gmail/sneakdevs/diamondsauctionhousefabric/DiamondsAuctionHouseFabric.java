package com.gmail.sneakdevs.diamondsauctionhousefabric;

import com.gmail.sneakdevs.diamondsauctionhouse.AuctionHouseCommand;
import com.gmail.sneakdevs.diamondsauctionhouse.DiamondsAuctionHouse;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class DiamondsAuctionHouseFabric implements ModInitializer {
    private static void serverStarted(MinecraftServer server){
        DiamondsAuctionHouse.initServer(server);
    }

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> AuctionHouseCommand.register(dispatcher));
        ServerLifecycleEvents.SERVER_STARTED.register(DiamondsAuctionHouseFabric::serverStarted);
        DiamondsAuctionHouse.init();
    }
}
