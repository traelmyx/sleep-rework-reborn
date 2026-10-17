package party.lemons.sleeprework.test;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BedPart;
//? if <1.21.11 {
import net.minecraft.world.level.levelgen.PhantomSpawner;
//?}
import net.minecraft.world.phys.Vec3;
import party.lemons.sleeprework.SleepRework;
import party.lemons.sleeprework.effect.SleepReworkPotions;
import party.lemons.sleeprework.handler.ReworkedPhantomSpawner;
import party.lemons.sleeprework.handler.ServerHandler;
import party.lemons.sleeprework.handler.ServerPlayerSleepData;
import party.lemons.sleeprework.handler.SleepDataHolder;

public final class SleepTests {
    private SleepTests() {
    }

    private static void place(ServerPlayer player, Vec3 at) {
        //? if >=1.21.5 {
        /*player.snapTo(at.x, at.y, at.z);
        *///?} else {
        player.moveTo(at.x, at.y, at.z);
        //?}
    }

    private static net.minecraft.world.level.block.state.BlockState redBed() {
        //? if >=26 {
        /*return Blocks.BED.red().defaultBlockState();
        *///?} else {
        return Blocks.RED_BED.defaultBlockState();
        //?}
    }

    private static void command(GameTestHelper helper, String command) {
        var server = helper.getLevel().getServer();
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack().withSuppressedOutput(), command);
    }

    private static ServerPlayerSleepData data(ServerPlayer player) {
        return ((SleepDataHolder) player).getSleepData();
    }

    private static boolean close(float a, float b) {
        return Math.abs(a - b) < 0.0001f;
    }

    public static void tirednessGrowsAndPotionsChangeIt(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        float base = SleepRework.CONFIG.playerConfig().tirednessIncreasePerMinute();
        ServerHandler.setPlayerTiredness(player, 0);
        data(player).increaseTiredness(player);
        helper.assertTrue(close(ServerHandler.getPlayerTiredness(player), base), "one minute added " + ServerHandler.getPlayerTiredness(player));
        player.addEffect(new MobEffectInstance(SleepReworkPotions.DROWSINESS, 200));
        helper.assertTrue(close(ServerHandler.getPlayerTirednessIncrease(player), base * 2), "drowsiness rate " + ServerHandler.getPlayerTirednessIncrease(player));
        player.removeAllEffects();
        player.addEffect(new MobEffectInstance(SleepReworkPotions.LIVELINESS, 200));
        helper.assertTrue(close(ServerHandler.getPlayerTirednessIncrease(player), base * 0.5f), "liveliness rate " + ServerHandler.getPlayerTirednessIncrease(player));
        helper.succeed();
    }

    public static void onlyTiredPlayersCanSleep(GameTestHelper helper) {
        // "is it night" follows the sky darkness, which only updates on the next tick
        command(helper, "time set 18000");
        BlockPos foot = new BlockPos(1, 2, 1);
        helper.setBlock(foot, redBed().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH).setValue(BlockStateProperties.BED_PART, BedPart.FOOT));
        helper.setBlock(foot.south(), redBed().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH).setValue(BlockStateProperties.BED_PART, BedPart.HEAD));
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        place(player, helper.absoluteVec(new Vec3(2.5, 2, 2.5)));
        helper.runAfterDelay(3, () -> {
            ServerHandler.setPlayerTiredness(player, 0);
            helper.useBlock(foot.south(), player);
            helper.assertFalse(player.isSleeping(), "a rested player fell asleep");
            ServerHandler.setPlayerTiredness(player, SleepRework.CONFIG.playerConfig().minSleepLevel() + 0.1f);
            helper.useBlock(foot.south(), player);
            helper.assertTrue(player.isSleeping(), "a tired player could not sleep");
            player.stopSleeping();
            command(helper, "time set 1000");
            helper.succeed();
        });
    }

    public static void wakingUpRestsAndHeals(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        ServerHandler.setPlayerTiredness(player, 2.5f);
        ServerHandler.handleWakeUp(player);
        helper.assertTrue(close(ServerHandler.getPlayerTiredness(player), 0), "still tired after waking: " + ServerHandler.getPlayerTiredness(player));
        helper.assertTrue(player.hasEffect(MobEffects.REGENERATION), "no wake-up regeneration");
        helper.succeed();
    }

    public static void potionsBrew(GameTestHelper helper) {
        //? if >=26.3 {
        /*// from 26.3 the mixes are recipes
        var recipes = helper.getLevel().getServer().getRecipeManager();
        for (String id : new String[] {"potion_awkward_honeycomb", "potion_liveliness_fermented_spider_eye", "splash_potion_drowsiness_glowstone_dust"}) {
            var key = net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.RECIPE, SleepRework.id("brewing/" + id));
            helper.assertTrue(recipes.byKey(key).isPresent(), "missing brewing recipe " + id);
        }
        helper.succeed();
        *///?} else {

        var brewing = helper.getLevel().potionBrewing();
        ItemStack awkward = PotionContents.createItemStack(Items.POTION, Potions.AWKWARD);
        ItemStack honeycomb = new ItemStack(SleepRework.CONFIG.potionConfig().livelinessBrewingItem());
        helper.assertTrue(brewing.hasMix(awkward, honeycomb), "awkward potion + honeycomb has no recipe");
        ItemStack liveliness = brewing.mix(honeycomb, awkward);
        helper.assertTrue(liveliness.getOrDefault(net.minecraft.core.component.DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(SleepReworkPotions.LIVELINESS_POTION), "brewed " + liveliness);
        ItemStack drowsy = brewing.mix(new ItemStack(Items.FERMENTED_SPIDER_EYE), liveliness);
        helper.assertTrue(drowsy.getOrDefault(net.minecraft.core.component.DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(SleepReworkPotions.DROWSINESS_POTION), "corrupted to " + drowsy);
        helper.succeed();
        //?}
    }

    public static void phantomsFollowTiredness(GameTestHelper helper) {
        //? if <1.21.11 {
        helper.assertTrue(new PhantomSpawner().tick(helper.getLevel(), true, true) == 0, "the vanilla phantom spawner still runs");
        //?}
        ReworkedPhantomSpawner spawner = new ReworkedPhantomSpawner();
        int count = spawner.getSpawnCount(SleepRework.CONFIG.playerConfig().maxTiredness(), helper.getLevel().getRandom());
        helper.assertTrue(count >= 1, "a fully tired player would get " + count + " phantoms");
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        ServerHandler.setPlayerTiredness(player, 0);
        helper.assertFalse(data(player).doesSpawnPhantoms(), "a rested player attracts phantoms");
        ServerHandler.setPlayerTiredness(player, SleepRework.CONFIG.phantomConfig().phantomSpawnTiredness() + 0.5f);
        helper.assertTrue(data(player).doesSpawnPhantoms(), "a very tired player does not attract phantoms");
        helper.succeed();
    }

    public static void tirednessIsSavedAndCommandWorks(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        var server = helper.getLevel().getServer();
        // every mock player has the same name, so pick this one by where it stands
        place(player, helper.absoluteVec(new Vec3(0.5, 2, 0.5)));
        String command = String.format(java.util.Locale.ROOT, "tiredness @a[x=%.2f,y=%.2f,z=%.2f,distance=..0.1,limit=1] 150 set", player.getX(), player.getY(), player.getZ());
        try {
            int result = server.getCommands().getDispatcher().execute(command, server.createCommandSourceStack().withSuppressedOutput());
            helper.assertTrue(result == 1, command + " returned " + result);
        } catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) {
            helper.fail(command + " failed: " + e.getMessage());
        }
        helper.assertTrue(close(ServerHandler.getPlayerTiredness(player), 1.5f), "/tiredness set gave " + ServerHandler.getPlayerTiredness(player));
        //? if >=1.21.6 {
        /*var output = net.minecraft.world.level.storage.TagValueOutput.createWithContext(net.minecraft.util.ProblemReporter.DISCARDING, helper.getLevel().registryAccess());
        player.saveWithoutId(output);
        CompoundTag tag = output.buildResult();
        ServerHandler.setPlayerTiredness(player, 0);
        player.load(net.minecraft.world.level.storage.TagValueInput.create(net.minecraft.util.ProblemReporter.DISCARDING, helper.getLevel().registryAccess(), tag));
        *///?} else {
        CompoundTag tag = new CompoundTag();
        player.addAdditionalSaveData(tag);
        ServerHandler.setPlayerTiredness(player, 0);
        player.readAdditionalSaveData(tag);
        //?}
        helper.assertTrue(close(ServerHandler.getPlayerTiredness(player), 1.5f), "tiredness not restored from save: " + ServerHandler.getPlayerTiredness(player));
        helper.succeed();
    }
}
