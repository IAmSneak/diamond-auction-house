package com.gmail.sneakdevs.diamondsauctionhouse.gui;

import com.gmail.sneakdevs.diamondeconomy.DiamondEconomy;
import com.gmail.sneakdevs.diamondeconomy.DiamondUtils;
import com.gmail.sneakdevs.diamondsauctionhouse.DiamondsAuctionHouse;
import com.gmail.sneakdevs.diamondsauctionhouse.auction.AuctionItem;
import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.elements.GuiElementBuilderInterface;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class AuctionItemGui extends SimpleGui {
    private int ticker = 0;
    private final AuctionItem item;

    public AuctionItemGui(ServerPlayer player, AuctionItem item) {
        super(MenuType.GENERIC_9x1, player, false);
        this.item = item;
    }

    public void updateDisplay() {
        for (int i = 0; i < 9; i++) {
            var navElement = this.getNavElement(i);

            if (navElement == null) {
                navElement = DisplayElement.EMPTY;
            }

            if (navElement.element != null) {
                this.setSlot(i, navElement.element);
            } else if (navElement.slot != null) {
                this.setSlotRedirect(i, navElement.slot);
            }
        }
    }

    protected DisplayElement getNavElement(int id) {
        return switch (id) {
            case 0 -> DisplayElement.of(
                    new GuiElementBuilder(Items.CLOCK)
                            .setName(new TextComponent("Time Left: " + item.getTimeLeft()).withStyle(ChatFormatting.BLUE))
                            .hideFlags()
            );
            case 1 -> DisplayElement.of(
                    new GuiElementBuilder(Items.PAPER)
                            .setName(new TextComponent("Price: $" + item.getPrice()).withStyle(ChatFormatting.BLUE))
                            .hideFlags()
            );
            case 2 -> skull();
            case 4 -> DisplayElement.of(GuiElementBuilder.from(item.getItemStack()));
            case 6 -> confirm();
            case 7 -> DisplayElement.of(
                    new GuiElementBuilder(Items.RED_STAINED_GLASS_PANE)
                            .setName(new TextComponent("Cancel").withStyle(ChatFormatting.RED))
                            .hideFlags()
                            .setCallback((x, y, z) -> {
                                playClickSound(this.player);
                                this.close();
                            })
            );
            case 8 -> trash();

            default -> DisplayElement.filler();
        };
    }

    @Override
    public void onTick() {
        ticker++;
        if (ticker >= 5) {
            ticker = 0;
            updateDisplay();
        }

        super.onTick();
    }

    public static void playClickSound(ServerPlayer player) {
        player.playNotifySound(SoundEvents.UI_BUTTON_CLICK, SoundSource.MASTER, 1, 1);
    }

    private DisplayElement confirm() {
        if (item.getPrice() < DiamondUtils.getDatabaseManager().getBalanceFromUUID(player.getStringUUID())) {
            return DisplayElement.of(
                    new GuiElementBuilder(Items.GREEN_STAINED_GLASS_PANE)
                            .setName(new TextComponent("Confirm").withStyle(ChatFormatting.GREEN))
                            .hideFlags()
                            .setCallback((x, y, z) -> {
                                playClickSound(this.player);
                                this.buy();
                            }));
        } else {
            return DisplayElement.of(
                    new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE)
                            .setName(new TextComponent("Confirm").withStyle(ChatFormatting.DARK_GRAY))
                            .hideFlags());
        }
    }

    private DisplayElement trash() {
        if (player.hasPermissions(4) || player.getStringUUID().equals(item.getUuid())) {
            return DisplayElement.of(
                    new GuiElementBuilder(Items.HOPPER)
                            .setName(new TextComponent("Remove from Auction").withStyle(ChatFormatting.RED))
                            .hideFlags()
                            .setCallback((x, y, z) -> {
                                playClickSound(this.player);
                                this.remove();
                            }));
        } else {
            return DisplayElement.EMPTY;
        }
    }

    private DisplayElement skull() {
        ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
        stack.getOrCreateTag().putString("SkullOwner",  item.getOwner());
        return DisplayElement.of(GuiElementBuilder.from(stack)
                .setName(new TextComponent("Owner: " + item.getOwner()).withStyle(ChatFormatting.BLUE))
                .hideFlags());
    }

    @Override
    public void close() {
        AuctionHouseGui gui = new AuctionHouseGui(player);
        gui.updateDisplay();
        gui.setTitle(new TextComponent("Auction House"));
        gui.open();
        super.close();
    }

    private void remove() {
        DiamondsAuctionHouse.getDatabaseManager().expireItem(item);
        close();
    }

    private void buy() {
        if (DiamondsAuctionHouse.getDatabaseManager().isItemForAuction(item.getId())) {
            if (player.getInventory().getFreeSlot() != -1) {
                if (DiamondUtils.getDatabaseManager().changeBalance(item.getUuid(), item.getPrice())) {
                    DiamondUtils.getDatabaseManager().changeBalance(player.getStringUUID(), -item.getPrice());
                    DiamondsAuctionHouse.getDatabaseManager().removeItemFromAuction(item);
                    DiamondsAuctionHouse.ah.removeItem(item);
                    player.getInventory().add(item.getItemStack());
                }
            }
        } else {
            player.sendMessage(new TextComponent("That item was already bought"), player.getUUID());
        }
        super.close();
    }

    public record DisplayElement(@Nullable GuiElementInterface element, @Nullable Slot slot) {
        private static final DisplayElement EMPTY = DisplayElement.of(new GuiElement(ItemStack.EMPTY, GuiElementInterface.EMPTY_CALLBACK));
        private static final DisplayElement FILLER = DisplayElement.of(
                new GuiElementBuilder(Items.LIGHT_GRAY_STAINED_GLASS_PANE)
                        .setName(new TextComponent(""))
                        .hideFlags()
        );

        public static DisplayElement of(GuiElementInterface element) {
            return new DisplayElement(element, null);
        }

        public static DisplayElement of(GuiElementBuilderInterface<?> element) {
            return new DisplayElement(element.build(), null);
        }

        public static DisplayElement filler() {
            return FILLER;
        }
    }
}