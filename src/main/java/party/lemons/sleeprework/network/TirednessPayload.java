package party.lemons.sleeprework.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import party.lemons.sleeprework.SleepRework;

/** Server to client: the player's current tiredness, for the inventory widget. */
public record TirednessPayload(float tiredness) implements CustomPacketPayload {
    public static final Type<TirednessPayload> TYPE = new Type<>(SleepRework.id("sync_tiredness"));
    public static final StreamCodec<ByteBuf, TirednessPayload> CODEC = ByteBufCodecs.FLOAT.map(TirednessPayload::new, TirednessPayload::tiredness);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handleOnClient() {
        SleepRework.localSleepData.setTiredness(null, tiredness);
    }
}
