package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.items.KitItemBuilder;
import de.hglabor.plugins.kitapi.kit.settings.BetaKit;
import de.hglabor.plugins.kitapi.kit.settings.IntArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import de.hglabor.plugins.kitapi.util.BukkitUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.UUID;

import static de.hglabor.plugins.kitapi.util.BukkitUtils.optionalPlayer;

@BetaKit
public class VentKit extends AbstractKit implements Listener {
  public final static VentKit INSTANCE = new VentKit();
  private final static String IMPOSTER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdkNWViMGFlYTVkNjFiYTNmZjQ5OTY0MTZhOTAwOTZhOWQ3NzYwOWViY2QzYjMwOGY5MDZhZTg4OGE0NWY2ZCJ9fX0=";
  private static final String VENT_OWNER = "vent-owner";
  private static final String VENT_PAIR = "vent-pair";
  private final String lastVentKey;
  private final String ventListKey;

  @IntArg
  private final int maxAmountOfVentPairs;

  private VentKit() {
    super("Vent");
    setDisplayItem(new KitItemBuilder(Material.PLAYER_HEAD).setPlayerSkullSkin(IMPOSTER_HEAD).build());
    setMainKitItem(Material.IRON_TRAPDOOR);
    this.maxAmountOfVentPairs = 4;
    this.ventListKey = "ventListKey" + getName();
    this.lastVentKey = "lastVentKey" + getName();
  }

  @Override
  public void onDeactivation(KitPlayer kitPlayer) {
    ArrayList<Block> ventBlocks = kitPlayer.getKitAttributeOrDefault(ventListKey, new ArrayList<>());
    for (Block ventBlock : ventBlocks) {
      if (ventBlock.hasMetadata(VENT_OWNER) || ventBlock.hasMetadata(VENT_PAIR)) {
        ventBlock.removeMetadata(VENT_OWNER, KitApi.getInstance().getPlugin());
        ventBlock.removeMetadata(VENT_PAIR, KitApi.getInstance().getPlugin());
        if (ventBlock.getType().equals(Material.IRON_TRAPDOOR)) {
          // TODO ppPoof effect
          ventBlock.setType(Material.AIR);
        }
      }
    }
  }

  // TODO this doesnt work on explosion, digger (or any other kit which does setAir)
  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    if (!isEnabled()) {
      return;
    }
    removeVentAndNotify(event.getBlock());
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }
    if (event.getClickedBlock() != null && event.getClickedBlock().hasMetadata(VENT_PAIR)) {
      Player player = event.getPlayer();
      player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_TRAPDOOR_OPEN, 1, 1);
      Block vent = event.getClickedBlock();
      Block ventPair = (Block) vent.getMetadata(VENT_PAIR).get(0).value();
      if (ventPair != null) {
        player.teleport(ventPair.getLocation());
        UUID ventOwnerId = getVentOwnerId(vent);
        if (ventOwnerId != player.getUniqueId()) {
          optionalPlayer(ventOwnerId).ifPresent(imposter -> {
            imposter.playSound(imposter.getLocation(), Sound.BLOCK_WOODEN_TRAPDOOR_OPEN, 1, 1);
            imposter.sendMessage(ChatColor.GRAY + player.getName() + " hat deinen Vent benutzt.");
          });
        }
      }
    }
  }

  @KitEvent
  public void onPlayerRightClickKitItem(PlayerInteractEvent event, KitPlayer kitPlayer) {
    Player player = event.getPlayer();
    ArrayList<Block> ventBlocks = kitPlayer.getKitAttributeOrDefault(ventListKey, new ArrayList<>());

    if (ventBlocks.size() >= maxAmountOfVentPairs) {
      player.sendMessage(ChatColor.RED + "Du hast die maximale Anzahl an Vents platziert.");
      return;
    }

    if (event.getClickedBlock() != null) {
      Block clickedBlock = event.getClickedBlock().getRelative(event.getBlockFace());
      clickedBlock.setType(Material.IRON_TRAPDOOR);
      clickedBlock.setMetadata(VENT_OWNER, new FixedMetadataValue(KitApi.getInstance().getPlugin(), player.getUniqueId()));

      ventBlocks.add(clickedBlock);
      kitPlayer.putKitAttribute(ventListKey, ventBlocks);

      Block lastVent = kitPlayer.getKitAttribute(lastVentKey);
      if (lastVent != null) {
        lastVent.setMetadata(VENT_PAIR, new FixedMetadataValue(KitApi.getInstance().getPlugin(), clickedBlock));
        clickedBlock.setMetadata(VENT_PAIR, new FixedMetadataValue(KitApi.getInstance().getPlugin(), lastVent));
        BukkitUtils.playSound(player, Sound.BLOCK_BEACON_ACTIVATE);
        player.sendMessage(ChatColor.GREEN + "Vents erfolgreich verbunden");
        kitPlayer.putKitAttribute(lastVentKey, null);
      } else {
        BukkitUtils.playSound(player, Sound.BLOCK_METAL_PLACE);
        player.sendMessage(ChatColor.GREEN + "Erster Vent erfolgreich platziert.");
        kitPlayer.putKitAttribute(lastVentKey, clickedBlock);
      }
    }
  }

  private void removeVentAndNotify(Block block) {
    if (hasVentOwner(block)) {
      block.setType(Material.AIR);
      UUID ventOwnerId = getVentOwnerId(block);
      block.removeMetadata(VENT_OWNER, KitApi.getInstance().getPlugin());

      KitPlayer kitPlayer = KitApi.getInstance().getPlayerSupplier().getKitPlayer(ventOwnerId);

      ArrayList<Block> ventBlocks = kitPlayer.getKitAttributeOrDefault(ventListKey, new ArrayList<>());
      ventBlocks.remove(block);
      kitPlayer.putKitAttribute(ventListKey, ventBlocks);

      if (hasVentPair(block)) {
        Block ventPair = getVentPair(block);
        ventPair.removeMetadata(VENT_PAIR, KitApi.getInstance().getPlugin());
        block.removeMetadata(VENT_PAIR, KitApi.getInstance().getPlugin());
        removeVentAndNotify(ventPair);
      }

      optionalPlayer(ventOwnerId).ifPresent(player -> {
        BukkitUtils.playSound(player, Sound.BLOCK_BEACON_DEACTIVATE, 1, 1);
        player.sendMessage(ChatColor.RED + "Dein Vent bei " + printVentLocation(block) + ChatColor.RED + " wurde zerstört.");
      });
    }
  }

  private String printVentLocation(Block block) {
    Location location = block.getLocation();
    return ChatColor.GOLD + "[X: " + location.getBlockX() + " Y: " + location.getBlockY() + " Z: " + location.getBlockZ() + "]";
  }

  private boolean hasVentOwner(Block block) {
    return block.hasMetadata(VENT_OWNER);
  }

  private boolean hasVentPair(Block block) {
    return block.hasMetadata(VENT_PAIR);
  }

  private UUID getVentOwnerId(Block block) {
    return (UUID) block.getMetadata(VENT_OWNER).get(0).value();
  }

  private Block getVentPair(Block block) {
    return (Block) block.getMetadata(VENT_PAIR).get(0).value();
  }
}
