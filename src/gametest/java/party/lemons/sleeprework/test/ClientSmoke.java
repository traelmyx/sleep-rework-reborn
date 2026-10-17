package party.lemons.sleeprework.test;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.level.GameType;

import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import party.lemons.sleeprework.SleepRework;
import party.lemons.sleeprework.client.SleepDisplayWidget;
import party.lemons.sleeprework.handler.ServerHandler;

import java.nio.file.Files;

// Run with -Dsmoketest=true: creates a flat world, takes a screenshot and quits. Exit code 1 on failure.
public final class ClientSmoke {

    public static final boolean ENABLED = Boolean.getBoolean("smoketest");
    private static final long TIMEOUT_MS = 5 * 60 * 1000;

    private static int stage;
    private static int ticks;

    private ClientSmoke() {
    }

    public static boolean start() {
        if (!ENABLED) {
            return false;
        }
        Thread watchdog = new Thread(() -> {
            try {
                Thread.sleep(TIMEOUT_MS);
                fail("timed out");
            } catch (InterruptedException ignored) {
            }
        }, "smoketest-watchdog");
        watchdog.setDaemon(true);
        watchdog.start();
        return true;
    }

    public static void tick(Minecraft mc) {
        try {
            switch (stage) {
                case 0 -> {
                    Screen screen = SmokeCompat.screen(mc);
                    if (screen != null && SmokeCompat.overlayHidden(mc) && ++ticks >= 20) {
                        if (screen.getClass().getSimpleName().equals("LoadingErrorScreen")) {
                            fail("mod loading errors");
                        }
                        ticks = 0;
                        mc.options.tutorialStep = net.minecraft.client.tutorial.TutorialSteps.NONE;
                        SmokeCompat.createFlatWorld(mc, GameType.SURVIVAL);
                        stage = 1;
                    }
                }
                case 1 -> {
                    if (mc.level != null && mc.player != null && SmokeCompat.screen(mc) == null) {
                        stage = 2;
                    }
                }
                case 2 -> {
                    ticks++;
                    if (ticks == 40) {
                        var server = mc.getSingleplayerServer();
                        server.execute(() -> ServerHandler.setPlayerTiredness(server.getPlayerList().getPlayers().get(0), 2.0f));
                    }
                    if (ticks == 70) {
                        SmokeCompat.setScreen(mc, new InventoryScreen(mc.player));
                    }
                    if (ticks == 90) {
                        // typed as the vanilla class so the calls are remapped in the production Fabric run
                        boolean icon = SmokeCompat.screen(mc).children().stream().anyMatch(c -> c instanceof SleepDisplayWidget);
                        if (!icon) fail("no tiredness icon on the inventory screen");
                    }
                    if (ticks == 120) {
                        SmokeCompat.screenshot(mc, "tiredness.png");
                    }
                    if (ticks >= 130) {
                        float seen = SleepRework.localSleepData.getTiredness();
                        if (Math.abs(seen - 2.0f) > 0.01f) fail("client sees tiredness " + seen);
                        SmokeCompat.setScreen(mc, null);
                        Files.writeString(mc.gameDirectory.toPath().resolve("smoke-ok"), "ok\n");
                        stage = 3;
                        mc.stop();
                    }
                }
                default -> {
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            fail(t.toString());
        }
    }

    public static void fail(String why) {
        System.err.println("Smoke test failed: " + why);
        System.out.println("[clientsmoke] FAILED: " + why);
        System.err.flush();
        Runtime.getRuntime().halt(1);
    }
}
