package de.hglabor.plugins.kitapi.pvp;

import de.hglabor.plugins.kitapi.KitApi;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;

public class CPSChecker implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			// KitApi.getInstance().getPlayer(event.getPlayer()).addLeftClick(System.currentTimeMillis());
		}
	}

	@EventHandler
	public void onPlayerAnimation(PlayerAnimationEvent event) {
		if (event.getAnimationType() == PlayerAnimationType.ARM_SWING) {
			KitApi.getInstance().getPlayer(event.getPlayer()).addLeftClick(System.currentTimeMillis());
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player)) {
			return;
		}
		KitApi.getInstance().getPlayer((Player) event.getDamager()).addLeftClick(System.currentTimeMillis());
	}
}
