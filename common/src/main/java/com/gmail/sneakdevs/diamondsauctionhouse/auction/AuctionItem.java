package com.gmail.sneakdevs.diamondsauctionhouse.auction;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AuctionItem {
    private final ItemStack itemStack;
    private final String uuid;
    private final String owner;
    private final int price;
    private final int id;
    private int secondsLeft;

    public AuctionItem(int id, String playerUuid, String owner, int price, int secondsLeft, ItemStack stack) {
        this.id = id;
        this.itemStack = stack;
        this.uuid = playerUuid;
        this.owner = owner;
        this.price = price;
        this.secondsLeft = secondsLeft;
    }

    public AuctionItem(int id, String playerUuid, String owner, int price, int secondsLeft, String tag) {
        ItemStack itemStack1;
        this.id = id;
        //todo currently does correct item and stack size, needs to add other data
        try {
            itemStack1 = ItemStack.of(NbtUtils.snbtToStructure(tag));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            itemStack1 = new ItemStack(Items.AIR);
        }
        this.itemStack = itemStack1;
        this.uuid = playerUuid;
        this.owner = owner;
        this.price = price;
        this.secondsLeft = secondsLeft;
    }

    public int getId() {
        return id;
    }

    public int getSecondsLeft() {
        return secondsLeft;
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
        if (secondsLeft > 0) {
            secondsLeft--;
        }
        return secondsLeft == 0;
    }
}
