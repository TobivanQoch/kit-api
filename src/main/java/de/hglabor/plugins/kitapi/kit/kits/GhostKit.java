package de.hglabor.plugins.kitapi.kit.kits;

import com.destroystokyo.paper.event.player.PlayerStartSpectatingEntityEvent;
import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.items.KitItemBuilder;
import de.hglabor.plugins.kitapi.kit.settings.*;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import de.hglabor.utils.noriskutils.TimeConverter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

@BetaKit
public class GhostKit extends AbstractKit implements Listener {
  public final static GhostKit INSTANCE = new GhostKit();
  private final static String GHOST_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjhkMjE4MzY0MDIxOGFiMzMwYWM1NmQyYWFiN2UyOWE5NzkwYTU0NWY2OTE2MTllMzg1NzhlYTRhNjlhZTBiNiJ9fX0=";
  @FloatArg
  private final float cooldown;
  private final String isGhostModeKey;
  private final String ghostModeTaskKey;
  @IntArg
  private final int transformationParticleAmount, transformationParticleLoopAmount, trailParticleAmount, transformationInSeconds;
  @ParticleArg
  private final Particle particle;
  @SoundArg
  private final Sound sound;

  private GhostKit() {
    super("Ghost");
    ItemStack ghostHead = new KitItemBuilder(Material.PLAYER_HEAD).setPlayerSkullSkin(GHOST_HEAD).build();
    setDisplayItem(ghostHead);
    setMainKitItem(ghostHead);
    this.cooldown = 30F;
    this.isGhostModeKey = "isGhostMode" + getName();
    this.ghostModeTaskKey = "ghostMode" + getName();
    this.transformationParticleAmount = 30;
    this.transformationParticleLoopAmount = 15;
    this.trailParticleAmount = 5;
    this.transformationInSeconds = 5;
    this.particle = Particle.WHITE_ASH;
    this.sound = Sound.ENTITY_VEX_AMBIENT;
  }

  @Override
  public void onEnable(KitPlayer kitPlayer) {
    //kitPlayer.getBukkitPlayer().ifPresent(player -> player.setSilent(true));
  }

  @Override
  public void onDisable(KitPlayer kitPlayer) {
    //kitPlayer.getBukkitPlayer().ifPresent(player -> player.setSilent(false));
    GhostMode task = kitPlayer.getKitAttribute(ghostModeTaskKey);
    if (task != null) {
      task.end();
    }
  }

  @KitEvent
  @Override
  public void onPlayerLeftClickKitItem(PlayerInteractEvent event, KitPlayer kitPlayer) {
    doGhostTransformation(kitPlayer);
  }

  @KitEvent
  @Override
  public void onPlayerRightClickKitItem(PlayerInteractEvent event, KitPlayer kitPlayer) {
    doGhostTransformation(kitPlayer);
  }

  @KitEvent(ignoreCooldown = true)
  @Override
  public void onPlayerMoveEvent(PlayerMoveEvent event, KitPlayer kitPlayer) {
    if (kitPlayer.getKitAttributeOrDefault(isGhostModeKey, false)) {
      Player player = event.getPlayer();
      player.getWorld().spawnParticle(this.particle, player.getLocation(), this.trailParticleAmount);
    }
  }

  @EventHandler
  public void onPlayerStartSpectatingEntity(PlayerStartSpectatingEntityEvent event) {
    KitPlayer kitPlayer = KitApi.getInstance().getPlayer(event.getPlayer());
    if (kitPlayer.getKitAttributeOrDefault(isGhostModeKey, false)) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onPlayerTeleport(PlayerTeleportEvent event) {
    KitPlayer kitPlayer = KitApi.getInstance().getPlayer(event.getPlayer());
    if (!event.getCause().equals(PlayerTeleportEvent.TeleportCause.SPECTATE)) {
      return;
    }
    if (kitPlayer.getKitAttributeOrDefault(isGhostModeKey, false)) {
      event.setCancelled(true);
    }
  }

  private void doGhostTransformation(KitPlayer kitPlayer) {
    if (!kitPlayer.getKitAttributeOrDefault(isGhostModeKey, false)) {
      kitPlayer.getBukkitPlayer().ifPresent(player -> {
        GhostMode ghostMode = new GhostMode(kitPlayer, player);
        ghostMode.runTaskTimer(KitApi.getInstance().getPlugin(), 0, 20L);
        kitPlayer.putKitAttribute(ghostModeTaskKey, ghostMode);
      });
    }
  }


  @Override
  public float getCooldown() {
    return cooldown;
  }

  private class GhostMode extends BukkitRunnable {
    private final KitPlayer kitPlayer;
    private final Player player;
    private int counter;

    private GhostMode(KitPlayer kitPlayer, Player player) {
      this.kitPlayer = kitPlayer;
      this.player = player;
      init();
    }

    private void init() {
      for (int i = 0; i < transformationParticleLoopAmount; i++) {
        player.getWorld().spawnParticle(particle, player.getLocation(), transformationParticleAmount);
      }
      player.setGameMode(GameMode.SPECTATOR);
      kitPlayer.putKitAttribute(isGhostModeKey, true);
      player.sendMessage(ChatColor.GREEN + "Ghost-Mode wurde aktiviert!");
    }

    @Override
    public void run() {
      int timeLeft = transformationInSeconds - counter;

      if (counter >= transformationInSeconds || (!kitPlayer.isValid())) {
        end();
        return;
      }

      player.getNearbyEntities(10, 10, 10).stream().filter(Player.class::isInstance).forEach(entity -> {
        entity.getWorld().playSound(player, sound, 1F, 1F);
      });
      player.playSound(player, sound, 1F, 1F);

      player.sendActionBar("Ghost-Mode endet in: " + TimeConverter.stringify(timeLeft));
      counter++;
    }

    public void end() {
      this.cancel();
      if (player.getGameMode() == GameMode.SPECTATOR) {
        player.setGameMode(GameMode.SURVIVAL);
      }
      player.sendMessage(ChatColor.RED + "Ghost-Mode wurde deaktiviert!");
      kitPlayer.putKitAttribute(isGhostModeKey, false);
      kitPlayer.putKitAttribute(ghostModeTaskKey, null);
      kitPlayer.activateKitCooldown(GhostKit.this);
    }
  }
}
