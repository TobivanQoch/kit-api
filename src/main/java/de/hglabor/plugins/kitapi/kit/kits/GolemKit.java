package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.items.KitItemBuilder;
import de.hglabor.plugins.kitapi.kit.settings.BetaKit;
import de.hglabor.plugins.kitapi.kit.settings.DoubleArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import de.hglabor.plugins.kitapi.util.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

@BetaKit
public class GolemKit extends AbstractKit implements Listener {
	public final static GolemKit INSTANCE = new GolemKit();
	private final static String IRON_GOLEM_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTEzZjM0MjI3MjgzNzk2YmMwMTcyNDRjYjQ2NTU3ZDY0YmQ1NjJmYTlkYWIwZTEyYWY1ZDIzYWQ2OTljZjY5NyJ9fX0=";
	@DoubleArg
	private final double movementSpeed, maxHealth;


	private GolemKit() {
		super("Golem", new KitItemBuilder(Material.PLAYER_HEAD).setPlayerSkullSkin(IRON_GOLEM_HEAD).build());
		this.movementSpeed = 0.05D;
		this.maxHealth = 55D;
	}

	@Override
	public void onEnable(KitPlayer kitPlayer) {
		kitPlayer.getBukkitPlayer().ifPresent(player -> {
			BukkitUtils.setAttributeValue(player, Attribute.GENERIC_MOVEMENT_SPEED, movementSpeed);
			BukkitUtils.setAttributeValue(player, Attribute.GENERIC_MAX_HEALTH, maxHealth);
			switch (new Random().nextInt(3)) {
				case 1 -> player.getInventory().setHelmet(new ItemStack(Material.MELON));
				case 2 -> player.getInventory().setHelmet(new ItemStack(Material.COPPER_BLOCK));
				default -> player.getInventory().setHelmet(new ItemStack(Material.CARVED_PUMPKIN));
			}
			player.setHealth(player.getMaxHealth());
		});
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getSlotType() != InventoryType.SlotType.ARMOR) {
			return;
		}
		if (!isEnabled()) {
			return;
		}
		HumanEntity whoClicked = event.getWhoClicked();
		if (KitApi.getInstance().getPlayerSupplier().getKitPlayer(((Player) whoClicked)).hasKit(this)) {
			// 5 = Helmet
			if (event.getRawSlot() == 5) {
				event.setCancelled(true);
			}
		}
	}

	@Override
	public void onDisable(KitPlayer kitPlayer) {
		kitPlayer.getBukkitPlayer().ifPresent(player -> {
			BukkitUtils.setAttributeValue(player, Attribute.GENERIC_MOVEMENT_SPEED, 0.10000000149011612D);
			BukkitUtils.setAttributeValue(player, Attribute.GENERIC_MAX_HEALTH, 20D);
			player.setHealth(player.getMaxHealth());
		});
	}
}
