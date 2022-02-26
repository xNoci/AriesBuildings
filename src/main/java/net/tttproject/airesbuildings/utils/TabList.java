package net.tttproject.airesbuildings.utils;

import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.tttproject.airesbuildings.AiresBuildings;
import net.tttproject.airesbuildings.scoreboardteam.Team;
import net.tttproject.airesbuildings.scoreboardteam.TeamRegistryManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TabList {

    private static final String HEADER = "";
    private static final String FOOTER = "";

    private static final String AFK_SUFFIX = " §o§7[AFK]";

    public static void updateAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setPlayerListHeaderFooter(HEADER, FOOTER);
            createPlayer(player);
        }
    }

    private static void createPlayer(Player player) {
        TeamRegistryManager teamRegistry = TeamRegistryManager.instance();
        Team team = teamRegistry.getTeam(player.getUniqueId());
        if (team != null) {
            updatePlayer(player);
            return;
        }


        String teamName = 999 + player.getUniqueId().toString().replaceAll("-", "");
        String prefix = "§7Gast §8| §7";
        ChatColor color = ChatColor.GRAY;

        Group targetGroup = AiresBuildings.getUserGroup(player.getUniqueId());
        if (targetGroup != null) {
            teamName = getSortID(targetGroup) + player.getUniqueId().toString().replaceAll("-", "");

            CachedMetaData metaData = targetGroup.getCachedData().getMetaData();
            String metaPrefix = metaData.getPrefix();
            String metaColor = metaData.getMetaValue("color");

            if (metaPrefix != null) {
                prefix = metaPrefix;
            }

            if (metaColor != null) {
                if (metaColor.startsWith("§") && metaColor.length() >= 2) {
                    color = ChatColor.getByChar(metaColor.charAt(1));
                } else {
                    color = ChatColor.valueOf(metaColor);
                }
            }
        }


        team = new Team(teamName);
        team.getPlayers().add(player);
        team.setCanSeeFriendlyInvisibles(true);
        team.setCollisionRule(Team.CollisionRule.NEVER);
        team.setColor(color);
        team.setPrefix(prefix);

        if (AFKHandler.instance().isAFK(player.getUniqueId())) {
            team.setSuffix(AFK_SUFFIX);
        }

        teamRegistry.addTeam(player.getUniqueId(), team);
        team.broadcastPacket(Team.Mode.TEAM_CREATED);
    }

    private static void updatePlayer(Player player) {
        TeamRegistryManager teamRegistry = TeamRegistryManager.instance();
        Team team = teamRegistry.getTeam(player.getUniqueId());
        if (team == null) return;


        if (AFKHandler.instance().isAFK(player.getUniqueId())) {
            team.setSuffix(AFK_SUFFIX);
        } else {
            team.setSuffix("");
        }
        team.broadcastPacket(Team.Mode.TEAM_UPDATED);
    }

    private static String getSortID(Group group) {
        if (group == null) return "999";
        CachedMetaData metaData = group.getCachedData().getMetaData();
        int sortID = metaData.getMetaValue("sortID", Integer::parseInt).orElse(999);
        return String.format("%03d", sortID);
    }

}
