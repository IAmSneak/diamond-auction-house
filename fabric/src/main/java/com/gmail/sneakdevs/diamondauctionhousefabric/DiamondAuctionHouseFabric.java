package com.gmail.sneakdevs.diamondauctionhousefabric;

import com.gmail.sneakdevs.diamondauctionhouse.AuctionHouseCommand;
import com.gmail.sneakdevs.diamondauctionhouse.DiamondAuctionHouse;
import com.gmail.sneakdevs.diamondauctionhouse.auction.AuctionHouse;
import com.gmail.sneakdevs.diamondauctionhouse.config.DiamondAuctionHouseConfig;
import com.gmail.sneakdevs.diamondauctionhouse.sql.AuctionHouseSQLiteDatabaseManager;
import com.gmail.sneakdevs.diamondeconomy.DiamondEconomy;
import com.gmail.sneakdevs.diamondeconomy.config.DiamondEconomyConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.File;

public class DiamondAuctionHouseFabric implements ModInitializer {
    private static void serverStarting(MinecraftServer server){
        AuctionHouseSQLiteDatabaseManager.createNewDatabase((DiamondEconomyConfig.getInstance().fileLocation != null) ? (new File(DiamondEconomyConfig.getInstance().fileLocation)) : server.getWorldPath(LevelResource.ROOT).resolve(DiamondEconomy.MODID + ".sqlite").toFile());
        DiamondAuctionHouse.init(new AuctionHouse(AuctionHouseSQLiteDatabaseManager.getItemList()));
    }

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> AuctionHouseCommand.register(dispatcher));
        AutoConfig.register(DiamondAuctionHouseConfig.class, JanksonConfigSerializer::new);
        ServerLifecycleEvents.SERVER_STARTING.register(DiamondAuctionHouseFabric::serverStarting);
    }
}
