package de.hglabor.plugins.kitapi.util;

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.level.World;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class NMSUtils {

  //noriskutils NMSUtils want a 1.16 packet but i have no bock to update them

  private NMSUtils() {
  }

  public static World getWorld(Entity entity) {
    return ((CraftEntity) entity).getHandle().getWorld();
  }

  public static void sendPacket(Player player, Packet<?> packet) {
    ((CraftPlayer) player).getHandle().b.sendPacket(packet);
  }

  public static EntityLiving getEntityLiving(LivingEntity entity) {
    return (EntityLiving) ((CraftEntity) entity).getHandle();
  }
}
