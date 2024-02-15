package de.metopia.lib.data.constants;

public final class BukkitConstants {

    public static final String PREFIX = "";





    public static String convertToChatColor(String hex) {
        StringBuilder sb = new StringBuilder("§x");
        for(char c : hex.substring(1).toCharArray()) {
            sb.append("§").append(c);
        }
        return sb.toString();
    }


}
