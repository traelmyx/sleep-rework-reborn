package party.lemons.sleeprework.test;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;

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
                    Screen screen = mc.screen;
                    if (screen != null && mc.getOverlay() == null && ++ticks >= 20) {
                        if (screen.getClass().getSimpleName().equals("LoadingErrorScreen")) {
                            fail("mod loading errors");
                        }
                        ticks = 0;
                        mc.options.tutorialStep = net.minecraft.client.tutorial.TutorialSteps.NONE;
                        createWorld(mc);
                        stage = 1;
                    }
                }
                case 1 -> {
                    if (mc.level != null && mc.player != null && mc.screen == null) {
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
                        mc.setScreen(new InventoryScreen(mc.player));
                    }
                    if (ticks == 90) {
                        // hover the tiredness icon so its tooltip shows in the screenshot
                        var widget = mc.screen.children().stream().filter(c -> c instanceof SleepDisplayWidget).map(c -> (SleepDisplayWidget) c).findFirst().orElse(null);
                        if (widget == null) fail("no tiredness icon on the inventory screen");
                        double scale = mc.getWindow().getGuiScale();
                        org.lwjgl.glfw.GLFW.glfwSetCursorPos(mc.getWindow().getWindow(), (widget.getX() + 6) * scale, (widget.getY() + 6) * scale);
                    }
                    if (ticks == 120) {
                        Screenshot.grab(mc.gameDirectory, "tiredness.png", mc.getMainRenderTarget(), msg -> {});
                    }
                    if (ticks >= 130) {
                        float seen = SleepRework.localSleepData.getTiredness();
                        if (Math.abs(seen - 2.0f) > 0.01f) fail("client sees tiredness " + seen);
                        mc.setScreen(null);
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

    private static void createWorld(Minecraft mc) {
        LevelSettings settings = new LevelSettings("smoke", GameType.SURVIVAL, false, Difficulty.PEACEFUL, true, new GameRules(), WorldDataConfiguration.DEFAULT);
        mc.createWorldOpenFlows().createFreshLevel("smoke", settings, new WorldOptions(0L, false, false),
            registries -> registries.registryOrThrow(Registries.WORLD_PRESET).getHolderOrThrow(WorldPresets.FLAT).value().createWorldDimensions(),
            mc.screen);
    }

    public static void fail(String why) {
        System.err.println("Smoke test failed: " + why);
        System.out.println("[clientsmoke] FAILED: " + why);
        System.err.flush();
        Runtime.getRuntime().halt(1);
    }
}
