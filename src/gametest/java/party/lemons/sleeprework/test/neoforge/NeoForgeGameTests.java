package party.lemons.sleeprework.test.neoforge;

//? if neoforge {
import party.lemons.sleeprework.test.ClientSmoke;
import party.lemons.sleeprework.test.SleepTests;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
//? if >=1.21.5 {
/*import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;
*///?} else {
import net.minecraft.gametest.framework.GameTest;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
//?}

//? if <1.21.5 {
@GameTestHolder(NeoForgeGameTests.MOD_ID)
@PrefixGameTestTemplate(false)
//?}
@Mod(NeoForgeGameTests.MOD_ID)
public class NeoForgeGameTests {
	public static final String MOD_ID = "sleeprework_gametest";

	public NeoForgeGameTests(IEventBus modBus, Dist dist) {
		//? if >=1.21.5 {
		/*FUNCTIONS.register(modBus);
		modBus.addListener(NeoForgeGameTests::registerTests);
		*///?}
		if (dist.isClient() && ClientSmoke.start()) {
			NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post e) -> ClientSmoke.tick(net.minecraft.client.Minecraft.getInstance()));
		}
	}

	//? if >=1.21.5 {
	/*private static final DeferredRegister<Consumer<GameTestHelper>> FUNCTIONS = DeferredRegister.create(Registries.TEST_FUNCTION, MOD_ID);
	private static final Identifier EMPTY = Identifier.fromNamespaceAndPath(MOD_ID, "empty");

	static {
		FUNCTIONS.register("tiredness_grows_and_potions_change_it", () -> SleepTests::tirednessGrowsAndPotionsChangeIt);
		FUNCTIONS.register("only_tired_players_can_sleep", () -> SleepTests::onlyTiredPlayersCanSleep);
		FUNCTIONS.register("waking_up_rests_and_heals", () -> SleepTests::wakingUpRestsAndHeals);
		FUNCTIONS.register("potions_brew", () -> SleepTests::potionsBrew);
		FUNCTIONS.register("phantoms_follow_tiredness", () -> SleepTests::phantomsFollowTiredness);
		FUNCTIONS.register("tiredness_is_saved_and_command_works", () -> SleepTests::tirednessIsSavedAndCommandWorks);
	}

	private static void registerTests(RegisterGameTestsEvent event) {
		//? if >=26 {
		Holder<TestEnvironmentDefinition<?>> env = event.registerEnvironment(Identifier.fromNamespaceAndPath(MOD_ID, "default"));
		//?} else
		/^Holder<TestEnvironmentDefinition> env = event.registerEnvironment(Identifier.fromNamespaceAndPath(MOD_ID, "default"));^/
		register(event, env, "tiredness_grows_and_potions_change_it", 100, 1, 1);
		register(event, env, "only_tired_players_can_sleep", 100, 1, 1);
		register(event, env, "waking_up_rests_and_heals", 100, 1, 1);
		register(event, env, "potions_brew", 100, 1, 1);
		register(event, env, "phantoms_follow_tiredness", 100, 1, 1);
		register(event, env, "tiredness_is_saved_and_command_works", 100, 1, 1);
	}

	//? if >=26 {
	private static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env, String name, int maxTicks, int attempts, int successes) {
	//?} else
	/^private static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition> env, String name, int maxTicks, int attempts, int successes) {^/
		Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, name);
		event.registerTest(id, new FunctionGameTestInstance(ResourceKey.create(Registries.TEST_FUNCTION, id),
			//? if >=26.3 {
			new TestData<>(env, net.minecraft.world.level.Level.OVERWORLD, EMPTY, maxTicks, 0, true,
				net.minecraft.world.level.block.Rotation.NONE, false, attempts, successes, false, 0)));
			//?} else if >=26 {
			/^new TestData<>(env, EMPTY, maxTicks, 0, true,
				net.minecraft.world.level.block.Rotation.NONE, false, attempts, successes, false, 0)));
			^///?} else {
			/^new TestData<>(env, EMPTY, maxTicks, 0, true,
				net.minecraft.world.level.block.Rotation.NONE, false, attempts, successes, false)));
			^///?}
	}
	*///?} else {
	@GameTest(template = "empty", timeoutTicks = 100)
	public static void tirednessGrowsAndPotionsChangeIt(GameTestHelper helper) {
		SleepTests.tirednessGrowsAndPotionsChangeIt(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 100)
	public static void onlyTiredPlayersCanSleep(GameTestHelper helper) {
		SleepTests.onlyTiredPlayersCanSleep(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 100)
	public static void wakingUpRestsAndHeals(GameTestHelper helper) {
		SleepTests.wakingUpRestsAndHeals(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 100)
	public static void potionsBrew(GameTestHelper helper) {
		SleepTests.potionsBrew(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 100)
	public static void phantomsFollowTiredness(GameTestHelper helper) {
		SleepTests.phantomsFollowTiredness(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 100)
	public static void tirednessIsSavedAndCommandWorks(GameTestHelper helper) {
		SleepTests.tirednessIsSavedAndCommandWorks(helper);
	}
	//?}
}
//?}
