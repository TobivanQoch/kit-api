package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.items.KitItemBuilder;
import de.hglabor.plugins.kitapi.kit.settings.DoubleArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BoxerKit extends AbstractKit {

	public static final BoxerKit INSTANCE = new BoxerKit();
	private final static String FIST = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmQ3ZjZhODk3NjFlYTU4MzkxYzljMTllMzdhYjBmOThkMjYzMmIzN2NkZGFiZDdmMDEzODUzNDNjZmY0N2M1MyJ9fX0=";

	@DoubleArg
	private final double damageMultiplier;

	private BoxerKit() {
		super("Boxer", new KitItemBuilder(Material.PLAYER_HEAD).setPlayerSkullSkin(FIST).build());
		damageMultiplier = 0.5D;
	}

	@KitEvent
	@Override
	public void onPlayerAttacksLivingEntity(EntityDamageByEntityEvent event, KitPlayer attacker, LivingEntity entity) {
		if (attacker.getBukkitPlayer().isPresent()) {
			if (attacker.getBukkitPlayer().get().getInventory().getItemInMainHand().getType() == Material.AIR) {
				event.setDamage(2.5);
			}
		}
	}

	@KitEvent
	@Override
	public void onPlayerGetsAttackedByLivingEntity(EntityDamageByEntityEvent event, Player player, LivingEntity attacker) {
		event.setDamage(event.getDamage() * damageMultiplier);
	}
}
