package com.infamous.simply_harder.custom.loot;

import com.google.common.collect.Sets;
import com.infamous.simply_harder.SimplyHarder;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class SHBuiltInLootTables {
    private static final String[] LEVEL_NAMES = {"apprentice", "journeyman", "expert", "master"};
    private static final Set<ResourceLocation> LOCATIONS = Sets.newHashSet();
    private static final Set<ResourceLocation> IMMUTABLE_LOCATIONS = Collections.unmodifiableSet(LOCATIONS);

    public static final List<ResourceLocation> ARMORER_PROGRESSION_REWARDS = buildProprietaryMerchantProgressionsFor("armorer");
    public static final List<ResourceLocation> LEATHERWORKER_PROGRESSION_REWARDS = buildProprietaryMerchantProgressionsFor("leatherworker");
    public static final List<ResourceLocation> TOOLSMITH_PROGRESSION_REWARDS = buildProprietaryMerchantProgressionsFor("toolsmith");
    public static final List<ResourceLocation> WEAPONSMITH_PROGRESSION_REWARDS = buildProprietaryMerchantProgressionsFor("weaponsmith");

    private static List<ResourceLocation> buildProprietaryMerchantProgressionsFor(String profession){
        List<ResourceLocation> merchantProgressions = new LinkedList<>();
        for(String levelName : LEVEL_NAMES){
            merchantProgressions.add(registerProprietaryLootTable(buildMerchantProgressionPath(profession, levelName)));
        }
        return merchantProgressions;
    }

    private static String buildMerchantProgressionPath(String profession, String levelName) {
        return "gameplay/merchant_progression/" + profession + "/" + levelName;
    }

    private static ResourceLocation registerProprietaryLootTable(String path) {
        return register(new ResourceLocation(SimplyHarder.MOD_ID, path));
    }

    private static ResourceLocation register(ResourceLocation location) {
        if (LOCATIONS.add(location)) {
            return location;
        } else {
            throw new IllegalArgumentException(location + " is already a registered built-in loot table");
        }
    }

    public static Set<ResourceLocation> all() {
        return IMMUTABLE_LOCATIONS;
    }
}
