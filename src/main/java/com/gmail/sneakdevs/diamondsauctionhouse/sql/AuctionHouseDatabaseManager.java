package com.gmail.sneakdevs.diamondsauctionhouse.sql;

import com.gmail.sneakdevs.diamondsauctionhouse.auction.AuctionItem;

public interface AuctionHouseDatabaseManager {
    int addItemToAuction(String playerUuid, String owner, String tag, String item, int count, int price, int secondsLeft);
    int getMostRecentId();
    int playerItemCount(String playeruuid, String table);
    boolean isItemForAuction(int id);
    void updateTime(int id, int seconds);
    void removeItemFromAuction(AuctionItem item);
    void removeItemFromExpired(AuctionItem item);
    void expireItem(AuctionItem item);
}
