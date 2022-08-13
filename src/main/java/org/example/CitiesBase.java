package org.example;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CitiesBase {
    private static CitiesBase INSTANCE;
    private static List<String> cities;

    public static CitiesBase getInstance() {
        if (INSTANCE == null) {
            cities = fill();
            INSTANCE = new CitiesBase();
        }
        return INSTANCE;
    }

    private static List<String> fill() {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader("russian-cities.json");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<String> cities = new LinkedList<>();
        JsonElement jsonElement = JsonParser.parseReader(fileReader);
        for (JsonElement element : jsonElement.getAsJsonArray()) {
            JsonObject asJsonObject = element.getAsJsonObject();
            String name = asJsonObject.get("name").getAsString();
            cities.add(name);
        }
        return cities;
    }

    public static boolean contains(String city) {
        if (INSTANCE == null) {
            throw new RuntimeException("Слишком рано");
        }
        return cities.contains(city);
    }
    public static Iterator<String> iterator() {
        return cities.iterator();
    }
}
