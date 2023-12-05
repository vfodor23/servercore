package hu.telcat.servercore.permission;

public class CoreGroup {

    private String name, prefix, nameTag;
    private boolean defaultGroup;

    public CoreGroup(String name, String prefix, String nameTag, boolean defaultGroup){
        this.name = name;
        this.prefix = prefix;
        this.nameTag = nameTag;
        this.defaultGroup = defaultGroup;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isDefaultGroup() {
        return defaultGroup;
    }

    public String getNameTag() {
        return nameTag;
    }
}
