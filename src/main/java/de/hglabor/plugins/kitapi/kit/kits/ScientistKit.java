package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.items.KitItemBuilder;
import de.hglabor.plugins.kitapi.kit.settings.FloatArg;
import de.hglabor.plugins.kitapi.kit.settings.IntArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import de.hglabor.plugins.kitapi.util.RotationUtils;
import de.hglabor.plugins.kitapi.util.Utils;
import de.hglabor.utils.noriskutils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ScientistKit extends AbstractKit implements Listener {

  public static final ScientistKit INSTANCE = new ScientistKit();
  private static final ArrayList<ArmorStand> TURRETS = new ArrayList<>();
  private static final String remainingTurretsKey = "remainingTurrets";
  @IntArg
  private final int turretAmount, shotDelay, radius;
  private final ItemStack skull;
  @FloatArg
  private final float shotDamage;

  private ScientistKit() {
    super("Scientist");
    skull = new KitItemBuilder(Material.PLAYER_HEAD).setPlayerSkullSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmEyYzNlNzlkNWYzNWE5ZGNhYjE5ZTQzYzNlM2E2NTE5ZTQyNmI2NGE2MTIxM2NkMmYxZDI4YjU3MDM2ZjYifX19").build();
    setDisplayItem(skull);
    setMainKitItem(skull);
    turretAmount = 3;
    shotDelay = 40;
    shotDamage = 2;
    radius = 15;
  }

  @Override
  public void onEnable(KitPlayer kitPlayer) {
    kitPlayer.putKitAttribute(remainingTurretsKey, turretAmount);
  }

  @Override
  public void onDisable(KitPlayer kitPlayer) {
    kitPlayer.getBukkitPlayer().ifPresent(it -> {
      for (ArmorStand turret : Utils.cloneList(TURRETS)) {
        for (String tag : turret.getScoreboardTags()) {
          if (tag.equalsIgnoreCase("turret#" + kitPlayer.getName())) {
            TURRETS.remove(turret);
            turret.remove();
          }
        }
      }
    });
  }

  @Override
  public void onDeactivation(KitPlayer kitPlayer) {
    kitPlayer.getBukkitPlayer().ifPresent(it -> {
      for (ArmorStand turret : Utils.cloneList(TURRETS)) {
        for (String tag : turret.getScoreboardTags()) {
          if (tag.equalsIgnoreCase("turret#" + kitPlayer.getName())) {
            TURRETS.remove(turret);
            turret.remove();
          }
        }
      }
    });
  }

  @KitEvent
  @Override
  public void onPlayerRightClickKitItem(PlayerInteractEvent event, KitPlayer kitPlayer) {
    Player player = event.getPlayer();
    Integer remainingTurrets = kitPlayer.getKitAttribute(remainingTurretsKey);
    if (remainingTurrets != null) {
      if (remainingTurrets > 0) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
          Block block = event.getClickedBlock();
          if (block != null) {
            ArmorStand turret = block.getWorld().spawn(block.getLocation().clone().add(0, 1, 0), ArmorStand.class);
            turret.setSmall(true);
            turret.setArms(true);
            turret.getEquipment().setHelmet(skull);
            turret.getEquipment().setChestplate(new ItemBuilder(Material.IRON_CHESTPLATE).build());
            turret.getEquipment().setLeggings(new ItemBuilder(Material.IRON_LEGGINGS).build());
            turret.getEquipment().setBoots(new ItemBuilder(Material.NETHERITE_BOOTS).build());
            turret.setItem(EquipmentSlot.OFF_HAND, new ItemStack(Material.BOW));
            for (ArmorStand.LockType lockType : ArmorStand.LockType.values()) {
              for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                turret.addEquipmentLock(equipmentSlot, lockType);
              }
            }
            turret.addScoreboardTag("turret#" + player.getName());
            turret.setLeftArmPose(new EulerAngle(-30f, 330f, 0f));
            TURRETS.add(turret);
            remainingTurrets -= 1;
            kitPlayer.putKitAttribute(remainingTurretsKey, remainingTurrets);
            AtomicInteger tick = new AtomicInteger();
            Bukkit.getScheduler().runTaskTimer(KitApi.getInstance().getPlugin(), task -> {
              if (turret.isDead()) {
                TURRETS.remove(turret);
                task.cancel();
              }
              tick.getAndIncrement();
              for (double i = 0; i < 0.5; i++) {
                for (double y = 0.0; y < (Math.PI * 2); y += .1) {
                  double x = radius * Math.cos(y);
                  double z = radius * Math.sin(y);
                  turret.getWorld().spawnParticle(Particle.REDSTONE, turret.getLocation().clone().add(x, i / 4, z), 0, 0, 0, 0, 5, new Particle.DustOptions(Color.RED, 1f));
                }
              }
              for (Entity nearby : turret.getNearbyEntities(radius, radius, radius)) {
                if (nearby instanceof Player) {
                  if (nearby.getUniqueId() == player.getUniqueId()) {
                    continue;
                  }
                  RotationUtils.Rotation neededRotation = RotationUtils.getNeededRotations(nearby, turret);
                  turret.setRotation(neededRotation.getYaw(), neededRotation.getPitch());
                  if (tick.get() == shotDelay) {
                    tick.set(0);
                    Arrow arrow = turret.launchProjectile(Arrow.class, turret.getLocation().getDirection().multiply(2));
                    arrow.setRotation(neededRotation.getYaw(), neededRotation.getPitch());
                    arrow.setShooter(player);
                    arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                    arrow.setDamage(shotDamage);
                    arrow.setGravity(false);
                  }
                  break;
                }
              }
            }, 20, 1);
            return;
          }
        }
      }
    }
    player.sendMessage(Component.text("You don't have any remaining turrets").color(TextColor.color(255, 0, 0)));
  }

  @EventHandler
  public void onEntityDamage(EntityDamageEvent event) {
    if (event.getEntity() instanceof ArmorStand) {
      if (TURRETS.contains((ArmorStand) event.getEntity())) {
        event.setCancelled(true);
        event.getEntity().remove();
        for (ArmorStand turret : Utils.cloneList(TURRETS)) {
          if (turret.getUniqueId() == event.getEntity().getUniqueId()) {
            TURRETS.remove(turret);
          }
          for (String tag : turret.getScoreboardTags()) {
            if (tag.startsWith("turret#")) {
              String name = tag.split("#")[1];
              Player player = Bukkit.getPlayer(name);
              if (player != null) {
                player.sendMessage(Component.text("One of your turrets got destroyed").color(TextColor.color(0, 0, 255)));
                break;
              }
            }
          }
        }
      }
    }
  }

}
