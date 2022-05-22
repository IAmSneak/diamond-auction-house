package com.gmail.sneakdevs.diamondsauctionhouse.config;

import com.gmail.sneakdevs.diamondsauctionhouse.DiamondsAuctionHouse;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = DiamondsAuctionHouse.MODID)
public class DiamondsAuctionHouseConfig implements ConfigData {
    @Comment("Name of the command to open the auction house GUI")
    public String auctionHouseCommandName = "ah";
    @Comment("Name of the command to put something up for auction")
    public String auctionCommandName = "auc";
    @Comment("Use the base diamond economy command")
    public boolean useBaseCommand = false;
    @Comment("Maximum items a player can have up for auction (expired included)")
    public int maxPlayerItems = 3;
    @Comment("Maximum number of pages the auction house can have")
    public int maxPages = 8;
    @Comment("Seconds the item is put on the auction for")
    public int auctionSeconds = 259200;

    public static DiamondsAuctionHouseConfig getInstance() {
        return AutoConfig.getConfigHolder(DiamondsAuctionHouseConfig.class).getConfig();
    }
}