package com.infamous.simply_harder.capability;

import com.infamous.simply_harder.SimplyHarder;
import dev._100media.capabilitysyncer.core.CapabilityAttacher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nullable;

public class PeakExperienceAttacher extends CapabilityAttacher {
    public static final ResourceLocation LOCATION = new ResourceLocation(SimplyHarder.MOD_ID, "peak_experience");
    private static final Class<PeakExperience> CAPABILITY_CLASS = PeakExperience.class;
    public static final Capability<PeakExperience> PEAK_EXPERIENCE_CAP = getCapability(new CapabilityToken<>() {});

    @Nullable
    public static PeakExperience getPeakExperienceUnwrap(Player player) {
        return getPeakExperience(player).orElse(null);
    }

    public static LazyOptional<PeakExperience> getPeakExperience(Player player) {
        return player.getCapability(PEAK_EXPERIENCE_CAP);
    }

    private static void attach(AttachCapabilitiesEvent<Entity> event, Player player) {
        genericAttachCapability(event, new PeakExperience(player), PEAK_EXPERIENCE_CAP, LOCATION);
    }

    public static void register() {
        CapabilityAttacher.registerCapability(CAPABILITY_CLASS);
        CapabilityAttacher.registerPlayerAttacher(PeakExperienceAttacher::attach, PeakExperienceAttacher::getPeakExperience, true);
    }
}