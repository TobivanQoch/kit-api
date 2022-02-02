package de.hglabor.plugins.kitapi.kit.kits;

/*import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.settings.FloatArg;
import de.hglabor.plugins.kitapi.kit.settings.IntArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import de.hglabor.plugins.kitapi.util.pathfinder.LaborPathfinderMoveToLocation;
import de.hglabor.utils.noriskutils.pvpbots.PvPBot;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.PathfinderGoalSelector;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

public class MagicianKit extends AbstractKit {

  public static final MagicianKit INSTANCE = new MagicianKit();

  @FloatArg(min = 0.0F)
  private final float cooldown;
  @IntArg
  private int durationInSeconds;
  @IntArg
  private int validColorRgb;
  @IntArg
  private int invalidColorRgb;

  protected MagicianKit() {
    super("Magician", Material.DRAGON_BREATH);
    setMainKitItem(getDisplayMaterial());
    this.cooldown = 35f;
    this.durationInSeconds = 6;
    this.validColorRgb = 11585581;
    this.invalidColorRgb = 2547655;
  }

  @KitEvent
  @Override
  public void onPlayerRightClickKitItem(PlayerInteractEvent event, KitPlayer kitPlayer) {
    new MagicianClone(true, kitPlayer, MagicianDirection.POSITIVE_X, validColorRgb, invalidColorRgb).spawn(durationInSeconds);
    new MagicianClone(false, kitPlayer, MagicianDirection.NEGATIVE_X, validColorRgb, invalidColorRgb).spawn(durationInSeconds);
    new MagicianClone(false, kitPlayer, MagicianDirection.POSITIVE_Z, validColorRgb, invalidColorRgb).spawn(durationInSeconds);
    new MagicianClone(false, kitPlayer, MagicianDirection.NEGATIVE_Z, validColorRgb, invalidColorRgb).spawn(durationInSeconds);
    kitPlayer.activateKitCooldown(this);
  }

  @Override
  public float getCooldown() {
    return this.cooldown;
  }

  public enum MagicianDirection {

    POSITIVE_X,
    NEGATIVE_X,
    POSITIVE_Z,
    NEGATIVE_Z;

  }

  public static class MagicianClone {

    protected final boolean isValid;
    protected final KitPlayer kitPlayer;
    protected final MagicianDirection magicianDirection;
    protected final int validColorRgb;
    protected final int invalidColorRgb;

    public MagicianClone(boolean isValid, KitPlayer kitPlayer, MagicianDirection magicianDirection, int validColorRgb, int invalidColorRgb) {
      this.isValid = isValid;
      this.kitPlayer = kitPlayer;
      this.magicianDirection = magicianDirection;
      this.validColorRgb = validColorRgb;
      this.invalidColorRgb = invalidColorRgb;
    }

    public void spawn(int durationTime) {
      Optional<Player> optionalPlayer = kitPlayer.getBukkitPlayer();
      if (optionalPlayer.isPresent()) {
        Player player = optionalPlayer.get();
        PvPBot pvpBot = new PvPBot(player.getWorld(), player.getName(), player, KitApi.getInstance().getPlugin());
        pvpBot.withHealth(120);
        pvpBot.withMovementSpeed(0.17);
        pvpBot.spawn(player.getLocation().clone().add(0, 1, 0));
        Zombie zombie = pvpBot.getEntity();
        Mob mob = (Mob) zombie;
        net.minecraft.world.entity.Mob craftMonster = (net.minecraft.world.entity.Mob) ((CraftEntity) mob).getHandle();
        clearPathfinders(craftMonster);
        mob.setTarget(null);
        Location location = player.getLocation().clone();
        switch (magicianDirection) {
          case POSITIVE_X:
            location.add(15, 1, 0);
            break;
          case NEGATIVE_X:
            location.add(-15, 1, 0);
            break;
          case POSITIVE_Z:
            location.add(0, 1, 15);
            break;
          case NEGATIVE_Z:
            location.add(0, 1, -15);
            break;
        }
        craftMonster.goalSelector.addGoal(0, new LaborPathfinderMoveToLocation(location, craftMonster));
        craftMonster.goalSelector.addGoal(1, new FloatGoal(craftMonster));
        long activationTime = System.currentTimeMillis();
        new BukkitRunnable() {
          @Override
          public void run() {
            if (System.currentTimeMillis() >= activationTime + durationTime * 1000D || !kitPlayer.isValid()) {
              pvpBot.die(DamageSource.GENERIC);
              cancel();
              for (Player p : Bukkit.getOnlinePlayers().stream().filter(it -> it != player).toList()) {
                p.showPlayer(KitApi.getInstance().getPlugin(), player);
              }
            } else {
              if (isValid) {
                for (Player p : Bukkit.getOnlinePlayers().stream().filter(it -> it != player).toList()) {
                  p.hidePlayer(KitApi.getInstance().getPlugin(), player);
                }
                if (!zombie.getPassengers().contains(player)) {
                  zombie.addPassenger(player);
                }
              }
              zombie.getWorld().spawnParticle(Particle.REDSTONE, zombie.getLocation().clone().add(0, 0.5, 0), 0, new Particle.DustOptions(Color.fromBGR(isValid ? validColorRgb : invalidColorRgb), 1.5f));
            }
          }
        }.runTaskTimer(KitApi.getInstance().getPlugin(), 1, 1);
      }
    }

    private void clearPathfinders(EntityInsentient entity) {
      entity.goalSelector = new PathfinderGoalSelector(entity.getWorld().getMethodProfilerSupplier());
      entity.targetSelector = new PathfinderGoalSelector(entity.getWorld().getMethodProfilerSupplier());
    }
  }
}*/
