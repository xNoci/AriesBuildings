package net.tttproject.ariesbuildings.listeners;

import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.md_5.bungee.api.ChatColor;
import net.tttproject.ariesbuildings.hooks.LuckPermsHook;
import net.tttproject.ariesbuildings.scoreboardteam.TeamRegistryManager;
import net.tttproject.ariesbuildings.utils.AFKHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        String color = ChatColor.GRAY.toString();
        String displayName = "Default";

        if (LuckPermsHook.isEnabled()) {
            Group group = LuckPermsHook.getUserGroup(player.getUniqueId());

            if (group != null) {
                CachedMetaData metaData = group.getCachedData().getMetaData();
                color = metaData.getMetaValue("color");
                displayName = group.getDisplayName();
            }
        }

        event.setQuitMessage("§c« %s".formatted(color + displayName + color + " §8| " + color + player.getName()));

        AFKHandler.instance().removePlayer(player);
        TeamRegistryManager.instance().removeRegistry(player);
    }

}
