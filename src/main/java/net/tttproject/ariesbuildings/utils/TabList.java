package net.tttproject.ariesbuildings.utils;

import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.tttproject.ariesbuildings.hooks.LuckPermsHook;
import net.tttproject.ariesbuildings.scoreboardteam.PlayerTeamRegistry;
import net.tttproject.ariesbuildings.scoreboardteam.Team;
import net.tttproject.ariesbuildings.scoreboardteam.TeamRegistryManager;
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
            setTeams(player);
        }
    }

    private static void setTeams(Player player) {
        TeamRegistryManager teamRegistryManager = TeamRegistryManager.instance();
        PlayerTeamRegistry teamRegistry = teamRegistryManager.getRegistry(player);

        for (Player target : Bukkit.getOnlinePlayers()) {
            Team targetTeam = teamRegistry.getTeam(target.getUniqueId());
            if (targetTeam != null) {
                updateTeam(targetTeam, target);
                continue;
            }

            targetTeam = createTeam(target);
            teamRegistry.addTeam(target.getUniqueId(), targetTeam);
            teamRegistry.sendPacket(target.getUniqueId(), Team.Mode.TEAM_CREATED);
        }

        teamRegistry.sendPackets(Team.Mode.TEAM_UPDATED);

    }

    private static Team createTeam(Player target) {
        String teamName = 999 + target.getUniqueId().toString().replaceAll("-", "");
        String prefix = "§7Gast §8| §7";
        ChatColor color = ChatColor.GRAY;


        if (LuckPermsHook.isEnabled()) {
            Group targetGroup = LuckPermsHook.getUserGroup(target.getUniqueId());
            if (targetGroup != null) {
                teamName = getSortID(targetGroup) + target.getUniqueId().toString().replaceAll("-", "");

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
        }

        Team team = new Team(teamName);
        team.getPlayers().add(target);
        team.setCanSeeFriendlyInvisibles(true);
        team.setCollisionRule(Team.CollisionRule.NEVER);
        team.setColor(color);
        team.setPrefix(prefix);

        if (AFKHandler.instance().isAFK(target.getUniqueId())) {
            team.setSuffix(AFK_SUFFIX);
        }

        return team;
    }

    private static void updateTeam(Team team, Player player) {
        if (LuckPermsHook.isEnabled()) {
            Group targetGroup = LuckPermsHook.getUserGroup(player.getUniqueId());
            if (targetGroup != null) {
                CachedMetaData metaData = targetGroup.getCachedData().getMetaData();
                String metaPrefix = metaData.getPrefix();
                String metaColor = metaData.getMetaValue("color");

                if (metaPrefix != null) {
                    team.setPrefix(metaPrefix);
                }

                if (metaColor != null) {
                    if (metaColor.startsWith("§") && metaColor.length() >= 2) {
                        team.setColor(ChatColor.getByChar(metaColor.charAt(1)));
                    } else {
                        team.setColor(ChatColor.valueOf(metaColor));
                    }
                }
            }
        }

        if (AFKHandler.instance().isAFK(player.getUniqueId())) {
            team.setSuffix(AFK_SUFFIX);
        } else {
            team.setSuffix("");
        }
    }

    private static String getSortID(Group group) {
        if (group == null) return "999";
        CachedMetaData metaData = group.getCachedData().getMetaData();
        int sortID = metaData.getMetaValue("sortID", Integer::parseInt).orElse(999);
        return String.format("%03d", sortID);
    }

}
