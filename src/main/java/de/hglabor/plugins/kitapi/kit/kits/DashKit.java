package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.items.KitItemBuilder;
import de.hglabor.plugins.kitapi.kit.settings.FloatArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class DashKit extends AbstractKit implements Listener {

  public static final DashKit INSTANCE = new DashKit();
  private static final String ARROW = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTRhNTY2N2VmNzI4NWM5MjI1ZmMyNjdkNDUxMTdlYWI1NDc4Yzc4NmJkNWFmMGExOTljMjlhMmMxNGMxZiJ9fX0=";
  private final ItemStack skull;

  @FloatArg(min = 0.0F)
  private final float cooldown;

  private DashKit() {
    super("Dash");
    skull = new KitItemBuilder(Material.PLAYER_HEAD).setPlayerSkullSkin(ARROW).build();
    setDisplayItem(skull);
    setMainKitItem(skull);
    this.cooldown = 15F;
  }

  @Override
  @KitEvent
  public void onPlayerRightClickKitItem(PlayerInteractEvent event, KitPlayer kitPlayer) {
    Player player = event.getPlayer();
    player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 3, new Particle.DustOptions(Color.WHITE, 3f));
    player.setVelocity(player.getLocation().getDirection().multiply(2.2));
    kitPlayer.activateKitCooldown(INSTANCE);
  }

  @Override
  public float getCooldown() {
    return cooldown;
  }
}
