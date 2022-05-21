package com.gmail.sneakdevs.diamondauctionhouseforge;

import com.gmail.sneakdevs.diamondauctionhouse.DiamondAuctionHouse;
import com.gmail.sneakdevs.diamondauctionhouse.config.DiamondAuctionHouseConfig;
import com.gmail.sneakdevs.diamondauctionhouseforge.events.RegisterCommandEventHandler_DiamondAuctionHouse;
import com.gmail.sneakdevs.diamondauctionhouseforge.events.StartEventHandler_DiamondAuctionHouse;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(DiamondAuctionHouse.MODID)
public class DiamondAuctionHouseForge {
    public DiamondAuctionHouseForge() {
        MinecraftForge.EVENT_BUS.addListener(StartEventHandler_DiamondAuctionHouse::diamondauctionhouse_onServerStartingEvent);
        MinecraftForge.EVENT_BUS.addListener(RegisterCommandEventHandler_DiamondAuctionHouse::diamondauctionhouse_registerCommandsEvent);
        AutoConfig.register(DiamondAuctionHouseConfig.class, JanksonConfigSerializer::new);
    }
}
