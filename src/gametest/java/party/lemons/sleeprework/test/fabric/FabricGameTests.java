package party.lemons.sleeprework.test.fabric;

//? if fabric {
/*import party.lemons.sleeprework.test.SleepTests;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

public class FabricGameTests implements FabricGameTest {
    private static final String EMPTY = "sleeprework_gametest:empty";

    @GameTest(template = EMPTY)
    public void tirednessGrowsAndPotionsChangeIt(GameTestHelper helper) {
        SleepTests.tirednessGrowsAndPotionsChangeIt(helper);
    }

    @GameTest(template = EMPTY, batch = "sleep")
    public void onlyTiredPlayersCanSleep(GameTestHelper helper) {
        SleepTests.onlyTiredPlayersCanSleep(helper);
    }

    @GameTest(template = EMPTY)
    public void wakingUpRestsAndHeals(GameTestHelper helper) {
        SleepTests.wakingUpRestsAndHeals(helper);
    }

    @GameTest(template = EMPTY)
    public void potionsBrew(GameTestHelper helper) {
        SleepTests.potionsBrew(helper);
    }

    @GameTest(template = EMPTY)
    public void phantomsFollowTiredness(GameTestHelper helper) {
        SleepTests.phantomsFollowTiredness(helper);
    }

    @GameTest(template = EMPTY)
    public void tirednessIsSavedAndCommandWorks(GameTestHelper helper) {
        SleepTests.tirednessIsSavedAndCommandWorks(helper);
    }
}
*///?}
