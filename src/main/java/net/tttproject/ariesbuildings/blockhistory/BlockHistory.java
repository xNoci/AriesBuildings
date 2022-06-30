package net.tttproject.ariesbuildings.blockhistory;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.*;
import java.util.stream.Collectors;

public class BlockHistory {

    private final Location location;

    private ArrayList<BlockHistoryEntry> entries = Lists.newArrayList();

    public BlockHistory(Location location) {
        this.location = location;
    }

    public Material getCurrentType() {
        return location.getBlock().getType();
    }

    public Set<BlockHistoryEntry> getEntries() {
        return entries.stream().sorted(Comparator.comparingLong(BlockHistoryEntry::getTimestamp)).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public void addEntry(UUID uuid, BlockHistoryAction action, Material material, long timestamp) {
        entries.add(new BlockHistoryEntry(uuid, action, material, timestamp));
    }


}
