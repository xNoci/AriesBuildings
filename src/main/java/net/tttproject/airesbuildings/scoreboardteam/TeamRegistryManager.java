package net.tttproject.airesbuildings.scoreboardteam;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.UUID;

public class TeamRegistryManager {

    private static class InstanceHolder {
        private static final TeamRegistryManager instance = new TeamRegistryManager();
    }

    public static TeamRegistryManager instance() {
        return TeamRegistryManager.InstanceHolder.instance;
    }

    private final HashMap<UUID, Team> teamsMap = Maps.newHashMap();

    private TeamRegistryManager() {
    }

    public Team getTeam(UUID uuid) {
        return teamsMap.get(uuid);
    }

    public void removeTeam(UUID uuid) {
        Team team = getTeam(uuid);
        if (team == null) return;
        team.broadcastPacket(Team.Mode.TEAM_REMOVED);
        teamsMap.remove(uuid);
    }

    public void addTeam(UUID uuid, Team team) {
        removeTeam(uuid);
        teamsMap.put(uuid, team);
    }


}
