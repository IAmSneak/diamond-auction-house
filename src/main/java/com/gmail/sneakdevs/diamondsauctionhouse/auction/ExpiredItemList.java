package com.gmail.sneakdevs.diamondsauctionhouse.auction;

import java.util.ArrayList;

public class ExpiredItemList {
    private ArrayList<AuctionItem> items;

    public ExpiredItemList(ArrayList<AuctionItem> items){
        this.items = items;
    }

    public void addItem(AuctionItem item) {
        items.add(item);
    }

    public void removeItem(AuctionItem item) {
        items.remove(item);
    }

    public int size(){
        return items.size();
    }

    public AuctionItem getItem(int item) {
        return items.get(item);
    }

    public ExpiredItemList getPlayerExpiredItems(String uuid) {
        ExpiredItemList ei = new ExpiredItemList(new ArrayList<>());
        for (AuctionItem item: items) {
            if (item.getUuid().equals(uuid)) {
                ei.addItem(item);
            }
        }
        return ei;
    }
}
