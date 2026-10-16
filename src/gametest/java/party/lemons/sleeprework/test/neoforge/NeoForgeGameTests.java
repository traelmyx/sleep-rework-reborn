package party.lemons.sleeprework.test.neoforge;

//? if neoforge {
import party.lemons.sleeprework.test.ClientSmoke;
import party.lemons.sleeprework.test.SleepTests;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(NeoForgeGameTests.MOD_ID)
@PrefixGameTestTemplate(false)
@Mod(NeoForgeGameTests.MOD_ID)
public class NeoForgeGameTests {
    public static final String MOD_ID = "sleeprework_gametest";

    public NeoForgeGameTests(Dist dist) {
        if (dist.isClient() && ClientSmoke.start()) {
            NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post e) -> ClientSmoke.tick(net.minecraft.client.Minecraft.getInstance()));
        }
    }

    @GameTest(template = "empty")
    public static void tirednessGrowsAndPotionsChangeIt(GameTestHelper helper) {
        SleepTests.tirednessGrowsAndPotionsChangeIt(helper);
    }

    @GameTest(template = "empty", batch = "sleep")
    public static void onlyTiredPlayersCanSleep(GameTestHelper helper) {
        SleepTests.onlyTiredPlayersCanSleep(helper);
    }

    @GameTest(template = "empty")
    public static void wakingUpRestsAndHeals(GameTestHelper helper) {
        SleepTests.wakingUpRestsAndHeals(helper);
    }

    @GameTest(template = "empty")
    public static void potionsBrew(GameTestHelper helper) {
        SleepTests.potionsBrew(helper);
    }

    @GameTest(template = "empty")
    public static void phantomsFollowTiredness(GameTestHelper helper) {
        SleepTests.phantomsFollowTiredness(helper);
    }

    @GameTest(template = "empty")
    public static void tirednessIsSavedAndCommandWorks(GameTestHelper helper) {
        SleepTests.tirednessIsSavedAndCommandWorks(helper);
    }
}
//?}
