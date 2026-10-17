package party.lemons.sleeprework;

//? if >=1.21.11 {
/*import net.minecraft.resources.Identifier;
*///?} else {
import net.minecraft.resources.ResourceLocation;
//?}
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import party.lemons.sleeprework.config.SleepReworkConfig;
import party.lemons.sleeprework.handler.PlayerSleepData;

import java.nio.file.Path;
import java.util.function.BiConsumer;

public class SleepRework
{
    public static final String MODID = "sleeprework";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static SleepReworkConfig CONFIG = new SleepReworkConfig();
    public static Path configDir = Path.of("config");
    public static PlayerSleepData localSleepData = new PlayerSleepData();
    /** Sends a player their tiredness; set by the loader-specific networking. */
    public static BiConsumer<ServerPlayer, Float> syncTiredness = (player, tiredness) -> {};

    public static void init(Path configDirectory)
    {
        configDir = configDirectory;
        CONFIG = SleepReworkConfig.loadConfig();
    }

    //? if >=1.21.11 {
    /*public static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(MODID, name);
    }
    *///?} else {
    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name);
    }
    //?}
}
