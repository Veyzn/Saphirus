package com.ryan.utils.gui.events;

import org.bukkit.inventory.Inventory;

public interface GuiCloseEvent {
    void onClose( Inventory finalInventory );
}