package io.papermc.sebxstt.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.papermc.sebxstt.adapters.LocalDateTimeAdapter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

public class FileManagerJSON {
    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public static void create(File file) {
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();

            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write("{}");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T read(File file, Class<T> clazz) {
        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> void edit(File file, T data) {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(data, writer);
            writer.flush();
        } catch (IOException e) {
            System.out.println("[FileManagerJSON] Error al guardar en el archivo:");
            e.printStackTrace();
        }
    }
}