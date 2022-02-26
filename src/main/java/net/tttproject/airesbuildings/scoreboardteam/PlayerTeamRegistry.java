package net.tttproject.airesbuildings.scoreboardteam;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerTeamRegistry {

    private final HashMap<UUID, Team> teamsMap = Maps.newHashMap();

    private final Player owner;

    PlayerTeamRegistry(Player owner) {
        this.owner = owner;
    }

    public Team getTeam(UUID uuid) {
        return teamsMap.get(uuid);
    }


    public void clearTeams() {
        sendPackets(Team.Mode.TEAM_REMOVED);
        teamsMap.clear();
    }

    public void addTeam(UUID uuid, Team team) {
        removeTeam(uuid);
        teamsMap.put(uuid, team);
    }

    public void sendPackets(Team.Mode mode) {
        for (Team value : teamsMap.values()) {
            sendPacket(value, mode);
        }
    }

    private void sendPacket(Team team, Team.Mode mode) {
        if (owner == null || !owner.isOnline()) return;
        team.sendPacket(owner, mode);
    }

    public void sendPacket(UUID targetUUID, Team.Mode mode) {
        if (owner == null || !owner.isOnline()) return;
        Team team = getTeam(targetUUID);
        if (team == null) return;
        team.sendPacket(owner, mode);
    }

    public void removeTeam(UUID uuid) {
        Team team = getTeam(uuid);
        if (team == null) return;
        sendPacket(team, Team.Mode.TEAM_REMOVED);
        teamsMap.remove(uuid);
    }

}
