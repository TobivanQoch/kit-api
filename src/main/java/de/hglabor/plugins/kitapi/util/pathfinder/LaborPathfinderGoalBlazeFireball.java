package de.hglabor.plugins.kitapi.util.pathfinder;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;

import java.util.EnumSet;

public class LaborPathfinderGoalBlazeFireball extends Goal {
  private final Blaze blaze;
  private int attackStep;
  private int attackTime;
  private int lastSeen;

  public LaborPathfinderGoalBlazeFireball(Blaze blaze) {
    this.blaze = blaze;
    this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
  }

  @Override
  public boolean canUse() {
    LivingEntity livingEntity = this.blaze.getTarget();
    return livingEntity != null && livingEntity.isAlive() && this.blaze.canAttack(livingEntity);
  }

  @Override
  public void start() {
    this.attackStep = 0;
  }

  @Override
  public void stop() {
    //this.blaze.setCharged(false);
    this.lastSeen = 0;
  }

  @Override
  public void tick() {
    --this.attackTime;
    LivingEntity livingEntity = this.blaze.getTarget();
    if (livingEntity != null) {
      boolean bl = this.blaze.getSensing().hasLineOfSight(livingEntity);
      if (bl) {
        this.lastSeen = 0;
      } else {
        ++this.lastSeen;
      }

      double d = this.blaze.distanceToSqr(livingEntity);
      if (d < 4.0D) {
        if (!bl) {
          return;
        }

        if (this.attackTime <= 0) {
          this.attackTime = 20;
          this.blaze.doHurtTarget(livingEntity);
        }

        this.blaze.getMoveControl().setWantedPosition(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1.0D);
      } else if (d < this.getFollowDistance() * this.getFollowDistance() && bl) {
        double e = livingEntity.getX() - this.blaze.getX();
        double f = livingEntity.getY(0.5D) - this.blaze.getY(0.5D);
        double g = livingEntity.getZ() - this.blaze.getZ();
        if (this.attackTime <= 0) {
          ++this.attackStep;
          if (this.attackStep == 1) {
            this.attackTime = 60;
            //this.blaze.setCharged(true);
          } else if (this.attackStep <= 4) {
            this.attackTime = 6;
          } else {
            this.attackTime = 100;
            this.attackStep = 0;
            //this.blaze.setCharged(false);
          }

          if (this.attackStep > 1) {
            double h = Math.sqrt(Math.sqrt(d)) * 0.5D;
            if (!this.blaze.isSilent()) {
              this.blaze.level.levelEvent((Player) null, 1018, this.blaze.blockPosition(), 0);
            }

            for (int i = 0; i < 1; ++i) {
              SmallFireball smallFireball = new SmallFireball(this.blaze.level, this.blaze, e + this.blaze.getRandom().nextGaussian() * h, f, g + this.blaze.getRandom().nextGaussian() * h);
              smallFireball.setPos(smallFireball.getX(), this.blaze.getY(0.5D) + 0.5D, smallFireball.getZ());
              this.blaze.level.addFreshEntity(smallFireball);
            }
          }
        }

        this.blaze.getLookControl().setLookAt(livingEntity, 10.0F, 10.0F);
      } else if (this.lastSeen < 5) {
        this.blaze.getMoveControl().setWantedPosition(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1.0D);
      }

      super.tick();
    }
  }

  private double getFollowDistance() {
    return this.blaze.getAttributeValue(Attributes.FOLLOW_RANGE);
  }
}
