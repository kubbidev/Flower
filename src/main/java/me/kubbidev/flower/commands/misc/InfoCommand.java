package me.kubbidev.flower.commands.misc;

import me.kubbidev.flower.command.access.CommandPermission;
import me.kubbidev.flower.command.spec.CommandSpec;
import me.kubbidev.flower.command.util.ArgumentList;
import me.kubbidev.flower.command.abstraction.CommandException;
import me.kubbidev.flower.command.abstraction.SingleCommand;
import me.kubbidev.flower.locale.Message;
import me.kubbidev.flower.plugin.FlowerPlugin;
import me.kubbidev.flower.sender.Sender;
import me.kubbidev.flower.util.Predicates;

public class InfoCommand extends SingleCommand {
    public InfoCommand() {
        super(CommandSpec.INFO, "Info", CommandPermission.INFO, Predicates.alwaysFalse());
    }

    @Override
    public void execute(FlowerPlugin plugin, Sender sender, ArgumentList args, String label) throws CommandException {
        Message.INFO.send(sender, plugin, plugin.getStorage().getMeta());
    }
}