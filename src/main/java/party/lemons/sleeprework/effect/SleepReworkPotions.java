package party.lemons.sleeprework.effect;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import party.lemons.sleeprework.SleepRework;

import java.util.function.Supplier;

public class SleepReworkPotions
{
    /** Registers into the game's registries; NeoForge and Fabric each provide one. */
    public interface Registrar {
        Holder<MobEffect> effect(String name, Supplier<MobEffect> effect);

        Holder<Potion> potion(String name, Supplier<Potion> potion);
    }

    public static Holder<MobEffect> LIVELINESS;
    public static Holder<MobEffect> DROWSINESS;
    public static Holder<Potion> LIVELINESS_POTION;
    public static Holder<Potion> LONG_LIVELINESS_POTION;
    public static Holder<Potion> STRONG_LIVELINESS_POTION;
    public static Holder<Potion> DROWSINESS_POTION;
    public static Holder<Potion> LONG_DROWSINESS_POTION;
    public static Holder<Potion> STRONG_DROWSINESS_POTION;

    public static void register(Registrar registrar)
    {
        LIVELINESS = registrar.effect("liveliness", () -> new TirednessModifierEffect(SleepRework.CONFIG.potionConfig().livelinessTirednessModifier(), MobEffectCategory.BENEFICIAL, 0xddff00));
        DROWSINESS = registrar.effect("drowsiness", () -> new TirednessModifierEffect(SleepRework.CONFIG.potionConfig().drowsinessTirednessModifier(), MobEffectCategory.HARMFUL, 0x3c2d5c));
        // the long and strong versions share their base potion's name, like vanilla's, so they share its translation
        LIVELINESS_POTION = registrar.potion("liveliness", () -> new Potion("liveliness", new MobEffectInstance(LIVELINESS, 24000)));
        LONG_LIVELINESS_POTION = registrar.potion("long_liveliness", () -> new Potion("liveliness", new MobEffectInstance(LIVELINESS, 36000)));
        STRONG_LIVELINESS_POTION = registrar.potion("strong_liveliness", () -> new Potion("liveliness", new MobEffectInstance(LIVELINESS, 15000, 1)));
        DROWSINESS_POTION = registrar.potion("drowsiness", () -> new Potion("drowsiness", new MobEffectInstance(DROWSINESS, 24000)));
        LONG_DROWSINESS_POTION = registrar.potion("long_drowsiness", () -> new Potion("drowsiness", new MobEffectInstance(DROWSINESS, 36000)));
        STRONG_DROWSINESS_POTION = registrar.potion("strong_drowsiness", () -> new Potion("drowsiness", new MobEffectInstance(DROWSINESS, 15000, 1)));
    }

    public static void registerBrewing(PotionBrewing.Builder builder)
    {
        builder.addMix(Potions.AWKWARD, SleepRework.CONFIG.potionConfig().livelinessBrewingItem(), LIVELINESS_POTION);
        builder.addMix(LIVELINESS_POTION, Items.REDSTONE, LONG_LIVELINESS_POTION);
        builder.addMix(LIVELINESS_POTION, Items.GLOWSTONE_DUST, STRONG_LIVELINESS_POTION);
        builder.addMix(LIVELINESS_POTION, Items.FERMENTED_SPIDER_EYE, DROWSINESS_POTION);
        builder.addMix(LONG_LIVELINESS_POTION, Items.FERMENTED_SPIDER_EYE, LONG_DROWSINESS_POTION);
        builder.addMix(STRONG_LIVELINESS_POTION, Items.FERMENTED_SPIDER_EYE, STRONG_DROWSINESS_POTION);
        builder.addMix(DROWSINESS_POTION, Items.REDSTONE, LONG_DROWSINESS_POTION);
        builder.addMix(DROWSINESS_POTION, Items.GLOWSTONE_DUST, STRONG_DROWSINESS_POTION);
    }
}
