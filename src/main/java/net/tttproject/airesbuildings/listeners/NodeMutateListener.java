package net.tttproject.airesbuildings.listeners;

import lombok.extern.slf4j.Slf4j;
import net.luckperms.api.event.node.NodeMutateEvent;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.tttproject.airesbuildings.scoreboardteam.TeamRegistryManager;
import net.tttproject.airesbuildings.utils.TabList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Slf4j
public class NodeMutateListener {

    public static void handleNodeMutate(NodeMutateEvent event) {
        if (event.getTarget() instanceof User user) {
            TeamRegistryManager.instance().removeTeam(user.getUniqueId());

        }

        if (event.getTarget() instanceof Group group) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                TeamRegistryManager.instance().removeTeam(player.getUniqueId());
            }
            TabList.updateAll();
        }

        TabList.updateAll();
    }

}
