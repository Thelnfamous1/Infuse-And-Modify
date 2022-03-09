package com.infamous.simply_harder.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Function;

public interface SHPacket {
    static <T extends SHPacket> void register(SimpleChannel channel, int id, NetworkDirection direction, Class<T> packetClass, Function<FriendlyByteBuf, T> readFunc) {
        register(channel.messageBuilder(packetClass, id, direction), readFunc);
    }

    static <T extends SHPacket> void register(SimpleChannel.MessageBuilder<T> builder, Function<FriendlyByteBuf, T> readFunc) {
        builder.encoder(SHPacket::write)
                .decoder(readFunc)
                .consumer((p, sup) -> {
                    p.handle(sup.get());
                    return true;
                })
                .add();
    }

    void handle(NetworkEvent.Context context);

    void write(FriendlyByteBuf packetBuf);
}