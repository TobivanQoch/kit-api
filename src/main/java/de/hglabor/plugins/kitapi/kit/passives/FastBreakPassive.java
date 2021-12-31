package de.hglabor.plugins.kitapi.kit.passives;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.Passive;
import de.hglabor.plugins.kitapi.kit.settings.IntArg;
import de.hglabor.plugins.kitapi.kit.settings.PotionEffectArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FastBreakPassive extends Passive implements Listener {

  public static final FastBreakPassive INSTANCE = new FastBreakPassive();

  @IntArg
  private final int effectDuration, effectAmpflier;

  @PotionEffectArg
  private final PotionEffectType effectType;

  private FastBreakPassive() {
    super("FastBreak", Material.NETHERITE_AXE);
    this.effectDuration = 60;
    this.effectAmpflier = 2;
    this.effectType = PotionEffectType.FAST_DIGGING;
  }

  @EventHandler
  public void onPlayerLeftClickBlock(PlayerInteractEvent event) {
    KitPlayer kitPlayer = KitApi.getInstance().getPlayer(event.getPlayer());
    if (kitPlayer.getPassive().equals(FastBreakPassive.INSTANCE)) {
      if (event.hasBlock() && event.getClickedBlock() != null) {
        if (event.getClickedBlock().getType().name().contains("WOOD") || event.getClickedBlock().getType().name().contains("LOG") || event.getClickedBlock().getType().name().contains("STEM")) {
          kitPlayer.getBukkitPlayer().ifPresent(it -> it.addPotionEffect(new PotionEffect(effectType, effectDuration, effectAmpflier - 1, false, false)));
        }
      }
    }

  }

}
