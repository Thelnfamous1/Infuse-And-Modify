package com.infamous.simply_harder.custom.container;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;

public class SimpleAnvilContainer extends SimpleContainer {
    private final AnvilMenu anvilMenu;
    private final Player player;

    public SimpleAnvilContainer(AnvilMenu anvilMenu, Player player, ItemStack left, ItemStack right) {
        super(left, right);
        this.player = player;
        this.anvilMenu = anvilMenu;
    }

    public ItemStack getLeft(){
        return this.getItem(AnvilMenu.INPUT_SLOT);
    }

    public ItemStack getRight(){
        return this.getItem(AnvilMenu.ADDITIONAL_SLOT);
    }

    public int getRepairCost(){
        return this.anvilMenu.getCost();
    }

    public int getRepairItemCountCost(){
        return this.anvilMenu.repairItemCountCost;
    }

    public Player getPlayer() {
        return this.player;
    }
}
