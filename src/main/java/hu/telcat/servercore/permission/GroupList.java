package hu.telcat.servercore.permission;

import hu.telcat.servercore.ServerCore;
import hu.telcat.servercore.entity.CoreInventory;
import hu.telcat.servercore.log.Log;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.data.Group;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.json.JSONArray;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupList {

    private static HashMap<String, CoreGroup> loadedGroups = new HashMap<>();
    private static GroupManager groupManager;

    private static boolean hasGroupManager() {
        if(groupManager != null) return true;
        final PluginManager pluginManager = ServerCore.getInstance().getServer().getPluginManager();
        final Plugin groupManagerPlugin = pluginManager.getPlugin("GroupManager");

        if(groupManagerPlugin != null && groupManagerPlugin.isEnabled()){
            groupManager = (GroupManager) groupManagerPlugin;
            return true;
        }
        return false;
    }

    private static boolean createGroup(CoreGroup coreGroup){
        if(!hasGroupManager()) return false;
        final OverloadedWorldHolder worldHolder = groupManager.getWorldsHolder().getDefaultWorld();
        if(worldHolder == null) return false;
        Group group;
        if(!worldHolder.groupExists(coreGroup.getName())){
            group = worldHolder.createGroup(coreGroup.getName());
        }else{
            group = worldHolder.getGroup(coreGroup.getName());
        }
        Map<String, Object> prefix = new HashMap<>();
        prefix.put("prefix", coreGroup.getPrefix());
        group.setVariables(prefix);
        if(coreGroup.isDefaultGroup()) worldHolder.setDefaultGroup(group);
        return true;
    }

    public static void loadGroup(String name, ResultSet result){
        try {
            String prefix = result.getString("prefix");
            String nameTag = result.getString("nameTag");
            boolean defaultGroup = result.getInt("default") == 1;
            CoreGroup coreGroup = new CoreGroup(name, prefix, nameTag, defaultGroup);
            if(createGroup(coreGroup)){
                loadedGroups.put(name, coreGroup);
            }else{
                Log.error("Error during the creating of the " + coreGroup.getName() + " group.");
            }
        } catch (SQLException e) {
            Log.error("Error during the loading of group " + name + ". Please check log!");
            e.printStackTrace();
        }
    }

    public static CoreGroup getGroup(String group){
        return loadedGroups.get(group);
    }

    public static int groupSize(){
        return loadedGroups.size();
    }
}
