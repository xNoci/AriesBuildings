package net.tttproject.ariesbuildings.blockhistory;

import lombok.Getter;
import net.tttprojekt.system.ProjectSystem;
import net.tttprojekt.system.utils.ProjectFuture;
import org.bukkit.Material;

import java.util.UUID;

public class BlockHistoryEntry {

    @Getter private final UUID uuid;
    @Getter private final BlockHistoryAction action;
    @Getter private final Material material;
    @Getter private final long timestamp;

    protected BlockHistoryEntry(UUID uuid, BlockHistoryAction action, Material material, long timestamp) {
        this.uuid = uuid;
        this.action = action;
        this.material = material;
        this.timestamp = timestamp;
    }

    public ProjectFuture<String> getPlayerName() {
        return ProjectSystem.getUserAPI().getName(this.uuid);
    }


}
