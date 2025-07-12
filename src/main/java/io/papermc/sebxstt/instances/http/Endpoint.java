package io.papermc.sebxstt.instances.http;

import java.util.HashMap;

public class Endpoint {
    public String url;
    public HashMap<String, String> params = new HashMap<>();

    public Endpoint(String url) {
        this.url = url;
    }

    public void addParam(String key, String value) {
        this.params.put(key, value);
    }

    public String resolve() {
        StringBuilder result = new StringBuilder(url);

        for (String key : params.keySet()) {
            result.append("?").append(key).append("=").append(params.get(key));
        }

        return result.toString();
    }
}
