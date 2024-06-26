package net.flower.api.event.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents the position of a parameter within an event.
 *
 * <p>This is an implementation detail and should not be relied upon.</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {

    /**
     * Gets the index of the parameter.
     *
     * @return the index
     */
    int value();

}