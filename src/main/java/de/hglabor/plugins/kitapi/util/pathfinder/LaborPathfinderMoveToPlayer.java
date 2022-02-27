package de.hglabor.plugins.kitapi.util.pathfinder;


import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

public class LaborPathfinderMoveToPlayer extends Goal {
	private final Player player;
	private final Mob mob;

	public LaborPathfinderMoveToPlayer(Player player, Mob mob) {
		this.player = player;
		this.mob = mob;
	}

	@Override
	public void start() {
		if (player == null || player.isDeadOrDying()) {
			return;
		}

		if (mob == null) {
			return;
		}

		if (mob.getBukkitEntity().getLocation().distanceSquared(player.getBukkitEntity().getLocation()) >= 1200) {
			mob.getBukkitEntity().teleport(player.getBukkitEntity().getLocation().clone().add(0, 1, 0));
			mob.setTarget(null);
			return;
		}

		if (mob.getTarget() != null) {
			return;
		}

		if (mob.getBukkitEntity().getLocation().distanceSquared(player.getBukkitEntity().getLocation()) >= 24.0) {
			mob.getNavigation().moveTo(player.getX(), player.getY() + 1.0D, player.getZ(), 1.5F);
			mob.getLookControl().setLookAt(player, 10.0F, 0.0F);
		}
	}

	@Override
	public boolean canUse() {
		return player != null && !player.isDeadOrDying();
	}
}
