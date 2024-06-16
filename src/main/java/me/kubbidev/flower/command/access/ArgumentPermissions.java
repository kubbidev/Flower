package me.kubbidev.flower.command.access;

import me.kubbidev.flower.config.ConfigKeys;
import me.kubbidev.flower.model.User;
import me.kubbidev.flower.plugin.FlowerPlugin;
import me.kubbidev.flower.sender.Sender;
import net.flower.api.util.Tristate;

/**
 * Implements argument based permission checks for use in command implementations.
 */
public final class ArgumentPermissions {
    private ArgumentPermissions() {
    }

    private static final String USER_VIEW_SELF = CommandPermission.ROOT + "view.user.self";
    private static final String USER_VIEW_OTHERS = CommandPermission.ROOT + "view.user.others";

    /**
     * Checks if the sender has permission to view the given target
     *
     * @param plugin the plugin instance
     * @param sender the sender to check
     * @param base the base permission for the command
     * @param target the object the sender is truing to view
     * @return true if the sender should NOT be allowed to view the target, true if they should
     */
    public static boolean checkViewPerms(FlowerPlugin plugin, Sender sender, CommandPermission base, Object target) {
        if (!plugin.getConfiguration().get(ConfigKeys.USE_ARGUMENT_BASED_COMMAND_PERMISSIONS)) {
            return false;
        }

        if (target instanceof User) {
            User targetUser = (User) target;
            if (targetUser.getUniqueId().equals(sender.getUniqueId())) {
                // the sender is trying to view themselves
                Tristate state = sender.getPermissionValue(base.getPermission() + ".view.self");
                if (state != Tristate.UNDEFINED) {
                    return !state.asBoolean();
                } else {
                    // fallback to the global perm if the one for the specific command is undefined
                    Tristate globalState = sender.getPermissionValue(USER_VIEW_SELF);
                    return !globalState.asBoolean();
                }
            } else {
                // they're trying to view another user
                Tristate state = sender.getPermissionValue(base.getPermission() + ".view.others");
                if (state != Tristate.UNDEFINED) {
                    return !state.asBoolean();
                } else {
                    // fallback to the global perm if the one for the specific command is undefined
                    Tristate globalState = sender.getPermissionValue(USER_VIEW_OTHERS);
                    return !globalState.asBoolean();
                }
            }
        }

        return false;
    }
}