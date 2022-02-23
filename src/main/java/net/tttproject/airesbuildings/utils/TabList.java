package net.tttproject.airesbuildings.utils;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.tttproject.airesbuildings.AiresBuildings;
import net.tttproject.airesbuildings.packets.WrapperPlayServerScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collections;

public class TabList {

    private static final String HEADER = "";
    private static final String FOOTER = "";


    public static void updateAll() {
        Bukkit.getOnlinePlayers().forEach(TabList::set);
    }

    public static void set(Player player) {
        player.setPlayerListHeaderFooter(HEADER, FOOTER);

        for (Player target : Bukkit.getOnlinePlayers()) {
            Group targetGroup = AiresBuildings.getUserGroup(target.getUniqueId());
            WrapperPlayServerScoreboardTeam team = new WrapperPlayServerScoreboardTeam();
            team.setPlayers(Collections.singletonList(target.getName()));
            team.setPackOptionData(0x02);

            if (targetGroup != null) {
                String teamName = getSortID(targetGroup) + target.getUniqueId().toString().replaceAll("-", "");
                if (teamName.length() > 16) teamName = teamName.substring(0, 16);
                team.setName(teamName);
                team.setPlayers(Collections.singletonList(target.getName()));

                CachedMetaData metaData = targetGroup.getCachedData().getMetaData();
                String prefix = metaData.getPrefix();
                String color = metaData.getMetaValue("color");

                if (prefix != null) {
                    team.setPrefix(WrappedChatComponent.fromText(prefix));
                }

                if (color != null) {
                    ChatColor chatColor;

                    if (color.startsWith("§") && color.length() >= 2) {
                        chatColor = ChatColor.getByChar(color.charAt(1));
                    } else {
                        chatColor = ChatColor.valueOf(color);
                    }

                    team.setColor(chatColor);
                }
            } else {
                String teamName = target.getUniqueId().toString().replaceAll("-", "");
                if (teamName.length() > 16) teamName = teamName.substring(0, 16);
                team.setName(teamName);
                team.setColor(ChatColor.GRAY);
                team.setPrefix(WrappedChatComponent.fromText("§7Gast §6| §7"));
            }


            team.setMode(WrapperPlayServerScoreboardTeam.Mode.TEAM_REMOVED);
            team.sendPacket(player);
            team.setMode(WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED);
            team.sendPacket(player);
        }

    }

    private static String getSortID(Group group) {
        if (group == null) return "999";
        CachedMetaData metaData = group.getCachedData().getMetaData();
        int sortID = metaData.getMetaValue("sortID", Integer::parseInt).orElse(999);
        return String.format("%03d", sortID);
    }

}
