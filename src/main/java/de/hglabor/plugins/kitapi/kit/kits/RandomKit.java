package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.events.event.PlayerAteSoupEvent;
import de.hglabor.plugins.kitapi.kit.items.KitItemBuilder;
import de.hglabor.plugins.kitapi.kit.settings.BetaKit;
import de.hglabor.plugins.kitapi.kit.settings.BoolArg;
import de.hglabor.plugins.kitapi.kit.settings.IntArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import de.hglabor.plugins.kitapi.util.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

@BetaKit
public class RandomKit extends AbstractKit {
    private final static String QUESTION_MARK_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWU1YmI4YjMxZjQ2YWE5YWYxYmFhODhiNzRmMGZmMzgzNTE4Y2QyM2ZhYWM1MmEzYWNiOTZjZmU5MWUyMmViYyJ9fX0";
    public final static RandomKit INSTANCE = new RandomKit();
    @IntArg
    private final int randomItemAfterXSoups, itemAmount;
    @BoolArg
    private final boolean withPresoup;
    private final String soupAmountKey;

    private RandomKit() {
        super("Random");
        this.randomItemAfterXSoups = 10;
        this.itemAmount = 1;
        this.withPresoup = false;
        this.soupAmountKey = "soupAmount" + getName();
        setDisplayItem(new KitItemBuilder(Material.PLAYER_HEAD).setPlayerSkullSkin(QUESTION_MARK_HEAD).build());
    }

    @KitEvent
    @Override
    public void onPlayerAteSoupEvent(PlayerAteSoupEvent event, KitPlayer kitPlayer) {
        boolean hasPresouped = event.hasPresouped();
        if (withPresoup) {
            handleEatingSoup(kitPlayer);
        } else if (!hasPresouped) {
            handleEatingSoup(kitPlayer);
        }
    }

    private void handleEatingSoup(KitPlayer kitPlayer) {
        AtomicInteger soupAmount = kitPlayer.getKitAttributeOrDefault(soupAmountKey, new AtomicInteger());
        soupAmount.incrementAndGet();
        kitPlayer.putKitAttribute(soupAmountKey, soupAmount);

        if (soupAmount.get() >= randomItemAfterXSoups) {
            ItemStack itemStack = new ItemStack(BukkitUtils.getRandomMaterial(), itemAmount);
            KitApi.getInstance().giveKitItemsIfInvFull(kitPlayer, this, Collections.singletonList(itemStack));
            kitPlayer.putKitAttribute(soupAmountKey, new AtomicInteger());
        }
    }
}
