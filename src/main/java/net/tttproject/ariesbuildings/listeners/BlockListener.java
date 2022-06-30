package net.tttproject.ariesbuildings.listeners;

import net.tttproject.ariesbuildings.blockhistory.BlockHistoryAction;
import net.tttproject.ariesbuildings.blockhistory.BlockHistoryManager;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {

    @EventHandler
    public void handleBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        BlockHistoryManager.instance().updateHistory(event.getPlayer().getUniqueId(), BlockHistoryAction.PLACE, block.getType(), block.getLocation());
    }

    @EventHandler
    public void handleBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        BlockHistoryManager.instance().updateHistory(event.getPlayer().getUniqueId(), BlockHistoryAction.BREAK, block.getType(), block.getLocation());
    }

}
