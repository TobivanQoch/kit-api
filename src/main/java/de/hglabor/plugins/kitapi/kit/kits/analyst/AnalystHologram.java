package de.hglabor.plugins.kitapi.kit.kits.analyst;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import de.hglabor.plugins.kitapi.pvp.recraft.Recraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AnalystHologram extends ArmorStand {
	private final Player target;
	private final Player owner;
	private final HologramType type;
	private final double boost;

	public AnalystHologram(World world, Player target, Player owner, HologramType type, double boost) {
		super(EntityType.ARMOR_STAND, ((CraftWorld) world).getHandle());
		this.owner = owner;
		this.type = type;
		this.boost = boost;
		this.target = target;
		this.persist = false;
		this.setCustomNameVisible(true);
		this.setInvisible(true);
		this.setMarker(true);
		this.setSilent(true);
	}

	@Override
	public void tick() {
		if (!valid) {
			return;
		}
		super.tick();
		if (!target.isOnline() || target.isDead() || !target.isValid()) {
			this.die(DamageSource.GENERIC);
			return;
		}
		if (!owner.isOnline() || owner.isDead() || !owner.isValid()) {
			this.die(DamageSource.GENERIC);
			return;
		}
		this.setCustomName(new TextComponent(getInformation()));
		Location location = target.getEyeLocation().clone();
		this.setPos(location.getX(), location.getY() + boost, location.getZ());
	}

	private String getInformation() {
		switch (type) {
			case SOUPS:
				return "Soups: " + getMaterialAmount(org.bukkit.Material.MUSHROOM_STEW);
			case RECRAFT:
				Recraft recraft = new Recraft();
				recraft.calcRecraft(target.getInventory().getContents());
				return String.format("Recraft: %sx", recraft.getRecraftPoints());
			case CPS:
				KitPlayer player = KitApi.getInstance().getPlayer(target);
				return String.format("CPS: %s", player.getLeftCps());
			default:
				return "";
		}
	}

	public int getMaterialAmount(org.bukkit.Material material) {
		return target.getInventory().all(material).values().stream().mapToInt(ItemStack::getAmount).sum();
	}

	enum HologramType {
		SOUPS, RECRAFT, CPS
	}
}
