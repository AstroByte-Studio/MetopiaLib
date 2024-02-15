package de.metopia.lib.data.constants;

import net.kyori.adventure.text.format.TextColor;

public final class BungeeConstants {

    public static final String PREFIX = "";






    public static TextColor convertToChatColor(String hex) {
        return TextColor.fromHexString(hex);
    }

    public static TextColor convertToChatColor(int r, int g, int b) {
        return TextColor.color(r,g,b);
    }


}
