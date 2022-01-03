package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.settings.FloatArg;
import de.hglabor.plugins.kitapi.kit.settings.IntArg;
import de.hglabor.plugins.kitapi.kit.settings.MaterialArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class JellyfishKit extends AbstractKit implements Listener {

  public static final JellyfishKit INSTANCE = new JellyfishKit();

  @IntArg
  private final int waterRemoveDeleay;
  @IntArg
  private final int maxUses;
  @FloatArg(min = 0.0F)
  private final float cooldown;
  @MaterialArg
  private final Material liquidMaterial;

  private JellyfishKit() {
    super("Jellyfish", Material.PUFFERFISH_BUCKET);
    maxUses = 10;
    cooldown = 30F;
    waterRemoveDeleay = 50;
    liquidMaterial = Material.WATER;
  }

  private static final String WATER_KEY = "jellyfishWater";

  @KitEvent
  @Override
  public void onPlayerRightClicksBlock(PlayerInteractEvent event, KitPlayer kitPlayer, Block block) {
    Player player = event.getPlayer();
    event.getClickedBlock().getRelative(BlockFace.UP).setType(liquidMaterial);
    Bukkit.getScheduler().runTaskLater(KitApi.getInstance().getPlugin(), () -> event.getClickedBlock().getRelative(BlockFace.UP).setType(Material.AIR), waterRemoveDeleay);
    event.getClickedBlock().setMetadata(WATER_KEY, new FixedMetadataValue(KitApi.getInstance().getPlugin(),true));
    KitApi.getInstance().checkUsesForCooldown(player, this, maxUses);
  }

  @Override
  public float getCooldown() {
    return cooldown;
  }

  @EventHandler
  @KitEvent(ignoreCooldown = true, clazz = BlockFromToEvent.class)
  public void onBlockFromTo(BlockFromToEvent event) {
    if(event.getBlock().getType() == liquidMaterial) {
      if(event.getBlock().hasMetadata(WATER_KEY)) {
        event.setCancelled(true);
      }
    }
  }
}
