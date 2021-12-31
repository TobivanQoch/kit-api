package de.hglabor.plugins.kitapi.kit.passives;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.Passive;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class NoSoupDropPassive extends Passive implements Listener {

    public static final NoSoupDropPassive INSTANCE = new NoSoupDropPassive();

    private NoSoupDropPassive() {
        super("NoSoupDrop", Material.MUSHROOM_STEW);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        KitPlayer kitPlayer = KitApi.getInstance().getPlayer(event.getPlayer());
        if (hasPassive(kitPlayer)) {
            Material item = event.getItemDrop().getItemStack().getType();
            ArrayList<Material> prohibitedItems = new ArrayList<>(Arrays.stream(Material.values()).filter(material -> material.name().contains("STEW")).toList());
            if (prohibitedItems.contains(item)) {
                if (kitPlayer.isInCombat()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    private boolean hasPassive(KitPlayer kitPlayer) {
        return kitPlayer.getPassive().equals(NoSoupDropPassive.INSTANCE);
    }
}
