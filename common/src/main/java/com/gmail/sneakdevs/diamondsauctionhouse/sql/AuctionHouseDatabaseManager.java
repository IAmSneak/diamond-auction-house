package com.gmail.sneakdevs.diamondsauctionhouse.sql;

public interface AuctionHouseDatabaseManager {
    int addItem(String playerUuid, String owner, int price, String tag, int secondsLeft);
    int getMostRecentId();
    void updateTime(int id, int seconds);
}
