package de.hglabor.plugins.kitapi.kit;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class Passive extends AbstractKit {
	protected Passive(String name, Material material) {
		super(name, material);
	}

	protected Passive(String name) {
		super(name);
	}

	protected Passive(String name, Material material, List<ItemStack> additionalKitItems) {
		super(name, material, additionalKitItems);
	}

	protected Passive(String name, ItemStack displayItem) {
		super(name, displayItem);
	}
}
