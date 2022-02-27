package de.hglabor.plugins.kitapi.kit.events;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface KitEvent {
	Class<?> clazz() default Void.class;

	/**
	 * decides whether to print or not cooldown message
	 */
	boolean ignoreCooldown() default false;
}
