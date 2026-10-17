package party.lemons.sleeprework.util;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
//? if >=26 {
/*import net.minecraft.world.entity.EntityTypes;
*///?}
//? if >=1.21.11 {
/*import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.gamerules.GameRules;
*///?} else {
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.GameRules;
//?}

// Version differences in one place, so the mod code reads the same on every Minecraft version.
public final class Compat {
    private Compat() {
    }

    /** The action-bar message under the hotbar. */
    public static void overlay(Player player, Component message) {
        //? if >=26 {
        /*player.sendOverlayMessage(message);
        *///?} else {
        player.displayClientMessage(message, true);
        //?}
    }

    /** Whether the insomnia/phantom game rule is on. */
    public static boolean phantomRule(ServerLevel level) {
        //? if >=1.21.11 {
        /*return level.getGameRules().get(GameRules.SPAWN_PHANTOMS);
        *///?} else {
        return level.getGameRules().getBoolean(GameRules.RULE_DOINSOMNIA);
        //?}
    }

    /** The answer a rested player gets from the bed. From 1.21.11 the problem carries its own message. */
    public static Player.BedSleepingProblem notTired() {
        //? if >=1.21.11 {
        /*return new Player.BedSleepingProblem(Component.translatable("sleeprework.sleep.not_tired"));
        *///?} else {
        return Player.BedSleepingProblem.NOT_POSSIBLE_HERE;
        //?}
    }

    /** Whether notTired() already shows its own message. */
    public static boolean problemHasMessage() {
        //? if >=1.21.11 {
        /*return true;
        *///?} else {
        return false;
        //?}
    }

    public static EntityType<Phantom> phantomType() {
        //? if >=26 {
        /*return EntityTypes.PHANTOM;
        *///?} else {
        return EntityType.PHANTOM;
        //?}
    }

    public static Phantom spawnPhantom(ServerLevel level, net.minecraft.core.BlockPos pos, DifficultyInstance difficulty, SpawnGroupData[] group) {
        //? if >=1.21.11 {
        /*Phantom phantom = phantomType().create(level, EntitySpawnReason.NATURAL);
        if (phantom == null) return null;
        phantom.snapTo(pos, 0.0F, 0.0F);
        group[0] = phantom.finalizeSpawn(level, difficulty, EntitySpawnReason.NATURAL, group[0]);
        *///?} else {
        Phantom phantom = phantomType().create(level);
        if (phantom == null) return null;
        phantom.moveTo(pos, 0.0F, 0.0F);
        group[0] = phantom.finalizeSpawn(level, difficulty, MobSpawnType.NATURAL, group[0]);
        //?}
        return phantom;
    }
}
