package com.gmail.sneakdevs.diamondauctionhouse;

import com.gmail.sneakdevs.diamondauctionhouse.config.DiamondAuctionHouseConfig;
import com.gmail.sneakdevs.diamondauctionhouse.gui.PagedGui;
import com.gmail.sneakdevs.diamondeconomy.config.DiamondEconomyConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Items;

public class AuctionHouseCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        if (DiamondEconomyConfig.getInstance().commandName != null) {
            dispatcher.register(
                    Commands.literal(DiamondAuctionHouseConfig.getInstance().commandName)
                            .executes(AuctionHouseCommand::auctionhouseCommand)
            );
        } else {
            dispatcher.register(
                    Commands.literal(DiamondEconomyConfig.getInstance().commandName)
                            .then(
                                    Commands.literal(DiamondAuctionHouseConfig.getInstance().commandName)
                                            .executes(AuctionHouseCommand::auctionhouseCommand)
                            )
            );
        }
    }

    private static int auctionhouseCommand(CommandContext<CommandSourceStack> objectCommandContext) {
        try {
            ServerPlayer player = objectCommandContext.getSource().getPlayerOrException();
            PagedGui gui = new PagedGui(player, PagedGui::onClose) {
                @Override
                protected int getPageAmount() {
                    return DiamondAuctionHouseConfig.getInstance().maxPages;
                }

                @Override
                protected DisplayElement getElement(int id) {
                    return null;
                }
            };

            //gui.setSlot(53, new GuiElementBuilder(Items.BARRIER, 1).setCallback((x, y, z) -> gui.close()));

            gui.setTitle(new TextComponent("Auction House"));
            gui.open();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}