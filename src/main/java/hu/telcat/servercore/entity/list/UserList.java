package hu.telcat.servercore.entity.list;

import hu.telcat.servercore.entity.User;

import java.util.HashMap;
import java.util.UUID;

public class UserList {

    private static HashMap<UUID, User> uuidToUser = new HashMap<>();

    public static User getUser(UUID uuid){
        if(uuidToUser.containsKey(uuid)){
            return uuidToUser.get(uuid);
        }
        User user = new User(uuid);
        uuidToUser.put(uuid, user);
        return user;
    }

    public static void removeUser(UUID uuid){
        uuidToUser.remove(uuid);
    }
}
