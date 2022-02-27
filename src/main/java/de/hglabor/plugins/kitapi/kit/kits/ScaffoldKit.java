package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.settings.BetaKit;
import de.hglabor.plugins.kitapi.kit.settings.FloatArg;
import de.hglabor.plugins.kitapi.kit.settings.IntArg;
import de.hglabor.plugins.kitapi.kit.settings.MaterialArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import de.hglabor.plugins.kitapi.util.BukkitUtils;
import de.hglabor.utils.noriskutils.TimeConverter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static de.hglabor.plugins.kitapi.util.BukkitUtils.runTaskLater;

@BetaKit
public class ScaffoldKit extends AbstractKit {
	public final static ScaffoldKit INSTANCE = new ScaffoldKit();
	@FloatArg(min = 0.0F)
	private final float cooldown;
	@IntArg
	private final int effectDurationInSeconds, blockRemoveDelayInTicks;
	@MaterialArg
	private final Material scaffoldMaterial;
	private final String isScaffolding;
	private final String scaffoldingTaskKey;

	private ScaffoldKit() {
		super("Scaffold", Material.SCAFFOLDING);
		this.cooldown = 30;
		this.effectDurationInSeconds = 10;
		this.blockRemoveDelayInTicks = 20;
		this.scaffoldMaterial = Material.SNOW_BLOCK;
		this.isScaffolding = "isScaffolding" + getName();
		this.scaffoldingTaskKey = "scaffoldingTask" + getName();
		setMainKitItem(getDisplayMaterial());
	}

	@Override
	public void onDeactivation(KitPlayer kitPlayer) {
		ScaffoldMode task = kitPlayer.getKitAttribute(scaffoldingTaskKey);
		if (task != null) {
			task.end();
		}
	}

	@KitEvent(ignoreCooldown = true)
	@Override
	public void onPlayerMoveEvent(PlayerMoveEvent event, KitPlayer kitPlayer) {
		if (kitPlayer.getKitAttributeOrDefault(isScaffolding, false)) {
			Block to = event.getTo().getBlock().getRelative(BlockFace.DOWN);
			if (to.getType().equals(Material.AIR)) {
				BukkitUtils.playSound(event.getPlayer(), Sound.BLOCK_WOOD_PLACE);
				to.setType(this.scaffoldMaterial);
				runTaskLater(() -> {
					if (to.getType().equals(scaffoldMaterial)) to.setType(Material.AIR);
				}, blockRemoveDelayInTicks);
			}
		}
	}

	@KitEvent
	@Override
	public void onPlayerRightClickKitItem(PlayerInteractEvent event, KitPlayer kitPlayer) {
		if (!kitPlayer.getKitAttributeOrDefault(isScaffolding, false)) {
			ScaffoldMode scaffoldMode = new ScaffoldMode(kitPlayer, event.getPlayer());
			scaffoldMode.runTaskTimer(KitApi.getInstance().getPlugin(), 0, 20L);
			kitPlayer.putKitAttribute(this.scaffoldingTaskKey, scaffoldMode);
		}
	}

	@Override
	public float getCooldown() {
		return this.cooldown;
	}

	private class ScaffoldMode extends BukkitRunnable {
		private final KitPlayer kitPlayer;
		private final Player player;
		private int counter;

		private ScaffoldMode(KitPlayer kitPlayer, Player player) {
			this.kitPlayer = kitPlayer;
			this.player = player;
			init();
		}

		private void init() {
			kitPlayer.putKitAttribute(isScaffolding, true);
			player.sendMessage(ChatColor.GREEN + "Scaffolding activated!");
		}

		@Override
		public void run() {
			int timeLeft = effectDurationInSeconds - counter;

			if (counter >= effectDurationInSeconds || (!kitPlayer.isValid())) {
				end();
				return;
			}

			player.sendActionBar("Scaffolding endet in: " + TimeConverter.stringify(timeLeft));
			counter++;
		}

		public void end() {
			this.cancel();
			player.sendMessage(ChatColor.RED + "Scaffolding wurde deaktiviert!");
			kitPlayer.putKitAttribute(isScaffolding, false);
			kitPlayer.putKitAttribute(scaffoldingTaskKey, null);
			kitPlayer.activateKitCooldown(ScaffoldKit.this);
		}
	}
}
