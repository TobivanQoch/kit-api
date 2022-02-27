package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.items.KitItemBuilder;
import de.hglabor.plugins.kitapi.kit.settings.BetaKit;
import de.hglabor.plugins.kitapi.kit.settings.IntArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import de.hglabor.utils.noriskutils.ChanceUtils;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@BetaKit
public class TrashtalkerKit extends AbstractKit {
	public final static TrashtalkerKit INSTANCE = new TrashtalkerKit();
	private final static String TALKING_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjgxNDIyZThkZGMwZDMxMDlhYTY1N2I4OWIwYjBlYjFkMjVjYjNiYzhkNTRkYzZjOTljM2M5YzA4MTQ0MDI1NCJ9fX0=";
	private final static List<String> texts = Arrays.asList(
			"%PLAYER_NAME% geh weiter skywars spielen c:",
			"Spielst du zum ersten mal? %PLAYER_NAME%",
			"LuciferGHG wird dich verfluchen %PLAYER_NAME%",
			"%PLAYER_NAME% ich liebe dich",
			"%PLAYER_NAME% willst du mich heiraten",
			"%PLAYER_NAME% wenn ich du wäre, wäre ich nicht ich.",
			"%PLAYER_NAME% wer? Der Bär",
			"%PLAYER_NAME% wichtiger fanboy",
			"%PLAYER_NAME% EZZZZZZZZZZZZZZZZZZZZ",
			"%PLAYER_NAME% DOGSHIT",
			"%PLAYER_NAME% an deiner Stelle würde ich bei NoRisk subben",
			"%PLAYER_NAME% du bist so geil",
			"%PLAYER_NAME% ich sitze grade heulend vor meinem drecks pc wegen einam kack mc ban es währe ja nichtmal schlimm wenn ihr einen grund hättet sie haben mich nur gebanned weil sie spaß dran haben sie vergessen komplett das hinter dem scheiß namen auch noch ein mensch mit gefühlen sitzt",
			"%PLAYER_NAME% Hallo ich bin Bam.",
			"Wow $PLAYER_NAME% Was soll die scheisse!!!!!!!",
			"%PLAYER_NAME% das bombt mich bis nach albanien",
			"Das hat mich ziemlich aus den Latschen gehauen %PLAYER_NAME%",
			"%PLAYER_NAME% weil sie mich früher dass auch getan haben",
			"TaitoPvP wird euch alle kriegen"
	);
	@IntArg
	private final int likelihoodToTrashtalkOnHit;

	private TrashtalkerKit() {
		super("Trashtalker", new KitItemBuilder(Material.PLAYER_HEAD).setPlayerSkullSkin(TALKING_HEAD).build());
		this.likelihoodToTrashtalkOnHit = 5;
	}

	@KitEvent
	public void onPlayerKillsPlayer(KitPlayer killer, KitPlayer dead) {
		killer.getBukkitPlayer().ifPresent(player -> player.chat(getRandomText(dead.getName())));
	}

	@KitEvent
	public void onPlayerAttacksLivingEntity(EntityDamageByEntityEvent event, KitPlayer attacker, LivingEntity entity) {
		if (ChanceUtils.roll(this.likelihoodToTrashtalkOnHit)) {
			attacker.getBukkitPlayer().ifPresent(player -> player.chat(getRandomText(entity.getName())));
		}
	}

	private String getRandomText(String name) {
		String text = texts.get(ThreadLocalRandom.current().nextInt(texts.size()));
		return text.replaceAll("%PLAYER_NAME%", name);
	}
}
