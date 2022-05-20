package com.gmail.sneakdevs.diamondauctionhouse.config;

import com.gmail.sneakdevs.diamondauctionhouse.DiamondAuctionHouse;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = DiamondAuctionHouse.MODID)
public class DiamondAuctionHouseConfig implements ConfigData {
    public String commandName = "ah";
    public int maxPages = 3;

    public static DiamondAuctionHouseConfig getInstance() {
        return AutoConfig.getConfigHolder(DiamondAuctionHouseConfig.class).getConfig();
    }
}