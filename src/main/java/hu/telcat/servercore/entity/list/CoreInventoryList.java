package hu.telcat.servercore.entity.list;

import hu.telcat.servercore.entity.CoreInventory;
import hu.telcat.servercore.entity.Item;
import hu.telcat.servercore.log.Log;
import org.bukkit.Location;
import org.bukkit.Material;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

public class CoreInventoryList {

    private static HashMap<String, CoreInventory> inventoryByName = new HashMap<>();

    public static void addInventory(String name, ResultSet result) {
        inventoryByName.remove(name);
        try {
            String displayName = result.getString("displayname");
            int size = result.getInt("size");
            JSONArray array = new JSONArray(result.getString("items"));
            CoreInventory inventory = new CoreInventory(name, displayName, size, array);
            inventoryByName.put(name, inventory);
        } catch (SQLException e) {
            Log.error("Error during the loading of item " + name + ". Please check log!");
            e.printStackTrace();
        }
    }

    public static CoreInventory getInventory(String name){
        return inventoryByName.get(name);
    }

    public static boolean isInventoryExist(String name){
        return inventoryByName.containsKey(name);
    }

    public static int inventorySize(){
        return inventoryByName.size();
    }
}
