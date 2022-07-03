package net.tttproject.ariesbuildings.blockhistory;

import lombok.Getter;

public enum BlockHistoryAction {

    PLACE("Placed"),
    BREAK("Destroyed"),
    WORLD_EDIT_PASTE("Pasted (WE)", "Block was placed using WorldEdit '//paste' or '//place'."),
    WORLD_EDIT_SET("Set (WE)", "Block was changed using WorldEdit.");

    @Getter private final String actionName;
    @Getter private final String detailedInfo;

    BlockHistoryAction(String actionName) {
        this(actionName, "");
    }

    BlockHistoryAction(String actionName, String detailedInfo) {
        this.actionName = actionName;
        this.detailedInfo = detailedInfo;
    }

    public static BlockHistoryAction byName(String name) {
        for (BlockHistoryAction value : values()) {
            if (value.name().equalsIgnoreCase(name)) return value;
        }
        return null;
    }

}
