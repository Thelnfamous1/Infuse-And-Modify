package com.infamous.simply_harder.datagen.builder;

import com.infamous.simply_harder.custom.data.GearMod;
import com.infamous.simply_harder.custom.data.WrappedAttributeModifierMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class GearModBuilder {

    private final ResourceLocation id;
    private Ingredient installable = Ingredient.EMPTY;
    private int levelCost;
    private WrappedAttributeModifierMap wrappedAttributeModifiers = WrappedAttributeModifierMap.EMPTY;

    public GearModBuilder(ResourceLocation id){
        this.id = id;
    }

    public static GearModBuilder gearMod(ResourceLocation id){
        return new GearModBuilder(id);
    }

    public GearModBuilder installable(Ingredient installable){
        this.installable = installable;
        return this;
    }

    public GearModBuilder levelCost(int levelCost){
        this.levelCost = levelCost;
        return this;
    }

    public GearModBuilder wrappedAttributeModifiers(WrappedAttributeModifierMap wrappedAttributeModifiers){
        this.wrappedAttributeModifiers = wrappedAttributeModifiers;
        return this;
    }

    public GearMod build(){
        return new GearMod(this.id, this.installable, this.levelCost, this.wrappedAttributeModifiers);
    }

    public void save(Consumer<GearMod> onFinished) {
        onFinished.accept(this.build());
    }
}
