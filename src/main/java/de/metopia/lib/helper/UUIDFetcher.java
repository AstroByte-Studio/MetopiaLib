package de.metopia.lib.helper;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.metopia.lib.data.constants.CacheConstants;
import de.metopia.lib.utils.RedisCache;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class UUIDFetcher {

    private static Gson gson = (new GsonBuilder()).registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();

    private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s?at=%d";
    private static final String NAME_URL = "https://api.mojang.com/user/profiles/%s/names";

    private static Cache<String, UUID> playerCache = CacheBuilder.newBuilder().maximumSize(1000L).expireAfterWrite(Duration.of(4L, ChronoUnit.HOURS)).build();
    private static Cache<UUID, String> nameCache = CacheBuilder.newBuilder().maximumSize(1000L).expireAfterWrite(Duration.of(4L, ChronoUnit.HOURS)).build();

    private static ExecutorService pool = Executors.newCachedThreadPool();

    private String name;
    private UUID id;

    public static UUID getUUID(String name) {
        UUID uuid = null;
        try {
            uuid = getUUIDAt(name, System.currentTimeMillis());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return uuid;
    }

    public static UUID getUUIDAt(String name, long timestamp) throws Exception {
        String finalName = name.toLowerCase();
        return playerCache.get(finalName, () -> {
            HttpURLConnection connection = (HttpURLConnection)(new URL(String.format("https://api.mojang.com/users/profiles/minecraft/%s?at=%d", new Object[] { finalName, Long.valueOf(timestamp / 1000L) }))).openConnection();
            connection.setReadTimeout(5000);
            UUIDFetcher data = gson.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), UUIDFetcher.class);
            return data.id;
        });
    }

    public static void getName(UUID uuid, Consumer<String> action) {
        pool.execute(() -> {
            try {
                action.accept(getName(uuid));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static String getName(UUID uuid) throws Exception {
        return nameCache.get(uuid, () -> {
            try {
                HttpURLConnection connection = (HttpURLConnection)(new URL(String.format("https://api.mojang.com/user/profiles/%s/names", new Object[] { UUIDTypeAdapter.fromUUID(uuid) }))).openConnection();
                connection.setReadTimeout(5000);
                UUIDFetcher[] nameHistory = gson.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), UUIDFetcher[].class);
                UUIDFetcher currentNameData = nameHistory[nameHistory.length - 1];
                return currentNameData.name;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }
}