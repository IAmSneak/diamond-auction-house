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
                    Commands.literal(DiamondAuctionHouseConfig.getInstance().commandName).executes(AuctionHouseCommand::auctionhouseCommand)
                    Commands.literal(DiamondAuctionHouseConfig.getInstance().auctionCommandName).executes(AuctionHouseCommand::auctionhouseCommand)
            );
        } else {
            dispatcher.register(
                    Commands.literal(DiamondEconomyConfig.getInstance().commandName)
                            .then(
                                    Commands.literal(DiamondAuctionHouseConfig.getInstance().commandName).executes(AuctionHouseCommand::auctionhouseCommand)
                                    Commands.literal(DiamondAuctionHouseConfig.getInstance().auctionCommandName).executes(AuctionHouseCommand::auctionCommand)
                            )
            );
        }
    }

    private static int auctionhouseCommand(CommandContext<CommandSourceStack> objectCommandContext) {
        try {
            ServerPlayer player = objectCommandContext.getSource().getPlayerOrException();
            PagedGui gui = new PagedGui(player) {
                @Override
                protected DisplayElement getElement(int id) {
                    return null;
                }
            };

            for(int i = 0; i < 45; i++) {
                //todo: replace filler with correct auction item
                gui.setSlot(i, PagedGui.filler());
            }

            gui.setSlot(49, new GuiElementBuilder(Items.BARRIER, 1)
                                .setName(new TranslatableComponent("spectatorMenu.close").withStyle(ChatFormatting.WHITE))
                                .hideFlags()
                                .setCallback((x, y, z) -> gui.close()));

            gui.setSlot(48, PagedGui.previousPage());
            gui.setSlot(50, PagedGui.nextPage());

            gui.setTitle(new TextComponent("Auction House"));
            gui.open();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static int auctionCommand(CommandContext<CommandSourceStack> objectCommandContext) {
        return 0;
    }
}