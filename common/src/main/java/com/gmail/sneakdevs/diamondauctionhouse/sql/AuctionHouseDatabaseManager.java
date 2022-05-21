package com.gmail.sneakdevs.diamondauctionhouse.sql;

public interface AuctionHouseDatabaseManager {
    int addItem(String playerUuid, String owner, int price, int secondsLeft, String tag);
}
