package com.ryan.utils.gui;

import com.ryan.utils.ItemUtils;
import com.ryan.utils.gui.events.ClickedGuiItem;
import com.ryan.utils.gui.events.GuiClickEvent;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an item in the {@link Gui} class. Contains information about
 * the item, the slot in the GUI, and the lambda statement (if any) to run when
 * this item is clicked by the player.
 *
 * @author CyberRyan
 */
public class GuiItem {

    private ItemStack item;
    private int slot;
    private GuiClickEvent clickEvent = null;
    private boolean allowItemMovement = false;

    public GuiItem( Material material, String name, int slot ) {
        this.item = ItemUtils.createItem( material, name );
        this.slot = slot;
    }

    public GuiItem( Material material, String name, int slot, GuiClickEvent clickEvent ) {
        this.item = ItemUtils.createItem( material, name );
        this.slot = slot;
        this.clickEvent = clickEvent;
    }

    public GuiItem( ItemStack item, int slot ) {
        this.item = item;
        this.slot = slot;
    }

    public GuiItem( ItemStack item, int slot, GuiClickEvent clickEvent ) {
        this.item = item;
        this.slot = slot;
        this.clickEvent = clickEvent;
    }

    /**
     * Sets the {@link ItemStack} for this item
     * @param item New itemstack
     */
    public void setItem( ItemStack item ) {
        this.item = item;
    }

    /**
     * Sets the item using a {@link org.bukkit.Material} and name
     * @param material New material
     * @param name New name
     */
    public void setItem( Material material, String name ) {
        this.item = ItemUtils.setItemName( new ItemStack( material ), name );
    }

    /**
     * Sets a new slot in the GUI
     * @param slot New slot
     */
    public void setSlot( int slot ) {
        this.slot = slot;
    }

    /**
     * Sets the amount of the item in the GUI
     * @param amount New amount
     */
    public void setItemAmount( int amount ) {
        this.item.setAmount( amount );
    }

    /**
     * Sets what lambda statement that is run when the item is clicked
     * @param clickEvent New lambda statement through {@link GuiClickEvent}
     */
    public void setExecuteOnClick( GuiClickEvent clickEvent ) {
        this.clickEvent = clickEvent;
    }

    /**
     * Sets whether or not this item can be picked up by the player. Note that if this item has
     * an event to run on click, that will run and the player will not be able to pick up
     * this item.
     * @param allowItemMovement True if the player can pick up this item, false if not
     */
    public void setAllowItemMovement( boolean allowItemMovement ) {
        this.allowItemMovement = allowItemMovement;
    }

    /**
     * Returns the item this currently represents
     * @return {@link ItemStack} currently represented
     */
    public ItemStack getItem() { return this.item; }

    /**
     * Returns the type of the item this represents
     * @return {@link Material} used for this item
     */
    public Material getType() { return this.item.getType(); }

    /**
     * Returns the current slot this item will be placed in the GUI
     * @return slot in GUI
     */
    public int getSlot() { return slot; }

    /**
     * Returns the lambda statement (no arg) currently being used when the item is clicked
     * @return {@link GuiClickEvent} ran when clicked
     */
    public GuiClickEvent getClickEvent() { return clickEvent; }

    /**
     * Returns if the item currently runs some other code when it is clicked or if it will do nothing
     * @return true if it will run something when clicked, false if not
     */
    public boolean isExecutable() {
        return clickEvent != null;
    }

    /**
     * Returns if the item can be picked up by the player
     * @return true if the player can pick up the item, false if not
     */
    public boolean isAllowItemMovement() {
        return allowItemMovement;
    }

    /**
     * Runs the lambda statement through the {@link GuiClickEvent}
     * @param event The {@link InventoryClickEvent} that triggered this
     * @throws NullPointerException if the item has no lambda statements to run
     */
    public void execute( InventoryClickEvent event ) {
        // Passes a copy of this item to the lambda statement
        if ( clickEvent != null ) { clickEvent.onClick( new ClickedGuiItem( this.item.clone(), this.slot, event ) ); }
        else { throw new NullPointerException( "Couldn't find any lambda statement to run when item is clicked" ); }
    }
}