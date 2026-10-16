package party.lemons.sleeprework.neoforge;

//? if neoforge {
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import party.lemons.sleeprework.handler.ClientHandler;

// Client-only: adds the tiredness icon to the inventory screen.
final class SleepReworkNeoForgeClient {
    private SleepReworkNeoForgeClient() {
    }

    static void init() {
        NeoForge.EVENT_BUS.addListener((ScreenEvent.Init.Post event) -> {
            if (event.getScreen() instanceof InventoryScreen screen) event.addListener(ClientHandler.createSleepWidget(screen));
        });
    }
}
//?}
