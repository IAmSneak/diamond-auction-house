package com.gmail.sneakdevs.diamondauctionhouseforge.events;

import com.gmail.sneakdevs.diamondauctionhouse.DiamondAuctionHouse;
import com.gmail.sneakdevs.diamondauctionhouse.auction.AuctionHouse;
import com.gmail.sneakdevs.diamondauctionhouse.sql.AuctionHouseSQLiteDatabaseManager;
import com.gmail.sneakdevs.diamondeconomy.DiamondEconomy;
import com.gmail.sneakdevs.diamondeconomy.config.DiamondEconomyConfig;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.server.ServerStartingEvent;

import java.io.File;

public class StartEventHandler_DiamondAuctionHouse {
    public static void diamondauctionhouse_onServerStartingEvent(ServerStartingEvent event) {
        AuctionHouseSQLiteDatabaseManager.createNewDatabase((DiamondEconomyConfig.getInstance().fileLocation != null) ? (new File(DiamondEconomyConfig.getInstance().fileLocation)) : event.getServer().getWorldPath(LevelResource.ROOT).resolve(DiamondEconomy.MODID + ".sqlite").toFile());
        DiamondAuctionHouse.init(new AuctionHouse(AuctionHouseSQLiteDatabaseManager.getItemList()));
    }
}