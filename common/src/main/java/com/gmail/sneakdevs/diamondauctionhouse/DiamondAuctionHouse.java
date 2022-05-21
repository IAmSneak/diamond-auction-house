package com.gmail.sneakdevs.diamondauctionhouse;

import com.gmail.sneakdevs.diamondauctionhouse.auction.AuctionHouse;
import com.gmail.sneakdevs.diamondauctionhouse.sql.AuctionHouseDatabaseManager;
import com.gmail.sneakdevs.diamondauctionhouse.sql.AuctionHouseSQLiteDatabaseManager;

public class DiamondAuctionHouse {
    public static final String MODID = "diamondauctionhouse";
    public static AuctionHouse ah;
    public static AuctionHouseDatabaseManager getDatabaseManager() {
        return new AuctionHouseSQLiteDatabaseManager();
    }
    public static void init(AuctionHouse auctionHouse){
        ah = auctionHouse;
    }
}
