package com.gmail.sneakdevs.diamondsauctionhouse.mixin;

import com.gmail.sneakdevs.diamondsauctionhouse.DiamondsAuctionHouse;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin_DiamondsAuctionHouse {
    @Inject(method = "tickServer", at = @At("TAIL"))
    private void DiamondAuctionHouse_tickServerMixin(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        DiamondsAuctionHouse.ah.tick();
    }
}
