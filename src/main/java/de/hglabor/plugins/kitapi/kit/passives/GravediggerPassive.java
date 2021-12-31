package de.hglabor.plugins.kitapi.kit.passives;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.Passive;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.List;

public class GravediggerPassive extends Passive implements Listener {

    public static final GravediggerPassive INSTANCE = new GravediggerPassive();

    private GravediggerPassive() {
        super("Gravedigger", Material.SHULKER_BOX);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            assert killer != null;
            if (hasPassive(KitApi.getInstance().getPlayer(killer))) {
                List<ItemStack> drops = event.getDrops();
                KitPlayer deathPlayer = KitApi.getInstance().getPlayer(event.getEntity());
                List<AbstractKit> kits = deathPlayer.getKits();
                // remove KitItems from drops
                for (AbstractKit abstractKit : kits) {
                    Object[] kitItems = abstractKit.getKitItems().toArray();
                    for (Object item : kitItems) {
                        drops.remove(item);
                    }
                }
                // first ShulkerBox
                ItemStack itemStack1 = new ItemStack(Material.SHULKER_BOX);
                BlockStateMeta blockStateMeta1 = (BlockStateMeta) itemStack1.getItemMeta();
                ShulkerBox shulkerBox1 = (ShulkerBox) blockStateMeta1.getBlockState();
                // second ShulkerBox if first box is full
                ItemStack itemStack2 = new ItemStack(Material.SHULKER_BOX);
                BlockStateMeta blockStateMeta2 = (BlockStateMeta) itemStack2.getItemMeta();
                ShulkerBox shulkerBox2 = (ShulkerBox) blockStateMeta2.getBlockState();
                // add drops to ShulkerBox
                for (ItemStack i : drops) {
                    if (i != null && shulkerBox1.getInventory().firstEmpty() != -1) {
                        shulkerBox1.getInventory().addItem(i);
                    } else if (i != null) {
                        shulkerBox2.getInventory().addItem(i);
                    }
                }
                // add Shulker to Inventory or drop if inv is full
                addShulkerToInventory(killer, itemStack1, blockStateMeta1, shulkerBox1);
                if (shulkerBox1.getInventory().firstEmpty() == -1) {
                    addShulkerToInventory(killer, itemStack2, blockStateMeta2, shulkerBox2);
                }
                event.getDrops().clear();
            }
        }
    }

    private void addShulkerToInventory(Player killer, ItemStack itemStack, BlockStateMeta blockStateMeta, ShulkerBox shulkerBox) {
        blockStateMeta.setBlockState(shulkerBox);
        shulkerBox.update();
        itemStack.setItemMeta(blockStateMeta);
        if (killer.getInventory().firstEmpty() == -1) {
            killer.getWorld().dropItemNaturally(killer.getLocation(), itemStack);
        } else {
            killer.getInventory().addItem(itemStack);
        }
    }

    private boolean hasPassive(KitPlayer kitPlayer) {
        return kitPlayer.getPassive().equals(GravediggerPassive.INSTANCE);
    }
}
