package net.tttproject.airesbuildings.listeners;

import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.md_5.bungee.api.ChatColor;
import net.tttproject.airesbuildings.AiresBuildings;
import net.tttproject.airesbuildings.utils.AFKHandler;
import net.tttproject.airesbuildings.utils.TabList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Group group = AiresBuildings.getUserGroup(player.getUniqueId());

        String color = ChatColor.GRAY.toString();
        String displayName = "Default";

        if (group != null) {
            CachedMetaData metaData = group.getCachedData().getMetaData();
            color = metaData.getMetaValue("color");
            displayName = group.getDisplayName();
        }

        event.setQuitMessage(String.format("§c« %s", color + displayName + color + " §8| " + color + player.getName()));

        TabList.updateAll();
        AFKHandler.instance().removePlayer(player);
    }

}
