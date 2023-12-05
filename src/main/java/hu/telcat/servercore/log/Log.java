package hu.telcat.servercore.log;

import org.bukkit.Bukkit;

public class Log {

    public static void info(String message){
        Bukkit.getLogger().info("[ServerCore] " + message);
    }

    public static void warning(String message) {
        Bukkit.getLogger().info("[ServerCore] §e" + message);
    }

    public static void error(String message) {
        Bukkit.getLogger().info("[ServerCore] §c" + message);
    }
}
