package com.infamous.simply_harder.mixin;

import net.minecraftforge.common.ToolAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ToolAction.class)
public interface ToolActionAccessor {

    @Accessor
    static Map<String, ToolAction> getActions(){
        throw new UnsupportedOperationException();
    }
}
