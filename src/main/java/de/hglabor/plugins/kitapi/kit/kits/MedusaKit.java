package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.settings.DoubleArg;
import de.hglabor.plugins.kitapi.kit.settings.FloatArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import de.hglabor.utils.noriskutils.WorldEditUtils;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MedusaKit extends AbstractKit {

	public static final MedusaKit INSTANCE = new MedusaKit();

	@FloatArg
	private final float cooldown;

	@DoubleArg(min = 1.0, max = 100.0)
	private final double radius;

	private MedusaKit() {
		super("Medusa", Material.ZOMBIE_HEAD);
		setMainKitItem(getDisplayMaterial());
		cooldown = 30f;
		radius = 30.0;
	}

	@KitEvent
	@Override
	public void onPlayerRightClickKitItem(PlayerInteractEvent event, KitPlayer kitPlayer) {
		Player player = event.getPlayer();
		for (LivingEntity nearby : player.getWorld().getNearbyLivingEntities(player.getLocation(), radius)) {
			if (nearby instanceof Player) {
				KitPlayer nearbyKitPlayer = KitApi.getInstance().getPlayer((Player) nearby);
				if (!nearbyKitPlayer.isValid()) {
					continue;
				}
				if (nearby.getUniqueId() == player.getUniqueId()) {
					continue;
				}
				if (nearby.getTargetEntity(30) != null && nearby.getTargetEntity(30).getUniqueId() == player.getUniqueId()) {
					//Set stone
					//nearby.getLocation().clone().getBlock().setType(Material.COBBLESTONE);
					//nearby.getLocation().clone().add(0, 1, 0).getBlock().setType(Material.COBBLESTONE);
					WorldEditUtils.createCylinder(nearby.getWorld(), nearby.getLocation(), 1, true, 2, Material.COBBLESTONE);
					nearby.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1, false, false));
					nearby.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 10, false, false));
				}
			}

		}
		kitPlayer.activateKitCooldown(this);
	}

	@Override
	public float getCooldown() {
		return cooldown;
	}
}
