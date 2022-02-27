package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.items.KitItemBuilder;
import de.hglabor.plugins.kitapi.kit.settings.BetaKit;
import de.hglabor.plugins.kitapi.kit.settings.IntArg;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;

@BetaKit
public class SpiegelKit extends AbstractKit {
	public static final SpiegelKit INSTANCE = new SpiegelKit();
	private final static String MIRROR_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWEwNDQxMjhlNjc3YjFkMDQzODMwZDcyYTFjNjVhMjUxZWI4NDA0YmVjYjcyODE0NTcxMzMwMjE4ZWY5Yjg1NiJ9fX0=";

	@IntArg
	private final int multiplier;

	private SpiegelKit() {
		super("Spiegel", new KitItemBuilder(Material.PLAYER_HEAD).setPlayerSkullSkin(MIRROR_HEAD).build());
		this.multiplier = -1;
	}

	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent event) {
		if (!(event.getHitEntity() instanceof Player player)) {
			return;
		}

		if (KitApi.getInstance().getPlayer(player).hasKit(this)) {
			event.getEntity().remove();
			event.setCancelled(true);
			player.launchProjectile(event.getEntity().getClass(), event.getEntity().getVelocity().multiply(multiplier));
		}
	}
}
