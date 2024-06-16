package net.flower.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Provides static access to the {@link Flower} API.
 *
 * <p>Ideally, the ServiceManager for the platform should be used to obtain an
 * instance, however, this provider can be used if this is not viable.</p>
 */
public final class FlowerProvider {
    private static Flower instance = null;

    /**
     * Gets an instance of the {@link Flower} API,
     * throwing {@link IllegalStateException} if the API is not loaded yet.
     *
     * <p>This method will never return null.</p>
     *
     * @return an instance of the Flower API
     * @throws IllegalStateException if the API is not loaded yet
     */
    public static @NotNull Flower get() {
        Flower instance = FlowerProvider.instance;
        if (instance == null) {
            throw new NotLoadedException();
        }
        return instance;
    }

    @ApiStatus.Internal
    static void register(Flower instance) {
        FlowerProvider.instance = instance;
    }

    @ApiStatus.Internal
    static void unregister() {
        FlowerProvider.instance = null;
    }

    @ApiStatus.Internal
    private FlowerProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    /**
     * Exception thrown when the API is requested before it has been loaded.
     */
    private static final class NotLoadedException extends IllegalStateException {
        private static final String MESSAGE = "The Flower API isn't loaded yet!\n" +
                                              "This could be because:\n" +
                                              "  a) the Flower plugin is not installed or it failed to enable\n" +
                                              "  b) the plugin in the stacktrace does not declare a dependency on Flower\n" +
                                              "  c) the plugin in the stacktrace is retrieving the API before the plugin 'enable' phase\n" +
                                              "     (call the #get method in onEnable, not the constructor!)\n";

        NotLoadedException() {
            super(MESSAGE);
        }
    }
}