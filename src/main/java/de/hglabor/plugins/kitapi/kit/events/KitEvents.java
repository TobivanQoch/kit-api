package de.hglabor.plugins.kitapi.kit.events;

import de.hglabor.plugins.kitapi.kit.events.event.PlayerAteSoupEvent;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;

public abstract class KitEvents {

	public void onPlayerAttacksLivingEntity(EntityDamageByEntityEvent event, KitPlayer attacker, LivingEntity entity) {
	}

	public void onPlayerGetsAttackedByLivingEntity(EntityDamageByEntityEvent event, Player player, LivingEntity attacker) {
	}

	public void onPlayerRightClicksOneOfMultipleKitItems(PlayerInteractEvent event, KitPlayer kitPlayer, ItemStack item) {
	}

	public void onPlayerLeftClicksOneOfMultipleKitItems(PlayerInteractEvent event, KitPlayer kitPlayer, ItemStack item) {
	}

	public void onHitLivingEntityWithKitItem(EntityDamageByEntityEvent event, KitPlayer attacker, LivingEntity entity) {
	}

	public void onPlayerKillsPlayer(KitPlayer killer, KitPlayer dead) {
	}

	public void onPlayerIsSneakingEvent(PlayerToggleSneakEvent event, KitPlayer kitPlayer) {
	}

	public void onPlayerIsNotSneakingAnymoreEvent(PlayerToggleSneakEvent event, KitPlayer kitPlayer) {
	}

	public void onPlayerRightClickKitItem(PlayerInteractEvent event, KitPlayer kitPlayer) {
	}

	public void onPlayerLeftClickKitItem(PlayerInteractEvent event, KitPlayer kitPlayer) {
	}

	public void onPlayerRightClickPlayerWithKitItem(PlayerInteractAtEntityEvent event, KitPlayer kitPlayer, Player rightClicked) {
	}

	public void onPlayerRightClickLivingEntityWithKitItem(PlayerInteractAtEntityEvent event, KitPlayer kitPlayer, LivingEntity entity) {
	}

	public void onEntityResurrect(EntityResurrectEvent event) {
	}

	public void onEntityDeath(EntityDeathEvent event) {
	}

	public void onPlayerKillsLivingEntity(EntityDeathEvent event, Player killer, Entity entity) {
	}

	public void onPlayerMoveEvent(PlayerMoveEvent event, KitPlayer kitPlayer) {
	}

	public void onKitPlayerDeath(PlayerDeathEvent event) {
	}

	public void onEntityDamage(EntityDamageEvent event) {
	}

	public void onCraftItem(CraftItemEvent event) {
	}

	public void onDropItem(PlayerDropItemEvent event, KitPlayer kitPlayer) {
	}

	public void onEntityMoveWithPassengerEvent(EntityMoveEvent event, KitPlayer kitPlayer) {
	}

	public void onVehicleMoveEvent(VehicleMoveEvent event, KitPlayer kitPlayer) {
	}

	public void onProjectileLaunch(ProjectileLaunchEvent event) {
	}

	public void onKitPlayerShootBow(EntityShootBowEvent event, KitPlayer kitPlayer, Entity projectile) {
	}

	public void onBlockBreakWithKitItem(BlockBreakEvent event) {
	}

	public void onHitEntityWithKitItem(EntityDamageByEntityEvent event, KitPlayer kitPlayer, Entity entity) {
	}

	public void onPlayerAteSoupEvent(PlayerAteSoupEvent event, KitPlayer kitPlayer) {
	}

	public void onPlayerRightClickEntityWithKitItem(PlayerInteractAtEntityEvent event, KitPlayer kitPlayer, Entity entity) {
	}

	public void onProjectileHitEvent(ProjectileHitEvent event, KitPlayer kitPlayer, Entity hitEntity) {
	}

	public void onPlayerRightClicksBlock(PlayerInteractEvent event, KitPlayer kitPlayer, Block block) {
	}
}
