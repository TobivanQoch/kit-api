package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.settings.FloatArg;
import de.hglabor.plugins.kitapi.kit.settings.IntArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Phantom;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.atomic.AtomicInteger;

public class FalcoKit extends AbstractKit implements Listener {

  public static final FalcoKit INSTANCE = new FalcoKit();

  @FloatArg
  private final float cooldown;

  @IntArg
  private final int flashDuration, flashRadius;

  private FalcoKit() {
    super("Falco", Material.ACACIA_LEAVES);
    setMainKitItem(getDisplayMaterial());
    this.cooldown = 60;
    this.flashDuration = 60;
    this.flashRadius = 15;
  }

  @KitEvent
  @Override
  public void onPlayerRightClickKitItem(PlayerInteractEvent event, KitPlayer kitPlayer) {
    kitPlayer.getBukkitPlayer().ifPresent(it -> {
      Phantom phantom = it.getWorld().spawn(it.getLocation(), Phantom.class);
      phantom.setShouldBurnInDay(false);
      phantom.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 25, false, false));
      phantom.setVelocity(it.getLocation().getDirection().multiply(2));
      AtomicInteger tick = new AtomicInteger();
      Bukkit.getScheduler().runTaskLater(KitApi.getInstance().getPlugin(), () -> {
        phantom.getWorld().playSound(phantom.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1f, 10f);
      }, 40);
      Bukkit.getScheduler().runTaskTimer(KitApi.getInstance().getPlugin(), task -> {
        tick.getAndIncrement();
        if (tick.get() == flashDuration) {
          task.cancel();
          phantom.remove();
        } else {
          for (Entity nearby : phantom.getNearbyEntities(flashRadius, flashRadius, flashRadius)) {
            nearby.getWorld().spawnParticle(Particle.FLASH, nearby.getLocation().clone().add(0, 1, 0), 0);
          }
        }
      }, 40, 1);
    });
    kitPlayer.activateKitCooldown(this);
  }

  @Override
  public float getCooldown() {
    return cooldown;
  }
}
