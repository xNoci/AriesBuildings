package net.tttproject.ariesbuildings.listeners;

import lombok.extern.slf4j.Slf4j;
import net.luckperms.api.event.node.NodeMutateEvent;
import net.tttproject.ariesbuildings.scoreboardteam.TeamRegistryManager;
import net.tttproject.ariesbuildings.utils.TabList;

@Slf4j
public class NodeMutateListener {

    public static void handleNodeMutate(NodeMutateEvent event) {
        TeamRegistryManager.instance().clearRegistry();
        TabList.updateAll();
    }

}
