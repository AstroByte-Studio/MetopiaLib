package de.metopia.lib.utils;

import com.google.gson.Gson;
import de.metopia.lib.data.Cachable;
import de.metopia.lib.data.LibConfig;
import dev.morphia.query.experimental.filters.Filters;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;

public class RedisCache {

    private static List<RedisCacheInitHandler> initHandlers = new ArrayList<>();
    private static JedisPool pool;
    private static final Gson gson = new Gson();

    public static void addInitHandler(RedisCacheInitHandler handler) {
        initHandlers.add(handler);
    }

    public static void init() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMinIdle(5);
        config.setMaxIdle(30);

        pool = new JedisPool(config, LibConfig.getRedisURI());

        initHandlers.forEach(handler -> handler.handleCacheInit(pool));



        Jedis jedis = pool.getResource();
        System.out.println("[MetopiaLib] Redis connected! DatabaseIndex: " + jedis.getDB());
        jedis.close();

    }

    public static boolean isCached(String cachePrefix, String cacheId) {
        Jedis jedis = pool.getResource();
        boolean cached = jedis.exists(cachePrefix+"-"+cacheId);
        jedis.close();
        return cached;
    }

    public static <T> T getValue(String cachePrefix, String cacheId, Class typeClass) {
        Jedis jedis = pool.getResource();
        T obj = (T) gson.fromJson(jedis.get(cachePrefix+"-"+cacheId), typeClass);
        jedis.close();
        return obj;
    }

    public static <T> T getValue(String cachePrefix, String cacheId, Class typeClass, long expire) {
        Jedis jedis = pool.getResource();
        T obj = (T) gson.fromJson(jedis.get(cachePrefix+"-"+cacheId), typeClass);
        jedis.expire(cachePrefix+"-"+cacheId, expire);
        jedis.close();
        return obj;
    }

    public static void setValue(Cachable cachable, Class typeClass) {
        Jedis jedis = pool.getResource();
        jedis.set(cachable.getCachePrefix()+"-"+cachable.getCacheId(), gson.toJsonTree(cachable, typeClass).toString());
        jedis.close();
    }

    public static void setValue(Cachable cachable, Class typeClass, long expire) {
        Jedis jedis = pool.getResource();
        jedis.set(cachable.getCachePrefix()+"-"+cachable.getCacheId(), gson.toJsonTree(cachable, typeClass).toString());
        jedis.expire(cachable.getCachePrefix()+"-"+cachable.getCacheId(), expire);
        jedis.close();
    }

    public static void invalidate(Cachable cachable) {
        Jedis jedis = pool.getResource();
        jedis.del(cachable.getCachePrefix()+"-"+cachable.getCacheId());
        jedis.close();
    }

    public static void invalidate(String cachePrefix, String cacheId) {
        Jedis jedis = pool.getResource();
        jedis.del(cachePrefix+"-"+cacheId);
        jedis.close();
    }

    public static void flushAll() {
        Jedis jedis = pool.getResource();
        jedis.flushAll();
        jedis.close();
    }

    public static <T> T getCachedIfPossible(String cachePrefix, String cacheValue, Class<T> typeClass, String databaseField, String databaseValue) {
        if(!Cachable.class.isAssignableFrom(typeClass)) {
            System.err.println("[MetopiaLib] " + typeClass.getSimpleName() + " is not cacheable!");
            return null;
        }

        T object = null;

        if (RedisCache.isCached(cachePrefix, cacheValue)) {
            object = RedisCache.getValue(cachePrefix, cacheValue, typeClass);
        } else {
            object = MongoDatabase.getDatastore().find(typeClass).filter(Filters.eq(databaseField, databaseValue)).first();
            if (object != null) RedisCache.setValue((Cachable) object, typeClass);
        }

        return object;
    }




}
