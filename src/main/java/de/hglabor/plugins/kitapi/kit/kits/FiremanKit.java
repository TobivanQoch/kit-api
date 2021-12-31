package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.settings.BetaKit;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

@BetaKit
public class FiremanKit extends AbstractKit implements Listener {
  public static final FiremanKit INSTANCE = new FiremanKit();

  private FiremanKit() {
    super("Fireman", Material.WATER_BUCKET);
  }

  @Override
  public void onEnable(KitPlayer kitPlayer) {
    kitPlayer.getBukkitPlayer().ifPresent(player -> player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET)));
  }

  @KitEvent
  @Override
  public void onEntityDamage(EntityDamageEvent event) {
    if (event.getEntity() instanceof Player player) {
      if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
        event.setCancelled(true);
        player.setFireTicks(0);
      }
    }
  }
}
