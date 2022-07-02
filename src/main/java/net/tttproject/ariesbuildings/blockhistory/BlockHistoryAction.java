package net.tttproject.ariesbuildings.blockhistory;

import lombok.Getter;

public enum BlockHistoryAction {

    PLACE("Placed"),
    BREAK("Destroyed"),
    WORLD_EDIT_SET("Set (WE)");

    @Getter private final String info;

    BlockHistoryAction(String info) {
        this.info = info;
    }

    public static BlockHistoryAction byName(String name) {
        for (BlockHistoryAction value : values()) {
            if (value.name().equalsIgnoreCase(name)) return value;
        }
        return null;
    }

}
