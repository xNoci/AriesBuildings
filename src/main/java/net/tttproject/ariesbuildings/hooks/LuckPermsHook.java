package net.tttproject.ariesbuildings.hooks;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.LuckPermsEvent;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.tttproject.ariesbuildings.AriesBuildings;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class LuckPermsHook {


    private static boolean checktPlugin = false;
    private static boolean found;

    public static boolean isEnabled() {
        if (!checktPlugin) {
            checktPlugin = true;
            Plugin plugin = Bukkit.getPluginManager().getPlugin("LuckPerms");
            found = plugin != null;

            AriesBuildings.getInstance().getLogger().info("%s is %s.".formatted(LuckPermsHook.class.getSimpleName(), found ? "enabled" : "disabled"));
        }
        return found;
    }

    public static <T extends LuckPermsEvent> void subscribe(JavaPlugin plugin, Class<T> eventClass, Consumer<? super T> handler) {
        if (!isEnabled()) return;
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
