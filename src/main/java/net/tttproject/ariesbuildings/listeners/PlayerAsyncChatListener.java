package net.tttproject.ariesbuildings.listeners;

import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.md_5.bungee.api.ChatColor;
import net.tttproject.ariesbuildings.hooks.LuckPermsHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerAsyncChatListener implements Listener {

    private static final String CHAT_FORMAT = "%prefix%%name%§8: §f%text%";

    @EventHandler
    public void handleAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        String message = event.getMessage();
        message = message.replace("%", "%%");
        message = message.replace("\\", "\\\\");
        if (player.hasPermission("chatcolor")) {
            message = message.replace("&", "§");
        }

        message = formatMessage(player, message);

        event.setFormat(message);
    }

    private String formatMessage(Player player, String message) {
        String color = ChatColor.GRAY.toString();
        String displayName = "Default";

        if (LuckPermsHook.isEnabled()) {
            Group group = LuckPermsHook.getUserGroup(player.getUniqueId());

            if (group != null) {
                CachedMetaData metaData = group.getCachedData().getMetaData();
                color = metaData.getMetaValue("color");
                displayName = group.getDisplayName();
            }
        }

        String format = CHAT_FORMAT;
        format = format.replaceAll("%prefix%", color + displayName + " §8| " + color);
        format = format.replaceAll("%name%", player.getName());
        format = format.replaceAll("%text%", message);
        return format;
    }


}
