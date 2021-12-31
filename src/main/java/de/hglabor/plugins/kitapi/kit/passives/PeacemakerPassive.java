package de.hglabor.plugins.kitapi.kit.passives;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.Passive;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.items.KitItemBuilder;
import de.hglabor.plugins.kitapi.kit.settings.BetaKit;
import de.hglabor.plugins.kitapi.kit.settings.FloatArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

@BetaKit
public class PeacemakerPassive extends Passive {
  public static final PeacemakerPassive INSTANCE = new PeacemakerPassive();
  private final static String HEART_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjRlYWY4OTQyOGEzNjQ5YzI2ZWRjMWY3MWNjMTlmMjYzZTlmNGViMzFlZDE4Yzk3Njg2YWFjODJmNzY0MjQyIn19fQ==";

  @FloatArg
  private final float damageMultiplier;

  private PeacemakerPassive() {
    super("Peacemaker", new KitItemBuilder(Material.PLAYER_HEAD).setPlayerSkullSkin(HEART_HEAD).build());
    this.damageMultiplier = 0.7F;
  }

  @KitEvent
  public void onEntityDamage(EntityDamageEvent event) {
    if (event.getEntity() instanceof Player) {
      if (hasPassive(KitApi.getInstance().getPlayer((Player) (event.getEntity())))) {
        event.setDamage(event.getDamage() * damageMultiplier);
      }
    }
  }

  @KitEvent
  public void onPlayerAttacksLivingEntity(EntityDamageByEntityEvent event, KitPlayer attacker, LivingEntity entity) {
    if (hasPassive(attacker)) {
      event.setDamage(event.getDamage() * damageMultiplier);
    }
  }

  private boolean hasPassive(KitPlayer kitPlayer) {
    return kitPlayer.getPassive().equals(INSTANCE);
  }

}
