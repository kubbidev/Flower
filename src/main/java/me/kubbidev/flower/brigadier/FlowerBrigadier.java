package me.kubbidev.flower.brigadier;

import com.mojang.brigadier.tree.LiteralCommandNode;
import me.kubbidev.flower.FBukkitPlugin;
import me.kubbidev.flower.sender.Sender;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import me.lucko.commodore.file.CommodoreFileReader;
import org.bukkit.command.Command;

import java.io.InputStream;

/**
 * Registers Flower command data to brigadier using {@link Commodore}.
 */
public final class FlowerBrigadier {
    private FlowerBrigadier() {
    }

    public static void register(FBukkitPlugin plugin, Command pluginCommand) throws Exception {
        Commodore commodore = CommodoreProvider.getCommodore(plugin.getLoader());
        try (InputStream is = plugin.getBootstrap().getResourceStream("flower.commodore")) {
            if (is == null) {
                throw new Exception("Brigadier command data missing from jar");
            }

            LiteralCommandNode<?> commandNode = CommodoreFileReader.INSTANCE.parse(is);
            commodore.register(pluginCommand, commandNode, player -> {
                Sender playerAsSender = plugin.getSenderFactory().wrap(player);
                return plugin.getCommandManager().hasPermissionForAny(playerAsSender);
            });
        }
    }

}