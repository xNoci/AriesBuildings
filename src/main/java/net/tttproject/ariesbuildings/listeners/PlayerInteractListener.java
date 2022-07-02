package net.tttproject.ariesbuildings.listeners;

import net.tttproject.ariesbuildings.AriesBuildings;
import net.tttproject.ariesbuildings.blockhistory.BlockHistoryManager;
import net.tttproject.ariesbuildings.utils.Items;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.upperlevel.spigot.book.BookUtil;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void handlePlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();

        if (Items.BLOCK_HISTORY.isSameItem(event.getItem())) {
            if (!player.hasPermission("aries.blockhistory")) {
                player.sendMessage(AriesBuildings.NO_PERMISSION);
                return;
            }

            BlockHistoryManager.instance().getHistory(event.getClickedBlock().getLocation())
                    .onSuccess(blockHistory -> {
                        Bukkit.getScheduler().runTask(AriesBuildings.getInstance(), () -> BookUtil.openPlayer(player, blockHistory.getBookItem()));
                    });
            return;
        }
    }

}
