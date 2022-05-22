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

public class ExpiredItemsGui extends SimpleGui {
    public static final int PAGE_SIZE = 45; //9x5
    protected int page = 0;

    public <T extends ExpiredItemsGui> ExpiredItemsGui(ServerPlayer player) {
        super(MenuType.GENERIC_9x6, player, false);
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
                element = AuctionHouseDisplayElement.empty();
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
                navElement = AuctionHouseDisplayElement.EMPTY;
            }

            if (navElement.element != null) {
                this.setSlot(i + PAGE_SIZE, navElement.element);
            } else if (navElement.slot != null) {
                this.setSlotRedirect(i + PAGE_SIZE, navElement.slot);
            }
        }
    }

    protected AuctionHouseDisplayElement getNavElement(int id) {
        return switch (id) {
            case 3 -> AuctionHouseDisplayElement.previousPage(this);
            case 4 -> AuctionHouseDisplayElement.of(
                    new GuiElementBuilder(Items.BARRIER)
                            .setName(new TranslatableComponent("spectatorMenu.close").withStyle(ChatFormatting.RED))
                            .hideFlags()
                            .setCallback((x, y, z) -> {
                                playClickSound(this.player);
                                this.close();
                            })
            );
            case 5 -> AuctionHouseDisplayElement.nextPage(this);
            default -> AuctionHouseDisplayElement.filler();
        };
    }

    protected int getPage() {
        return this.page;
    }

    protected int getPageAmount() {
        return Math.min(DiamondsAuctionHouseConfig.getInstance().maxPages, DiamondsAuctionHouse.ah.size() / PAGE_SIZE + 1);
    }

    protected AuctionHouseDisplayElement getElement(int id) {
        final int id1 = page * PAGE_SIZE + id;
        //todo add expired lists
        if (id1 >= DiamondsAuctionHouse.ah.size()) {
            return null;
        }
        return AuctionHouseDisplayElement.of(
                GuiElementBuilder.from(DiamondsAuctionHouse.ah.getItem(id1).getItemStack())
                        .setCallback((x, y, z) -> {
                            playClickSound(this.player);
                            collectItem(DiamondsAuctionHouse.ah.getItem(id1));
                        }));
    }

    public void collectItem(AuctionItem item) {
        if (player.getInventory().getFreeSlot() != -1) {
            DiamondsAuctionHouse.getDatabaseManager().removeItemFromExpired(item);
            player.getInventory().add(item.getItemStack());
            updateDisplay();
        }
    }

    public static void playClickSound(ServerPlayer player) {
        player.playNotifySound(SoundEvents.UI_BUTTON_CLICK, SoundSource.MASTER, 1, 1);
    }

    public record AuctionHouseDisplayElement(@Nullable GuiElementInterface element, @Nullable Slot slot) {
        private static final AuctionHouseDisplayElement EMPTY = AuctionHouseDisplayElement.of(new GuiElement(ItemStack.EMPTY, GuiElementInterface.EMPTY_CALLBACK));
        private static final AuctionHouseDisplayElement FILLER = AuctionHouseDisplayElement.of(
                new GuiElementBuilder(Items.LIGHT_GRAY_STAINED_GLASS_PANE)
                        .setName(new TextComponent(""))
                        .hideFlags()
        );

        public static AuctionHouseDisplayElement of(GuiElementInterface element) {
            return new AuctionHouseDisplayElement(element, null);
        }

        public static AuctionHouseDisplayElement of(GuiElementBuilderInterface<?> element) {
            return new AuctionHouseDisplayElement(element.build(), null);
        }

        public static AuctionHouseDisplayElement nextPage(ExpiredItemsGui gui) {
            if (gui.canNextPage()) {
                return AuctionHouseDisplayElement.of(
                        new GuiElementBuilder(Items.PLAYER_HEAD)
                                .setName(new TranslatableComponent("spectatorMenu.next_page").withStyle(ChatFormatting.WHITE))
                                .hideFlags()
                                .setSkullOwner(GuiTextures.GUI_NEXT_PAGE)
                                .setCallback((x, y, z) -> {
                                    playClickSound(gui.player);
                                    gui.nextPage();
                                }));
            } else {
                return AuctionHouseDisplayElement.of(
                        new GuiElementBuilder(Items.PLAYER_HEAD)
                                .setName(new TranslatableComponent("spectatorMenu.next_page").withStyle(ChatFormatting.DARK_GRAY))
                                .hideFlags()
                                .setSkullOwner(GuiTextures.GUI_NEXT_PAGE_BLOCKED));
            }
        }

        public static AuctionHouseDisplayElement previousPage(ExpiredItemsGui gui) {
            if (gui.canPreviousPage()) {
                return AuctionHouseDisplayElement.of(
                        new GuiElementBuilder(Items.PLAYER_HEAD)
                                .setName(new TranslatableComponent("spectatorMenu.previous_page").withStyle(ChatFormatting.WHITE))
                                .hideFlags()
                                .setSkullOwner(GuiTextures.GUI_PREVIOUS_PAGE)
                                .setCallback((x, y, z) -> {
                                    playClickSound(gui.player);
                                    gui.previousPage();
                                }));
            } else {
                return AuctionHouseDisplayElement.of(
                        new GuiElementBuilder(Items.PLAYER_HEAD)
                                .setName(new TranslatableComponent("spectatorMenu.previous_page").withStyle(ChatFormatting.DARK_GRAY))
                                .hideFlags()
                                .setSkullOwner(GuiTextures.GUI_PREVIOUS_PAGE_BLOCKED));
            }
        }

        public static AuctionHouseDisplayElement filler() {
            return FILLER;
        }

        public static AuctionHouseDisplayElement empty() {
            return EMPTY;
        }
    }
}