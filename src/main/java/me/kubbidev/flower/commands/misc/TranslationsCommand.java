package me.kubbidev.flower.commands.misc;

import me.kubbidev.flower.command.abstraction.CommandException;
import me.kubbidev.flower.command.abstraction.SingleCommand;
import me.kubbidev.flower.command.access.CommandPermission;
import me.kubbidev.flower.command.spec.CommandSpec;
import me.kubbidev.flower.command.util.ArgumentList;
import me.kubbidev.flower.locale.Message;
import me.kubbidev.flower.plugin.FlowerPlugin;
import me.kubbidev.flower.sender.Sender;
import me.kubbidev.flower.util.Predicates;

import java.util.Locale;
import java.util.stream.Collectors;

public class TranslationsCommand extends SingleCommand {

    public TranslationsCommand() {
        super(CommandSpec.TRANSLATIONS, "Translations", CommandPermission.TRANSLATIONS, Predicates.alwaysFalse());
    }

    @Override
    public void execute(FlowerPlugin plugin, Sender sender, ArgumentList args, String label) throws CommandException {
        Message.TRANSLATIONS_SEARCHING.send(sender);
        Message.INSTALLED_TRANSLATIONS.send(sender, plugin.getTranslationManager().getInstalledLocales().stream().map(Locale::toLanguageTag).sorted().collect(Collectors.toList()));
    }
}