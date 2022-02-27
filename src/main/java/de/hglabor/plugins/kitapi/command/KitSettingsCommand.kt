package de.hglabor.plugins.kitapi.command

import com.mojang.brigadier.arguments.StringArgumentType
import de.hglabor.plugins.kitapi.KitApi
import de.hglabor.plugins.kitapi.config.KitApiConfig
import de.hglabor.plugins.kitapi.kit.AbstractKit
import de.hglabor.plugins.kitapi.kit.settings.*
import de.hglabor.plugins.kitapi.util.ReflectionUtils
import de.hglabor.plugins.kitapi.util.Utils
import de.hglabor.utils.noriskutils.PermissionUtils
import net.axay.kspigot.commands.*
import net.axay.kspigot.extensions.onlinePlayers
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType
import org.bukkit.util.NumberConversions
import kotlin.math.max
import kotlin.math.min

@Suppress("Deprecation", "Unused")
object KitSettingsCommand {
	private const val PERMISSION = "hglabor.kitapi.changeKitSettings"

	fun enable() {
		println("Enabling Kit Command") //haha print
		command("hi") {
			runs { this.player.sendMessage("hi") } //hi
		}
		command("kitsettings") {
			requiresPermission(PERMISSION)
			KitApi.getInstance().allKits.forEach { kit ->
				literal(kit.name) {
					argument("setting", StringArgumentType.word()) {
						suggests { commandContext, suggestionsBuilder ->
							getFieldNames(kit).forEach {
								suggestionsBuilder.suggest(it)
							}
							suggestionsBuilder.buildFuture()
						}
						argument("value", StringArgumentType.word()) {
							suggests { commandContext, suggestionsBuilder ->
								kit.values(commandContext.getArgument("setting")).forEach {
									suggestionsBuilder.suggest(it)
								}
								suggestionsBuilder.buildFuture()
							}
							runs {
								if (PermissionUtils.checkForHigherRank(player)) {
									player.sendMessage(ChatColor.RED.toString() + "Player with higher rank is online.")
									return@runs
								}
								val fieldName = this.getArgument<String>("setting")
								val value = this.getArgument<String>("value")
								var replacement = ""
								val field = ReflectionUtils.getField(kit.javaClass, fieldName)
								if (field == null) {
									player.sendMessage(ChatColor.RED.toString() + "Incorrect field")
									return@runs
								}
								if (ReflectionUtils.isFloat(field)) {
									val annotation = getAnnotation(field.declaredAnnotations, FloatArg::class.java)
									val max =
										max(annotation!!.min, min(annotation.max, NumberConversions.toFloat(value)))
									replacement = max.toString()
									ReflectionUtils.set(field, kit, max)
								} else if (ReflectionUtils.isDouble(field)) {
									val annotation = getAnnotation(field.declaredAnnotations, DoubleArg::class.java)
									val max =
										max(annotation!!.min, min(annotation.max, NumberConversions.toDouble(value)))
									replacement = max.toString()
									ReflectionUtils.set(field, kit, max)
								} else if (ReflectionUtils.isInt(field)) {
									val annotation = getAnnotation(field.declaredAnnotations, IntArg::class.java)
									val max = max(annotation!!.min, min(annotation.max, NumberConversions.toInt(value)))
									replacement = max.toString()
									ReflectionUtils.set(field, kit, max)
								} else if (ReflectionUtils.isLong(field)) {
									val annotation = getAnnotation(field.declaredAnnotations, LongArg::class.java)
									val max =
										max(annotation!!.min, min(annotation.max, NumberConversions.toLong(value)))
									replacement = max.toString()
									ReflectionUtils.set(field, kit, max)
								} else if (ReflectionUtils.isBool(field)) {
									val bool = value.toBoolean()
									replacement = value
									KitApi.getInstance().enableKit(kit, bool)
								} else if (ReflectionUtils.isMaterial(field)) {
									val material = Material.valueOf(value)
									replacement = material.name
									ReflectionUtils.set(field, kit, material)
								} else if (ReflectionUtils.isPotionType(field)) {
									val potionType = PotionType.valueOf(value)
									replacement = potionType.name
									ReflectionUtils.set(field, kit, potionType)
								} else if (ReflectionUtils.isEntityType(field)) {
									val entityType = EntityType.valueOf(value)
									replacement = entityType.name
									ReflectionUtils.set(field, kit, entityType)
								} else if (ReflectionUtils.isPotionEffect(field)) {
									val potionEffectType = PotionEffectType.getByName(value)
									if (potionEffectType != null) {
										replacement = potionEffectType.name
										ReflectionUtils.set(field, kit, potionEffectType)
									}
								} else if (ReflectionUtils.isSound(field)) {
									val sound = Sound.valueOf(value)
									replacement = sound.name
									ReflectionUtils.set(field, kit, sound)
								}
								player.sendKitChangeMessage(kit, fieldName, replacement)
							}
						}
					}
				}
			}
			literal("debug") {
				runs {
					KitApiConfig.isDebug = !KitApiConfig.isDebug
					this.player.sendMessage("Debug ist ${KitApiConfig.isDebug}")
				}
			}
		}
	}

