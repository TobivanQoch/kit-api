package de.hglabor.plugins.kitapi.supplier;

import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface KitPlayerSupplier {
	KitPlayer getKitPlayer(Player player);

	KitPlayer getKitPlayer(UUID uuid);

	KitPlayer getRandomAlivePlayer();
}
