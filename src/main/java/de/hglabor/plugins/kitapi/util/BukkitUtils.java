package de.hglabor.plugins.kitapi.util;

import de.hglabor.plugins.kitapi.KitApi;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public final class BukkitUtils {
    private BukkitUtils() {
    }

    public static BukkitTask runTaskLater(Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLater(KitApi.getInstance().getPlugin(), runnable, delay);
    }

    public static Material getRandomMaterial() {
        return Material.values()[new Random().nextInt(Material.values().length)];
    }

    public static Optional<Player> optionalPlayer(UUID uuid) {
        return Optional.ofNullable(Bukkit.getPlayer(uuid));
    }

    public static void setAttributeValue(Player player, Attribute attribute, double value) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null) {
            instance.setBaseValue(value);
        }
    }

    public static void resetAttributeValue(Player player, Attribute attribute) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null) {
            instance.setBaseValue(instance.getDefaultValue());
        }
    }

    public static void playSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 1, 1);
    }

    public static void playSound(Player player, Sound sound, float volume) {
        playSound(player, sound, volume, 1f);
    }

    public static void playSound(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }
}
