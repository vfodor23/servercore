package hu.telcat.servercore;

import hu.telcat.servercore.command.core.*;
import hu.telcat.servercore.command.server.TestSpawn;
import hu.telcat.servercore.database.DatabaseManager;
import hu.telcat.servercore.database.MySQL;
import hu.telcat.servercore.entity.list.CoreCommandList;
import hu.telcat.servercore.entity.list.CoreInventoryList;
import hu.telcat.servercore.entity.list.ItemList;
import hu.telcat.servercore.entity.list.LocationList;
import hu.telcat.servercore.entity.npc.NPC;
import hu.telcat.servercore.entity.npc.NPCList;
import hu.telcat.servercore.entity.npc.SheepNPC;
import hu.telcat.servercore.listener.*;
import hu.telcat.servercore.log.Log;
import hu.telcat.servercore.network.CerberusClient;
import hu.telcat.servercore.network.CerberusQueue;
import hu.telcat.servercore.network.packet.PacketHandler;
import hu.telcat.servercore.network.packet.common.C01ServerConnectionStart;
import hu.telcat.servercore.permission.GroupList;
import hu.telcat.servercore.utils.ServerConstants;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;

public class ServerCore extends JavaPlugin {

    //Main stuff
    private static ServerCore instance;
    private CoreCommandList coreCommandList = new CoreCommandList();

    //Cerberus stuff
    private CerberusClient cerberusClient;
    private CerberusQueue cerberusQueue;
    private static PacketHandler packetHandler;

    //Database stuff
    private MySQL mySQL;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable(){
        instance = this;
        //Saving and loading the default config
        saveDefaultConfig();
        ServerConstants.loadServerData();
        //Loading the database
        mySQL = new MySQL();
        if(mySQL.isSuccess()){
            databaseManager = new DatabaseManager();
            databaseManager.createTables();
            Log.info("**************[Loading data from MySQL]**************");
            databaseManager.loadLocations();
            Log.info("Loaded " + LocationList.locationSize() + " locations from the database");
            databaseManager.loadSettings();
            Log.info("Loaded " + ServerConstants.loadedSettings + " settings from the database");
            databaseManager.loadItems();
            Log.info("Loaded " + ItemList.itemSize() + " items from the database");
            databaseManager.loadInventories();
            Log.info("Loaded " + CoreInventoryList.inventorySize() + " inventories from the database");
            databaseManager.loadGroups();
            Log.info("Loaded " + GroupList.groupSize() + " groups from the database");
            databaseManager.loadNPC();
            Log.info("Loaded " + NPCList.npcSize() + " npc from the database");
            Log.info("*****************[Loading has ended]*****************");
        }
        //Loading the Cerberus system
        //initializeCerberus();
        //Registering server stuff
        registerListeners();
        registerCommands();
        registerCoreCommands();
        //Spawn NPC
        spawnNPC();
    }

    @Override
    public void onDisable(){
        Log.info("Trying to remove NPCs");
        despawnNPC();
        Log.info("Disabling Cerberus system..");
        //cerberusClient.getConnection().close();
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new MiscListener(), this);
        pluginManager.registerEvents(new PlayerInteractListener(), this);
        pluginManager.registerEvents(new InventoryClickListener(), this);
        pluginManager.registerEvents(new PlayerQuitListener(), this);
        pluginManager.registerEvents(new PlayerChatListener(), this);
        pluginManager.registerEvents(new PlayerInteractEntityListener(), this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    private void registerCommands() {
        new BukkitRunnable() {
            @Override
            public void run() {
                new TestSpawn().register();
            }
        }.runTaskLater(this, 60);
    }

    private void registerCoreCommands() {
        coreCommandList.addCommand("teszt", TestCommand.class);
        coreCommandList.addCommand("open", OpenCommand.class);
        coreCommandList.addCommand("lobby", LobbyCommand.class);
        coreCommandList.addCommand("game", GameCommand.class);
        coreCommandList.addCommand("closeInventory", CloseInventoryCommand.class);
        coreCommandList.addCommand("playSound", PlaySoundCommand.class);
        coreCommandList.addCommand("newMail", NewMailCommand.class);
    }

    /**
     * Cerberus initializing
     */
    private void initializeCerberus() {
        if(!getConfig().getBoolean("cerberus.enabled")){
            Log.info("§cCerberus is not enabled the plugin will NOT work properly!");
            return;
        }
        cerberusClient = new CerberusClient(getConfig().getString("cerberus.host"), getConfig().getInt("cerberus.port"));
        cerberusClient.start();
        cerberusQueue = new CerberusQueue();
        packetHandler = new PacketHandler();
        packetHandler.registerPackets();

        //Server information packet
        cerberusQueue.queuePacket(new C01ServerConnectionStart());
    }

    /**
     * Spawn the npc loaded from database
     */
    private void spawnNPC(){
        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                Log.info("Trying to spawn NPCs");
                for(NPC npc : NPCList.getNPCByName().values()){
                    npc.spawn();
                }
            }
        }, 80L);
    }

    private void despawnNPC() {
        for(NPC npc : NPCList.getNPCByName().values()){
            npc.remove();
        }
    }

    public static ServerCore getInstance(){
        return instance;
    }

    public CerberusQueue getCerberusQueue() {
        return cerberusQueue;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public CoreCommandList getCoreCommandList() {
        return coreCommandList;
    }

    public CoreCommand getCoreCommand(String command){
        try {
            return coreCommandList.getCommand(command).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Log.error("Error during the creation of a new " + command + " instance");
            e.printStackTrace();
            return  null;
        }
    }

    public static PacketHandler getPacketHandler() {
        return packetHandler;
    }
}