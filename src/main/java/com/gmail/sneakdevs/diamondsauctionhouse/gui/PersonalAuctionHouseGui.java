/*
Copyright (c) 2020 DeatHunter

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

// This is heavily based off of PagedGui from the Polymer Port of Fabric Waystones

package com.gmail.sneakdevs.diamondsauctionhouse.gui;

import com.gmail.sneakdevs.diamondsauctionhouse.DiamondsAuctionHouse;
import com.gmail.sneakdevs.diamondsauctionhouse.auction.AuctionHouse;
import com.gmail.sneakdevs.diamondsauctionhouse.auction.AuctionItem;
import com.gmail.sneakdevs.diamondsauctionhouse.config.DiamondsAuctionHouseConfig;
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

public class PersonalAuctionHouseGui extends SimpleGui {
    public static final int PAGE_SIZE = 45; //9x5
    protected int page = 0;
    private int ticker = 0;
    private AuctionHouse ah;

    public <T extends PersonalAuctionHouseGui> PersonalAuctionHouseGui(ServerPlayer player) {
        super(MenuType.GENERIC_9x6, player, false);
        ah = DiamondsAuctionHouse.ah.getPlayerAuctionHouse(player.getStringUUID());
    }

    protected void nextPage() {
        this.page = Math.min(this.getPageAmount() - 1, this.page + 1);
        this.updateDisplay();
    }

    protected boolean canNextPage() {
        return this.getPageAmount() > this.page + 1;
    }

    protected void previousPage() {
        this.page = Math.max(0, this.page - 1);
        this.updateDisplay();
    }

    protected boolean canPreviousPage() {
        return this.page - 1 >= 0;
    }

    public void updateDisplay() {
        for (int i = 0; i < PAGE_SIZE; i++) {
            var element = this.getElement(i);

            if (element == null) {
                element = DisplayElement.empty();
            }

            if (element.element() != null) {
                this.setSlot(i, element.element());
            } else if (element.slot() != null) {
                this.setSlotRedirect(i, element.slot());
            }
        }

        for (int i = 0; i < 9; i++) {
            var navElement = this.getNavElement(i);

            if (navElement == null) {
                navElement = DisplayElement.EMPTY;
            }

            if (navElement.element != null) {
                this.setSlot(i + PAGE_SIZE, navElement.element);
            } else if (navElement.slot != null) {
                this.setSlotRedirect(i + PAGE_SIZE, navElement.slot);
            }
        }
    }

    protected DisplayElement getNavElement(int id) {
        return switch (id) {
            case 0 -> DisplayElement.of(
                    new GuiElementBuilder(Items.RED_CONCRETE)
                            .setName(new TextComponent("Back").withStyle(ChatFormatting.RED))
                            .hideFlags()
                            .setCallback((x, y, z) -> {
                                playClickSound(this.player);
                                openPublic();
                            }));
            case 3 -> DisplayElement.previousPage(this);
            case 4 -> DisplayElement.of(
                    new GuiElementBuilder(Items.BARRIER)
                            .setName(new TranslatableComponent("spectatorMenu.close").withStyle(ChatFormatting.RED))
                            .hideFlags()
                            .setCallback((x, y, z) -> {
                                playClickSound(this.player);
                                this.close();
                            })
            );
            case 5 -> DisplayElement.nextPage(this);
            case 8 -> DisplayElement.of(
                    new GuiElementBuilder(Items.HOPPER)
                            .setName(new TextComponent("Expired Items").withStyle(ChatFormatting.RED))
                            .hideFlags()
                            .setCallback((x, y, z) -> {
                                playClickSound(this.player);
                                this.openExpiredGui();
                            })
            );

            default -> DisplayElement.filler();
        };
    }

    protected int getPageAmount() {
        return Math.min(DiamondsAuctionHouseConfig.getInstance().maxPages, ah.size() / PAGE_SIZE + 1);
    }

    private void openPublic() {
        this.close();
        AuctionHouseGui gui = new AuctionHouseGui(player);
        gui.updateDisplay();
        gui.setTitle(new TextComponent("Auction House"));
        gui.open();
    }

    protected DisplayElement getElement(int id) {
        final int id1 = page * PAGE_SIZE + id;
        if (id1 >= ah.size()) {
            return null;
        }
        AuctionItem ai = ah.getItem(id1);
        return DisplayElement.of(
                GuiElementBuilder.from(ai.getItemStack())
                        .addLoreLine(new TextComponent(ai.getTimeLeft()).withStyle(ChatFormatting.DARK_PURPLE))
                        .addLoreLine(new TextComponent("$" + ai.getPrice()).withStyle(ChatFormatting.DARK_PURPLE))
                        .setCallback((x, y, z) -> {
                            playClickSound(this.player);
                            openItemGui(ah.getItem(id1));
                        }));
    }

    @Override
    public void onTick() {
        ticker++;
        if (ticker >= 20) {
            ticker = 0;
            ah = DiamondsAuctionHouse.ah.getPlayerAuctionHouse(player.getStringUUID());
            updateDisplay();
        }

        super.onTick();
    }

    private void openItemGui(AuctionItem item) {
        this.close();
        AuctionItemGui gui = new AuctionItemGui(player, item);
        gui.updateDisplay();
        gui.setTitle(new TextComponent("Buy"));
        gui.open();
    }

    private void openExpiredGui() {
        this.close();
        ExpiredItemsGui gui = new ExpiredItemsGui(player);
        gui.updateDisplay();
        gui.setTitle(new TextComponent("Expired Items"));
        gui.open();
    }

    private static void playClickSound(ServerPlayer player) {
        player.playNotifySound(SoundEvents.UI_BUTTON_CLICK, SoundSource.MASTER, 1, 1);
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

        public static DisplayElement nextPage(PersonalAuctionHouseGui gui) {
            if (gui.canNextPage()) {
                return DisplayElement.of(
                        new GuiElementBuilder(Items.PLAYER_HEAD)
                                .setName(new TranslatableComponent("spectatorMenu.next_page").withStyle(ChatFormatting.WHITE))
                                .hideFlags()
                                .setSkullOwner(GuiTextures.GUI_NEXT_PAGE)
                                .setCallback((x, y, z) -> {
                                    playClickSound(gui.player);
                                    gui.nextPage();
                                }));
            } else {
                return DisplayElement.of(
                        new GuiElementBuilder(Items.PLAYER_HEAD)
                                .setName(new TranslatableComponent("spectatorMenu.next_page").withStyle(ChatFormatting.DARK_GRAY))
                                .hideFlags()
                                .setSkullOwner(GuiTextures.GUI_NEXT_PAGE_BLOCKED));
            }
        }

        public static DisplayElement previousPage(PersonalAuctionHouseGui gui) {
            if (gui.canPreviousPage()) {
                return DisplayElement.of(
                        new GuiElementBuilder(Items.PLAYER_HEAD)
                                .setName(new TranslatableComponent("spectatorMenu.previous_page").withStyle(ChatFormatting.WHITE))
                                .hideFlags()
                                .setSkullOwner(GuiTextures.GUI_PREVIOUS_PAGE)
                                .setCallback((x, y, z) -> {
                                    playClickSound(gui.player);
                                    gui.previousPage();
                                }));
            } else {
                return DisplayElement.of(
                        new GuiElementBuilder(Items.PLAYER_HEAD)
                                .setName(new TranslatableComponent("spectatorMenu.previous_page").withStyle(ChatFormatting.DARK_GRAY))
                                .hideFlags()
                                .setSkullOwner(GuiTextures.GUI_PREVIOUS_PAGE_BLOCKED));
            }
        }

        public static DisplayElement filler() {
            return FILLER;
        }

        public static DisplayElement empty() {
            return EMPTY;
        }
    }
}