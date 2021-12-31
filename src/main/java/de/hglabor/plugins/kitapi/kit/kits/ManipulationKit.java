package de.hglabor.plugins.kitapi.kit.kits;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import com.google.common.collect.ImmutableMap;
import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.config.KitMetaData;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.settings.IntArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import de.hglabor.plugins.kitapi.util.pathfinder.LaborPathfinderFindTarget;
import de.hglabor.plugins.kitapi.util.pathfinder.LaborPathfinderGhastAttack;
import de.hglabor.plugins.kitapi.util.pathfinder.LaborPathfinderGoalBlazeFireball;
import de.hglabor.plugins.kitapi.util.pathfinder.LaborPathfinderGoalGhastIdleMove;
import de.hglabor.plugins.kitapi.util.pathfinder.LaborPathfinderMoveToPlayer;
import de.hglabor.utils.localization.Localization;
import de.hglabor.utils.noriskutils.ChatUtils;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Ghast;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static de.hglabor.utils.localization.Localization.t;

public class ManipulationKit extends AbstractKit implements Listener {
  public final static ManipulationKit INSTANCE = new ManipulationKit();
  @IntArg
  private final int maxManipulatedMobs;
  private final String collectedMobsKey;

  public ManipulationKit() {
    super("Manipulation", Material.IRON_NUGGET);
    setMainKitItem(getDisplayMaterial());
    collectedMobsKey = this.getName() + "collectedMobs";
    maxManipulatedMobs = 4;
  }

  @Override
  public void onEnable(KitPlayer kitPlayer) {
    kitPlayer.putKitAttribute(collectedMobsKey, new HashSet<UUID>());
  }

  @Override
  public void onDeactivation(KitPlayer kitPlayer) {
    Set<UUID> controlledMobs = kitPlayer.getKitAttribute(collectedMobsKey);
    if (controlledMobs == null) {
      return;
    }
    for (UUID controlledMob : controlledMobs) {
      Entity entity = Bukkit.getEntity(controlledMob);
      if (entity != null) {
        entity.remove();
      }
    }
  }

