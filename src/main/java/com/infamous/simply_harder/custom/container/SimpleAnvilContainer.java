package com.infamous.simply_harder.custom.container;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;

public class SimpleAnvilContainer extends SimpleContainer {
    private final AnvilMenu anvilMenu;

    public SimpleAnvilContainer(AnvilMenu anvilMenu, ItemStack... itemStacks) {
        super(itemStacks);
        this.anvilMenu = anvilMenu;
    }

    public int getRepairCost(){
        return this.anvilMenu.getCost();
    }

    public int getRepairItemCountCost(){
        return this.anvilMenu.repairItemCountCost;
    }
}
