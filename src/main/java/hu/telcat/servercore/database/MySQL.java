package hu.telcat.servercore.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hu.telcat.servercore.log.Log;
import hu.telcat.servercore.utils.ServerConstants;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class MySQL {

    private final String host;
    private final int port;
    private final String user;
    private final String password;
    private final String database;

    private HikariDataSource dataSource;

    private boolean success = false;

    /**
     * MySQL Constructor
     */
    public MySQL() {
        File mySQLFile = new File("plugins//ServerCore//mysql.yml");
        if(!mySQLFile.exists()){
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("mysql.yml");
            try {
                Files.copy(inputStream, Path.of(mySQLFile.toURI()));
            } catch (IOException e) {
                Log.error("Couldn't save mysql.yml file to the path!");
                e.printStackTrace();
            }
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(mySQLFile);
        if(!config.getBoolean("enabled")){
            Log.error("MySQL is not enabled! It is mandatory to our system to work properly!");
            //To make Java happy, but if we get into this server will shut down
            host = ""; user = ""; password = ""; database = ""; port = 0;
            Bukkit.getServer().shutdown();
            return;
        }
        this.host = config.getString("host");
        this.port = config.getInt("port");
        this.user = config.getString("user");
        this.password = config.getString("password");
        this.database = config.getString("database");

        initialize();
    }

    /**
     * Creating the HikariCP Datasource
     */
    private void initialize() {
        HikariConfig config = new HikariConfig();
        Log.info("Trying to connect to the MySQL database!");
        config.setJdbcUrl("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true");
        config.setUsername(this.user);
        if(!Objects.equals(this.password, "") || !Objects.equals(this.password, " ")){
            config.setPassword(this.password);
        }
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        this.dataSource = new HikariDataSource(config);
        success = true;
    }

    /**
     * Returns the dataSource connection
     * @return Connection
     */
    public Connection getConnection() {
        try {
            return this.dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns if initialize is success
     * @return boolean
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Returns the script for the location table
     * @return String
     */
    public String getLocationsCreateScript() {
        return "CREATE TABLE IF NOT EXISTS `locations` (" +
                "  `id` INT NOT NULL AUTO_INCREMENT," +
                "  `server` VARCHAR(128) NOT NULL," +
                "  `name` VARCHAR(128) NOT NULL," +
                "  `world` VARCHAR(128) NOT NULL DEFAULT 'world'," +
                "  `x` DOUBLE NOT NULL," +
                "  `y` DOUBLE NOT NULL," +
                "  `z` DOUBLE NOT NULL," +
                "  `pitch` FLOAT NOT NULL," +
                "  `yaw` FLOAT NOT NULL," +
                "  PRIMARY KEY (`id`));";
    }

    /**
     * Returns the script to get the locations by group
     * @return String
     */
    public String getLocationGetByGroupScript() {
        return "SELECT * FROM locations WHERE server = '" + ServerConstants.getServerGroup() + "';";
    }

    /**
     * Returns the script to get the locations by server name
     * @return String
     */
    public String getLocationGetByNameScript() {
        return "SELECT * FROM locations WHERE server = '" + ServerConstants.getServerName() + "';";
    }

    /**
     * Returns the script to create the settings table
     * @return String
     */
    public String getSettingsCreateScript() {
        return "CREATE TABLE IF NOT EXISTS `settings` (" +
                "  `id` INT NOT NULL AUTO_INCREMENT," +
                "  `server` VARCHAR(128) NOT NULL," +
                "  `rule` VARCHAR(255) NOT NULL," +
                "  `value` VARCHAR(128) NOT NULL," +
                "  PRIMARY KEY (`id`));";
    }

    /**
     * Returns the script to get the settings by group
     * @return String
     */
    public String getSettingsGetByGroupScript() {
        return "SELECT * FROM settings WHERE server = '" + ServerConstants.getServerGroup() + "';";
    }

    /**
     * Returns the script to get the settings by server name
     * @return String
     */
    public String getSettingsGetByNameScript() {
        return "SELECT * FROM settings WHERE server = '" + ServerConstants.getServerName() + "';";
    }

    /**
     * Returns the script to create the items table
     * @return String
     */
    public String getItemsCreateScript() {
        return "CREATE TABLE IF NOT EXISTS `items` (" +
                "  `id` INT NOT NULL AUTO_INCREMENT," +
                "  `name` VARCHAR(128) NOT NULL," +
                "  `material` VARCHAR(128) NOT NULL," +
                "  `amount` INT NOT NULL," +
                "  `displayName` VARCHAR(128) NULL," +
                "  `attributes` VARCHAR(1024) NULL," +
                "  `interact` VARCHAR(128) NULL," +
                "  `command` VARCHAR(512) NULL," +
                "  PRIMARY KEY (`id`));";
    }

    /**
     * Returns the script to get the items by group
     * @return String
     */
    public String getItemsGetScript() {
        return "SELECT * FROM items;";
    }

    /**
     * Returns the script to create the inventories table
     * @return String
     */
    public String getInventoriesCreateScript() {
        return "CREATE TABLE IF NOT EXISTS `inventories` (" +
                "  `id` INT NOT NULL AUTO_INCREMENT," +
                "  `server` VARCHAR(128) NOT NULL," +
                "  `name` VARCHAR(128) NOT NULL," +
                "  `displayName` VARCHAR(128) NOT NULL," +
                "  `size` INT NOT NULL," +
                "  `items` VARCHAR(1024) NOT NULL," +
                "  PRIMARY KEY (`id`));";
    }

    /**
     * Returns the script to get the inventories by group
     * @return String
     */
    public String getInventoriesGetByGroupScript() {
        return "SELECT * FROM inventories WHERE server = '" + ServerConstants.getServerGroup() + "';";
    }

    /**
     * Returns the script to get the inventories by server name
     * @return String
     */
    public String getInventoriesGetByNameScript() {
        return "SELECT * FROM inventories WHERE server = '" + ServerConstants.getServerName() + "';";
    }

    /**
     * Returns the script to get the inventories by global
     * @return String
     */
    public String getInventoriesGetByGlobalScript() {
        return "SELECT * FROM inventories WHERE server = 'global';";
    }

    /**
     * Returns the script to create the groups table
     * @return String
     */
    public String getGroupsCreateScript(){
        return "CREATE TABLE IF NOT EXISTS `perm_groups` (" +
                "  `id` INT NOT NULL AUTO_INCREMENT," +
                "  `name` VARCHAR(128) NOT NULL," +
                "  `prefix` VARCHAR(128) NOT NULL," +
                "  `nameTag` VARCHAR(128) NOT NULL," +
                "  `default` TINYINT NOT NULL DEFAULT 0," +
                "  PRIMARY KEY (`id`));";
    }

    /**
     * Returns the script to get the groups
     * @return String
     */
    public String getGroupsGetScript() {
        return "SELECT * FROM perm_groups;";
    }

    /**
     * Returns the script to create the npc table
     * @return String
     */
    public String getNPCCreateScript() {
        return "CREATE TABLE IF NOT EXISTS `npc` (" +
                "  `id` INT NOT NULL AUTO_INCREMENT," +
                "  `server` VARCHAR(128) NOT NULL," +
                "  `name` VARCHAR(128) NOT NULL," +
                "  `type` VARCHAR(128) NOT NULL," +
                "  `skin` VARCHAR(128) NOT NULL," +
                "  `text` VARCHAR(1024) NOT NULL DEFAULT '[]'," +
                "  `location` VARCHAR(128) NOT NULL," +
                "  `command` VARCHAR(1024) NULL," +
                "  PRIMARY KEY (`id`));";
    }

    /**
     * Returns the script to get the npc by group
     * @return String
     */
    public String getNPCGetByGroupScript() {
        return "SELECT * FROM npc WHERE server = '" + ServerConstants.getServerGroup() + "';";
    }

    /**
     * Returns the script to get the npc by server name
     * @return String
     */
    public String getNPCGetByNameScript() {
        return "SELECT * FROM npc WHERE server = '" + ServerConstants.getServerName() + "';";
    }
}
