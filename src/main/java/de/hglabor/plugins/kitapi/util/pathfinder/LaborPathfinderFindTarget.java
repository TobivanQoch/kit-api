package de.hglabor.plugins.kitapi.util.pathfinder;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.UUID;

public class LaborPathfinderFindTarget extends Goal {
	private final KitPlayer kitPlayer;
	public Mob mob;
	public UUID safe;
	public boolean attack;

	public LaborPathfinderFindTarget(Mob mob, UUID safe, boolean attack) {
		this.mob = mob;
		this.safe = safe;
		this.attack = attack;
		this.kitPlayer = KitApi.getInstance().getPlayer(Bukkit.getPlayer(safe));
	}

	@Override
	public boolean canUse() {
		Entity target = kitPlayer.getLastHitInformation().getLastEntity();

		if (kitPlayer.getLastHitInformation().getEntityTimeStamp() + (long) (10 * 1000) < System.currentTimeMillis()) {
			mob.setTarget(null);
			return false;
		}

		if (target == null) {
			mob.setTarget(null);
			return false;
		}

		if (!(((CraftLivingEntity) target).getHandle() instanceof Player)) {
			mob.setTarget(null);
			return false;
		}

		if (target.getUniqueId().equals(safe)) {
			mob.setTarget(null);
			return false;
		}

		mob.setTarget(((CraftPlayer) target).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, true);
		return true;
	}

	@Override
	public void start() {
		if (mob.getTarget() == null) {
			return;
		}

		Location location = mob.getTarget().getBukkitEntity().getLocation();
		mob.getNavigation().moveTo(location.getX(), location.getY() + 1.0D, location.getZ(), 1.5F);

		if (!attack) { // We want the entity to not attack but able to move from the method above.
			return;
		}

		if (mob.getBukkitEntity().getLocation().distance(mob.getTarget().getBukkitEntity().getLocation()) <= 1.5D) {
			if (mob.getSensing().hasLineOfSight(mob.getTarget())) { // canSee method
				((LivingEntity) mob.getTarget().getBukkitEntity()).damage(4.0D, Bukkit.getPlayer(safe));
			}
		}
	}

	@Override
	public boolean isInterruptable() {
		return false;
	}
}
