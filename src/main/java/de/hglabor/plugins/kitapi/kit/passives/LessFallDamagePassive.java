package de.hglabor.plugins.kitapi.kit.passives;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.Passive;
import de.hglabor.plugins.kitapi.kit.settings.DoubleArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class LessFallDamagePassive extends Passive implements Listener {

    public static final LessFallDamagePassive INSTANCE = new LessFallDamagePassive();

    @DoubleArg
    public double reduce;

    private LessFallDamagePassive() {
        super("LessFallDamage", Material.GOLDEN_BOOTS);
        this.reduce = 0.60;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (hasPassive(KitApi.getInstance().getPlayer((Player) event.getEntity()))) {
                if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    event.setDamage(event.getDamage() * this.reduce);
                }
            }
        }
    }

    private boolean hasPassive(KitPlayer kitPlayer) {
        return kitPlayer.getPassive().equals(LessFallDamagePassive.INSTANCE);
    }
}
