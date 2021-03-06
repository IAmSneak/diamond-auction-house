package com.gmail.sneakdevs.diamondsauctionhouse;

import com.gmail.sneakdevs.diamondeconomy.DiamondUtils;
import com.gmail.sneakdevs.diamondsauctionhouse.auction.AuctionHouse;
import com.gmail.sneakdevs.diamondsauctionhouse.auction.ExpiredItemList;
import com.gmail.sneakdevs.diamondsauctionhouse.config.DiamondsAuctionHouseConfig;
import com.gmail.sneakdevs.diamondsauctionhouse.sql.AuctionHouseDatabaseManager;
import com.gmail.sneakdevs.diamondsauctionhouse.sql.AuctionHouseSQLiteDatabaseManager;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class DiamondsAuctionHouse implements ModInitializer {
    public static final String MODID = "diamondsauctionhouse";
    public static AuctionHouse ah;
    public static ExpiredItemList ei;

    public static AuctionHouseDatabaseManager getDatabaseManager() {
        return new AuctionHouseSQLiteDatabaseManager();
    }

    public static void initServer(MinecraftServer server){
        ah = new AuctionHouse(AuctionHouseSQLiteDatabaseManager.getItemList());
        ei = new ExpiredItemList(AuctionHouseSQLiteDatabaseManager.getExpiredItemList());
    }

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> AuctionHouseCommand.register(dispatcher));
        ServerLifecycleEvents.SERVER_STARTED.register(DiamondsAuctionHouse::initServer);
        DiamondUtils.registerTable("CREATE TABLE IF NOT EXISTS auctionhouse (id integer PRIMARY KEY AUTOINCREMENT, playeruuid text NOT NULL, owner text NOT NULL, tag text NOT NULL, item text NOT NULL, count integer NOT NULL, price integer NOT NULL, secondsleft integer NOT NULL);");
        DiamondUtils.registerTable("CREATE TABLE IF NOT EXISTS expireditems (id integer PRIMARY KEY, playeruuid text NOT NULL, owner text NOT NULL, tag text NOT NULL, item text NOT NULL, count integer NOT NULL, price integer NOT NULL);");
        AutoConfig.register(DiamondsAuctionHouseConfig.class, JanksonConfigSerializer::new);
    }
}
