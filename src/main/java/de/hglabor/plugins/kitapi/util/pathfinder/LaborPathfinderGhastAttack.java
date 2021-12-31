package de.hglabor.plugins.kitapi.util.pathfinder;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LaborPathfinderGhastAttack extends Goal {
  private final Ghast ghast;
  public int chargeTime;

  public LaborPathfinderGhastAttack(Ghast ghast) {
    this.ghast = ghast;
  }

  @Override
  public boolean canUse() {
    return this.ghast.getTarget() != null;
  }

  @Override
  public void start() {
    this.chargeTime = 0;
  }

  @Override
  public void stop() {
    this.ghast.setCharging(false);
  }

  @Override
  public void tick() {
    LivingEntity entityliving = this.ghast.getTarget();
    double d0 = 64.0D;

    if (entityliving == null) return;

    if (entityliving.distanceToSqr((Entity) this.ghast) < 4096.0D && this.ghast.hasLineOfSight(entityliving)) {
      Level world = this.ghast.level;

      ++this.chargeTime;
      if (this.chargeTime == 10 && !this.ghast.isSilent()) {
        world.levelEvent((Player) null, 1015, this.ghast.blockPosition(), 0);
      }

      if (this.chargeTime == 20) {
        double d1 = 4.0D;
        Vec3 vec3d = this.ghast.getViewVector(1.0F);
        double d2 = entityliving.getX() - (this.ghast.getX() + vec3d.x * 4.0D);
        double d3 = entityliving.getY(0.5D) - (0.5D + this.ghast.getY(0.5D));
        double d4 = entityliving.getZ() - (this.ghast.getZ() + vec3d.z * 4.0D);

        if (!this.ghast.isSilent()) {
          world.levelEvent((Player) null, 1016, this.ghast.blockPosition(), 0);
        }

        LargeFireball entitylargefireball = new LargeFireball(world, this.ghast, d2, d3, d4, this.ghast.getExplosionPower());

        // CraftBukkit - set bukkitYield when setting explosionpower
        entitylargefireball.bukkitYield = entitylargefireball.explosionPower = this.ghast.getExplosionPower();
        entitylargefireball.setPos(this.ghast.getX() + vec3d.x * 4.0D, this.ghast.getY(0.5D) + 0.5D, entitylargefireball.getZ() + vec3d.z * 4.0D);
        world.addFreshEntity(entitylargefireball);
        this.chargeTime = -40;
      }
    } else if (this.chargeTime > 0) {
      --this.chargeTime;
    }

    this.ghast.setCharging(this.chargeTime > 10);
  }
}
