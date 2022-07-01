package net.tttproject.ariesbuildings;

import net.luckperms.api.event.node.NodeMutateEvent;
import net.tttproject.ariesbuildings.commands.BlockHistoryCommand;
import net.tttproject.ariesbuildings.hooks.LuckPermsHook;
import net.tttproject.ariesbuildings.listeners.*;
import net.tttproject.ariesbuildings.sql.BlockHistorySQL;
import net.tttproject.ariesbuildings.utils.AFKHandler;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AriesBuildings extends JavaPlugin {

    public static final String PREFIX = "§9AriesBuildings §8» §7";

    @Override
    public void onEnable() {
        BlockHistorySQL.create();

        AFKHandler.instance().register(this);
        AFKHandler.instance().start(this);

        registerListener();
        registerCommands();
    }

    @Override
    public void onDisable() {
        AFKHandler.instance().stop();
    }

    private void registerListener() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new PlayerAsyncChatListener(), this);
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerQuitListener(), this);
        pluginManager.registerEvents(new PlayerAFKToggleListener(), this);

        if (LuckPermsHook.isEnabled()) {
            LuckPermsHook.subscribe(this, NodeMutateEvent.class, NodeMutateListener::handleNodeMutate);
        }
    }

    private void registerCommands() {
        new BlockHistoryCommand(this);
    }

}
