package net.strobel.inventive_inventory.config;

public enum Mode implements Comparable<Mode> {
    STANDARD("Standard"),
    INVERTED("Inverted"),
    FAST_LOAD("Fast Load"),
    NAME("Name"),
    ITEM_TYPE("Item Type");

    private final String name;

    Mode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
