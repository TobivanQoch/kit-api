package de.hglabor.plugins.kitapi.kit.events.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerAteSoupEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private final boolean hasPresouped;
    private final ItemStack soup;

    public PlayerAteSoupEvent(@NotNull Player who, boolean hasPresouped, ItemStack soup) {
        super(who);
        this.hasPresouped = hasPresouped;
        this.soup = soup;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public boolean hasPresouped() {
        return hasPresouped;
    }

    public ItemStack getSoup() {
        return soup;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
