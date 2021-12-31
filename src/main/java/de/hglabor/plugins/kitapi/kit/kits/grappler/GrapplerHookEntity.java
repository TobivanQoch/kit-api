package de.hglabor.plugins.kitapi.kit.kits.grappler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.Level;

class GrapplerHookEntity extends FishingHook {
  public GrapplerHookEntity(Player entityhuman, Level world, int i, int j) {
    super(entityhuman, world, i, j);
  }

  //TODO hier hatte ich mal noch ne methode überschrieben ka ob die wichtig war

  protected void remove() {
    super.remove(RemovalReason.DISCARDED);
    Player entityhuman = (Player) this.getOwner();
    if (entityhuman != null) {
      entityhuman.fishing = null;
    }
  }
}
