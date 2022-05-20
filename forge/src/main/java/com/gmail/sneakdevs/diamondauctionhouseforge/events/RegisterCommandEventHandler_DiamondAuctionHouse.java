package com.gmail.sneakdevs.diamondauctionhouseforge.events;

import com.gmail.sneakdevs.diamondauctionhouse.AuctionHouseCommand;
import net.minecraftforge.event.RegisterCommandsEvent;

public class RegisterCommandEventHandler_DiamondAuctionHouse {
    public static void diamondauctionhouse_registerCommandsEvent(RegisterCommandsEvent event) {
        AuctionHouseCommand.register(event.getDispatcher());
    }
}
