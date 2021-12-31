package de.hglabor.plugins.kitapi.kit.passives;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.Passive;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Illusioner;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.Pillager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;

public class MonsterPassive extends Passive implements Listener {

  public static final MonsterPassive INSTANCE = new MonsterPassive();

  private MonsterPassive() {
    super("Monster", Material.ZOMBIE_HEAD);
  }

  @EventHandler
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    if (event.getEntity() instanceof Player) {
      if (hasPassive(KitApi.getInstance().getPlayer((Player) event.getEntity()))) {
        Entity damager = event.getDamager();
        if (damager instanceof Monster || event.getCause() == EntityDamageEvent.DamageCause.MAGIC || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
          event.setCancelled(true);
        }
      }
    }
  }

  @EventHandler
  public void onProjectileHit(ProjectileHitEvent event) {
    Entity hitEntity = event.getHitEntity();
    if (hitEntity != null) {
      if (hasPassive(KitApi.getInstance().getPlayer((Player) hitEntity))) {
        Projectile projectile = event.getEntity();
        if (isForbiddenShooter(projectile.getShooter())) {
          event.setCancelled(true);
        }
      }
    }
  }

  private boolean isForbiddenShooter(ProjectileSource projectileSource) {
    return projectileSource instanceof Skeleton || projectileSource instanceof Illusioner || projectileSource instanceof Piglin || projectileSource instanceof Pillager || projectileSource instanceof Blaze || projectileSource instanceof Wither;
  }

  private boolean hasPassive(KitPlayer kitPlayer) {
    return kitPlayer.getPassive().equals(MonsterPassive.INSTANCE);
  }
}
