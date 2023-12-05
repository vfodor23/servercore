package hu.telcat.servercore.entity.list;

import hu.telcat.servercore.ServerCore;
import hu.telcat.servercore.log.Log;
import org.bukkit.Location;

import java.util.HashMap;

public class LocationList {

    private static HashMap<String, Location> locationByName = new HashMap<String, Location>();

    public static void addLocation(String name, Location location) {
        locationByName.remove(name);
        locationByName.put(name, location);
    }

    public static Location getLocation(String name){
        return locationByName.get(name);
    }

    public static boolean isLocationExist(String name){
        return locationByName.containsKey(name);
    }

    public static int locationSize(){
        return locationByName.size();
    }
}
