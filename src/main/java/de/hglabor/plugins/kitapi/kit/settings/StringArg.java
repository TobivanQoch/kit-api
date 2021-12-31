package de.hglabor.plugins.kitapi.kit.settings;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface StringArg {
  /**
   * Only one of these is allowed to be true, an exception will be thrown otherwise.
   *
   * @see com.mojang.brigadier.arguments.StringArgumentType.StringType
   */
  boolean word() default false;

  boolean greedy() default false;
}
