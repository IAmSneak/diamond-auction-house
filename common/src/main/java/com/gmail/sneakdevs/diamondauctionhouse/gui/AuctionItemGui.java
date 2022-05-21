package com.gmail.sneakdevs.diamondauctionhouse.gui;

import com.gmail.sneakdevs.diamondauctionhouse.auction.AuctionItem;
import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.elements.GuiElementBuilderInterface;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

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
                navElement = AuctionItemDisplayElement.EMPTY;
            }

            if (navElement.element != null) {
                this.setSlot(i, navElement.element);
            } else if (navElement.slot != null) {
                this.setSlotRedirect(i, navElement.slot);
            }
        }
    }

    protected AuctionItemDisplayElement getNavElement(int id) {
        return switch (id) {
            case 0 -> AuctionItemDisplayElement.of(
                    new GuiElementBuilder(Items.CLOCK)
                            .setName(new TextComponent("Time Left: " + item.getTimeLeft()).withStyle(ChatFormatting.BLUE))
                            .hideFlags()
            );
            case 1 -> AuctionItemDisplayElement.of(
                    new GuiElementBuilder(Items.FLOWER_BANNER_PATTERN)
                            .setName(new TextComponent("Price: $" + item.getPrice()).withStyle(ChatFormatting.BLUE))
                            .hideFlags()
            );
            case 2 -> AuctionItemDisplayElement.of(
                    new GuiElementBuilder(Items.PLAYER_HEAD)
                            .setSkullOwner("", null, UUID.fromString(item.getUuid()))
                            .setName(new TextComponent("Owner: " + item.getOwner()).withStyle(ChatFormatting.BLUE))
                            .hideFlags()
            );
            case 4 -> AuctionItemDisplayElement.of(GuiElementBuilder.from(item.getItemStack()));
            //todo buy
            case 6 -> AuctionItemDisplayElement.of(
                    new GuiElementBuilder(Items.GREEN_STAINED_GLASS_PANE)
                            .setName(new TextComponent("Confirm").withStyle(ChatFormatting.GREEN))
                            .hideFlags()
            );
            case 7 -> AuctionItemDisplayElement.of(
                    new GuiElementBuilder(Items.RED_STAINED_GLASS_PANE)
                            .setName(new TextComponent("Cancel").withStyle(ChatFormatting.RED))
                            .hideFlags()
                            .setCallback((x, y, z) -> {
                                playClickSound(this.player);
                                this.close();
                            })
            );
            case 8 -> trash();

            default -> AuctionItemDisplayElement.filler();
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

    //todo
    public AuctionItemDisplayElement trash() {
        if (player.hasPermissions(4) || player.getStringUUID().equals(item.getUuid())) {
            return AuctionItemDisplayElement.of(
                    new GuiElementBuilder(Items.HOPPER)
                            .setName(new TextComponent("Remove from Auction").withStyle(ChatFormatting.RED))
                            .hideFlags());
        } else {
            return AuctionItemDisplayElement.EMPTY;
        }
    }

    @Override
    public void close() {
        AuctionHouseGui gui = new AuctionHouseGui(player);
        gui.updateDisplay();
        gui.setTitle(new TextComponent("Auction House"));
        gui.open();
        super.close();
    }

    public record AuctionItemDisplayElement(@Nullable GuiElementInterface element, @Nullable Slot slot) {
        private static final AuctionItemDisplayElement EMPTY = AuctionItemDisplayElement.of(new GuiElement(ItemStack.EMPTY, GuiElementInterface.EMPTY_CALLBACK));
        private static final AuctionItemDisplayElement FILLER = AuctionItemDisplayElement.of(
                new GuiElementBuilder(Items.LIGHT_GRAY_STAINED_GLASS_PANE)
                        .setName(new TextComponent(""))
                        .hideFlags()
        );

        public static AuctionItemDisplayElement of(GuiElementInterface element) {
            return new AuctionItemDisplayElement(element, null);
        }

        public static AuctionItemDisplayElement of(GuiElementBuilderInterface<?> element) {
            return new AuctionItemDisplayElement(element.build(), null);
        }

        public static AuctionItemDisplayElement filler() {
            return FILLER;
        }
    }
}