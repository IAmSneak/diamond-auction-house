package com.gmail.sneakdevs.diamondsauctionhouse;

import com.gmail.sneakdevs.diamondsauctionhouse.auction.AuctionItem;
import com.gmail.sneakdevs.diamondsauctionhouse.config.DiamondsAuctionHouseConfig;
import com.gmail.sneakdevs.diamondsauctionhouse.gui.AuctionHouseGui;
import com.gmail.sneakdevs.diamondeconomy.config.DiamondEconomyConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

public class AuctionHouseCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        if (!DiamondsAuctionHouseConfig.getInstance().useBaseCommand || DiamondEconomyConfig.getInstance().commandName == null) {
            dispatcher.register(Commands.literal(DiamondsAuctionHouseConfig.getInstance().auctionHouseCommandName).executes(AuctionHouseCommand::auctionhouseCommand));
            dispatcher.register(
                    Commands.literal(DiamondsAuctionHouseConfig.getInstance().auctionCommandName)
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
                                    Commands.literal(DiamondsAuctionHouseConfig.getInstance().auctionHouseCommandName).executes(AuctionHouseCommand::auctionhouseCommand)
                            )
            );
            dispatcher.register(
                    Commands.literal(DiamondEconomyConfig.getInstance().commandName)
                            .then(
                                    Commands.literal(DiamondsAuctionHouseConfig.getInstance().auctionCommandName)
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
        if (DiamondsAuctionHouse.ah.canAddItem()) {
            CompoundTag tag = player.getMainHandItem().getOrCreateTag();
            DiamondsAuctionHouse.ah.addItem(new AuctionItem(DiamondsAuctionHouse.getDatabaseManager().addItemToAuction(player.getStringUUID(), player.getName().getString(), tag.getAsString(), String.valueOf(Registry.ITEM.getKey(player.getMainHandItem().getItem())), player.getMainHandItem().getCount(), price, DiamondsAuctionHouseConfig.getInstance().auctionSeconds), player.getStringUUID(), player.getName().getString(), player.getMainHandItem(), price, DiamondsAuctionHouseConfig.getInstance().auctionSeconds));
            player.getInventory().removeItem(player.getMainHandItem());
            ctx.getSource().sendSuccess(new TextComponent("Item successfully added to auction house for $" + price), true);
            return 0;
        }
        ctx.getSource().sendSuccess(new TextComponent("The auction house is full"), true);
        return 0;
    }
}