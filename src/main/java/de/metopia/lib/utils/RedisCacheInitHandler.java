package de.metopia.lib.utils;

import redis.clients.jedis.JedisPool;

public interface RedisCacheInitHandler {

    void handleCacheInit(JedisPool jedisPool);

}