  @KitEvent
  @Override
  public void onPlayerRightClickLivingEntityWithKitItem(PlayerInteractAtEntityEvent event, KitPlayer kitPlayer, LivingEntity entity) {
    Player player = event.getPlayer();
    if (entity instanceof Mob) {
      Mob mob = (Mob) event.getRightClicked();
      net.minecraft.world.entity.Mob craftMonster = (net.minecraft.world.entity.Mob) ((CraftEntity) mob).getHandle();

      if (isManipulatedMob(mob)) {
        Player manipulator = getManipulator(mob);
        if (manipulator != null && manipulator.getUniqueId().equals(player.getUniqueId())) {
          //NACHRICHT KOMMT 2x WEGEN 2 HÄNDEN
          player.sendMessage(t("manipulator.alreadyYourMob", ChatUtils.locale(player)));
          return;
        }
        player.sendMessage(t("manipulator.alreadyControlled", ChatUtils.locale(player)));
        return;
      }

      if (getManipulatedMobAmount(player) >= maxManipulatedMobs) {
        player.sendMessage(t("manipulator.maxAmount", ChatUtils.locale(player)));
        return;
      }

      mob.setMetadata(KitMetaData.MANIPULATED_MOB.getKey(), new FixedMetadataValue(KitApi.getInstance().getPlugin(), ""));
      mob.setMetadata(player.getUniqueId().toString(), new FixedMetadataValue(KitApi.getInstance().getPlugin(), ""));

      addMob(player, mob);
      boolean attack = mob.getType() != EntityType.CREEPER && mob.getType() != EntityType.SKELETON;

      // Clear pathfinders to apply ours.
      clearPathfinders(craftMonster);
      mob.setTarget(null);
      craftMonster.goalSelector.addGoal(0, new LaborPathfinderMoveToPlayer(((CraftPlayer) player).getHandle(), craftMonster));
      craftMonster.goalSelector.addGoal(1, new FloatGoal(craftMonster));
      craftMonster.targetSelector.addGoal(0, new LaborPathfinderFindTarget(craftMonster, player.getUniqueId(), attack));

      //TODO ghast wont shoot and creeper doesnt explode
      switch (mob.getType()) {
        case HUSK:
        case ZOMBIE:
        case ENDERMAN:
          craftMonster.goalSelector.addGoal(3, new MeleeAttackGoal((PathfinderMob) craftMonster, 0.3, false));
          break;
        case SKELETON:
          craftMonster.goalSelector.addGoal(3, new RangedBowAttackGoal<>((AbstractSkeleton) craftMonster, 1.0D, 20, 15.0F));
          break;
        case CREEPER:
          ((Creeper) mob).setPowered(false);
          ((Creeper) mob).setIgnited(false);
          craftMonster.goalSelector.addGoal(3, new SwellGoal((net.minecraft.world.entity.monster.Creeper) craftMonster));
          break;
        case BLAZE:
          craftMonster.goalSelector.addGoal(3, new LaborPathfinderGoalBlazeFireball((Blaze) craftMonster));
          break;
        case GHAST:
          craftMonster.goalSelector.addGoal(3, new LaborPathfinderGhastAttack((Ghast) craftMonster));
          craftMonster.goalSelector.addGoal(4, new LaborPathfinderGoalGhastIdleMove((Ghast) craftMonster));
          break;
        default:
          break;
      }

      player.getWorld().spawnParticle(Particle.HEART, mob.getEyeLocation(), 1);
      player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 10, 10);
    }
  }


  @EventHandler
  public void onEntityDespawn(EntityRemoveFromWorldEvent event) {
    if (!(event.getEntity() instanceof Mob mob)) {
      return;
    }
    if (isManipulatedMob(mob)) {
      removeMob(mob);
      if (getManipulator(mob) == null) {
        Player manipulator = getManipulator(mob);
        if (manipulator != null) {
          manipulator.sendMessage(Localization.INSTANCE.getMessage("manipulator.mobLoose",
            ImmutableMap.of("amount", String.valueOf(getManipulatedMobAmount(manipulator))),
            ChatUtils.locale(manipulator)));
        }
      }
    }
  }

  private void addMob(Player player, Entity mob) {
    KitPlayer kitPlayer = KitApi.getInstance().getPlayer(player);
    Set<UUID> controlledMobs = kitPlayer.getKitAttribute(collectedMobsKey);
    controlledMobs.add(mob.getUniqueId());
  }

  private void removeMob(Mob mob) {
    Player player = getManipulator(mob);
    if (player != null) {
      KitPlayer kitPlayer = KitApi.getInstance().getPlayer(player);
      Set<UUID> controlledMobs = kitPlayer.getKitAttribute(collectedMobsKey);
      controlledMobs.remove(mob.getUniqueId());
    }
  }

  private int getManipulatedMobAmount(Player player) {
    KitPlayer kitPlayer = KitApi.getInstance().getPlayer(player);
    Set<UUID> controlledMobs = kitPlayer.getKitAttribute(collectedMobsKey);
    return controlledMobs.size();
  }

  private boolean isManipulatedMob(Mob entity) {
    return entity.hasMetadata(KitMetaData.MANIPULATED_MOB.getKey());
  }

  private Player getManipulator(Mob entity) {
    for (Player player : Bukkit.getOnlinePlayers()) {
      KitPlayer kitPlayer = KitApi.getInstance().getPlayer(player);
      if (kitPlayer.isValid() && entity.hasMetadata(kitPlayer.getUUID().toString())) {
        return player;
      }
    }
    return null;
  }

  public void clearPathfinders(net.minecraft.world.entity.Mob entity) {
    entity.goalSelector = new GoalSelector(entity.level.getProfilerSupplier());
    entity.targetSelector = new GoalSelector(entity.level.getProfilerSupplier());
  }
}
