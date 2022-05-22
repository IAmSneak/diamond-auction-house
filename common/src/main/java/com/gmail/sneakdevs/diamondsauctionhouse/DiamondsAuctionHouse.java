package com.gmail.sneakdevs.diamondsauctionhouse;

import com.gmail.sneakdevs.diamondsauctionhouse.auction.AuctionHouse;
import com.gmail.sneakdevs.diamondsauctionhouse.config.DiamondsAuctionHouseConfig;
import com.gmail.sneakdevs.diamondsauctionhouse.sql.AuctionHouseDatabaseManager;
import com.gmail.sneakdevs.diamondsauctionhouse.sql.AuctionHouseSQLiteDatabaseManager;
import com.gmail.sneakdevs.diamondeconomy.DiamondEconomy;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.server.MinecraftServer;

public class DiamondsAuctionHouse {
    public static final String MODID = "diamondsauctionhouse";
    public static AuctionHouse ah;

    public static AuctionHouseDatabaseManager getDatabaseManager() {
        return new AuctionHouseSQLiteDatabaseManager();
    }

    public static void initServer(MinecraftServer server){
        ah = new AuctionHouse(AuctionHouseSQLiteDatabaseManager.getItemList());
    }

    public static void init(){
        DiamondEconomy.registerTable("CREATE TABLE IF NOT EXISTS auctionhouse (id integer PRIMARY KEY AUTOINCREMENT, playeruuid text, owner text NOT NULL, price integer, tag text, secondsleft integer);");
        DiamondEconomy.registerTable("CREATE TABLE IF NOT EXISTS expireditems (id integer PRIMARY KEY AUTOINCREMENT, playeruuid text, owner text NOT NULL, price integer, tag text);");
        AutoConfig.register(DiamondsAuctionHouseConfig.class, JanksonConfigSerializer::new);
    }
}
