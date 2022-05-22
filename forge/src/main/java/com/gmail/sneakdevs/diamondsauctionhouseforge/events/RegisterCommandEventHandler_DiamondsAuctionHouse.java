package com.gmail.sneakdevs.diamondsauctionhouseforge.events;

import com.gmail.sneakdevs.diamondsauctionhouse.AuctionHouseCommand;
import net.minecraftforge.event.RegisterCommandsEvent;

public class RegisterCommandEventHandler_DiamondsAuctionHouse {
    public static void diamondauctionhouse_registerCommandsEvent(RegisterCommandsEvent event) {
        AuctionHouseCommand.register(event.getDispatcher());
    }
}
