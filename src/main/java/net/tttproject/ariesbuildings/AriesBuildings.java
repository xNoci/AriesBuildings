package net.tttproject.ariesbuildings;

import lombok.Getter;
import net.luckperms.api.event.node.NodeMutateEvent;
import net.tttproject.ariesbuildings.commands.BlockHistoryCommand;
import net.tttproject.ariesbuildings.hooks.FaweHook;
import net.tttproject.ariesbuildings.hooks.LuckPermsHook;
import net.tttproject.ariesbuildings.listeners.*;
import net.tttproject.ariesbuildings.sql.BlockHistorySQL;
import net.tttproject.ariesbuildings.utils.AFKHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AriesBuildings extends JavaPlugin {

    @Getter private static AriesBuildings instance;

    public static final String PREFIX = "§9AriesBuildings §8» §7";
    public static final String NO_PERMISSION = "§cI'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.";

    @Override
    public void onEnable() {
        instance = this;
        BlockHistorySQL.create();

        AFKHandler.instance().register(this);
        AFKHandler.instance().start(this);

        registerListener();
        registerCommands();
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(PREFIX + "§cThe server will be restarted.\nIt should be available again shortly."));

        AFKHandler.instance().stop();
    }

    private void registerListener() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new PlayerAsyncChatListener(), this);
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerQuitListener(), this);
        pluginManager.registerEvents(new PlayerAFKToggleListener(), this);
        pluginManager.registerEvents(new PlayerInteractListener(), this);
        pluginManager.registerEvents(new PlayerDropItemListener(), this);
        pluginManager.registerEvents(new BlockListener(), this);

        LuckPermsHook.subscribe(this, NodeMutateEvent.class, NodeMutateListener::handleNodeMutate);
        FaweHook.registerEvent(new WorldEditListener());
    }

    private void registerCommands() {
        new BlockHistoryCommand(this);
    }

}
