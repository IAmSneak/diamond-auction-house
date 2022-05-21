package com.gmail.sneakdevs.diamondauctionhouse;

import com.gmail.sneakdevs.diamondauctionhouse.auction.AuctionItem;
import com.gmail.sneakdevs.diamondauctionhouse.config.DiamondAuctionHouseConfig;
import com.gmail.sneakdevs.diamondauctionhouse.gui.AuctionHouseGui;
import com.gmail.sneakdevs.diamondeconomy.config.DiamondEconomyConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

public class AuctionHouseCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        if (!DiamondAuctionHouseConfig.getInstance().useBaseCommand || DiamondEconomyConfig.getInstance().commandName == null) {
            dispatcher.register(Commands.literal(DiamondAuctionHouseConfig.getInstance().auctionHouseCommandName).executes(AuctionHouseCommand::auctionhouseCommand));
            dispatcher.register(
                    Commands.literal(DiamondAuctionHouseConfig.getInstance().auctionCommandName)
                            .then(
                                    Commands.argument("price", IntegerArgumentType.integer(0)).executes(e -> {
                                        int price = IntegerArgumentType.getInteger(e, "price");
                                        return auctionCommand(e, price);
                                    })
                            )
            );
        } else {
            dispatcher.register(
                    Commands.literal(DiamondEconomyConfig.getInstance().commandName)
                            .then(
                                    Commands.literal(DiamondAuctionHouseConfig.getInstance().auctionHouseCommandName).executes(AuctionHouseCommand::auctionhouseCommand)
                            )
            );
            dispatcher.register(
                    Commands.literal(DiamondEconomyConfig.getInstance().commandName)
                            .then(
                                    Commands.literal(DiamondAuctionHouseConfig.getInstance().auctionCommandName)
                                            .then(
                                                    Commands.argument("price", IntegerArgumentType.integer(0)).executes(e -> {
                                                        int price = IntegerArgumentType.getInteger(e, "price");
                                                        return auctionCommand(e, price);
                                                    })
                                            )
                            )
            );
        }
    }

    private static int auctionhouseCommand(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        AuctionHouseGui gui = new AuctionHouseGui(ctx.getSource().getPlayerOrException());
        gui.updateDisplay();
        gui.setTitle(new TextComponent("Auction House"));
        gui.open();
        return 0;
    }

    private static int auctionCommand(CommandContext<CommandSourceStack> ctx, int price) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        if (player.getMainHandItem().isEmpty()) {
            ctx.getSource().sendSuccess(new TextComponent("You must be holding an item"), true);
            return 0;
        }
        //todo check if player has too many items on auction
        if (DiamondAuctionHouse.ah.addItem(new AuctionItem(DiamondAuctionHouse.getDatabaseManager().addItem(player.getStringUUID(), player.getName().getString(), price, DiamondAuctionHouseConfig.getInstance().auctionSeconds, player.getMainHandItem().getTag().getAsString()), player.getStringUUID(), player.getName().getString(), price, DiamondAuctionHouseConfig.getInstance().auctionSeconds, player.getMainHandItem()))) {
            player.getInventory().removeItem(player.getMainHandItem());
            ctx.getSource().sendSuccess(new TextComponent("Item successfully added to auction house for $" + price), true);
            return 0;
        }
        ctx.getSource().sendSuccess(new TextComponent("The auction house is full"), true);
        return 0;
    }
}