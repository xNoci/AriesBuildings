package net.tttproject.ariesbuildings.blockhistory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.tttproject.ariesbuildings.sql.BlockHistorySQL;
import net.tttprojekt.system.utils.ProjectFuture;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BlockHistoryManager {

    private static class InstanceHolder {
        private static final BlockHistoryManager instance = new BlockHistoryManager();
    }

    public static BlockHistoryManager instance() {
        return BlockHistoryManager.InstanceHolder.instance;
    }


    private final Cache<Integer, BlockHistory> blockHistoryData;

    private BlockHistoryManager() {
        blockHistoryData = CacheBuilder.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .build();
    }

    public void updateHistory(UUID uuid, BlockHistoryAction action, Material material, Location location) {
        BlockHistorySQL.insert(uuid, action, material, location);

        getHistory(location).onSuccess(blockHistory -> {
            blockHistory.addEntry(uuid, action, material, System.currentTimeMillis());
        });
    }

    public ProjectFuture<BlockHistory> getHistory(Location location) {
        ProjectFuture<BlockHistory> future = new ProjectFuture<>();

        BlockHistory history = blockHistoryData.getIfPresent(location.hashCode());

        if (history != null) {
            future.set(history);
            return future;
        }

        BlockHistorySQL.getHistory(location).onSuccess(loadedHistory -> {
            blockHistoryData.put(location.hashCode(), loadedHistory);
            future.set(loadedHistory);
        });
        return future;
    }

}
