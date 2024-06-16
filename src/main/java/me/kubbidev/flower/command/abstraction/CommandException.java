package me.kubbidev.flower.command.abstraction;

import me.kubbidev.flower.sender.Sender;

/**
 * Exception to be thrown if there is an error processing a command
 */
public abstract class CommandException extends Exception {

    protected abstract void handle(Sender sender);

    public void handle(Sender sender, String label, Command<?> command) {
        handle(sender);
    }
}