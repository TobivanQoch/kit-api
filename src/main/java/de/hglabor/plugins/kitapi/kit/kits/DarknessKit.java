package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.items.KitItemBuilder;
import de.hglabor.plugins.kitapi.kit.settings.DoubleArg;
import de.hglabor.plugins.kitapi.kit.settings.IntArg;
import de.hglabor.plugins.kitapi.kit.settings.LongArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class DarknessKit extends AbstractKit {

  public static final DarknessKit INSTANCE = new DarknessKit();

  @IntArg
  private final int radius;

  private static final String bukkitTaskKey = "bukkitTask";
  private static final ArrayList<ArmorStand> DARK_SPOTS = new ArrayList<>();

  private DarknessKit() {
    super("Darkness", Material.BLACK_STAINED_GLASS);
    radius = 50;
  }

  @Override
  public void onEnable(KitPlayer kitPlayer) {
    Player player = Bukkit.getPlayer(kitPlayer.getUUID());
    if(player == null) {
      return;
    }
    BukkitTask task = Bukkit.getScheduler().runTaskTimer(KitApi.getInstance().getPlugin(), () -> {
      for (LivingEntity nearby : player.getWorld().getNearbyLivingEntities(player.getLocation(), radius)) {
        if (nearby instanceof Player) {
          KitPlayer nearbyKitPlayer = KitApi.getInstance().getPlayer((Player) nearby);
          if (!nearbyKitPlayer.isValid()) {
            continue;
          }
          if (nearby.getUniqueId() == player.getUniqueId()) {
            continue;
          }
          if(nearby.getTargetEntity(radius).getUniqueId() == player.getUniqueId()) {
            nearby.getWorld().spawnParticle(Particle.REDSTONE, nearby.getEyeLocation(), 5, new Particle.DustOptions(Color.BLACK, 3f));
            nearby.addPotionEffect(new PotionEffect(PotionEffectType.BAD_OMEN, 30, 2, false, false));
            nearby.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 2, false, false));
          }
        }
      }
    }, 0L, 1L);
    kitPlayer.putKitAttribute(bukkitTaskKey, task);
  }

  @Override
  public void onDisable(KitPlayer kitPlayer) {
    BukkitTask bukkitTask = kitPlayer.getKitAttribute(bukkitTaskKey);
    if(bukkitTask != null) {
      bukkitTask.cancel();
    }
  }

  @Override
  public void onDeactivation(KitPlayer kitPlayer) {
    BukkitTask bukkitTask = kitPlayer.getKitAttribute(bukkitTaskKey);
    if(bukkitTask != null) {
      bukkitTask.cancel();
    }
  }
}
