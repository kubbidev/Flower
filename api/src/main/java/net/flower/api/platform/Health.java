package net.flower.api.platform;

import java.util.Map;

/**
 * Represents the "health" status (healthcheck) of a Flower implementation.
 */
public interface Health {

    /**
     * Gets if Flower is healthy.
     *
     * @return if Flower is healthy
     */
    boolean isHealthy();

    /**
     * Gets extra metadata/details about the healthcheck result.
     *
     * @return details about the healthcheck status
     */
    Map<String, Object> getDetails();

}