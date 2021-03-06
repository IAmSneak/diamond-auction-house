package com.gmail.sneakdevs.diamondsauctionhouse.auction;

import com.gmail.sneakdevs.diamondsauctionhouse.DiamondsAuctionHouse;
import com.gmail.sneakdevs.diamondsauctionhouse.config.DiamondsAuctionHouseConfig;

import java.util.ArrayList;

public class AuctionHouse {
    private int tick = 0;
    private ArrayList<AuctionItem> items;

    public AuctionHouse(ArrayList<AuctionItem> items){
        this.items = items;
    }

    public boolean canAddItem() {
        return items.size() <= DiamondsAuctionHouseConfig.getInstance().maxPages * 45;
    }

    public void addItem(AuctionItem item) {
        items.add(item);
    }

    public void removeItem(AuctionItem item) {
        items.remove(item);
    }

    public AuctionItem getItem(int item) {
        return items.get(item);
    }

    public int size() {
        return items.size();
    }

    public void tick() {
        tick++;
        if (tick % 20 == 0) {
            int i = 0;
            while (i < items.size()) {
                if (items.get(i).tickDeath()) {
                    DiamondsAuctionHouse.getDatabaseManager().expireItem(items.get(i));
                } else {
                    i++;
                }
            }
        }
        if (tick % 300 == 0) {
            for (AuctionItem item: items) {
                DiamondsAuctionHouse.getDatabaseManager().updateTime(item.getId(), item.getSecondsLeft());
            }
        }
    }

    public AuctionHouse getPlayerAuctionHouse(String uuid) {
        AuctionHouse ah = new AuctionHouse(new ArrayList<>());
        for (AuctionItem item: items) {
            if (item.getUuid().equals(uuid)) {
                ah.addItem(item);
            }
        }
        return ah;
    }
}
