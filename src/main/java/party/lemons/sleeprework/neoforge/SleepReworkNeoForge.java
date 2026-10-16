package party.lemons.sleeprework.neoforge;

//? if neoforge {
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import party.lemons.sleeprework.SleepRework;
import party.lemons.sleeprework.command.TiredCommand;
import party.lemons.sleeprework.effect.SleepReworkPotions;
import party.lemons.sleeprework.handler.ServerHandler;
import party.lemons.sleeprework.network.TirednessPayload;

import java.util.function.Supplier;

@Mod(SleepRework.MODID)
public class SleepReworkNeoForge {
    private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, SleepRework.MODID);
    private static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, SleepRework.MODID);

    public SleepReworkNeoForge(IEventBus modBus, Dist dist) {
        SleepRework.init(FMLPaths.CONFIGDIR.get());
        SleepReworkPotions.register(new SleepReworkPotions.Registrar() {
            @Override
            public Holder<MobEffect> effect(String name, Supplier<MobEffect> effect) {
                return MOB_EFFECTS.register(name, effect);
            }

            @Override
            public Holder<Potion> potion(String name, Supplier<Potion> potion) {
                return POTIONS.register(name, potion);
            }
        });
        MOB_EFFECTS.register(modBus);
        POTIONS.register(modBus);

        modBus.addListener((RegisterPayloadHandlersEvent event) -> event.registrar("1").optional()
            .playToClient(TirednessPayload.TYPE, TirednessPayload.CODEC, (payload, context) -> context.enqueueWork(payload::handleOnClient)));
        SleepRework.syncTiredness = (player, tiredness) -> {
            if (player.connection.hasChannel(TirednessPayload.TYPE)) {
                PacketDistributor.sendToPlayer(player, new TirednessPayload(tiredness));
            }
        };

        var bus = NeoForge.EVENT_BUS;
        bus.addListener((RegisterBrewingRecipesEvent event) -> SleepReworkPotions.registerBrewing(event.getBuilder()));
        bus.addListener((ServerTickEvent.Pre event) -> ServerHandler.serverTick(event.getServer()));
        bus.addListener((LevelTickEvent.Post event) -> {
            if (event.getLevel() instanceof ServerLevel level) ServerHandler.levelTick(level);
        });
        bus.addListener((PlayerEvent.PlayerLoggedInEvent event) -> {
            if (event.getEntity() instanceof ServerPlayer player) ServerHandler.syncJoin(player);
        });
        bus.addListener((RegisterCommandsEvent event) -> TiredCommand.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection()));
        bus.addListener((CanPlayerSleepEvent event) -> {
            if (event.getEntity() instanceof ServerPlayer player && !ServerHandler.canPlayerSleep(player)) {
                player.displayClientMessage(Component.translatable("sleeprework.sleep.not_tired"), true);
                event.setProblem(Player.BedSleepingProblem.NOT_POSSIBLE_HERE);
            }
        });
        bus.addListener((PlayerWakeUpEvent event) -> {
            if (event.getEntity() instanceof ServerPlayer player) ServerHandler.handleStopSleeping(player);
        });
        if (dist.isClient()) {
            SleepReworkNeoForgeClient.init();
        }
    }
}
//?}
