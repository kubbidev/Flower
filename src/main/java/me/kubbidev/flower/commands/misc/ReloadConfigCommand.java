package me.kubbidev.flower.commands.misc;

import me.kubbidev.flower.command.spec.CommandSpec;
import me.kubbidev.flower.command.abstraction.CommandException;
import me.kubbidev.flower.command.abstraction.SingleCommand;
import me.kubbidev.flower.command.access.CommandPermission;
import me.kubbidev.flower.command.util.ArgumentList;
import me.kubbidev.flower.locale.Message;
import me.kubbidev.flower.plugin.FlowerPlugin;
import me.kubbidev.flower.sender.Sender;
import me.kubbidev.flower.util.Predicates;

public class ReloadConfigCommand extends SingleCommand {
    public ReloadConfigCommand() {
        super(CommandSpec.RELOAD_CONFIG, "ReloadConfig", CommandPermission.RELOAD_CONFIG, Predicates.alwaysFalse());
    }

    @Override
    public void execute(FlowerPlugin plugin, Sender sender, ArgumentList args, String label) throws CommandException {
        plugin.getConfiguration().reload();
        Message.RELOAD_CONFIG_SUCCESS.send(sender);
    }
}