package com.gmail.sneakdevs.diamondsauctionhouse.config;

import com.gmail.sneakdevs.diamondsauctionhouse.DiamondsAuctionHouse;
import me.lucko.fabric.api.permissions.v0.Permissions;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.server.level.ServerPlayer;

@Config(name = DiamondsAuctionHouse.MODID)
public class DiamondsAuctionHouseConfig implements ConfigData {
    @Comment("Name of the command to open the auction house GUI")
    public String auctionHouseCommandName = "auc";
    @Comment("Name of the command to put something up for auction")
    public String auctionCommandName = "auc";
    @Comment("Use the base diamond economy command")
    public boolean useBaseCommand = false;
    @Comment("Maximum items a player can have up for auction (expired included)")
    public int maxPlayerItems = 5;
    @Comment("Maximum number of pages the auction house can have")
    public int maxPages = 8;
    @Comment("Seconds the item is put on the auction for")
    public int auctionSeconds = 259200;

    public static DiamondsAuctionHouseConfig getInstance() {
        return AutoConfig.getConfigHolder(DiamondsAuctionHouseConfig.class).getConfig();
    }

    public static int getPlayerMaxItems(ServerPlayer player){
        if (Permissions.check(player, DiamondsAuctionHouse.MODID + ".infiniteitems")) {
            return -1;
        }
        if (Permissions.check(player, DiamondsAuctionHouse.MODID + ".noitems")) {
            return 0;
        }
        int items = getInstance().maxPlayerItems;
        if (Permissions.check(player, DiamondsAuctionHouse.MODID + ".quintupleitems")) {
            return items * 5;
        }
        if (Permissions.check(player, DiamondsAuctionHouse.MODID + ".quadrupleitems")) {
            return items * 4;
        }
        if (Permissions.check(player, DiamondsAuctionHouse.MODID + ".tripleitems")) {
            return items * 3;
        }
        if (Permissions.check(player, DiamondsAuctionHouse.MODID + ".doubleitems")) {
            return items * 2;
        }
        if (Permissions.check(player, DiamondsAuctionHouse.MODID + ".halfitems")) {
            return items / 2;
        }
        return items;
    }
}