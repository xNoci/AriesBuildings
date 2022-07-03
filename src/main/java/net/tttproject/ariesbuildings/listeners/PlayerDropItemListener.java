package net.tttproject.ariesbuildings.listeners;

import net.tttproject.ariesbuildings.utils.Items;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {

    @EventHandler
    public void handleItemDrop(PlayerDropItemEvent event) {
        if (Items.BLOCK_HISTORY.isSameItem(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
        }
    }

}
