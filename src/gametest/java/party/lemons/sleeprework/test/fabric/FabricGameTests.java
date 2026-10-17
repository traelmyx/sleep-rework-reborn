package party.lemons.sleeprework.test.fabric;

//? if fabric {
/*import party.lemons.sleeprework.test.SleepTests;
import net.minecraft.gametest.framework.GameTestHelper;
//? if >=1.21.5 {
import net.fabricmc.fabric.api.gametest.v1.GameTest;
//?} else {
/^import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
^///?}

//? if >=1.21.5 {
public class FabricGameTests {
	private static final String EMPTY = "sleeprework_gametest:empty";

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void tirednessGrowsAndPotionsChangeIt(GameTestHelper helper) {
		SleepTests.tirednessGrowsAndPotionsChangeIt(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void onlyTiredPlayersCanSleep(GameTestHelper helper) {
		SleepTests.onlyTiredPlayersCanSleep(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void wakingUpRestsAndHeals(GameTestHelper helper) {
		SleepTests.wakingUpRestsAndHeals(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void potionsBrew(GameTestHelper helper) {
		SleepTests.potionsBrew(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void phantomsFollowTiredness(GameTestHelper helper) {
		SleepTests.phantomsFollowTiredness(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void tirednessIsSavedAndCommandWorks(GameTestHelper helper) {
		SleepTests.tirednessIsSavedAndCommandWorks(helper);
	}
}
//?} else {
/^public class FabricGameTests implements FabricGameTest {
	private static final String EMPTY = "sleeprework_gametest:empty";

	@GameTest(template = EMPTY, timeoutTicks = 100)
	public void tirednessGrowsAndPotionsChangeIt(GameTestHelper helper) {
		SleepTests.tirednessGrowsAndPotionsChangeIt(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100)
	public void onlyTiredPlayersCanSleep(GameTestHelper helper) {
		SleepTests.onlyTiredPlayersCanSleep(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100)
	public void wakingUpRestsAndHeals(GameTestHelper helper) {
		SleepTests.wakingUpRestsAndHeals(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100)
	public void potionsBrew(GameTestHelper helper) {
		SleepTests.potionsBrew(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100)
	public void phantomsFollowTiredness(GameTestHelper helper) {
		SleepTests.phantomsFollowTiredness(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100)
	public void tirednessIsSavedAndCommandWorks(GameTestHelper helper) {
		SleepTests.tirednessIsSavedAndCommandWorks(helper);
	}
}
^///?}
*///?}
