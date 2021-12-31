package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.settings.BetaKit;
import de.hglabor.plugins.kitapi.kit.settings.FloatArg;
import de.hglabor.plugins.kitapi.kit.settings.IntArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static de.hglabor.plugins.kitapi.util.BukkitUtils.runTaskLater;

@BetaKit
public class ElytraKit extends AbstractKit {
  public static final ElytraKit INSTANCE = new ElytraKit();

  @FloatArg(min = 0.0F)
  private final float cooldown;
  @FloatArg(min = 0.0F)
  private final float normalBoost;
  @FloatArg(min = 0.0F)
  private final float combatBoost;
  @IntArg
  private final int glidingDelay;

  private ElytraKit() {
    super("Elytra", Material.FEATHER);
    setMainKitItem(getDisplayMaterial());
    this.cooldown = 45;
    this.normalBoost = 2.5F;
    this.combatBoost = 1.0F;
    this.glidingDelay = 5;
  }

  @KitEvent
  public void onPlayerRightClickKitItem(PlayerInteractEvent event, KitPlayer kitPlayer) {
    Player player = event.getPlayer();
    if (player.isInsideVehicle()) {
      return;
    }
    if (kitPlayer.isInCombat()) {
      player.setVelocity(player.getVelocity().setY(combatBoost));
      player.sendActionBar(ChatColor.RED + "Elytra kann im Kampf nicht eingesetzt werden");
    } else {
      player.setVelocity(player.getVelocity().setY(normalBoost));
    }
    runTaskLater(() -> player.setGliding(true), glidingDelay);
    kitPlayer.activateKitCooldown(this);
  }

  @EventHandler
  public void onEntityToggleGlideEvent(EntityToggleGlideEvent event) {
    if (!(event.getEntity() instanceof Player player)) {
      return;
    }
    KitPlayer kitPlayer = KitApi.getInstance().getPlayer(player);
    if (!kitPlayer.hasKit(this)) {
      return;
    }
    ItemStack chestplate = player.getInventory().getChestplate();
    if (chestplate != null && chestplate.getType().equals(Material.ELYTRA)) {
      return;
    }
    if (!(player.isOnGround())) {
      event.setCancelled(true);
    }
  }

  @Override
  public float getCooldown() {
    return cooldown;
  }
}
