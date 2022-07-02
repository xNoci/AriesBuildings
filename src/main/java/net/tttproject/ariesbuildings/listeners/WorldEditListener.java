package net.tttproject.ariesbuildings.listeners;

import com.fastasyncworldedit.core.event.extent.PasteEvent;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.util.eventbus.Subscribe;

public class WorldEditListener {

    @Subscribe
    public void handlePaste(PasteEvent event) {
        //TODO BlockHistory
    }

    @Subscribe
    public void handlePaste(EditSessionEvent event) {
        //TODO BlockHistory
    }

}
