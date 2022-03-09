package com.infamous.simply_harder.network;

import com.infamous.simply_harder.custom.data.GearMod;
import com.infamous.simply_harder.SimplyHarder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;

public class ClientboundUpdateGearModsPacket implements SHPacket {
   private final Map<ResourceLocation, GearMod> gearMods;

   public ClientboundUpdateGearModsPacket(Map<ResourceLocation, GearMod> gearMods) {
      this.gearMods = gearMods;
   }

   // aka, "read"
   public ClientboundUpdateGearModsPacket(FriendlyByteBuf byteBuf) {
      this.gearMods = byteBuf.readMap(FriendlyByteBuf::readResourceLocation, GearMod::fromNetwork);
   }

   @Override
   public void handle(NetworkEvent.Context context) {
      SimplyHarder.GEAR_MOD_MANAGER.replaceGearMods(this.gearMods);
   }

   @Override
   public void write(FriendlyByteBuf byteBuf) {
      byteBuf.writeMap(this.gearMods, FriendlyByteBuf::writeResourceLocation, GearMod::toNetwork);
   }

}