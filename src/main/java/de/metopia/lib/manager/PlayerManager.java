package de.metopia.lib.manager;

import de.metopia.lib.data.constants.CacheConstants;
import de.metopia.lib.data.models.Player;
import de.metopia.lib.data.models.player.PlayerInfo;
import de.metopia.lib.helper.UUIDFetcher;
import de.metopia.lib.utils.MongoDatabase;
import de.metopia.lib.utils.RedisCache;

import java.util.UUID;

public class PlayerManager {

    public Player getPlayer(String uuid) {
        Player player = RedisCache.getCachedIfPossible(CacheConstants.PLAYER, uuid, Player.class, "playerInfo.uuid", uuid);

        if (player == null) return null;

        return player;
    }

    public Player getPlayerByName(String name) {
        UUID uuid = UUIDFetcher.getUUID(name);

        if (uuid == null) return null;

        return getPlayer(uuid.toString());
    }

    public void createPlayerEntry(String name, String uuid) {
        Player player = new Player(new PlayerInfo(name, uuid));


        MongoDatabase.getDatastore().save(player);
        RedisCache.setValue(player, Player.class);

    }


}
