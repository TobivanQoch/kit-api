package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.settings.DoubleArg;
import de.hglabor.plugins.kitapi.kit.settings.FloatArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import de.hglabor.plugins.kitapi.util.Utils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class UltimatoKit extends AbstractKit {

	public static final UltimatoKit INSTANCE = new UltimatoKit();
	private static final String ultimatoFightKey = "ultimatoFight";

	@DoubleArg
	private final double radius;

	@DoubleArg
	private final double strength;

	@FloatArg
	private final float cooldown;

	private UltimatoKit() {
		super("Ultimato", Material.EMERALD);
		this.radius = 10.0D;
		this.strength = 2.3;
		this.cooldown = 2.0F;
		setMainKitItem(getDisplayMaterial());
	}

	@KitEvent
	@Override
	public void onHitLivingEntityWithKitItem(EntityDamageByEntityEvent event, KitPlayer attacker, LivingEntity entity) {
		if(!(entity instanceof Player)) {
			return;
		}
		KitPlayer kitPlayerEntity = KitApi.getInstance().getPlayer((Player) entity);
		if(attacker.getBukkitPlayer().isEmpty()) {
			return;
		}
		if(attacker.getKitAttribute(ultimatoFightKey) == null) {
			if(attacker.getKitAttribute(ultimatoFightKey) != null) {
				attacker.getBukkitPlayer().ifPresent(player -> player.sendMessage(ChatColor.RED + "You can't attack someone who is already in a fight"));
				return;
			}
			Fight fight = new Fight(attacker.getBukkitPlayer().get(), (Player) entity, radius, strength);
			entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1f, 1f);
			kitPlayerEntity.putKitAttribute(ultimatoFightKey, fight);
			attacker.putKitAttribute(ultimatoFightKey, fight);
			Bukkit.getScheduler().runTaskTimer(KitApi.getInstance().getPlugin(), (task) -> {
				boolean shouldCancel = fight.tick();
				if(shouldCancel) {
					task.cancel();
					kitPlayerEntity.putKitAttribute(ultimatoFightKey, null);
					attacker.putKitAttribute(ultimatoFightKey, null);
					attacker.activateKitCooldown(INSTANCE);
				}
			}, 0L, 1L);
		} else {
			attacker.getBukkitPlayer().ifPresent(player -> player.sendMessage(ChatColor.RED + "You are already in a fight"));
		}
	}

	@Override
	public float getCooldown() {
		return cooldown;
	}

	public static class Fight {

		private final Player attacker;
		private final Player victim;
		private final double radius;
		private final double strength;

		public Fight(Player attacker, Player victim, double radius, double strength) {
			this.attacker = attacker;
			this.victim = victim;
			this.radius = radius;
			this.strength = strength;
		}

		public Player getAttacker() {
			return attacker;
		}

		public Player getVictim() {
			return victim;
		}

		public double getRadius() {
			return radius;
		}

		public double getStrength() {
			return strength;
		}

		/**
		 * @return true if the task can be cancelled
		 */
		public boolean tick() {
			KitPlayer attackerKitPlayer = KitApi.getInstance().getPlayer(attacker);
			KitPlayer victimKitPlayer = KitApi.getInstance().getPlayer(victim);
			if(!attackerKitPlayer.isValid() || !victimKitPlayer.isValid()) {
				return true;
			}
			for (Entity otherEntities : attacker.getNearbyEntities(radius, radius, radius)) {
				if(otherEntities.getLocation().distance(attacker.getLocation()) >= radius) {
					Vector direction = attacker.getLocation().toVector().subtract(otherEntities.getLocation().toVector()).normalize();
					if(otherEntities != victim) {
						otherEntities.setVelocity(direction.multiply(getStrength()));
					} else {
						otherEntities.setVelocity(direction.multiply(-(getStrength()*2)));
					}
				}
			}
			Utils.drawCircle(radius, getAttacker().getLocation(), Particle.REDSTONE, new Particle.DustOptions(Color.RED, 1f));
			Utils.drawCircle(radius, getAttacker().getLocation().clone().add(0, 1, 0), Particle.REDSTONE, new Particle.DustOptions(Color.RED, 1f));
			Utils.drawCircle(radius, getAttacker().getLocation().clone().add(0, 2, 0), Particle.REDSTONE, new Particle.DustOptions(Color.RED, 1f));
			Utils.drawCircle(radius, getAttacker().getLocation().clone().subtract(0, 1, 0), Particle.REDSTONE, new Particle.DustOptions(Color.RED, 1f));
			Utils.drawCircle(radius, getAttacker().getLocation().clone().subtract(0, 2, 0), Particle.REDSTONE, new Particle.DustOptions(Color.RED, 1f));
			return false;
		}
	}
}

