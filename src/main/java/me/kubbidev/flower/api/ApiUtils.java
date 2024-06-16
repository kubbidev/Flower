package me.kubbidev.flower.api;

import com.google.common.base.Preconditions;
import me.kubbidev.flower.config.ConfigKeys;
import me.kubbidev.flower.plugin.FlowerPlugin;
import me.kubbidev.flower.storage.misc.DataConstraints;

import java.util.function.Predicate;

public final class ApiUtils {
    private ApiUtils() {}

    public static String checkUsername(String s, FlowerPlugin plugin) {
        if (s == null) {
            return null;
        }

        Predicate<String> test = plugin.getConfiguration().get(ConfigKeys.ALLOW_INVALID_USERNAMES) ?
                DataConstraints.PLAYER_USERNAME_TEST_LENIENT :
                DataConstraints.PLAYER_USERNAME_TEST;

        Preconditions.checkArgument(test.test(s), "Invalid username entry: " + s);
        return s;
    }
}