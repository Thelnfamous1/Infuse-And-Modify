package com.infamous.simply_harder.datagen.provider;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public interface FinishedMasterworkProgression {

   JsonObject serializeMasterworkProgression();

   ResourceLocation getId();
}