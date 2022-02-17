package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.items.KitItemBuilder;
import de.hglabor.plugins.kitapi.kit.settings.FloatArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import de.hglabor.plugins.kitapi.util.RotationUtils;
import de.hglabor.plugins.kitapi.util.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class SpyKit extends AbstractKit implements Listener {

  private final static String CAMERA = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2ZkMzNiZjNlMGE3Mzg1N2JlNzNhNTA4NmQyYTYyYzM5Nzg3ZGRhYTM4NTA0NjE1NWZjNjgxNTNjODJhNzZmYiJ9fX0=";
  public static final SpyKit INSTANCE = new SpyKit();
  private static final String cameraArmorStandKey = "cameraArmorStand";
  private static final String oldLocationKey = "oldLocationKey";
  private final ItemStack skull;
  @FloatArg(min = 0.0F)
  private final float cooldown;

  private SpyKit() {
    super("Spy");
    skull = new KitItemBuilder(Material.PLAYER_HEAD).setPlayerSkullSkin(CAMERA).build();
    setDisplayItem(skull);
    setMainKitItem(skull);
    cooldown = 35f;
  }

  @Override
  public void onDisable(KitPlayer kitPlayer) {
    ArmorStand cam = kitPlayer.getKitAttribute(cameraArmorStandKey);
    if(cam != null) {
      cam.remove();
    }
  }

  @Override
  public void onDeactivation(KitPlayer kitPlayer) {
    ArmorStand cam = kitPlayer.getKitAttribute(cameraArmorStandKey);
    if(cam != null) {
      cam.remove();
    }
  }

  @KitEvent
  @Override
  public void onPlayerRightClickKitItem(PlayerInteractEvent event, KitPlayer kitPlayer) {
    Player player = event.getPlayer();
    if (kitPlayer.getKitAttribute(cameraArmorStandKey) == null) {
      if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
        Block block = event.getClickedBlock();
        if (block != null) {
          ArmorStand cam = block.getWorld().spawn(block.getLocation().clone().add(0, 0, 0), ArmorStand.class);
          cam.setGravity(false);
          cam.getEquipment().setHelmet(skull);
          cam.setInvisible(true);
          cam.setCustomName(player.getName());
          cam.addScoreboardTag("camera#" + player.getName());
          RotationUtils.Rotation rotation = RotationUtils.getNeededRotations(player, cam);
          cam.setRotation(rotation.getYaw(), rotation.getPitch());
          for (ArmorStand.LockType lockType : ArmorStand.LockType.values()) {
            for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
              cam.addEquipmentLock(equipmentSlot, lockType);
            }
          }
          kitPlayer.putKitAttribute(cameraArmorStandKey, cam);
        }
      }
      player.sendMessage(Component.text("You can't place more than 1 camera").color(TextColor.color(255, 0, 0)));
    } else {
      if(player.getGameMode() != GameMode.SPECTATOR) {
        ArmorStand cam = kitPlayer.getKitAttribute(cameraArmorStandKey);
        RotationUtils.Rotation rotation = RotationUtils.getNeededRotations(player, cam);
        cam.setRotation(rotation.getYaw(), rotation.getPitch());
        kitPlayer.putKitAttribute(oldLocationKey, player.getLocation());
        player.setSneaking(false);
        player.setGameMode(GameMode.SPECTATOR);
        player.setSpectatorTarget(cam);
        player.setSneaking(false);
        cam.setCustomNameVisible(true);
      }
    }
  }

  @KitEvent
  @Override
  public void onPlayerIsSneakingEvent(PlayerToggleSneakEvent event, KitPlayer kitPlayer) {
    Player player = event.getPlayer();
    if(player.getGameMode() == GameMode.SPECTATOR) {
      player.teleport((Location) kitPlayer.getKitAttribute(oldLocationKey));
      player.setGameMode(GameMode.SURVIVAL);
      ArmorStand cam = kitPlayer.getKitAttribute(cameraArmorStandKey);
      cam.setCustomNameVisible(false);
      kitPlayer.activateKitCooldown(INSTANCE);
    }
  }

  @EventHandler
  public void onPlayerTeleport(PlayerTeleportEvent event) {
    KitPlayer kitPlayer = KitApi.getInstance().getPlayer(event.getPlayer());
    if (!event.getCause().equals(PlayerTeleportEvent.TeleportCause.SPECTATE)) {
      return;
    }
    if (kitPlayer.hasKit(INSTANCE)) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  @KitEvent(ignoreCooldown = true)
  public void onEntityDamage(EntityDamageEvent event) {
    if(event.getEntity() instanceof ArmorStand) {
      ArmorStand cam = (ArmorStand) event.getEntity();
      for (String tag : cam.getScoreboardTags()) {
        if (tag.startsWith("camera#")) {
          event.setCancelled(true);
          event.getEntity().remove();
          String name = tag.split("#")[1];
          Player player = Bukkit.getPlayer(name);
          if (player != null) {
            KitPlayer kitPlayer = KitApi.getInstance().getPlayer(player);
            kitPlayer.activateKitCooldown(INSTANCE);
            kitPlayer.putKitAttribute(cameraArmorStandKey, null);
            player.teleport((Location) kitPlayer.getKitAttribute(oldLocationKey));
            player.setGameMode(GameMode.SURVIVAL);
            break;
          }
        }
      }
    }
  }

  @Override
  public float getCooldown() {
    return this.cooldown;
  }
}
