package hu.telcat.servercore.entity;

import hu.telcat.servercore.permission.GroupList;
import java.util.UUID;

public class User {

    private UUID uuid;
    private final String group = "default";
    private final String prefix;

    public User(UUID uuid){
        this.uuid = uuid;
        prefix = GroupList.getGroup(this.group).getPrefix();
    }

    public String getPrefix() {
        return prefix;
    }
}
