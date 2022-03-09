package com.infamous.simply_harder.network;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.custom.data.MasterworkProgression;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;

public class ClientboundUpdateMasterworkProgressionsPacket implements SHPacket{

    private final Map<ResourceLocation, MasterworkProgression> progressions;

    public ClientboundUpdateMasterworkProgressionsPacket(Map<ResourceLocation, MasterworkProgression> progressions) {
        this.progressions = progressions;
    }

    public ClientboundUpdateMasterworkProgressionsPacket(FriendlyByteBuf byteBuf){
        this.progressions = byteBuf.readMap(FriendlyByteBuf::readResourceLocation, MasterworkProgression::fromNetwork);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        SimplyHarder.MASTERWORK_PROGRESSION_MANAGER.replaceProgressions(this.progressions);
    }

    @Override
    public void write(FriendlyByteBuf packetBuf) {
        packetBuf.writeMap(this.progressions, FriendlyByteBuf::writeResourceLocation, MasterworkProgression::toNetwork);
    }
}
