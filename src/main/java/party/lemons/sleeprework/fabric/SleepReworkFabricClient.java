package party.lemons.sleeprework.fabric;

//? if fabric {
/*import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import party.lemons.sleeprework.handler.ClientHandler;
import party.lemons.sleeprework.network.TirednessPayload;

public class SleepReworkFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(TirednessPayload.TYPE, (payload, context) -> context.client().execute(payload::handleOnClient));
        ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
            if (screen instanceof InventoryScreen inventory) Screens.getButtons(screen).add(ClientHandler.createSleepWidget(inventory));
        });
    }
}
*///?}
