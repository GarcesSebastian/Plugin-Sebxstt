package io.papermc.sebxstt.serialize;

import io.papermc.sebxstt.instances.Main;
import io.papermc.sebxstt.managers.FileManagerJSON;
import io.papermc.sebxstt.providers.ConfigurationProvider;
import io.papermc.sebxstt.providers.PluginProvider;
import io.papermc.sebxstt.serialize.data.MainData;
import io.papermc.sebxstt.serialize.data.PlayerConfigData;
import io.papermc.sebxstt.serialize.data.PlayerGroupData;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;


public class DataStore {
    private static final Map<Class<?>, String> TABLE_FIELD_NAMES = Map.of(
            PlayerConfigData.class, "playersConfigData",
            PlayerGroupData.class, "playersGroupData"
    );
    public static DataStore instance;

    private DataStore() {}
    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }

        return instance;
    }

    private File verifyFile(File folder, String fileName) {
        if (!folder.exists()) folder.mkdirs();

        File file = new File(folder, fileName);

        if (!file.exists()) {
            System.out.println("[DataStore] El archivo no existe. Creando uno nuevo...");
            FileManagerJSON.create(file);
            System.out.println("[DataStore] Archivo creado " +  file.getName());
        }

        return file;
    }

    public Main load() {
        Main resolved = null;
        File file = verifyFile(PluginProvider.get().getDataFolder(), ConfigurationProvider.fileDataSaved);
        MainData data = FileManagerJSON.read(file, MainData.class);

        if (data == null) return resolved;

        resolved = data.resolve();
        System.out.println("[DataStore] se ha creado la instancia " + data.getClass().getName());
        return resolved;
    }

    public <T> T get(String key, String value, Class<T> clazz) {
        File file = verifyFile(PluginProvider.get().getDataFolder(), ConfigurationProvider.fileDataSaved);
        MainData data = FileManagerJSON.read(file, MainData.class);
        if (data == null) {
            System.out.println("[DataStore] data is NULL");
            return null;
        }

        String table = TABLE_FIELD_NAMES.get(clazz);
        if (table == null) {
            throw new RuntimeException("[DataStore] No se encontró una tabla asociada a " + clazz.getSimpleName());
        }

        ArrayList<T> rowData = null;
        try {
            var tableData = data.getClass().getDeclaredField(table);
            tableData.setAccessible(true);
            rowData = (ArrayList<T>) tableData.get(data);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("[DataStore] No existe la tabla " + table);
            throw new RuntimeException(e);
        }

        if (rowData == null) {
            System.out.println("[DataStore] No existe la tabla " + table);
            return null;
        }

        T result = rowData.stream().filter(cell -> {
            try {
                var field = cell.getClass().getDeclaredField(key);
                field.setAccessible(true);
                return field.get(cell).toString().equalsIgnoreCase(value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                System.out.println("[DataStore] No existe la columna " + key);
                throw new RuntimeException(e);
            }
        }).findFirst().orElse(null);

        if (result == null) {
            System.out.println("[DataStore] El campo " + key + ":" + value + " de " + table + " no existe.");
        }

        return result;
    }

    public <T> void delete(String key, String value, Class<T> clazz) {
        File file = verifyFile(PluginProvider.get().getDataFolder(), ConfigurationProvider.fileDataSaved);
        MainData data = FileManagerJSON.read(file, MainData.class);
        if (data == null) {
            System.out.println("[DataStore] data is NULL");
            return;
        }

        String table = TABLE_FIELD_NAMES.get(clazz);
        if (table == null) {
            throw new RuntimeException("[DataStore] No se encontró una tabla asociada a " + clazz.getSimpleName());
        }

        ArrayList<T> rowData = null;
        try {
            var tableData = data.getClass().getDeclaredField(table);
            tableData.setAccessible(true);
            rowData = (ArrayList<T>) tableData.get(data);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("[DataStore] No existe la tabla " + table);
            throw new RuntimeException(e);
        }

        if (rowData == null) {
            System.out.println("[DataStore] No existe la tabla " + table);
            return;
        }

        ArrayList<T> updated = rowData.stream()
                .filter(upd -> {
                    try {
                        var field = upd.getClass().getDeclaredField(key);
                        field.setAccessible(true);
                        return !field.get(upd).toString().equalsIgnoreCase(value);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        System.out.println("[DataStore] No existe la columna " + key);
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));

        try {
            var field = data.getClass().getDeclaredField(table);
            field.setAccessible(true);
            field.set(data, updated);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("[DataStore] No existe la tabla " + table);
            throw new RuntimeException(e);
        }

        FileManagerJSON.edit(file, data);
        System.out.println("[DataStore] se ha borrado la instancia " + key + ":" + value + " de la tabla " + table);
    }

    public <T> void create(T instance, Class<T> clazz) {
        File file = verifyFile(PluginProvider.get().getDataFolder(), ConfigurationProvider.fileDataSaved);
        MainData data = FileManagerJSON.read(file, MainData.class);
        if (data == null) {
            System.out.println("[DataStore] data is NULL");
            return;
        }

        String table = TABLE_FIELD_NAMES.get(clazz);
        if (table == null) {
            throw new RuntimeException("[DataStore] No se encontró una tabla asociada a " + clazz.getSimpleName());
        }

        ArrayList<T> rowData = null;
        try {
            var tableData = data.getClass().getDeclaredField(table);
            tableData.setAccessible(true);
            rowData = (ArrayList<T>) tableData.get(data);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("[DataStore] No existe la tabla " + table);
            throw new RuntimeException(e);
        }

        if (rowData == null) {
            System.out.println("[DataStore] No existe la tabla " + table);
            return;
        }

        rowData.add(instance);

        try {
            var field = data.getClass().getDeclaredField(table);
            field.setAccessible(true);
            field.set(data, rowData);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("[DataStore] No existe la tabla " + table);
            throw new RuntimeException(e);
        }

        FileManagerJSON.edit(file, data);
        System.out.println("[DataStore] se ha creado la instancia " + instance.getClass().getName() + " de la tabla " + table);
    }

    public <T> void edit(String key, String value, T instance, Class<T> clazz) {
        File file = verifyFile(PluginProvider.get().getDataFolder(), ConfigurationProvider.fileDataSaved);
        MainData data = FileManagerJSON.read(file, MainData.class);
        if (data == null) return;

        String table = TABLE_FIELD_NAMES.get(clazz);
        if (table == null) {
            throw new RuntimeException("[DataStore] No se encontró una tabla asociada a " + clazz.getSimpleName());
        }

        ArrayList<T> rowData = null;
        try {
            var tableData = data.getClass().getDeclaredField(table);
            tableData.setAccessible(true);
            rowData = (ArrayList<T>) tableData.get(data);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("[DataStore] No existe la tabla " + table);
            throw new RuntimeException(e);
        }

        if (rowData == null) {
            System.out.println("[DataStore] No existe la tabla " + table);
            return;
        }

        ArrayList<T> updated = rowData.stream()
                .filter(upd -> {
                    try {
                        var field = upd.getClass().getDeclaredField(key);
                        field.setAccessible(true);
                        return !field.get(upd).toString().equalsIgnoreCase(value);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        System.out.println("[DataStore] No existe la columna " + key);
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));

        updated.add(instance);

        try {
            var field = data.getClass().getDeclaredField(table);
            field.setAccessible(true);
            field.set(data, updated);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("[DataStore] No existe la tabla " + table);
            throw new RuntimeException(e);
        }

        FileManagerJSON.edit(file, data);
        System.out.println("[DataStore] se ha editado la instancia " + key + ":" + value + " de la tabla " + table);
    }
}