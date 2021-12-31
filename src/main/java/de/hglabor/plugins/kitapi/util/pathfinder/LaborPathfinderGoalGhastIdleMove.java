package de.hglabor.plugins.kitapi.util.pathfinder;

import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Ghast;

import java.util.EnumSet;
import java.util.Random;

public class LaborPathfinderGoalGhastIdleMove extends Goal {
  private final Ghast ghast;
  public LaborPathfinderGoalGhastIdleMove(Ghast ghast) {
    this.ghast = ghast;
    this.setFlags(EnumSet.of(Goal.Flag.MOVE));
  }

  @Override
  public boolean canUse() {
    MoveControl controllermove = this.ghast.getMoveControl();

    if (!controllermove.hasWanted()) {
      return true;
    } else {
      double d0 = controllermove.getWantedX() - this.ghast.getX();
      double d1 = controllermove.getWantedY() - this.ghast.getY();
      double d2 = controllermove.getWantedZ() - this.ghast.getZ();
      double d3 = d0 * d0 + d1 * d1 + d2 * d2;

      return d3 < 1.0D || d3 > 3600.0D;
    }
  }

  @Override
  public boolean canContinueToUse() {
    return false;
  }

  @Override
  public void start() {
    Random random = this.ghast.getRandom();
    double d0 = this.ghast.getX() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
    double d1 = this.ghast.getY() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
    double d2 = this.ghast.getZ() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);

    this.ghast.getMoveControl().setWantedPosition(d0, d1, d2, 1.0D);
  }
}
