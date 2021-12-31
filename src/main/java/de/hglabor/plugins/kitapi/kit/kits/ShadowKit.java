package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.settings.DoubleArg;
import de.hglabor.plugins.kitapi.kit.settings.FloatArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ShadowKit extends AbstractKit {

  public static final ShadowKit INSTANCE = new ShadowKit();

  @FloatArg
  private final float cooldown;

  @DoubleArg(min = 1.0, max = 100.0)
  private final double radius;

  private ShadowKit() {
    super("Shadow", Material.CHARCOAL);
    setMainKitItem(getDisplayMaterial());
    cooldown = 30f;
    radius = 30.0;
  }

  @KitEvent
  @Override
  public void onPlayerRightClickKitItem(PlayerInteractEvent event, KitPlayer kitPlayer) {
    Player player = event.getPlayer();
    for (LivingEntity nearby : player.getWorld().getNearbyLivingEntities(player.getLocation(), radius)) {
      if (nearby instanceof Player) {
        KitPlayer nearbyKitPlayer = KitApi.getInstance().getPlayer((Player) nearby);
        if (!nearbyKitPlayer.isValid()) {
          continue;
        }
        if (nearby.getUniqueId() == player.getUniqueId()) {
          continue;
        }
        nearby.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 5 * 20, 3, false, false));
        for (int i = 0; i < 3; i++) {
          nearby.getWorld().spawnParticle(Particle.SMOKE_LARGE, nearby.getLocation(), 6);
          player.getWorld().spawnParticle(Particle.SMOKE_LARGE, player.getLocation(), 6);
        }
      }

    }
    kitPlayer.activateKitCooldown(this);
  }

  @Override
  public float getCooldown() {
    return cooldown;
  }
}
