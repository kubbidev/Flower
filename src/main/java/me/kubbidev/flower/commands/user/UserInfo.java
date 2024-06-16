package me.kubbidev.flower.commands.user;

import me.kubbidev.flower.command.abstraction.ChildCommand;
import me.kubbidev.flower.command.access.CommandPermission;
import me.kubbidev.flower.command.spec.CommandSpec;
import me.kubbidev.flower.command.util.ArgumentList;
import me.kubbidev.flower.command.abstraction.CommandException;
import me.kubbidev.flower.command.access.ArgumentPermissions;
import me.kubbidev.flower.locale.Message;
import me.kubbidev.flower.model.User;
import me.kubbidev.flower.plugin.FlowerPlugin;
import me.kubbidev.flower.sender.Sender;
import me.kubbidev.flower.util.Predicates;
import me.kubbidev.flower.util.UniqueIdType;

public class UserInfo extends ChildCommand<User> {
    public UserInfo() {
        super(CommandSpec.USER_INFO, "info", CommandPermission.USER_INFO, Predicates.alwaysFalse());
    }

    @Override
    public void execute(FlowerPlugin plugin, Sender sender, User target, ArgumentList args, String label) throws CommandException {
        if (ArgumentPermissions.checkViewPerms(plugin, sender, getPermission().get(), target)) {
            Message.COMMAND_NO_PERMISSION.send(sender);
            return;
        }

        Message.USER_INFO_GENERAL.send(sender,
                target.getUsername().orElse("Unknown"),
                target.getUniqueId().toString(),
                UniqueIdType.determineType(target.getUniqueId(), plugin).describe(),
                plugin.getBootstrap().isPlayerOnline(target.getUniqueId())
        );
    }
}