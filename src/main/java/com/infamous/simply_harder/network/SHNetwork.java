package com.infamous.simply_harder.network;

import com.infamous.simply_harder.SimplyHarder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class SHNetwork {

    private static final ResourceLocation CHANNEL_NAME = new ResourceLocation(SimplyHarder.MOD_ID, "sync_channel");
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel SYNC_CHANNEL = NetworkRegistry.ChannelBuilder.named(
            CHANNEL_NAME)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
    private static int PACKET_ID_COUNTER = 0;

    public static void register(){
        SHPacket.register(SYNC_CHANNEL, PACKET_ID_COUNTER++, NetworkDirection.PLAY_TO_CLIENT, ClientboundUpdateGearModsPacket.class, ClientboundUpdateGearModsPacket::new);
        SHPacket.register(SYNC_CHANNEL, PACKET_ID_COUNTER++, NetworkDirection.PLAY_TO_CLIENT, ClientboundUpdateMasterworkProgressionsPacket.class, ClientboundUpdateMasterworkProgressionsPacket::new);
    }

    public static <MSG> void syncToPlayer(ServerPlayer serverPlayer, MSG message)
    {
        if (SYNC_CHANNEL.isRemotePresent(serverPlayer.connection.getConnection()) && !serverPlayer.connection.getConnection().isMemoryConnection())
        {
            SYNC_CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), message);
        }
    }
}
