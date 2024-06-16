package me.kubbidev.flower.commands.user;

import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import me.kubbidev.flower.command.spec.CommandSpec;
import me.kubbidev.flower.command.abstraction.Command;
import me.kubbidev.flower.command.abstraction.ParentCommand;
import me.kubbidev.flower.locale.Message;
import me.kubbidev.flower.model.User;
import me.kubbidev.flower.model.UserIdentifier;
import me.kubbidev.flower.plugin.FlowerPlugin;
import me.kubbidev.flower.sender.Sender;
import me.kubbidev.flower.util.CaffeineFactory;
import me.kubbidev.flower.util.Uuids;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class UserParentCommand extends ParentCommand<User, UserIdentifier> {

    // we use a lock per unique user
    // this helps prevent race conditions where commands are being executed concurrently
    // and overriding each other.
    // it's not a great solution, but it mostly works.
    private final LoadingCache<UUID, ReentrantLock> locks = CaffeineFactory.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build(key -> new ReentrantLock());

    public UserParentCommand() {
        super(CommandSpec.USER, "User", Type.TAKES_ARGUMENT_FOR_TARGET, ImmutableList.<Command<User>>builder()
                .add(new UserInfo())
                .build()
        );
    }

    public static UUID parseTargetUniqueId(String target, FlowerPlugin plugin, Sender sender) {
        UUID parsed = Uuids.parse(target);
        if (parsed != null) {
            return parsed;
        }

        if (!plugin.testUsernameValidity(target)) {
            Message.USER_INVALID_ENTRY.send(sender, target);
            return null;
        }

        UUID lookup = plugin.lookupUniqueId(target).orElse(null);
        if (lookup == null) {
            Message.USER_NOT_FOUND.send(sender, target);
            return null;
        }

        return lookup;
    }

    @Override
    protected List<String> getTargets(FlowerPlugin plugin) {
        return new ArrayList<>(plugin.getBootstrap().getPlayerList());
    }

    @Override
    protected UserIdentifier parseTarget(String target, FlowerPlugin plugin, Sender sender) {
        UUID uniqueId = parseTargetUniqueId(target, plugin, sender);
        if (uniqueId == null) {
            return null;
        }

        String name = plugin.getStorage().getPlayerName(uniqueId).join();
        return UserIdentifier.of(uniqueId, name);
    }

    @Override
    protected ReentrantLock getLockForTarget(UserIdentifier target) {
        return this.locks.get(target.getUniqueId());
    }

    @Override
    protected User getTarget(UserIdentifier target, FlowerPlugin plugin, Sender sender) {
        return plugin.getStorage().loadUser(target.getUniqueId(), target.getUsername().orElse(null)).join();
    }

    @Override
    protected void cleanup(User user, FlowerPlugin plugin) {
        plugin.getUserManager().getHouseKeeper().cleanup(user.getUniqueId());
    }
}