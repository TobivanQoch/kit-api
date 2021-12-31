package de.hglabor.plugins.kitapi.kit.kits;

import com.mojang.datafixers.util.Pair;
import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.items.KitItemBuilder;
import de.hglabor.plugins.kitapi.kit.settings.BetaKit;
import de.hglabor.plugins.kitapi.kit.settings.FloatArg;
import de.hglabor.plugins.kitapi.kit.settings.LongArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

@BetaKit
public class RefillKit extends AbstractKit {
    public final static RefillKit INSTANCE = new RefillKit();
    private final static String FORWARD_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjgxMzYzM2JkNjAxNTJkOWRmNTRiM2Q5ZDU3M2E4YmMzNjU0OGI3MmRjMWEzMGZiNGNiOWVjMjU2ZDY4YWUifX19";
    @FloatArg
    private final float cooldown;
    @LongArg
    private final long refillTickSpeed;
    private final String refillTaskString;

    private RefillKit() {
        super("Refill", Material.MUSHROOM_STEW);
        setMainKitItem(new KitItemBuilder(Material.PLAYER_HEAD).setPlayerSkullSkin(FORWARD_HEAD).build());
        this.cooldown = 25F;
        this.refillTickSpeed = 1L;
        this.refillTaskString = "refillTask" + getName();
    }

    @Override
    public void onDisable(KitPlayer kitPlayer) {
        RefillTask refillTask = kitPlayer.getKitAttribute(refillTaskString);
        if (refillTask != null) {
            refillTask.end();
        }
    }

    @KitEvent
    public void onPlayerRightClickKitItem(PlayerInteractEvent event, KitPlayer kitPlayer) {
        autoRefill(event.getPlayer(), kitPlayer);
    }

    @KitEvent
    public void onPlayerLeftClickKitItem(PlayerInteractEvent event, KitPlayer kitPlayer) {
        autoRefill(event.getPlayer(), kitPlayer);
    }

    private void autoRefill(Player player, KitPlayer kitPlayer) {
        PlayerInventory inventory = player.getInventory();
        Pair<Integer, ItemStack> soupsInInventory = getSoupOfInventory(inventory);

        if (soupsInInventory == null) {
            player.sendMessage(ChatColor.RED + "Keine Suppen mehr im Inventar");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        if (getFirstEmptySlot(inventory) == -1) {
            player.sendMessage(ChatColor.RED + "Du hast keine freie Hotbar");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        RefillTask refillTask = new RefillTask(player, kitPlayer);
        refillTask.runTaskTimer(KitApi.getInstance().getPlugin(), 0, refillTickSpeed);
        kitPlayer.putKitAttribute(refillTaskString, refillTask);
    }

    /**
     * @param inventory
     * @return -1 if there is no empty slot
     */
    private Integer getFirstEmptySlot(PlayerInventory inventory) {
        List<Material> emptyItems = Arrays.asList(Material.AIR, Material.BOWL);
        for (int i = 0; i < 9; i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null || emptyItems.contains(item.getType())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @param inventory
     * @return null if no soups in upper inventory
     */
    private Pair<Integer, ItemStack> getSoupOfInventory(PlayerInventory inventory) {
        for (int i = 9; i <= 35; i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && item.getType().equals(Material.MUSHROOM_STEW)) {
                return Pair.of(i, item);
            }
        }
        return null;
    }

    @Override
    public float getCooldown() {
        return cooldown;
    }

    private class RefillTask extends BukkitRunnable {
        private final Player player;
        private final KitPlayer kitPlayer;
        private boolean cooldownFlag;

        private RefillTask(Player player, KitPlayer kitPlayer) {
            this.player = player;
            this.kitPlayer = kitPlayer;
        }

        @Override
        public void run() {
            if (!kitPlayer.isValid() || isCancelled()) {
                end();
                return;
            }

            PlayerInventory inventory = player.getInventory();
            Pair<Integer, ItemStack> soupPair = getSoupOfInventory(inventory);

            if (soupPair == null) {
                player.sendMessage(ChatColor.RED + "Keine Suppen mehr im Inventar");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                end();
                return;
            }

            if (!cooldownFlag) {
                kitPlayer.activateKitCooldown(RefillKit.INSTANCE);
                cooldownFlag = true;
            }

            Integer emptySlot = getFirstEmptySlot(inventory);
            if (emptySlot != -1) {
                inventory.setItem(emptySlot, soupPair.getSecond());
                inventory.setItem(soupPair.getFirst(), new ItemStack(Material.AIR));
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
            } else {
                end();
            }
        }

        private void end() {
            kitPlayer.putKitAttribute(refillTaskString, null);
            cancel();
        }
    }
}
