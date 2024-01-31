package net.strobel.inventive_inventory.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

public class Checker {
    public static <Type> boolean isIncluded(Type toCheck, Type[] comparison_values) {
        for (Type value: comparison_values) {
            if (toCheck.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsInJson(int anElement, JsonArray array) {
        return array.contains(new JsonPrimitive(anElement));
    }
}
