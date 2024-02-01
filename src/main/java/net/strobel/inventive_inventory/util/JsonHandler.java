package net.strobel.inventive_inventory.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.stream.IntStream;

public class JsonHandler {
    public static int[] jsonArrayToIntArray(JsonArray array) {
        return IntStream.range(0, array.size())
                .mapToObj(array::get)
                .mapToInt(JsonElement::getAsInt)
                .toArray();
    }

    public static Integer[] jsonArrayToIntegerArray(JsonArray array) {
        return IntStream.range(0, array.size())
                .mapToObj(i -> array.get(i).getAsInt())
                .toArray(Integer[]::new);
    }

}
