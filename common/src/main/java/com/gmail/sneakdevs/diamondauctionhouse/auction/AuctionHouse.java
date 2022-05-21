package com.gmail.sneakdevs.diamondauctionhouse.auction;

import com.gmail.sneakdevs.diamondauctionhouse.config.DiamondAuctionHouseConfig;

import java.util.ArrayList;

public class AuctionHouse {
    private int tick = 0;
    private ArrayList<AuctionItem> items;

    public AuctionHouse(){
        this.items = new ArrayList<>();
    }

    public AuctionHouse(ArrayList<AuctionItem> items){
        this.items = items;
    }

    public boolean addItem(AuctionItem item) {
        if (items.size() <= DiamondAuctionHouseConfig.getInstance().maxPages * 45) {
            items.add(item);
            return true;
        }
        return false;
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
                    //todo add to player expired items
                    items.remove(i);
                } else {
                    i++;
                }
            }
        }
        if (tick % 600 == 0) {
            //todo set auction times to equal real auction times
        }
    }
}
