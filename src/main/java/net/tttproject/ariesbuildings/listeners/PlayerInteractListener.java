package net.tttproject.ariesbuildings.listeners;

import net.tttproject.ariesbuildings.AriesBuildings;
import net.tttproject.ariesbuildings.blockhistory.BlockHistoryManager;
import net.tttproject.ariesbuildings.utils.Items;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.upperlevel.spigot.book.BookUtil;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void handlePlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (Items.BLOCK_HISTORY.isSameItem(event.getItem())) {
            event.setCancelled(true);
            if (!player.hasPermission("aries.blockhistory.use")) {
                player.sendMessage(AriesBuildings.NO_PERMISSION);
                return;
            }

            if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK) {
                return;
            }

            Block selectedBlock = event.getClickedBlock();
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                BlockFace direction = event.getBlockFace();
                selectedBlock = selectedBlock.getRelative(direction);
            }

            BlockHistoryManager.instance().getHistory(selectedBlock.getLocation())
                    .onSuccess(blockHistory -> {
                        Bukkit.getScheduler().runTask(AriesBuildings.getInstance(), () -> BookUtil.openPlayer(player, blockHistory.getBookItem()));
                    });
        }
    }

}
