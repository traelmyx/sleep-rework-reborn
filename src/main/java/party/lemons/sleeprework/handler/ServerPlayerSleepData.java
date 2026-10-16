package party.lemons.sleeprework.handler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import party.lemons.sleeprework.SleepRework;
import party.lemons.sleeprework.effect.SleepReworkPotions;
import party.lemons.sleeprework.effect.TirednessModifierEffect;

public class ServerPlayerSleepData extends PlayerSleepData
{
    /*
        Server player Sleep data.
     */

    protected float sleepTimeout = 0.0F;

    public void increaseTiredness(ServerPlayer player)
    {
        float increase = getTirednessIncrease(player);

        setTiredness(player, tiredness + increase);
    }

    public boolean tickTimeout(ServerPlayer player)
    {
        sleepTimeout += 1;
        if(sleepTimeout >= SleepRework.CONFIG.playerConfig().sleepTimeout())
        {
            return true;
        }

        return false;
    }

    /*
        Gets the level of tiredness to increase for a player
        TODO: hook for other mods?
     */
    public float getTirednessIncrease(ServerPlayer player)
    {
        float increase = SleepRework.CONFIG.playerConfig().tirednessIncreasePerMinute();

        //Handle potions
        //TODO: What if there's a TirednessModifierEffect that isn't Drowsiness or Liveliness?
        if(player.hasEffect(SleepReworkPotions.DROWSINESS))
        {
            MobEffectInstance instance = player.getEffect(SleepReworkPotions.DROWSINESS);
            increase *= (((TirednessModifierEffect)instance.getEffect().value()).getModifier() * (1F + instance.getAmplifier()));
        }

        if(player.hasEffect(SleepReworkPotions.LIVELINESS))
        {
            MobEffectInstance instance = player.getEffect(SleepReworkPotions.LIVELINESS);
            float modifier = ((TirednessModifierEffect)instance.getEffect().value()).getModifier();
            float amplifier = 1F + instance.getAmplifier();

            increase *= Math.pow(modifier, amplifier);
        }

        return increase;
    }

    public void resetTiredness(ServerPlayer serverPlayer)
    {
        setTiredness(serverPlayer, 0);
    }

    public void resetTimeout(ServerPlayer player)
    {
        sleepTimeout = 0;
    }

    @Override
    public void setTiredness(@Nullable Player player, float value)
    {
        tiredness = Mth.clamp(value, 0.0F, SleepRework.CONFIG.playerConfig().maxTiredness());

        if(player != null)
            syncTo((ServerPlayer) player);
    }

    public void syncTo(ServerPlayer player)
    {
        if(player.connection != null)
            SleepRework.syncTiredness.accept(player, tiredness);
    }
}
