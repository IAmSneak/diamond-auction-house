package com.gmail.sneakdevs.diamondauctionhouse.auction;

import net.minecraft.world.item.ItemStack;

public class AuctionItem {
    private final ItemStack itemStack;
    private final String uuid;
    private final String owner;
    private final int price;
    private int secondsLeft;

    public AuctionItem(ItemStack stack, String uuid, String owner, int price, int secondsLeft) {
        this.itemStack = stack;
        this.uuid = uuid;
        this.owner = owner;
        this.price = price;
        this.secondsLeft = secondsLeft;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getUuid() {
        return uuid;
    }

    public String getOwner() {
        return owner;
    }

    public int getPrice() {
        return price;
    }

    public String getTimeLeft() {
        int seconds = secondsLeft;
        int days = seconds / 86400;
        seconds -= days * 86400;
        int hours = seconds / 3600;
        seconds -= hours * 3600;
        int minutes = seconds / 60;
        seconds -= minutes * 60;
        if (days > 0) {
            return String.format("%02d:%02d:%02d" + "m", days, hours, minutes);
        } else {
            if (hours > 0) {
                return String.format("%02d:%02d:%02d" + "s", hours, minutes, seconds);
            }
        }
        return (minutes > 0) ? String.format("%02d:%02d" + "s", minutes, seconds) : (seconds + "s");
    }

    public boolean tickDeath() {
        secondsLeft--;
        return secondsLeft <= 0;
    }
}
