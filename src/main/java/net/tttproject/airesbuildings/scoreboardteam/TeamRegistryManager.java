package net.tttproject.airesbuildings.scoreboardteam;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeamRegistryManager {

    private static class InstanceHolder {
        private static final TeamRegistryManager instance = new TeamRegistryManager();
    }

    public static TeamRegistryManager instance() {
        return TeamRegistryManager.InstanceHolder.instance;
    }

    private final HashMap<UUID, PlayerTeamRegistry> registryMap = Maps.newHashMap();

    private TeamRegistryManager() {
    }

    public PlayerTeamRegistry getRegistry(Player player) {
        return registryMap.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerTeamRegistry(player));
    }

    public void clearRegistry() {
        for (PlayerTeamRegistry teamRegistry : registryMap.values()) {
            teamRegistry.clearTeams();
        }

        registryMap.clear();
    }

    public void removeRegistry(Player player) {
        for (Map.Entry<UUID, PlayerTeamRegistry> registryEntry : registryMap.entrySet()) {
            UUID targetUUID = registryEntry.getKey();
            Player target = Bukkit.getPlayer(targetUUID);

            if (target == null || !target.isOnline()) {
                registryMap.remove(targetUUID);
                continue;
            }

            PlayerTeamRegistry targetRegistry = registryEntry.getValue();
            targetRegistry.removeTeam(player.getUniqueId());
        }

        registryMap.remove(player.getUniqueId());
    }

}
