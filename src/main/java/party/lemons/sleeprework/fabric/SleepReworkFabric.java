package party.lemons.sleeprework.fabric;

//? if fabric {
/*import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potion;
import party.lemons.sleeprework.SleepRework;
import party.lemons.sleeprework.command.TiredCommand;
import party.lemons.sleeprework.effect.SleepReworkPotions;
import party.lemons.sleeprework.handler.ServerHandler;
import party.lemons.sleeprework.network.TirednessPayload;

import java.util.function.Supplier;

public class SleepReworkFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SleepRework.init(FabricLoader.getInstance().getConfigDir());
        SleepReworkPotions.register(new SleepReworkPotions.Registrar() {
            @Override
            public Holder<MobEffect> effect(String name, Supplier<MobEffect> effect) {
                return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, SleepRework.id(name), effect.get());
            }

            @Override
            public Holder<Potion> potion(String name, Supplier<Potion> potion) {
                return Registry.registerForHolder(BuiltInRegistries.POTION, SleepRework.id(name), potion.get());
            }
        });
        FabricBrewingRecipeRegistryBuilder.BUILD.register(SleepReworkPotions::registerBrewing);

        PayloadTypeRegistry.playS2C().register(TirednessPayload.TYPE, TirednessPayload.CODEC);
        SleepRework.syncTiredness = (player, tiredness) -> {
            if (ServerPlayNetworking.canSend(player, TirednessPayload.TYPE)) {
                ServerPlayNetworking.send(player, new TirednessPayload(tiredness));
            }
        };

        ServerTickEvents.START_SERVER_TICK.register(ServerHandler::serverTick);
        ServerTickEvents.END_WORLD_TICK.register(ServerHandler::levelTick);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> ServerHandler.syncJoin(handler.getPlayer()));
        CommandRegistrationCallback.EVENT.register(TiredCommand::register);
        EntitySleepEvents.ALLOW_SLEEPING.register((player, sleepingPos) -> {
            if (player instanceof ServerPlayer serverPlayer && !ServerHandler.canPlayerSleep(serverPlayer)) {
                player.displayClientMessage(Component.translatable("sleeprework.sleep.not_tired"), true);
                return Player.BedSleepingProblem.NOT_POSSIBLE_HERE;
            }
            return null;
        });
        EntitySleepEvents.STOP_SLEEPING.register((entity, sleepingPos) -> {
            if (entity instanceof ServerPlayer player) ServerHandler.handleStopSleeping(player);
        });
    }
}
*///?}
