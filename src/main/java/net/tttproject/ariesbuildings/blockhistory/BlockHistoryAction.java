package net.tttproject.ariesbuildings.blockhistory;

public enum BlockHistoryAction {

    PLACE,
    BREAK;

    public static BlockHistoryAction byName(String name) {
        for (BlockHistoryAction value : values()) {
            if (value.name().equalsIgnoreCase(name)) return value;
        }
        return null;
    }

}
