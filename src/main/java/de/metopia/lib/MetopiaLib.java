package de.metopia.lib;

import de.metopia.lib.data.constants.CacheConstants;
import de.metopia.lib.helper.UUIDFetcher;
import de.metopia.lib.manager.PlayerManager;
import de.metopia.lib.utils.MongoDatabase;
import de.metopia.lib.utils.RedisCache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MetopiaLib {

    private static PlayerManager playerManager;

    public static void init() {
        MongoDatabase.init();
        RedisCache.init();

        playerManager = new PlayerManager();



    }

    public static PlayerManager getPlayerManager() {
        return playerManager;
    }




















    public static void main(String[] args) {
        init();





        String currentInput = "";
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));


        String playerName = "Planloserr";

        do {
            currentInput = "";
            try {
                currentInput = input.readLine();
            } catch (IOException ignored) {}

            switch (currentInput.toLowerCase()) {

                case "player get":
                    System.out.println(playerManager.getPlayerByName(playerName));
                    break;

                case "player set":
                    playerManager.createPlayerEntry(playerName, UUIDFetcher.getUUID(playerName).toString());
                    break;

                case "player cache":
                    System.out.println(RedisCache.isCached(CacheConstants.PLAYER, UUIDFetcher.getUUID(playerName).toString()));
                    break;

            }

        } while(!currentInput.equalsIgnoreCase("stop"));
    }

}