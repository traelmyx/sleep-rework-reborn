package party.lemons.sleeprework.handler;

import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import party.lemons.sleeprework.SleepRework;
import party.lemons.sleeprework.client.AbstractContainerScreenAccess;
import party.lemons.sleeprework.client.SleepDisplayWidget;

public class ClientHandler
{
    /*
        Creates the sleep widget for the inventory screen
     */
    public static SleepDisplayWidget createSleepWidget(InventoryScreen inventoryScreen)
    {
        int xx = ((AbstractContainerScreenAccess)inventoryScreen).getLeft() + SleepRework.CONFIG.clientConfig().iconX();
        int yy = ((AbstractContainerScreenAccess)inventoryScreen).getTop() + SleepRework.CONFIG.clientConfig().iconY();
        return new SleepDisplayWidget(xx, yy);
    }
}
