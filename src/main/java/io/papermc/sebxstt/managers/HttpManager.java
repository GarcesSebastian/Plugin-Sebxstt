package io.papermc.sebxstt.managers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.papermc.sebxstt.instances.http.Endpoint;
import io.papermc.sebxstt.instances.http.FetchProfile;
import io.papermc.sebxstt.instances.http.FetchTextures;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpManager {
    private static final String EndPointProfile = "https://api.mojang.com/users/profiles/minecraft/";
    private static final String EndPointTextures = "https://sessionserver.mojang.com/session/minecraft/profile/";

    public static FetchProfile getProfile(String profileName) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(new Endpoint(EndPointProfile + profileName).resolve()).openConnection();
        connection.setRequestMethod("GET");
        if (connection.getResponseCode() != 200) {
            throw new Exception(connection.getResponseMessage());
        }

        JsonObject data = JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonObject();

        String name = data.get("name").getAsString();
        String id = data.get("id").getAsString();

        return new FetchProfile(name, id);
    }

    public static FetchTextures getTextures(String profielId) throws Exception {
        Endpoint endpoint = new Endpoint(EndPointTextures + profielId);
        endpoint.addParam("unsigned", "false");
        HttpURLConnection connection = (HttpURLConnection) new URL(endpoint.resolve()).openConnection();
        connection.setRequestMethod("GET");
        if (connection.getResponseCode() != 200) {
            throw new Exception(connection.getResponseMessage());
        }

        JsonObject data = JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
        String texture = null;
        String signature = null;

        for (var el : data.getAsJsonArray("properties")) {
            JsonObject prop = el.getAsJsonObject();
            if ("textures".equals(prop.get("name").getAsString())) {
                texture = prop.get("value").getAsString();
                signature = prop.get("signature").getAsString();
                break;
            }
        }

        if (texture == null || signature == null) throw new Exception("Not Found texture or signature");
        return new FetchTextures(texture, signature);
    }
}