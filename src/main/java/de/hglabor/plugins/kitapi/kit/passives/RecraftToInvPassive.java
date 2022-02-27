package de.hglabor.plugins.kitapi.kit.passives;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.Passive;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class RecraftToInvPassive extends Passive implements Listener {

	public static final RecraftToInvPassive INSTANCE = new RecraftToInvPassive();

	private RecraftToInvPassive() {
		super("RecraftToInv", Material.CACTUS);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (hasPassive(KitApi.getInstance().getPlayer(player))) {
			ArrayList<Material> recraftItems = new ArrayList<>(Arrays.asList(Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.BROWN_MUSHROOM_BLOCK, Material.RED_MUSHROOM_BLOCK, Material.CACTUS, Material.COCOA, Material.COCOA_BEANS));
			Collection<ItemStack> drops = event.getBlock().getDrops();
			for (ItemStack drop : drops) {
				if (recraftItems.contains(drop.getType())) {
					if (event.getBlock().getType() == Material.CACTUS) {
						int blocksAmount = 1;
						while (event.getBlock().getRelative(BlockFace.UP, blocksAmount).getType() == Material.CACTUS) {
							event.getBlock().getRelative(BlockFace.UP, blocksAmount).setType(Material.AIR);
							blocksAmount++;
						}
						addItems(player, drop, blocksAmount - 1);
					}
					addItems(player, drop, drop.getAmount());
					event.setDropItems(false);
				}
			}
		}
	}

	private void addItems(Player player, ItemStack itemStack, int amount) {
		Map<Integer, ItemStack> items = player.getInventory().addItem(new ItemStack(itemStack.getType(), amount));
		for (ItemStack item : items.values()) {
			player.getWorld().dropItemNaturally(player.getLocation(), item);
		}
	}

	private boolean hasPassive(KitPlayer kitPlayer) {
		return kitPlayer.getPassive().equals(RecraftToInvPassive.INSTANCE);
	}
}
