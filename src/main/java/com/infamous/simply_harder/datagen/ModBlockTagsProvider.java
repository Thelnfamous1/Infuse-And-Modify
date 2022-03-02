package com.infamous.simply_harder.datagen;

import com.infamous.simply_harder.SimplyHarder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, SimplyHarder.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
    }
}
