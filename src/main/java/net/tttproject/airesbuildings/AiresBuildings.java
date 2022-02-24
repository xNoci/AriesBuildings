package net.tttproject.airesbuildings;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeMutateEvent;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.tttproject.airesbuildings.listeners.*;
import net.tttproject.airesbuildings.utils.AFKHandler;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class AiresBuildings extends JavaPlugin {

    public static final String PREFIX = "§9AiresBuildings §8» §7";

    @Override
    public void onEnable() {

        AFKHandler.instance().register(this);
        AFKHandler.instance().start(this);

        registerListener();
    }

    @Override
    public void onDisable() {
        AFKHandler.instance().stop();
    }

    private void registerListener() {
        PluginManager pluginManager = getServer().getPluginManager();
        EventBus eventBus = LuckPermsProvider.get().getEventBus();


        pluginManager.registerEvents(new PlayerAsyncChatListener(), this);
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerQuitListener(), this);
        pluginManager.registerEvents(new PlayerAFKToggleListener(), this);

        eventBus.subscribe(this, NodeMutateEvent.class, NodeMutateListener::handleNodeMutate);
    }

    public static Group getUserGroup(UUID uuid) {
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
        LuckPerms luckPerms = LuckPermsProvider.get();
        Set<Group> loadedGroups = luckPerms.getGroupManager().getLoadedGroups();

        return loadedGroups.stream().sorted((o1, o2) -> {
            Integer weightOne = o1.getWeight().orElse(0);
            int weightTwo = o2.getWeight().orElse(0);

            return weightOne.compareTo(weightTwo);
        }).collect(Collectors.toCollection(ArrayList::new));
    }

}
