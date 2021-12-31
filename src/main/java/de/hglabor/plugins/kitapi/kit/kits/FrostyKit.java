package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.settings.IntArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FrostyKit extends AbstractKit {

    public static final FrostyKit INSTANCE = new FrostyKit();

    @IntArg
    private final int potionAmpflier;

    private FrostyKit() {
        super("Frosty", Material.SNOW_BLOCK);
        potionAmpflier = 2;
    }

    @KitEvent
    @Override
    public void onPlayerMoveEvent(PlayerMoveEvent event, KitPlayer kitPlayer) {
        Player player = event.getPlayer();
        Material type = player.getLocation().clone().subtract(0, 1, 0).getBlock().getType();
        if(type == Material.SNOW_BLOCK || type == Material.SNOW) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, potionAmpflier));
            player.setFreezeTicks(60);
        }
    }
}
