package hu.telcat.servercore.entity;

import hu.telcat.servercore.entity.list.ItemList;
import hu.telcat.servercore.log.Log;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class CoreInventory {

    private int size = 9;
    private HashMap<Integer, String> itemsInInventory = new HashMap<>();
    private String displayName;
    private String name;

    public CoreInventory (String name, String displayName, int size, JSONArray items){
        this.name = name;
        this.displayName = displayName;
        this.size = size;

        for(Object obj : items){
            JSONObject jsonObject = new JSONObject(obj.toString());
            itemsInInventory.put(jsonObject.getInt("slot"), jsonObject.getString("item"));
        }
    }

    public Inventory getBukkitInventory(){
        Inventory inv = Bukkit.createInventory(null, this.size, this.displayName);
        for(int i = 0; i < this.size; i++){
            if(itemsInInventory.containsKey(i)){
                inv.setItem(i, ItemList.getItem(itemsInInventory.get(i)).getItem());
            }
        }
        return inv;
    }

    public void setItemsToInventory(Inventory inv){
        for(int i = 0; i < this.size; i++){
            if(itemsInInventory.containsKey(i)){
                inv.setItem(i, ItemList.getItem(itemsInInventory.get(i)).getItem());
            }
        }
    }
}
