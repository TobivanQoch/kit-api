package de.hglabor.plugins.kitapi.util;

import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.config.KitMetaData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Utils {
  private Utils() {
  }

  public static boolean isUnbreakableLaborBlock(Block b) {
    return b.hasMetadata(KitMetaData.GLADIATOR_BLOCK.getKey()) || b.hasMetadata(KitMetaData.FEAST_BLOCK.getKey()) || b.hasMetadata(KitMetaData.UNBREAKABLE_BLOCK.getKey()) || b.getType() == Material.BEDROCK;
  }

  public static List<Field> getAllFields(AbstractKit kit) {
    List<Field> fields = new ArrayList<>();
    Collections.addAll(fields, kit.getClass().getDeclaredFields());
    Collections.addAll(fields, kit.getClass().getSuperclass().getDeclaredFields());
    return fields;
  }

  //can be a feature in InventorBuilder itself
  public static int translateGuiScale(int toTranslate) {
    if (toTranslate < 10) {
      return 9;
    } else if (toTranslate > 10 && toTranslate <= 18) {
      return 18;
    } else if (toTranslate > 18 && toTranslate <= 27) {
      return 27;
    } else if (toTranslate > 27 && toTranslate <= 36) {
      return 36;
    } else if (toTranslate > 36 && toTranslate <= 45) {
      return 45;
    } else if (toTranslate > 45) {
      return 54;
    } else {
      return 9;
    }
  }

  public static <I> List<I> cloneList(List<I> list) {
    return new ArrayList<>(list);
  }

  public static  <T> void drawCircle(double radius, Location center, Particle particle, @Nullable T particleData) {
    for (double i = 0; i < 0.5; i++) {
      for (double y = 0.0; y < (Math.PI * 2); y += .1) {
        double x = radius * Math.cos(y);
        double z = radius * Math.sin(y);
        center.getWorld().spawnParticle(particle, center.clone().add(x, i / 4, z), 0, 0, 0, 0, 5, particleData);
      }
    }
  }
}
