package com.gmail.sneakdevs.diamondsauctionhouseforge.events;

import com.gmail.sneakdevs.diamondsauctionhouse.DiamondsAuctionHouse;
import net.minecraftforge.event.server.ServerStartedEvent;

public class StartEventHandler_DiamondsAuctionHouse {
    public static void diamondauctionhouse_onServerStartingEvent(ServerStartedEvent event) {
        DiamondsAuctionHouse.initServer(event.getServer());
    }
}