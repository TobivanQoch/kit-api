package de.hglabor.plugins.kitapi.util.pathfinder;


import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.bukkit.Location;

public class LaborPathfinderMoveToLocation extends Goal {
	private final Mob monster;
	private final Location location;
	private final double speed;

	public LaborPathfinderMoveToLocation(Location location, Mob monster) {
		this(location, monster, 1.5D);
	}

	public LaborPathfinderMoveToLocation(Location location, Mob monster, double speed) {
		this.location = location;
		this.monster = monster;
		this.speed = speed;
	}

	@Override
	public boolean canUse() {
		return monster.getTarget() == null;
	}

	@Override
	public void start() {
		if (monster == null) {
			return;
		}
		monster.getNavigation().moveTo(location.getX(), location.getY() + 1.0D, location.getZ(), this.speed);
	}

	@Override
	public boolean isInterruptable() {
		return false;
	}
}
