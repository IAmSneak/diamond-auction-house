package com.gmail.sneakdevs.diamondauctionhousefabric;

import com.gmail.sneakdevs.diamondauctionhouse.AuctionHouseCommand;
import com.gmail.sneakdevs.diamondauctionhouse.config.DiamondAuctionHouseConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.EventFactory;

public class DiamondAuctionHouseFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> AuctionHouseCommand.register(dispatcher));
        AutoConfig.register(DiamondAuctionHouseConfig.class, JanksonConfigSerializer::new);

    }
}
