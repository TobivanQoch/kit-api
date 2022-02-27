package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.items.KitItemBuilder;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class OpaKit extends AbstractKit {
	public static final OpaKit INSTANCE = new OpaKit();
	private final static String OPA_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2M4MmMwNTU3NTBlNDFhNDQwZTdjMjM4YjE2MWY1OGI2MjEyNDllNzgyNGY1YTBiNjEzNzRiOGQ4MTM4ZmU0ZCJ9fX0=";

	private OpaKit() {
		super("Opa", new KitItemBuilder(Material.PLAYER_HEAD).setPlayerSkullSkin(OPA_HEAD).build());
		setMainKitItem(new ItemBuilder(Material.STICK).setEnchantment(Enchantment.KNOCKBACK, 2).build());
	}
}