	private fun AbstractKit.values(setting: String): List<String> {
		val field = ReflectionUtils.getField(javaClass, setting)
		val currentValue = "Current Value: ${ReflectionUtils.get(field, this)}"
		if (ReflectionUtils.isFloat(field)) {
			val annotation: FloatArg? = getAnnotation(field.declaredAnnotations, FloatArg::class.java)
			if (annotation != null) {
				return listOf(
					currentValue,
					"Float value from " + annotation.min + " to " + annotation.max
				)
			}
		} else if (ReflectionUtils.isDouble(field)) {
			val annotation: DoubleArg? = getAnnotation(field.declaredAnnotations, DoubleArg::class.java)
			if (annotation != null) return listOf(
				currentValue,
				"Double value from " + annotation.min + " to " + annotation.max
			)
		} else if (ReflectionUtils.isInt(field)) {
			val annotation: IntArg? = getAnnotation(field.declaredAnnotations, IntArg::class.java)
			if (annotation != null) return listOf(
				currentValue,
				"Double value from " + annotation.min + " to " + annotation.max
			)
		} else if (ReflectionUtils.isLong(field)) {
			val annotation: LongArg? = getAnnotation(field.declaredAnnotations, LongArg::class.java)
			if (annotation != null) return listOf(
				currentValue,
				"Long value from " + annotation.min + " to " + annotation.max
			)
		} else if (ReflectionUtils.isBool(field)) {
			val annotation: BoolArg? = getAnnotation(field.declaredAnnotations, BoolArg::class.java)
			if (annotation != null) return listOf(currentValue, "Boolean: true or false")
		} else if (ReflectionUtils.isMaterial(field)) {
			val list: MutableList<String> = mutableListOf()
			list.add(currentValue)
			list.addAll(Material.values().map { it.name })
			return list
		} else if (ReflectionUtils.isPotionType(field)) {
			val list: MutableList<String> = java.util.ArrayList()
			list.add(currentValue)
			list.addAll(PotionType.values().map { it.name })
			return list
		} else if (ReflectionUtils.isEntityType(field)) {
			val list: MutableList<String> = mutableListOf()
			list.add(currentValue)
			list.addAll(EntityType.values().map { it.name })
			return list
		} else if (ReflectionUtils.isPotionEffect(field)) {
			val list: MutableList<String> = mutableListOf()
			list.add(currentValue)
			list.addAll(PotionEffectType.values().map { it.name })
			return list
		} else if (ReflectionUtils.isSound(field)) {
			val list: MutableList<String> = mutableListOf()
			list.add(currentValue)
			list.addAll(Sound.values().map { it.name })
			return list
		}
		return emptyList()
	}

	@Suppress("UNCHECKED_CAST")
	private fun <T : Annotation?> getAnnotation(annotations: Array<Annotation>, clazz: Class<T>): T? {
		return annotations.firstOrNull { it.annotationClass.javaObjectType == clazz }?.let { it as T }
	}

	private fun Player.sendKitChangeMessage(kit: AbstractKit, fieldName: String, value: String) {
		onlinePlayers.filter { it.hasPermission(PERMISSION) }.forEach {
			it.sendMessage("$name changed ${kit.name} -> $fieldName -> $value")
		}
	}

	private fun getFieldNames(kit: AbstractKit?): List<String> {
		//TODO get class recursively from package
		val kitAnnotations =
			listOf(
				DoubleArg::class.java, FloatArg::class.java, IntArg::class.java, BoolArg::class.java,
				LongArg::class.java, MaterialArg::class.java, StringArg::class.java, PotionTypeArg::class.java,
				EntityArg::class.java, SoundArg::class.java, ParticleArg::class.java
			)
		val names: MutableList<String> = ArrayList()
		for (field in Utils.getAllFields(kit)) {
			for (annotation in field.annotations) {
				if (kitAnnotations.contains(annotation.annotationClass.javaObjectType)) {
					names.add(field.name)
				}
			}
		}
		return names
	}
}

