package hu.telcat.servercore.utils;

import hu.telcat.servercore.ServerCore;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServerConstants {

    //Server constant
    private static String serverName;
    private static String serverGroup;
    private static String serverType;
    private static NamespacedKey hashCommand;

    //Server settings
    public static boolean clearInventoryOnJoin = false;
    public static boolean disableWeatherChange = false;
    public static boolean disableFoodLevelChange = false;
    public static boolean disableCropTrampling = false;
    public static boolean disableLeafDecay = false;
    public static int loadedSettings = 0;

    /**
     * Loading constants in
     */
    public static void loadServerData() {
        serverName = ServerCore.getInstance().getConfig().getString("server.name");
        serverGroup = ServerCore.getInstance().getConfig().getString("server.group");
        serverType = ServerCore.getInstance().getConfig().getString("server.type");
        hashCommand = new NamespacedKey(ServerCore.getInstance(), "hashCommand");
    }

    /**
     * Take care of the loaded settings
     */
    public static void loadServerSettings(ResultSet resultSet) throws SQLException {
        loadedSettings++;
        switch(resultSet.getString("rule")){
            case "clearInventoryOnJoin":
                clearInventoryOnJoin = resultSet.getString("value").equals("true");
                break;
            case "disableWeatherChange":
                disableWeatherChange = resultSet.getString("value").equals("true");
                break;
            case "disableFoodLevelChange":
                disableFoodLevelChange = resultSet.getString("value").equals("true");
                break;
            case "disableCropTrampling":
                disableCropTrampling = resultSet.getString("value").equals("true");
                break;
            case "disableLeafDecay":
                disableLeafDecay = resultSet.getString("value").equals("true");
                break;
        }
    }

    public static String getServerName() {
        return serverName;
    }

    public static String getServerGroup() {
        return serverGroup;
    }

    public static String getServerType() { return serverType; }

    public static NamespacedKey getHashCommandKey() { return hashCommand; }
}
