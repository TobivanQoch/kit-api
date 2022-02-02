package de.hglabor.plugins.kitapi.kit.kits.analyst;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.settings.FloatArg;
import de.hglabor.plugins.kitapi.kit.settings.IntArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.world.damagesource.DamageSource;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnalystKit extends AbstractKit implements Listener {
  public final static AnalystKit INSTANCE = new AnalystKit();
  @FloatArg
  public final float cooldown;
  @IntArg
  private final int hologramKeepAlive;
  private final Set<Integer> hologramIds;
  private final String hologramKey;

  private AnalystKit() {
    super("Analyst", Material.GLASS_PANE);
    setMainKitItem(getDisplayMaterial());
    hologramIds = new HashSet<>();
    hologramKeepAlive = 10;
    cooldown = 60F;
    hologramKey = this.getName() + "key";
  }

  @Override
  public void onDeactivation(KitPlayer kitPlayer) {
    List<AnalystHologram> holograms = kitPlayer.getKitAttributeOrDefault(hologramKey, new ArrayList<>());
    for (AnalystHologram hologram : holograms) {
      hologramIds.removeIf(integer -> integer == hologram.getId());
      hologram.die(DamageSource.GENERIC);
    }
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    hologramIds.forEach(hologramId -> ((CraftPlayer) player).getHandle().connection.send(new ClientboundRemoveEntitiesPacket(hologramId)));
  }

  @KitEvent
  @Override
  public void onPlayerRightClickPlayerWithKitItem(PlayerInteractAtEntityEvent event, KitPlayer kitPlayer, Player rightClicked) {
    Player player = event.getPlayer();
    double boost = 0.25D;
    List<AnalystHologram> analystHolograms = new ArrayList<>();
    for (AnalystHologram.HologramType type : AnalystHologram.HologramType.values()) {
      boost += 0.25D;
      World world = rightClicked.getWorld();
      AnalystHologram analystHologram = new AnalystHologram(world, rightClicked, player, type, boost);
      analystHolograms.add(analystHologram);
      hologramIds.add(analystHologram.getId());
      ((CraftWorld) world).getHandle().addFreshEntity(analystHologram);
      for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        if (onlinePlayer == player) {
          continue;
        }
        ((CraftPlayer) onlinePlayer).getHandle().connection.send(new ClientboundRemoveEntitiesPacket(analystHologram.getId()));
      }
    }
    Bukkit.getScheduler().runTaskLater(KitApi.getInstance().getPlugin(), () -> {
      for (AnalystHologram analystHologram : analystHolograms) {
        hologramIds.removeIf(integer -> integer == analystHologram.getId());
        analystHologram.die(DamageSource.GENERIC);
      }
    }, hologramKeepAlive * 20L);
    kitPlayer.putKitAttribute(hologramKey, analystHolograms);
    kitPlayer.activateKitCooldown(this);
  }

  @Override
  public float getCooldown() {
    return cooldown;
  }
}
