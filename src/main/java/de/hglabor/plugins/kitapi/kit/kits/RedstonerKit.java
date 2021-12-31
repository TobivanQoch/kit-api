package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RedstonerKit extends AbstractKit {

  public static final RedstonerKit INSTANCE = new RedstonerKit();

  private RedstonerKit() {
    super("Redstoner", Material.REPEATER);
  }

  @KitEvent
  @Override
  public void onEnable(KitPlayer kitPlayer) {
    Player player = Bukkit.getPlayer(kitPlayer.getUUID());
    if (player == null) {
      return;
    }
    player.getInventory().addItem(new ItemStack(Material.PISTON, 64));
    player.getInventory().addItem(new ItemStack(Material.REDSTONE_TORCH, 64));
    player.getInventory().addItem(new ItemStack(Material.SLIME_BLOCK, 64));
    player.getInventory().addItem(new ItemStack(Material.REDSTONE, 64));
  }
}
