package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.settings.BoolArg;
import de.hglabor.plugins.kitapi.kit.settings.FloatArg;
import de.hglabor.plugins.kitapi.kit.settings.IntArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import de.hglabor.utils.noriskutils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BomberKit extends AbstractKit {

    public static final BomberKit INSTANCE = new BomberKit();

    private final ItemStack BOMB;

    @IntArg
    private final int bombAmount, explosionDelay;

    @FloatArg
    private final float explosionPower;

    @BoolArg
    private final boolean makeFire;

    private final String remainingBombKey = "remainingBombs";

    private BomberKit() {
        super("Bomber", Material.TNT_MINECART);
        BOMB = new ItemBuilder(Material.TNT).setName(ChatColor.DARK_GRAY + "Bomb").build();
        setMainKitItem(BOMB);
        bombAmount = 3;
        explosionDelay = 5;
        explosionPower = 4;
        makeFire = true;
    }

    @Override
    public void onEnable(KitPlayer kitPlayer) {
        kitPlayer.putKitAttribute(remainingBombKey, bombAmount);
    }

    @KitEvent
    @Override
    public void onPlayerRightClickKitItem(PlayerInteractEvent event, KitPlayer kitPlayer) {
        Player player = event.getPlayer();
        Integer remainingBombs = kitPlayer.getKitAttribute(remainingBombKey);
        if(remainingBombs != null) {
            if(remainingBombs > 0) {
                Item bomb = player.getWorld().dropItem(player.getLocation(), BOMB);
                bomb.setCanPlayerPickup(false);
                bomb.setCanMobPickup(false);
                bomb.setVelocity(player.getLocation().getDirection().multiply(0.7).setY(0.2));
                remainingBombs-=1;
                kitPlayer.putKitAttribute(remainingBombKey, remainingBombs);
                Bukkit.getScheduler().runTaskLater(KitApi.getInstance().getPlugin(), () -> {
                    bomb.getWorld().createExplosion(player, bomb.getLocation(), explosionPower, makeFire, true);
                }, explosionDelay*20L);
                return;
            }
        }
        player.sendMessage(Component.text("Kill someone to get new bombs").color(TextColor.color(255, 0, 0)));
    }

    @KitEvent(clazz = PlayerDeathEvent.class)
    @Override
    public void onPlayerKillsPlayer(KitPlayer killer, KitPlayer dead) {
        Integer remainingBombs = killer.getKitAttribute(remainingBombKey);
        if(remainingBombs != null) {
            killer.putKitAttribute(remainingBombKey, remainingBombs+1);
        }
    }
}
