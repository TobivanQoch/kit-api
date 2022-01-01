package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.settings.FloatArg;
import de.hglabor.plugins.kitapi.kit.settings.IntArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class JellyfishKit extends AbstractKit implements Listener {

  public static final JellyfishKit INSTANCE = new JellyfishKit();

  @IntArg
  private final long waterRemoveDeleay;
  @IntArg
  private final int maxUses;
  @FloatArg(min = 0.0F)
  private final float cooldown;

  private JellyfishKit() {
    super("Jellyfish", Material.PUFFERFISH_BUCKET);
    maxUses = 10;
    cooldown = 30F;
    waterRemoveDeleay = 50L;
  }

  private static final String WATER_KEY = "jellyfishWater";

  @EventHandler
  @KitEvent
  public void onPlayerInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    KitPlayer kitPlayer = KitApi.getInstance().getPlayer(player);
    if(!kitPlayer.hasKit(this)) {
      return;
    }
    if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) {
      return;
    }
    if(event.getClickedBlock() == null) {
      return;
    }
    Material type = event.getClickedBlock().getType();
    event.getClickedBlock().setType(Material.WATER);
    Bukkit.getScheduler().runTaskLater(KitApi.getInstance().getPlugin(), () -> {
      event.getClickedBlock().setType(type);
    }, waterRemoveDeleay);
    event.getClickedBlock().setMetadata(WATER_KEY, new FixedMetadataValue(KitApi.getInstance().getPlugin(),true));
    KitApi.getInstance().checkUsesForCooldown(player, this, maxUses);
  }

  @Override
  public float getCooldown() {
    return cooldown;
  }

  @EventHandler
  public void onBlockFromTo(BlockFromToEvent event) {
    if(event.getBlock().getType() == Material.WATER) {
      if(event.getBlock().hasMetadata(WATER_KEY)) {
        event.setCancelled(true);
      }
    }
  }
}
