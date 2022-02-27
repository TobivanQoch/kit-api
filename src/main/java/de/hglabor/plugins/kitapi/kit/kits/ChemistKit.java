package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.events.event.PlayerAteSoupEvent;
import de.hglabor.plugins.kitapi.kit.settings.BetaKit;
import de.hglabor.plugins.kitapi.kit.settings.IntArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@BetaKit
public class ChemistKit extends AbstractKit implements Listener {
	public final static ChemistKit INSTANCE = new ChemistKit();
	private final NamespacedKey namespacedKey;
	private final List<PotionEffectType> potionEffects;
	@IntArg
	private final int durationInSeconds, amplifier;

	private ChemistKit() {
		super("Chemist", new ItemBuilder(Material.POTION).setPotionEffect(PotionEffectType.CONFUSION, Color.GREEN).build());
		this.namespacedKey = new NamespacedKey(KitApi.getInstance().getPlugin(), "chemistSoup");
		this.potionEffects = Arrays.asList(
				PotionEffectType.POISON, PotionEffectType.HUNGER, PotionEffectType.CONFUSION,
				PotionEffectType.BLINDNESS, PotionEffectType.WITHER, PotionEffectType.GLOWING, PotionEffectType.SLOW_DIGGING,
				PotionEffectType.WEAKNESS
		);
		this.durationInSeconds = 10;
		this.amplifier = 3;
	}

	@EventHandler
	public void onPlayerAteSoup(PlayerAteSoupEvent event) {
		ItemStack soup = event.getSoup();
		if (!soup.hasItemMeta()) {
			return;
		}
		String uuidString = soup.getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
		if (uuidString != null) {
			Player player = event.getPlayer();

			if (player.getUniqueId().toString().equalsIgnoreCase(uuidString)) {
				return;
			}

			UUID uuid = UUID.fromString(uuidString);

			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
			player.sendMessage(ChatColor.RED + "Du hast die vergiftete Suppe von " + offlinePlayer.getName() + " gegessen");
			PotionEffect randomPotionEffect = getRandomPotionEffect();
			player.addPotionEffect(randomPotionEffect);

			Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(chemist -> {
				chemist.sendMessage(ChatColor.GREEN + player.getName() + " hat " + ChatColor.GOLD + randomPotionEffect.getType().getName() + ChatColor.GREEN + " durch eine deiner vergifteten Suppen bekommen");
			});
		}
	}

	@KitEvent
	@Override
	public void onDropItem(PlayerDropItemEvent event, KitPlayer kitPlayer) {
		Item itemDrop = event.getItemDrop();
		ItemStack itemStack = itemDrop.getItemStack();
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, kitPlayer.getUUID().toString());
		itemStack.setItemMeta(itemMeta);
		event.getItemDrop().setItemStack(itemStack);
	}

	private PotionEffect getRandomPotionEffect() {
		return potionEffects.get(ThreadLocalRandom.current().nextInt(potionEffects.size())).createEffect(durationInSeconds * 20, amplifier);
	}
}
