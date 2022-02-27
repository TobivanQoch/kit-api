package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.settings.BetaKit;
import de.hglabor.plugins.kitapi.kit.settings.DoubleArg;
import de.hglabor.plugins.kitapi.kit.settings.FloatArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import de.hglabor.plugins.kitapi.util.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@BetaKit
public class FahrradKit extends AbstractKit implements Listener {
	public final static FahrradKit INSTANCE = new FahrradKit();
	@DoubleArg
	private final double divideBy;
	@FloatArg
	private final float cooldown;
	private final Map<UUID, Double> defaultMovementSpeed;
	private final String fahrradOwner;
	private final String fahrradId;

	private FahrradKit() {
		super("Fahrrad", Material.SADDLE);
		setMainKitItem(Material.BELL);
		this.divideBy = 18D;
		this.defaultMovementSpeed = new HashMap<>();
		this.fahrradOwner = "fahrradOwner" + getName();
		this.fahrradId = "fahrradId" + getName();
		this.cooldown = 90F;
	}

	@Override
	public float getCooldown() {
		return cooldown;
	}

	@Override
	public void onDisable(KitPlayer kitPlayer) {
		UUID uuid = kitPlayer.getKitAttribute(fahrradId);
		if (uuid != null) {
			Optional.ofNullable(Bukkit.getEntity(uuid)).ifPresent(Entity::remove);
		}
	}

	@KitEvent
	public void onPlayerLeftClickKitItem(PlayerInteractEvent event, KitPlayer kitPlayer) {
		Player player = event.getPlayer();
		if (player.isInsideVehicle()) {
			player.sendMessage(ChatColor.RED + "Du befindest dich bereits in einem Fahrzeug");
			return;
		}
		BukkitUtils.playSound(player, Sound.BLOCK_CHAIN_PLACE);
		Horse horse = player.getWorld().spawn(player.getLocation(), Horse.class);
		horse.setOwner(player);
		horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
		horse.setCustomName("Fahrrad von " + player.getName());
		horse.setCustomNameVisible(true);
		horse.setMetadata(fahrradOwner, new FixedMetadataValue(KitApi.getInstance().getPlugin(), player.getUniqueId()));
		kitPlayer.putKitAttribute(fahrradId, horse.getUniqueId());
		horse.addPassenger(player);
	}

	@KitEvent
	public void onPlayerRightClickKitItem(PlayerInteractEvent event, KitPlayer kitPlayer) {
		kitPlayer.getBukkitPlayer().ifPresent(player -> {
			if (player.isInsideVehicle()) {
				player.playSound(player.getLocation(), Sound.BLOCK_BELL_USE, 1F, 1F);
			}
		});
	}

	@EventHandler
	public void onEntityDismount(EntityDismountEvent event) {
		if (!isEnabled()) {
			return;
		}
		Entity entity = event.getDismounted();
		if (entity.hasMetadata(fahrradOwner)) {
			UUID uuid = (UUID) entity.getMetadata(fahrradOwner).get(0).value();
			entity.remove();
			KitApi.getInstance().getPlayerSupplier().getKitPlayer(uuid).activateKitCooldown(this);
		}
	}

	@KitEvent(ignoreCooldown = true)
	public void onPlayerMoveEvent(PlayerMoveEvent event, KitPlayer kitPlayer) {
		handleRidingEntity(event.getPlayer().getVehicle(), kitPlayer);
	}

	private void handleRidingEntity(Entity vehicle, KitPlayer kitPlayer) {
		if (vehicle instanceof Player) {
			return;
		}
		if (!(vehicle instanceof LivingEntity)) {
			return;
		}
		AttributeInstance attribute = ((LivingEntity) vehicle).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);

		if (attribute != null) {
			if (!defaultMovementSpeed.containsKey(vehicle.getUniqueId())) {
				defaultMovementSpeed.put(vehicle.getUniqueId(), attribute.getValue());
			}
			double defaultMovement = defaultMovementSpeed.get(vehicle.getUniqueId());
			double max = Math.max(defaultMovement, defaultMovement + kitPlayer.getLeftCps() / divideBy);
			kitPlayer.getBukkitPlayer().ifPresent(player -> player.sendActionBar(String.format("%.2f", max) + " km/h" + " | " + "(" + (kitPlayer.getLeftCps() + " CPS)")));
			attribute.setBaseValue(max);
		}
	}
}
