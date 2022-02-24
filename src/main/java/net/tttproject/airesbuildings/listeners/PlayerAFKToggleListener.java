package net.tttproject.airesbuildings.listeners;

import net.tttproject.airesbuildings.AiresBuildings;
import net.tttproject.airesbuildings.events.PlayerAFKToggleEvent;
import net.tttproject.airesbuildings.utils.TabList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerAFKToggleListener implements Listener {

    @EventHandler
    public void handlePlayerAFKToggle(PlayerAFKToggleEvent event) {
        Player player = event.getPlayer();
        if(event.getAfkType() == PlayerAFKToggleEvent.AFKType.AFK) {
            player.sendMessage(AiresBuildings.PREFIX + "You are now afk.");
        } else {
            player.sendMessage(AiresBuildings.PREFIX + "You are no longer afk.");
        }

        TabList.updateAll();
    }

}
