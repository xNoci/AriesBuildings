package net.tttproject.ariesbuildings.listeners;

import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.md_5.bungee.api.ChatColor;
import net.tttproject.ariesbuildings.hooks.LuckPermsHook;
import net.tttproject.ariesbuildings.utils.TabList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Group group = LuckPermsHook.getUserGroup(player.getUniqueId());

        String color = ChatColor.GRAY.toString();
        String displayName = "Default";

        if (group != null) {
            CachedMetaData metaData = group.getCachedData().getMetaData();
            color = metaData.getMetaValue("color");
            displayName = group.getDisplayName();
        }

        event.setJoinMessage(String.format("§a» %s", color + displayName + color + " §8| " + color + player.getName()));

        TabList.updateAll();
    }

}
