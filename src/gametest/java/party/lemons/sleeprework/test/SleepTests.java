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
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.levelgen.PhantomSpawner;
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
        helper.getLevel().setDayTime(18000);
        BlockPos foot = new BlockPos(1, 2, 1);
        helper.setBlock(foot, Blocks.RED_BED.defaultBlockState().setValue(BedBlock.FACING, Direction.SOUTH).setValue(BedBlock.PART, BedPart.FOOT));
        helper.setBlock(foot.south(), Blocks.RED_BED.defaultBlockState().setValue(BedBlock.FACING, Direction.SOUTH).setValue(BedBlock.PART, BedPart.HEAD));
        BlockPos head = helper.absolutePos(foot.south());
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        player.moveTo(helper.absoluteVec(new Vec3(2.5, 2, 2.5)));
        ServerHandler.setPlayerTiredness(player, 0);
        var rested = player.startSleepInBed(head);
        helper.assertTrue(rested.left().isPresent(), "a rested player fell asleep");
        helper.assertFalse(player.isSleeping(), "rested player is sleeping");
        ServerHandler.setPlayerTiredness(player, SleepRework.CONFIG.playerConfig().minSleepLevel() + 0.1f);
        var tired = player.startSleepInBed(head);
        helper.assertTrue(tired.right().isPresent() && player.isSleeping(), "a tired player could not sleep: " + tired.left().orElse(null));
        player.stopSleeping();
        helper.getLevel().setDayTime(1000);
        helper.succeed();
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
        var brewing = helper.getLevel().potionBrewing();
        ItemStack awkward = PotionContents.createItemStack(Items.POTION, Potions.AWKWARD);
        ItemStack honeycomb = new ItemStack(SleepRework.CONFIG.potionConfig().livelinessBrewingItem());
        helper.assertTrue(brewing.hasMix(awkward, honeycomb), "awkward potion + honeycomb has no recipe");
        ItemStack liveliness = brewing.mix(honeycomb, awkward);
        helper.assertTrue(liveliness.getOrDefault(net.minecraft.core.component.DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(SleepReworkPotions.LIVELINESS_POTION), "brewed " + liveliness);
        ItemStack drowsy = brewing.mix(new ItemStack(Items.FERMENTED_SPIDER_EYE), liveliness);
        helper.assertTrue(drowsy.getOrDefault(net.minecraft.core.component.DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(SleepReworkPotions.DROWSINESS_POTION), "corrupted to " + drowsy);
        helper.succeed();
    }

    public static void phantomsFollowTiredness(GameTestHelper helper) {
        helper.assertTrue(new PhantomSpawner().tick(helper.getLevel(), true, true) == 0, "the vanilla phantom spawner still runs");
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
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack().withSuppressedOutput(), "tiredness " + player.getGameProfile().getName() + " 150 set");
        helper.assertTrue(close(ServerHandler.getPlayerTiredness(player), 1.5f), "/tiredness set gave " + ServerHandler.getPlayerTiredness(player));
        CompoundTag tag = new CompoundTag();
        player.addAdditionalSaveData(tag);
        ServerHandler.setPlayerTiredness(player, 0);
        player.readAdditionalSaveData(tag);
        helper.assertTrue(close(ServerHandler.getPlayerTiredness(player), 1.5f), "tiredness not restored from save: " + ServerHandler.getPlayerTiredness(player));
        helper.succeed();
    }
}
