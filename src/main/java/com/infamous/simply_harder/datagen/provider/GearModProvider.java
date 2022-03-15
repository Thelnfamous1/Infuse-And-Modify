package com.infamous.simply_harder.datagen.provider;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.infamous.simply_harder.custom.data.GearMod;
import com.infamous.simply_harder.custom.loot.SHBuiltInGearMods;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class GearModProvider implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    protected final DataGenerator generator;

    public GearModProvider(DataGenerator dataGenerator) {
        this.generator = dataGenerator;
    }

    @Override
    public void run(HashCache hashCache) throws IOException {
        Path path = this.generator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();
        buildGearMods((gm) -> {
            ResourceLocation id = gm.id();
            if (!set.add(id)) {
                throw new IllegalStateException("Duplicate gear mod " + id);
            } else {
                saveGearMod(hashCache, gm.toJson(), path.resolve("data/" + id.getNamespace() + "/gear_mods/" + id.getPath() + ".json"));
            }
        });
    }

    private static void saveGearMod(HashCache hashCache, JsonObject serialized, Path path) {
        try {
            String s = GSON.toJson((JsonElement)serialized);
            String s1 = SHA1.hashUnencodedChars(s).toString();
            if (!Objects.equals(hashCache.getHash(path), s1) || !Files.exists(path)) {
                Files.createDirectories(path.getParent());
                BufferedWriter bufferedwriter = Files.newBufferedWriter(path);

                try {
                    bufferedwriter.write(s);
                } catch (Throwable throwable1) {
                    if (bufferedwriter != null) {
                        try {
                            bufferedwriter.close();
                        } catch (Throwable throwable) {
                            throwable1.addSuppressed(throwable);
                        }
                    }

                    throw throwable1;
                }

                if (bufferedwriter != null) {
                    bufferedwriter.close();
                }
            }

            hashCache.putNew(path, s1);
        } catch (IOException ioexception) {
            LOGGER.error("Couldn't save gear mod {}", path, ioexception);
        }

    }

    protected void buildGearMods(Consumer<GearMod> onFinished) {
        SHBuiltInGearMods.all().forEach(onFinished);
    }

    @Override
    public String getName() {
        return "Gear Mods";
    }
}
