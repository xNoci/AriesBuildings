package net.tttproject.airesbuildings.events.core;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;

public class CancellableEventCore extends EventCore implements Cancellable {

    @Getter @Setter private boolean cancelled;

    public CancellableEventCore(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
