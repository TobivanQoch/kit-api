package de.hglabor.plugins.kitapi;

import de.hglabor.plugins.kitapi.config.KitApiConfig;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.Passive;
import de.hglabor.plugins.kitapi.kit.config.Cooldown;
import de.hglabor.plugins.kitapi.kit.kits.ArcherKit;
import de.hglabor.plugins.kitapi.kit.kits.AutomaticKit;
import de.hglabor.plugins.kitapi.kit.kits.BearKit;
import de.hglabor.plugins.kitapi.kit.kits.BeequeenKit;
import de.hglabor.plugins.kitapi.kit.kits.BlinkKit;
import de.hglabor.plugins.kitapi.kit.kits.BomberKit;
import de.hglabor.plugins.kitapi.kit.kits.BoxerKit;
import de.hglabor.plugins.kitapi.kit.kits.CannibalKit;
import de.hglabor.plugins.kitapi.kit.kits.ChameleonKit;
import de.hglabor.plugins.kitapi.kit.kits.ChemistKit;
import de.hglabor.plugins.kitapi.kit.kits.ClawKit;
import de.hglabor.plugins.kitapi.kit.kits.CookiemonsterKit;
import de.hglabor.plugins.kitapi.kit.kits.CopyCatKit;
import de.hglabor.plugins.kitapi.kit.kits.DannyKit;
import de.hglabor.plugins.kitapi.kit.kits.DemomanKit;
import de.hglabor.plugins.kitapi.kit.kits.DiggerKit;
import de.hglabor.plugins.kitapi.kit.kits.ElytraKit;
import de.hglabor.plugins.kitapi.kit.kits.FahrradKit;
import de.hglabor.plugins.kitapi.kit.kits.FalcoKit;
import de.hglabor.plugins.kitapi.kit.kits.FiremanKit;
import de.hglabor.plugins.kitapi.kit.kits.FrostyKit;
import de.hglabor.plugins.kitapi.kit.kits.GamblerKit;
import de.hglabor.plugins.kitapi.kit.kits.GardenerKit;
import de.hglabor.plugins.kitapi.kit.kits.GhostKit;
import de.hglabor.plugins.kitapi.kit.kits.GladiatorKit;
import de.hglabor.plugins.kitapi.kit.kits.GravityKit;
import de.hglabor.plugins.kitapi.kit.kits.GripperKit;
import de.hglabor.plugins.kitapi.kit.kits.HemomancerKit;
import de.hglabor.plugins.kitapi.kit.kits.HulkKit;
import de.hglabor.plugins.kitapi.kit.kits.IrongolemKit;
import de.hglabor.plugins.kitapi.kit.kits.JackhammerKit;
import de.hglabor.plugins.kitapi.kit.kits.KangarooKit;
import de.hglabor.plugins.kitapi.kit.kits.KayaKit;
import de.hglabor.plugins.kitapi.kit.kits.LifestealerKit;
import de.hglabor.plugins.kitapi.kit.kits.LumberjackKit;
import de.hglabor.plugins.kitapi.kit.kits.MagmaKit;
import de.hglabor.plugins.kitapi.kit.kits.MedusaKit;
import de.hglabor.plugins.kitapi.kit.kits.MonkKit;
import de.hglabor.plugins.kitapi.kit.kits.NinjaKit;
import de.hglabor.plugins.kitapi.kit.kits.NoneKit;
import de.hglabor.plugins.kitapi.kit.kits.OpaKit;
import de.hglabor.plugins.kitapi.kit.kits.PenguinKit;
import de.hglabor.plugins.kitapi.kit.kits.PerfectKit;
import de.hglabor.plugins.kitapi.kit.kits.PoseidonKit;
import de.hglabor.plugins.kitapi.kit.kits.RandomKit;
import de.hglabor.plugins.kitapi.kit.kits.ReaperKit;
import de.hglabor.plugins.kitapi.kit.kits.RedstonerKit;
import de.hglabor.plugins.kitapi.kit.kits.RefillKit;
import de.hglabor.plugins.kitapi.kit.kits.ReviveKit;
import de.hglabor.plugins.kitapi.kit.kits.RogueKit;
import de.hglabor.plugins.kitapi.kit.kits.ScaffoldKit;
import de.hglabor.plugins.kitapi.kit.kits.ScientistKit;
import de.hglabor.plugins.kitapi.kit.kits.ScoutKit;
import de.hglabor.plugins.kitapi.kit.kits.ShadowKit;
import de.hglabor.plugins.kitapi.kit.kits.ShapeShifterKit;
import de.hglabor.plugins.kitapi.kit.kits.ShulkerKit;
import de.hglabor.plugins.kitapi.kit.kits.SlimeKit;
import de.hglabor.plugins.kitapi.kit.kits.SmogmogKit;
import de.hglabor.plugins.kitapi.kit.kits.SnailKit;
import de.hglabor.plugins.kitapi.kit.kits.SoulstealerKit;
import de.hglabor.plugins.kitapi.kit.kits.SpidermanKit;
import de.hglabor.plugins.kitapi.kit.kits.SpiegelKit;
import de.hglabor.plugins.kitapi.kit.kits.SpitKit;
import de.hglabor.plugins.kitapi.kit.kits.SquidKit;
import de.hglabor.plugins.kitapi.kit.kits.StomperKit;
import de.hglabor.plugins.kitapi.kit.kits.SurpriseKit;
import de.hglabor.plugins.kitapi.kit.kits.SwitcherKit;
import de.hglabor.plugins.kitapi.kit.kits.TankKit;
import de.hglabor.plugins.kitapi.kit.kits.ThorKit;
import de.hglabor.plugins.kitapi.kit.kits.TrashtalkerKit;
import de.hglabor.plugins.kitapi.kit.kits.VampireKit;
import de.hglabor.plugins.kitapi.kit.kits.VentKit;
import de.hglabor.plugins.kitapi.kit.kits.ViperKit;
import de.hglabor.plugins.kitapi.kit.kits.ZickZackKit;
import de.hglabor.plugins.kitapi.kit.passives.FastBreakPassive;
import de.hglabor.plugins.kitapi.kit.passives.GravediggerPassive;
import de.hglabor.plugins.kitapi.kit.passives.LessFallDamagePassive;
import de.hglabor.plugins.kitapi.kit.passives.MonsterPassive;
import de.hglabor.plugins.kitapi.kit.passives.NoCleanPassive;
import de.hglabor.plugins.kitapi.kit.passives.NoSoupDropPassive;
import de.hglabor.plugins.kitapi.kit.passives.NonePassive;
import de.hglabor.plugins.kitapi.kit.passives.PeacemakerPassive;
import de.hglabor.plugins.kitapi.kit.selector.KitSelector;
import de.hglabor.plugins.kitapi.kit.selector.PassiveSelector;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import de.hglabor.plugins.kitapi.supplier.KitItemSupplier;
import de.hglabor.plugins.kitapi.supplier.KitItemSupplierImpl;
import de.hglabor.plugins.kitapi.supplier.KitPlayerSupplier;
import de.hglabor.utils.noriskutils.feast.Feast;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public final class KitApi {
  private final static KitApi INSTANCE = new KitApi();
  public final List<AbstractKit> kits;
  private final List<Locale> supportedLanguages;
  private KitSelector kitSelector;
  private PassiveSelector passiveSelector;
  private KitPlayerSupplier playerSupplier;
  private KitItemSupplier itemSupplier;
  private JavaPlugin plugin;
  private Feast feast;

  private KitApi() {
    this.kits = new ArrayList<>();
    this.itemSupplier = KitItemSupplierImpl.INSTANCE;
    this.supportedLanguages = Arrays.asList(Locale.ENGLISH, Locale.GERMAN);
  }

  public static KitApi getInstance() {
    return INSTANCE;
  }

  public void checkUsesForCooldown(KitPlayer kitPlayer, AbstractKit kit, int maxUses) {
    String key = kit.getName() + "kitUses";
    if (kitPlayer.getKitAttribute(key) == null) {
      kitPlayer.putKitAttribute(key, new AtomicInteger());
    }
    AtomicInteger kitUses = kitPlayer.getKitAttribute(key);
    if (kitUses.getAndIncrement() > maxUses) {
      kitPlayer.activateKitCooldown(kit);
      kitUses.set(0);
    }
  }

  public String cooldownKey(AbstractKit kit) {
    return kit.getName() + "cooldown";
  }

  public void checkUsesForCooldown(Player player, AbstractKit kit, int maxUses) {
    checkUsesForCooldown(getPlayer(player), kit, maxUses);
  }

  public List<Locale> getSupportedLanguages() {
    return supportedLanguages;
  }

  public List<AbstractKit> emptyKitList() {
    int kitAmount = KitApiConfig.getInstance().getKitAmount();
    List<AbstractKit> emptyKitList = new ArrayList<>(kitAmount);
    for (int i = 0; i < kitAmount; i++) {
      emptyKitList.add(NoneKit.INSTANCE);
    }
    return emptyKitList;
  }

  public void enableKit(AbstractKit kit, boolean isEnabled) {
    kit.setEnabled(isEnabled);
    if (isEnabled) {
      if (kit instanceof Listener) {
        Bukkit.getPluginManager().registerEvents((Listener) kit, plugin);
      }
      for (Player player : Bukkit.getOnlinePlayers()) {
        KitPlayer kitPlayer = playerSupplier.getKitPlayer(player);
        if (kitPlayer.hasKit(kit)) {
          kit.onEnable(kitPlayer);
        }
      }
    } else {
      for (Player player : Bukkit.getOnlinePlayers()) {
        kit.onDeactivation(playerSupplier.getKitPlayer(player));
      }
      if (kit instanceof Listener) {
        HandlerList.unregisterAll((Listener) kit);
      }
    }
  }

  public void register(KitPlayerSupplier kitPlayerSupplier, KitSelector kitSelector, PassiveSelector passiveSelector, JavaPlugin plugin, Path path) {
    KitApiConfig.getInstance().register(path.toFile());
    this.playerSupplier = kitPlayerSupplier;
    this.kitSelector = kitSelector;
    this.passiveSelector = passiveSelector;
    this.plugin = plugin;
    if (kitSelector != null)
      plugin.getServer().getPluginManager().registerEvents(kitSelector, plugin);
    if (passiveSelector != null)
      plugin.getServer().getPluginManager().registerEvents(passiveSelector, plugin);
    kits.add(MagmaKit.INSTANCE);
    kits.add(NinjaKit.INSTANCE);
    kits.add(NoneKit.INSTANCE);
    kits.add(BlinkKit.INSTANCE);
    kits.add(SurpriseKit.INSTANCE);
    kits.add(CopyCatKit.INSTANCE);
    kits.add(GladiatorKit.INSTANCE);
    kits.add(GamblerKit.INSTANCE);
    kits.add(SmogmogKit.INSTANCE);
    kits.add(RogueKit.INSTANCE);
    kits.add(SnailKit.INSTANCE);
    kits.add(DiggerKit.INSTANCE);
    kits.add(FahrradKit.INSTANCE);
    kits.add(ReviveKit.INSTANCE);
    kits.add(TankKit.INSTANCE);
    kits.add(GravityKit.INSTANCE);
    kits.add(CannibalKit.INSTANCE);
    kits.add(ZickZackKit.INSTANCE);
    kits.add(ThorKit.INSTANCE);
    kits.add(TrashtalkerKit.INSTANCE);
    kits.add(StomperKit.INSTANCE);
    kits.add(DannyKit.INSTANCE);
    kits.add(JackhammerKit.INSTANCE);
    kits.add(IrongolemKit.INSTANCE);
    kits.add(SwitcherKit.INSTANCE);
    kits.add(SpitKit.INSTANCE);
    kits.add(SquidKit.INSTANCE);
    addLibsDisguiseKit(ShapeShifterKit.INSTANCE);
    kits.add(SpidermanKit.INSTANCE);
    //kits.add(ManipulationKit.INSTANCE);
    kits.add(EndermageKit.INSTANCE);
    kits.add(ViperKit.INSTANCE);
    kits.add(LumberjackKit.INSTANCE);
    kits.add(ReaperKit.INSTANCE);
    kits.add(GrapplerKit.INSTANCE);
    kits.add(ClawKit.INSTANCE);
    kits.add(AutomaticKit.INSTANCE);
    //kits.add(AnchorKit.INSTANCE);
    //kits.add(BarbarianKit.INSTANCE);
    //kits.add(TurtleKit.INSTANCE);
    kits.add(OpaKit.INSTANCE);
    //kits.add(BerserkerKit.INSTANCE);
    kits.add(ScoutKit.INSTANCE);
    kits.add(MonkKit.INSTANCE);
    kits.add(VampireKit.INSTANCE);
    kits.add(VentKit.INSTANCE);
    kits.add(KayaKit.INSTANCE);
    kits.add(SoulstealerKit.INSTANCE);
    //kits.add(AnalystKit.INSTANCE);
    kits.add(KangarooKit.INSTANCE);
    kits.add(HulkKit.INSTANCE);
    kits.add(DemomanKit.INSTANCE);
    kits.add(GardenerKit.INSTANCE);
    kits.add(GhostKit.INSTANCE);
    kits.add(BeequeenKit.INSTANCE);
    kits.add(PoseidonKit.INSTANCE);
    kits.add(RandomKit.INSTANCE);
    kits.add(ElytraKit.INSTANCE);
    kits.add(ArcherKit.INSTANCE);
    kits.add(HemomancerKit.INSTANCE);
    //kits.add(SpecialistKit.INSTANCE);
    kits.add(SpiegelKit.INSTANCE);
    addLibsDisguiseKit(ChameleonKit.INSTANCE);
    kits.add(ChemistKit.INSTANCE);
    kits.add(RefillKit.INSTANCE);
    //kits.add(SwordmanKit.INSTANCE);
    kits.add(BearKit.INSTANCE);
    //kits.add(MagicianKit.INSTANCE);
    kits.add(ShulkerKit.INSTANCE);
    //kits.add(SnakeKit.INSTANCE);
    kits.add(CookiemonsterKit.INSTANCE);
    kits.add(GripperKit.INSTANCE);
    kits.add(PenguinKit.INSTANCE);
    kits.add(PerfectKit.INSTANCE);
    kits.add(SlimeKit.INSTANCE);
    kits.add(ScaffoldKit.INSTANCE);
    //kits.add(AutopilotKit.INSTANCE);
    kits.add(MedusaKit.INSTANCE);
    kits.add(ShadowKit.INSTANCE);
    kits.add(LifestealerKit.INSTANCE);
    kits.add(BomberKit.INSTANCE);
    kits.add(ScientistKit.INSTANCE);
    kits.add(FalcoKit.INSTANCE);
    //kits.add(PirateKit.INSTANCE);
    //kits.add(BeamKit.INSTANCE);
    kits.add(FiremanKit.INSTANCE);
    kits.add(BoxerKit.INSTANCE);
    kits.add(FrostyKit.INSTANCE);
    kits.add(RedstonerKit.INSTANCE);


    kits.add(NonePassive.INSTANCE);
    kits.add(FastBreakPassive.INSTANCE);
    //kits.add(MonsterPassive.INSTANCE);
    kits.add(GravediggerPassive.INSTANCE);
    //kits.add(NoSwordDropPassive.INSTANCE); broken because of shop-api
    kits.add(NoSoupDropPassive.INSTANCE);
    //kits.add(RecraftToInvPassive.INSTANCE); broken for hg
    kits.add(PeacemakerPassive.INSTANCE);
    kits.add(NoCleanPassive.INSTANCE);
    kits.add(LessFallDamagePassive.INSTANCE);
    kits.add(MonsterPassive.INSTANCE);
    kits.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
    registerKits();
    if (kitSelector != null)
      kitSelector.load();
    if (passiveSelector != null)
      passiveSelector.load();

    Bukkit.getScheduler().runTaskTimer(plugin, () -> {
      for (Player player : Bukkit.getOnlinePlayers()) {
        getPlayer(player).tick();
      }
    }, 0, 1L);
  }

  private void registerKits() {
    KitApiConfig kitApiConfig = KitApiConfig.getInstance();
    for (AbstractKit kit : kits) {
      System.out.println(kit.getName());
      kitApiConfig.add(kit);
      kitApiConfig.load(kit);
      if (kit instanceof Listener) plugin.getServer().getPluginManager().registerEvents((Listener) kit, plugin);
    }
  }

  public KitItemSupplier getItemSupplier() {
    return itemSupplier;
  }

  public void setItemSupplier(KitItemSupplier itemSupplier) {
    this.itemSupplier = itemSupplier;
  }

  public List<AbstractKit> getEnabledKits() {
    return kits.stream().filter(AbstractKit::isEnabled).filter(it -> !(it instanceof Passive)).collect(Collectors.toList());
  }

  public List<Passive> getEnabledPassives() {
    return kits.stream().filter(AbstractKit::isEnabled).filter(it -> (it instanceof Passive)).map(it -> (Passive) it).collect(Collectors.toList());
  }

  public List<AbstractKit> getAllKits() {
    return kits;
  }

  public AbstractKit getAlphabeticallyKit(int index) {
    List<AbstractKit> kits = new ArrayList<>(getEnabledKits());
    kits.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
    return kits.get(index);
  }

  public AbstractKit getAlphabeticallyPassive(int index) {
    List<AbstractKit> kits = new ArrayList<>(getEnabledPassives());
    kits.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
    return kits.get(index);
  }

  public AbstractKit byItem(ItemStack itemStack) {
    for (AbstractKit kit : kits) {
      if (kit.getDisplayItems().stream().anyMatch(displayItem -> displayItem.isSimilar(itemStack))) {
        return kit;
      }
    }
    return null;
  }

  public KitPlayer getPlayer(Player player) {
    return playerSupplier.getKitPlayer(player);
  }

  public KitPlayer getRandomAlivePlayer() {
    return playerSupplier.getRandomAlivePlayer();
  }

  public boolean hasKitItemInAnyHand(Player player, AbstractKit kit) {
    //TODO edgecase hulk
    if (kit.getMainKitItem() != null && kit.getMainKitItem().getType().equals(Material.AIR) && player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
      return true;
    }
    return player.getInventory().getItemInOffHand().isSimilar(kit.getMainKitItem()) || player.getInventory().getItemInMainHand().isSimilar(kit.getMainKitItem());
  }

  public void giveKitItemsIfInvFull(KitPlayer kitPlayer, AbstractKit kit) {
    itemSupplier.giveKitItems(kitPlayer, kit);
  }

  public void giveKitItemsIfInvFull(KitPlayer kitPlayer, AbstractKit kit, List<ItemStack> items) {
    itemSupplier.giveKitItems(kitPlayer, kit, items);
  }

  public void giveItemsIfSlotEmpty(KitPlayer kitPlayer, List<ItemStack> kits) {
    itemSupplier.giveItems(kitPlayer, kits);
  }

  public void removeKitItems(AbstractKit kit, Player player) {
    player.getInventory().removeItem(kit.getKitItems().toArray(new ItemStack[0]));
  }

  public JavaPlugin getPlugin() {
    return plugin;
  }

  public KitSelector getKitSelector() {
    return kitSelector;
  }

  public PassiveSelector getPassiveSelector() {
    return passiveSelector;
  }

  public KitPlayerSupplier getPlayerSupplier() {
    return playerSupplier;
  }

  /**
   * used for not having third party libraries like libsdisguise...
   */
  private void addLibsDisguiseKit(AbstractKit kit) {
    if (Bukkit.getServer().getPluginManager().isPluginEnabled("LibsDisguises")) {
      this.kits.add(kit);
    }
  }

  public boolean sendCooldownMessage(KitPlayer kitPlayer, AbstractKit kit) {
    if (kit.getCooldown() > 0) {
      Cooldown kitCooldown = kitPlayer.getKitCooldown(kit);
      Player player = Bukkit.getPlayer(kitPlayer.getUUID());
      if (player == null) {
        return false;
      }
      if (kitCooldown.hasCooldown()) {
        long timeLeft = (kitCooldown.getEndTime()) - System.currentTimeMillis();
        if (timeLeft <= 0) {
          kitPlayer.clearCooldown(kit);
          return false;
        }
        //if (kit.getMainKitItem() != null && hasKitItemInAnyHand(player, kit)) {
        //player.sendActionBar(Localization.INSTANCE.getMessage("kit.cooldown", ImmutableMap.of("numberInSeconds", String.valueOf(timeLeft / 1000D)), ChatUtils.locale(player)));
        //} else if (kit.getMainKitItem() == null) {
        //player.sendActionBar(Localization.INSTANCE.getMessage("kit.cooldown", ImmutableMap.of("numberInSeconds", String.valueOf(timeLeft / 1000D)), ChatUtils.locale(player)));
        //}
        return true;
      }
    }
    return false;
  }

  public Feast getFeast() {
    return feast;
  }

  public void setFeast(Feast feast) {
    if (this.feast == null) {
      this.feast = feast;
    }
  }
}
