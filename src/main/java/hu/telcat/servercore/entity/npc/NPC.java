package hu.telcat.servercore.entity.npc;

import hu.telcat.servercore.command.CommandType;
import hu.telcat.servercore.entity.list.LocationList;
import org.bukkit.Location;
import org.bukkit.metadata.MetadataStore;
import org.json.JSONArray;
import org.json.JSONObject;

public class NPC {

    private final String name;
    private final String skin;
    private final JSONArray text;
    private Location location;
    private boolean enableCommand = false;
    private CommandType commandType;
    private String command;

    public NPC (String name, String skin, JSONArray text, String location) {
        this.name = name;
        this.skin = skin;
        this.text = text;
        this.location = LocationList.getLocation(location);
    }

    public NPC (String name, String skin, JSONArray text, String location, JSONObject command) {
        this.name = name;
        this.skin = skin;
        this.text = text;
        this.location = LocationList.getLocation(location);
        if(command != null){
            this.enableCommand = true;
            this.commandType = CommandType.valueOf(command.getString("type"));
            this.command = command.getString("command");
        }
    }

    public String getName() {
        return name;
    }

    public String getSkin() {
        return skin;
    }

    public JSONArray getText() {
        return text;
    }

    public Location getLocation() {
        return location;
    }

    public void spawn() {}

    public void remove() {}

    public boolean isCommandEnabled(){
        return enableCommand;
    }

    public String getCommand() {
        return command;
    }

    public CommandType getCommandType() {
        return commandType;
    }
}
