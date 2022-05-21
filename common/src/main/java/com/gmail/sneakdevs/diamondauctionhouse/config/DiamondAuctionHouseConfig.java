package com.gmail.sneakdevs.diamondauctionhouse.config;

import com.gmail.sneakdevs.diamondauctionhouse.DiamondAuctionHouse;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = DiamondAuctionHouse.MODID)
public class DiamondAuctionHouseConfig implements ConfigData {
    //todo add comments
    public String auctionHouseCommandName = "ah";
    public String auctionCommandName = "auction";
    public boolean useBaseCommand = false;
    public int maxPlayerItems = 3;
    public int maxPages = 3;
    public int auctionSeconds = 259200;

    public static DiamondAuctionHouseConfig getInstance() {
        return AutoConfig.getConfigHolder(DiamondAuctionHouseConfig.class).getConfig();
    }
}