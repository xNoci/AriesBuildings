package net.tttproject.ariesbuildings.hooks;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.LuckPermsEvent;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class LuckPermsHook {


    private static final Logger LOGGER = LoggerFactory.getLogger(LuckPermsHook.class);

    private static final boolean foundCloudNet;

    static {
        boolean found = false;
        try {
            Class.forName("net.luckperms.api.LuckPerms");
            found = true;
        } catch (ClassNotFoundException e) {
            LOGGER.info("Could not find LuckPerms API.");
        }

        foundCloudNet = found;
    }

    public static boolean isEnabled() {
        return foundCloudNet;
    }

    public static <T extends LuckPermsEvent> void subscribe(JavaPlugin plugin, Class<T> eventClass, Consumer<? super T> handler) {
        EventBus eventBus = LuckPermsProvider.get().getEventBus();
        eventBus.subscribe(plugin, eventClass, handler);
    }

    @Nullable
    public static Group getUserGroup(UUID uuid) {
        if (!isEnabled()) return null;

        LuckPerms luckPerms = LuckPermsProvider.get();
        User user = luckPerms.getUserManager().getUser(uuid);
        List<Group> groups = getGroupsSorted();

        if (user == null) {
            return groups.get(0);
        }

        String groupName = user.getPrimaryGroup();
        return luckPerms.getGroupManager().getGroup(groupName);
    }

    public static List<Group> getGroupsSorted() {
        if (!isEnabled()) return new ArrayList<>();

        LuckPerms luckPerms = LuckPermsProvider.get();
        Set<Group> loadedGroups = luckPerms.getGroupManager().getLoadedGroups();

        return loadedGroups.stream().sorted((o1, o2) -> {
            Integer weightOne = o1.getWeight().orElse(0);
            int weightTwo = o2.getWeight().orElse(0);

            return weightOne.compareTo(weightTwo);
        }).collect(Collectors.toCollection(ArrayList::new));
    }

}
