package net.strobel.inventive_inventory.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JsonHandler {

//    JsonHandler json = new JsonHandler(filepath);
//    json.get(key).asList();
//    FileHandler(filepath).get(key).asList();
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

    public static List<Integer> jsonArrayToIntegerList(JsonArray array) {
        return IntStream.range(0, array.size())
                .mapToObj(i -> array.get(i).getAsInt())
                .collect(Collectors.toList());
    }

    public static String[] jsonArrayToStringArray(JsonArray array) {
        return IntStream.range(0, array.size())
                .mapToObj(array::get)
                .map(JsonElement::getAsString)
                .toArray(String[]::new);
    }

    public static List<String> jsonArrayToStringList(JsonArray array) {
        return IntStream.range(0, array.size())
                .mapToObj(array::get)
                .map(JsonElement::getAsString)
                .collect(Collectors.toList());
    }

}
