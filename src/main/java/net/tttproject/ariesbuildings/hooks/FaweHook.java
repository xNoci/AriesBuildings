package net.tttproject.ariesbuildings.hooks;

import com.fastasyncworldedit.core.Fawe;
import net.tttproject.ariesbuildings.AriesBuildings;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class FaweHook {

    private static boolean checktPlugin = false;
    private static boolean found;

    public static boolean isEnabled() {
        if (!checktPlugin) {
            checktPlugin = true;
            Plugin plugin = Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit");
            found = plugin != null;

            AriesBuildings.getInstance().getLogger().info("%s is %s.".formatted(FaweHook.class.getSimpleName(), found ? "enabled" : "disabled"));
        }
        return found;
    }

    public static void registerEvent(Object event) {
        if (!isEnabled()) return;
        Fawe.instance().getWorldEdit().getEventBus().register(event);
    }


}
