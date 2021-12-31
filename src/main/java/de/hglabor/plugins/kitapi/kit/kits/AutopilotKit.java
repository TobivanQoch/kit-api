package de.hglabor.plugins.kitapi.kit.kits;

/*
@BetaKit
public class AutopilotKit extends AbstractKit implements Listener {
    public final static AutopilotKit INSTANCE = new AutopilotKit();
    private final static String BOT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjM3NmFhZjY3NzYxMjZmYjkzZTJhMWViZjI3NGM2NDYzM2UyZTI3MTQ0NzNmZTlmMmRkMDU5OGE1ZDc3YmQifX19";
    @DoubleArg
    private final double range, movementSpeed;
    @IntArg
    private final int autoPilotInSeconds, health;
    private final String isAutoPilotKey;
    private final String autopilotTaskKey;
    @FloatArg
    private final float cooldown;

    private AutopilotKit() {
        super("Autopilot");
        ItemStack itemStack = new KitItemBuilder(Material.PLAYER_HEAD).setPlayerSkullSkin(BOT_HEAD).build();
        setDisplayItem(itemStack);
        setMainKitItem(itemStack);
        this.range = 2.5;
        this.isAutoPilotKey = "isAutoPilot" + getName();
        this.autopilotTaskKey = "autoPilot" + getName();
        this.movementSpeed = 0.7;
        this.autoPilotInSeconds = 15;
        this.cooldown = 45F;
        this.health = 40;
    }

    @Override
    public void onDisable(KitPlayer kitPlayer) {
        AutopilotMode autopilotMode = kitPlayer.getKitAttribute(autopilotTaskKey);
        if (autopilotMode != null) {
            autopilotMode.end();
        }
    }

    @EventHandler
    public void onPlayerStopSpectatingEntity(PlayerStopSpectatingEntityEvent event) {
        if (KitApi.getInstance().getPlayer(event.getPlayer()).getKitAttributeOrDefault(isAutoPilotKey, false)) {
            event.setCancelled(true);
        }
    }

    @KitEvent
    @Override
    public void onPlayerRightClickPlayerWithKitItem(PlayerInteractAtEntityEvent event, KitPlayer kitPlayer, Player rightClicked) {
        Player player = event.getPlayer();
        if (!kitPlayer.getKitAttributeOrDefault(isAutoPilotKey, false)) {
            AutopilotMode autopilotMode = new AutopilotMode(kitPlayer, player, rightClicked);
            autopilotMode.runTaskTimer(KitApi.getInstance().getPlugin(), 0, 20L);
            kitPlayer.putKitAttribute(autopilotTaskKey, autopilotMode);
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        KitPlayer kitPlayer = KitApi.getInstance().getPlayer(event.getPlayer());
        if (!event.getCause().equals(PlayerTeleportEvent.TeleportCause.SPECTATE)) {
            return;
        }
        if (kitPlayer.getKitAttributeOrDefault(isAutoPilotKey,false)) {
            event.setCancelled(true);
        }
    }

    @Override
    public float getCooldown() {
        return cooldown;
    }

    private class AutopilotMode extends BukkitRunnable {
        private final KitPlayer kitPlayer;
        private final Player player;
        private final PvPBot bot;
        private int counter;

        private AutopilotMode(KitPlayer kitPlayer, Player player, Player target) {
            this.kitPlayer = kitPlayer;
            this.player = player;
            this.bot = new PvPBot(player.getWorld(), player.getName(), target, KitApi.getInstance().getPlugin())
                    .withMovementSpeed(movementSpeed)
                    .withSkin(player.getName())
                    .withHealth(health)
                    .withItemInSlot(EquipmentSlot.HAND, new ItemStack(Material.STONE_SWORD))
                    .withRange(range * 2);
            init();
        }

        private void init() {
            kitPlayer.putKitAttribute(isAutoPilotKey, true);
            bot.spawn(player.getLocation());
            player.setGameMode(GameMode.SPECTATOR);
            player.setSpectatorTarget(bot.getEntity());
            player.sendMessage(ChatColor.GREEN + "Autopilot aktiviert!");
        }

        @Override
        public void run() {
            int timeLeft = autoPilotInSeconds - counter;

            if (counter >= autoPilotInSeconds || (!kitPlayer.isValid())) {
                end();
                return;
            }

            player.sendActionBar("Autopilot endet in: " + TimeConverter.stringify(timeLeft));
            counter++;
        }

        public void end() {
            this.cancel();
            bot.die(DamageSource.GENERIC);
            player.sendMessage(ChatColor.RED + "Autopilot wurde deaktiviert!");
            kitPlayer.putKitAttribute(isAutoPilotKey, false);
            kitPlayer.putKitAttribute(autopilotTaskKey, null);
            if (player.getGameMode() == GameMode.SPECTATOR && kitPlayer.isValid()) {
                player.setGameMode(GameMode.SURVIVAL);
            }
            kitPlayer.activateKitCooldown(AutopilotKit.this);
        }
    }
}
*/
