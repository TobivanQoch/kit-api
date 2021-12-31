package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.settings.IntArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import de.hglabor.plugins.kitapi.util.Utils;
import de.hglabor.utils.localization.Localization;
import de.hglabor.utils.noriskutils.ChatUtils;
import de.hglabor.utils.noriskutils.InventoryBuilder;
import de.hglabor.utils.noriskutils.ItemBuilder;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ChameleonKit extends AbstractKit {

  public static final ChameleonKit INSTANCE = new ChameleonKit();
  private final String catchedMobKey = "catchedMobs";
  private final NamespacedKey BINDED_MOB_KEY = new NamespacedKey(KitApi.getInstance().getPlugin(), "binded_mob");
  @IntArg
  private int maxAnimals;


  protected ChameleonKit() {
    super("Chameleon", Material.DIRT_PATH);
    setMainKitItem(getDisplayMaterial());
    maxAnimals = 9;
  }

  @Override
  public void onDeactivation(KitPlayer kitPlayer) {
    Optional<Player> bukkitPlayer = kitPlayer.getBukkitPlayer();
    bukkitPlayer.ifPresent(player -> {
      DisguiseAPI.undisguiseToAll(player);
      player.removePotionEffect(PotionEffectType.INVISIBILITY);
    });
  }

  @KitEvent
  @Override
  public void onPlayerGetsAttackedByLivingEntity(EntityDamageByEntityEvent event, Player player, LivingEntity attacker) {
    if (attacker instanceof Player) {
      DisguiseAPI.undisguiseToAll(player);
      player.removePotionEffect(PotionEffectType.INVISIBILITY);
    }
  }

  @KitEvent
  @SuppressWarnings("unchecked")
  @Override
  public void onPlayerRightClickKitItem(PlayerInteractEvent event, KitPlayer kitPlayer) {
    Player player = event.getPlayer();
    InventoryBuilder builder = new InventoryBuilder(KitApi.getInstance().getPlugin());
    builder.withName(Localization.INSTANCE.getMessage("chameleon.inventoryName", ChatUtils.locale(player)));
    builder.withSlots(Utils.translateGuiScale(maxAnimals));
    int slot = -1;
    if (KitApi.getInstance().getPlayer(player).getKitAttribute(catchedMobKey) != null) {
      for (EntityType caughtEntity : ((List<EntityType>) KitApi.getInstance().getPlayer(player).getKitAttribute(catchedMobKey))) {
        slot++;
        Material material;
        try {
          material = Material.valueOf(caughtEntity.name() + "_SPAWN_EGG");
        } catch (Exception e) {
          material = Material.SPAWNER;
        }
        ItemStack itemStack = new ItemBuilder(material).setName(caughtEntity.name().replace("_", " ")).setDescription("", ChatColor.DARK_AQUA.toString() + ChatColor.ITALIC + Localization.INSTANCE.getMessage("chameleon.tooltip", ChatUtils.locale(player))).build();
        ItemMeta meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(BINDED_MOB_KEY, PersistentDataType.STRING, caughtEntity.name());
        itemStack.setItemMeta(meta);
        builder.withItem(itemStack, slot, onClick -> {
          Player clickedPlayer = (Player) onClick.getWhoClicked();
          clickedPlayer.closeInventory();
          KitPlayer clickedKitPlayer = KitApi.getInstance().getPlayer(clickedPlayer);
          if (!clickedKitPlayer.areKitsDisabled() && clickedKitPlayer.isValid()) {
            if (onClick.getCurrentItem().hasItemMeta()) {
              PersistentDataContainer dataContainer = onClick.getCurrentItem().getItemMeta().getPersistentDataContainer();
              if (dataContainer.has(BINDED_MOB_KEY, PersistentDataType.STRING)) {
                EntityType entityType = EntityType.valueOf(dataContainer.get(BINDED_MOB_KEY, PersistentDataType.STRING));
                clickedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 255, false, false));
                DisguiseAPI.disguiseEntity(clickedPlayer, new MobDisguise(DisguiseType.getType(entityType)));
                if (DisguiseAPI.getDisguise(clickedPlayer).getEntity() instanceof LivingEntity) {
                  ((LivingEntity) DisguiseAPI.getDisguise(clickedPlayer).getEntity()).removePotionEffect(PotionEffectType.INVISIBILITY);
                }
              }
            }
          }
        });
      }
    }
    player.openInventory(builder.build());
  }

  @KitEvent
  @Override
  public void onPlayerLeftClickKitItem(PlayerInteractEvent event, KitPlayer kitPlayer) {
    DisguiseAPI.undisguiseToAll(event.getPlayer());
    event.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
  }

  @KitEvent
  @Override
  public void onHitLivingEntityWithKitItem(EntityDamageByEntityEvent event, KitPlayer attacker, LivingEntity entity) {
    if (!(entity instanceof Player)) {
      if (attacker.getKitAttribute(catchedMobKey) == null) {
        attacker.putKitAttribute(catchedMobKey, new ArrayList<>(Collections.singletonList(entity.getType())));
      } else {
        ArrayList<EntityType> catchedMobs = attacker.getKitAttribute(catchedMobKey);
        if (catchedMobs.size() == maxAnimals) {
          attacker.getBukkitPlayer().ifPresent(player -> {
            player.sendMessage(Localization.INSTANCE.getMessage("chameleon.reachedAnimalLimit", ChatUtils.locale(player)));
          });
        } else {
          if (!catchedMobs.contains(entity.getType())) {
            catchedMobs.add(entity.getType());
            attacker.putKitAttribute(catchedMobKey, catchedMobs);
          }
          attacker.getBukkitPlayer().ifPresent(player -> {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 255, false, false));
            DisguiseAPI.disguiseEntity(player, new MobDisguise(DisguiseType.getType(entity.getType())));
            if (DisguiseAPI.getDisguise(player).getEntity() instanceof LivingEntity) {
              ((LivingEntity) DisguiseAPI.getDisguise(player).getEntity()).removePotionEffect(PotionEffectType.INVISIBILITY);
            }
          });
        }
      }
    }
  }
}
