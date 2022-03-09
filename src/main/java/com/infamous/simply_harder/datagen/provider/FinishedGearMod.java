package com.infamous.simply_harder.datagen.provider;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public interface FinishedGearMod {

   default JsonObject serializeGearMod() {
      JsonObject jsonobject = new JsonObject();
      this.serializeGearModData(jsonobject);
      return jsonobject;
   }

   void serializeGearModData(JsonObject jsonObject);

   ResourceLocation getId();
}