package net.tttproject.ariesbuildings.hooks;

import com.fastasyncworldedit.core.Fawe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FaweHook {


    private static final Logger LOGGER = LoggerFactory.getLogger(FaweHook.class);

    private static final boolean foundFawe;

    static {
        boolean found = false;
        try {
            Class.forName("com.fastasyncworldedit.core.Fawe");
            found = true;
        } catch (ClassNotFoundException e) {
            LOGGER.info("Could not find Fawe.");
        }

        foundFawe = found;
    }

    public static boolean isEnabled() {
        return foundFawe;
    }

    public static void registerEvent(Object event) {
        Fawe.instance().getWorldEdit().getEventBus().register(event);
    }


}
