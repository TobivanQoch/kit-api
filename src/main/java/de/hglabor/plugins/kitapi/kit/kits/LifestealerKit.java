package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.settings.LongArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class LifestealerKit extends AbstractKit implements Listener {

  public static final LifestealerKit INSTANCE = new LifestealerKit();

  @LongArg
  private final long time;

  private LifestealerKit() {
    super("Lifestealer", Material.RED_DYE);
    time = 30;
  }

  @KitEvent(clazz = PlayerDeathEvent.class)
  @Override
  public void onPlayerKillsPlayer(KitPlayer killer, KitPlayer dead) {
    Player player = Bukkit.getPlayer(killer.getUUID());
    Player killed = Bukkit.getPlayer(dead.getUUID());
    final double previousHearts = player.getMaxHealth();
    player.setMaxHealth(previousHearts + killed.getMaxHealth());
    player.setHealth(player.getMaxHealth());
    Bukkit.getScheduler().runTaskLater(KitApi.getInstance().getPlugin(), () -> {
      player.setMaxHealth(previousHearts);
    }, time * 20);
  }

  @Override
  public void onDeactivation(KitPlayer kitPlayer) {
    Player player = Bukkit.getPlayer(kitPlayer.getUUID());
    player.setMaxHealth(20.0);
  }

  @Override
  public void onDisable(KitPlayer kitPlayer) {
    Player player = Bukkit.getPlayer(kitPlayer.getUUID());
    player.setMaxHealth(20.0);
  }

  @KitEvent(clazz = EntityDamageByEntityEvent.class)
  @Override
  public void onPlayerGetsAttackedByLivingEntity(EntityDamageByEntityEvent event, Player player, LivingEntity attacker) {
    double health = player.getMaxHealth() - event.getDamage();
    if (health <= 20.0) {
      player.setMaxHealth(20.0);
    } else {
      player.setMaxHealth(health);
    }
  }
}
