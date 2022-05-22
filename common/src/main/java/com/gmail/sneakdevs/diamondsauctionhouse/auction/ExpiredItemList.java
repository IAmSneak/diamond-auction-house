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

    public AuctionItem getItem(int item) {
        return items.get(item);
    }

    public int size() {
        return items.size();
    }
}
