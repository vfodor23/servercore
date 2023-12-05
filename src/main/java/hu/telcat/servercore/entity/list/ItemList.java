package hu.telcat.servercore.entity.list;

import hu.telcat.servercore.command.CommandType;
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

public class ItemList {

    private static HashMap<String, Item> itemByName = new HashMap<String, Item>();

    public static void addItem(String name, ResultSet item) {
        itemByName.remove(name);
        Item dbItem;
        try {
            if(item.getString("attributes") != null && !Objects.equals(item.getString("attributes"), "")){
                dbItem = new Item(name, Material.valueOf(item.getString("material")), item.getString("displayName"),
                                item.getInt("amount"), new JSONObject(item.getString("attributes")),
                                item.getString("interact"), new JSONArray(item.getString("command")));
            }else if(item.getString("command") != null && !Objects.equals(item.getString("command"), "")){
                dbItem = new Item(name, Material.valueOf(item.getString("material")), item.getString("displayName"),
                        item.getInt("amount"), item.getString("interact"), new JSONArray(item.getString("command")));
            }else if(item.getString("interact") != null && !Objects.equals(item.getString("interact"), "")){
                dbItem = new Item(name, Material.valueOf(item.getString("material")), item.getString("displayName"),
                        item.getInt("amount"), item.getString("interact"));
            }else if(item.getString("displayName") != null && !Objects.equals(item.getString("displayName"), "")){
                dbItem = new Item(Material.valueOf(item.getString("material")), item.getString("displayName"),
                        item.getInt("amount"));
            }else{
                dbItem = new Item(Material.valueOf(item.getString("material")), item.getInt("amount"));
            }
            itemByName.put(name, dbItem);
        } catch (SQLException e) {
            Log.error("Error during the loading of item " + name + ". Please check log!");
            e.printStackTrace();
        }
    }

    public static Item getItem(String name){
        return itemByName.get(name);
    }

    public static boolean isItemExist(String name){
        return itemByName.containsKey(name);
    }

    public static int itemSize(){
        return itemByName.size();
    }
}
