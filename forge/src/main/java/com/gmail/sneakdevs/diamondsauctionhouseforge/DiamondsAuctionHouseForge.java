package com.gmail.sneakdevs.diamondsauctionhouseforge;

import com.gmail.sneakdevs.diamondsauctionhouse.DiamondsAuctionHouse;
import com.gmail.sneakdevs.diamondsauctionhouseforge.events.RegisterCommandEventHandler_DiamondsAuctionHouse;
import com.gmail.sneakdevs.diamondsauctionhouseforge.events.StartEventHandler_DiamondsAuctionHouse;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(DiamondsAuctionHouse.MODID)
public class DiamondsAuctionHouseForge {
    public DiamondsAuctionHouseForge() {
        MinecraftForge.EVENT_BUS.addListener(StartEventHandler_DiamondsAuctionHouse::diamondauctionhouse_onServerStartingEvent);
        MinecraftForge.EVENT_BUS.addListener(RegisterCommandEventHandler_DiamondsAuctionHouse::diamondauctionhouse_registerCommandsEvent);
        DiamondsAuctionHouse.init();
    }
}
