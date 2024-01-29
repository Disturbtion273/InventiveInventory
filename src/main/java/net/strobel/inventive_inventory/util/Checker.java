package net.strobel.inventive_inventory.util;

public class Checker {
    public static <Type> boolean isIncluded(Type toCheck, Type[] comparison_values) {
        for (Type value: comparison_values) {
            if (toCheck.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
