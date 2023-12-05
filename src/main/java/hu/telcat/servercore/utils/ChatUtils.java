package hu.telcat.servercore.utils;

import hu.telcat.servercore.entity.list.UserList;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

public class ChatUtils {

    private static final String CHAT_SEPARATOR = "»";
    public static MessageFormat getChatFormat(Player player) {
        String format = "{DISPLAYNAME} §8" + CHAT_SEPARATOR + " {MESSAGE}";

        String playerPrefix = "";
        if(player != null ) playerPrefix = UserList.getUser(player.getUniqueId()).getPrefix().replaceAll("&", "§");
        String chatColor = "§f";

        format = format.replace("{DISPLAYNAME}", playerPrefix + "%1$s");
        format = format.replace("{MESSAGE}", chatColor + "%2$s");
        format = format.replaceAll("\\{(\\D*?)\\}", "\\[$1\\]");

        return new MessageFormat(format);
    }

    public static MessageFormat getChatFormat() {
        return getChatFormat(null);
    }
}
