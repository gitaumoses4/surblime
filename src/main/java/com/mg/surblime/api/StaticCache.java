package com.mg.surblime.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.mg.surblime.util.ListParametrizedType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * Created by moses on 5/3/18.
 */

public abstract class StaticCache {

    private static final String TAG = StaticCache.class.getSimpleName();
    private static final String STATIC_CACHE_DIRECTORY = "surblime_static_cache";

    private final HashMap<Class, String> cache = new HashMap<>();
    private Context context;

    public abstract void inflateCache(HashMap<Class, String> cache);

    public StaticCache(){
        inflateCache(cache);
    }
    public StaticCache with(Context context) {
        this.context = context;
        return this;
    }

    private File getFile(String fileName) {
        File directory = new File(context.getCacheDir(), STATIC_CACHE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }
        return new File(context.getCacheDir(), STATIC_CACHE_DIRECTORY + File.separator + fileName);
    }

    private void saveToFile(String string, File file) {
        saveToFile(string, file, false);
    }

    private void appendToFile(String string, File file) {
        saveToFile(string, file, true);
    }

    private void saveToFile(String string, File file, boolean append) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            if (append) {
                fileWriter.append(string);
            } else {
                fileWriter.write(string);
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            Log.e(TAG, "saveToFile", e);
        }

    }

    private String readFromFile(File file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
        } catch (IOException e) {
            Log.e(TAG, "readFromFile", e);
        }
        return result.toString();
    }

    public <T> void saveList(List<T> t, Class<T> classOfT) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(t);
        try {
            saveToFile(new JSONArray(jsonString).toString(), getFile(cache.get(classOfT)));
        } catch (JSONException e) {
            Log.e(TAG, "saveList", e);
        }
    }

    public <T> void save(T t, Class<T> classOfT) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(t);
        try {
            saveToFile(new JSONObject(jsonString).toString(), getFile(cache.get(classOfT)));
        } catch (JSONException e) {
            Log.e(TAG, "saveList", e);
        }
    }

    public <T> T get(Class<T> classOfT) {
        File file = getFile(cache.get(classOfT));
        if (file.exists()) {
            Gson gson = new Gson();
            try {
                return gson.fromJson(new FileReader(file), classOfT);
            } catch (FileNotFoundException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public <T> List<T> getList(Class<T> classOfT) {
        File file = getFile(cache.get(classOfT));
        if (file.exists()) {
            Gson gson = new Gson();
            try {
                Type type = new ListParametrizedType(classOfT);
                return gson.fromJson(new FileReader(file), type);
            } catch (FileNotFoundException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public <T> boolean has(Class<T> classOfT) {
        return getFile(cache.get(classOfT)).exists();
    }

}
