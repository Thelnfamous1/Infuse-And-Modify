package com.infamous.simply_harder.capability;

import com.infamous.simply_harder.SimplyHarder;
import com.infamous.simply_harder.network.ModNetwork;
import dev._100media.capabilitysyncer.core.PlayerCapability;
import dev._100media.capabilitysyncer.network.EntityCapabilityStatusPacket;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.simple.SimpleChannel;

public class PeakExperience extends PlayerCapability {

    public static final String PEAK_EXPERIENCE_LEVEL_TAG = "PeakExperienceLevel";
    public static final String PEAK_TOTAL_EXPERIENCE_TAG = "PeakTotalExperience";
    private int peakExperienceLevel;
    private int peakTotalExperience;

    public PeakExperience(Player player){
        super(player);
    }

    public int getPeakExperienceLevel() {
        return this.peakExperienceLevel;
    }

    public void setPeakExperienceLevel(int peakExperienceLevel, boolean sync) {
        SimplyHarder.LOGGER.info("Setting peak experience level to: {}", peakExperienceLevel);
        this.peakExperienceLevel = peakExperienceLevel;
        if (sync) {
            // Send an update packet to all tracking clients
            this.updateTracking();
        }
    }

    public int getPeakTotalExperience() {
        return this.peakTotalExperience;
    }

    public void setPeakTotalExperience(int peakTotalExperience, boolean sync) {
        SimplyHarder.LOGGER.info("Setting peak total experience to: {}", peakTotalExperience);
        this.peakTotalExperience = peakTotalExperience;
        if (sync) {
            // Send an update packet to all tracking clients
            this.updateTracking();
        }
    }

    @Override
    public EntityCapabilityStatusPacket createUpdatePacket() {
        return new SimpleEntityCapabilityStatusPacket(player.getId(), PeakExperienceAttacher.LOCATION, this);
    }

    @Override
    public SimpleChannel getNetworkChannel() {
        return ModNetwork.getSyncChannel();
    }

    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag tag = new CompoundTag();
        tag.putInt(PEAK_EXPERIENCE_LEVEL_TAG, this.getPeakExperienceLevel());
        tag.putInt(PEAK_TOTAL_EXPERIENCE_TAG, this.getPeakTotalExperience());
        SimplyHarder.LOGGER.info("Serialized peak experience level to: {}", peakExperienceLevel);
        SimplyHarder.LOGGER.info("Serialized peak total experience to: {}", peakTotalExperience);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        this.peakExperienceLevel = nbt.getInt(PEAK_EXPERIENCE_LEVEL_TAG);
        this.peakTotalExperience = nbt.getInt(PEAK_TOTAL_EXPERIENCE_TAG);
        SimplyHarder.LOGGER.info("Deserialized peak experience level from: {}", peakExperienceLevel);
        SimplyHarder.LOGGER.info("Deserialized peak total experience from: {}", peakTotalExperience);
    }
}
