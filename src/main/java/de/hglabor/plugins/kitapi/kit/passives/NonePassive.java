package de.hglabor.plugins.kitapi.kit.passives;

import de.hglabor.plugins.kitapi.kit.Passive;
import org.bukkit.Material;

public class NonePassive extends Passive {
	public final static NonePassive INSTANCE = new NonePassive();

	private NonePassive() {
		super("None", Material.STRUCTURE_VOID);
	}
}
