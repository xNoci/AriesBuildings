package net.tttproject.ariesbuildings.listeners;

import net.tttproject.ariesbuildings.AriesBuildings;
import net.tttproject.ariesbuildings.events.PlayerAFKToggleEvent;
import net.tttproject.ariesbuildings.utils.TabList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerAFKToggleListener implements Listener {

    @EventHandler
    public void handlePlayerAFKToggle(PlayerAFKToggleEvent event) {
        Player player = event.getPlayer();
        if(event.getAfkType() == PlayerAFKToggleEvent.AFKType.AFK) {
            player.sendMessage(AriesBuildings.PREFIX + "You are now afk.");
        } else {
            player.sendMessage(AriesBuildings.PREFIX + "You are no longer afk.");
        }

        TabList.updateAll();
    }

}
