package com.infamous.simply_harder.network;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.capability.PeakExperienceAttacher;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {

    private static final ResourceLocation CHANNEL_NAME = new ResourceLocation(SimplyHarder.MOD_ID, "sync_channel");
    private static final String PROTOCOL_VERSION = "1.0";
    private static final SimpleChannel SYNC_CHANNEL = NetworkRegistry.ChannelBuilder.named(
            CHANNEL_NAME)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
    private static int PACKET_ID_COUNTER = 0;

    public static void register(){
        SimpleEntityCapabilityStatusPacket.register(PeakExperienceAttacher.LOCATION, PeakExperienceAttacher::getPeakExperienceUnwrap, SYNC_CHANNEL, PACKET_ID_COUNTER++);
    }

    public static SimpleChannel getSyncChannel() {
        return SYNC_CHANNEL;
    }
}
