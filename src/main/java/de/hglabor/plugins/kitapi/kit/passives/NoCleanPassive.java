package de.hglabor.plugins.kitapi.kit.passives;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.Passive;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NoCleanPassive extends Passive implements Listener {

  public static final NoCleanPassive INSTANCE = new NoCleanPassive();

  private NoCleanPassive() {
    super("NoClean", Material.IRON_SWORD);
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    Player player = event.getEntity().getKiller();
    if (player != null && hasPassive(KitApi.getInstance().getPlayer(player))) {
      player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 3000));
    }
  }

  private boolean hasPassive(KitPlayer kitPlayer) {
    return kitPlayer.getPassive().equals(NoCleanPassive.INSTANCE);
  }
}
