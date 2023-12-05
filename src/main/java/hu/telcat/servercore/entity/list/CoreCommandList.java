package hu.telcat.servercore.entity.list;

import hu.telcat.servercore.command.core.CoreCommand;

import java.util.HashMap;

public class CoreCommandList {

    private HashMap<String, Class<? extends CoreCommand>> commandByName = new HashMap<>();

    public void addCommand(String name, Class<? extends CoreCommand>  command) {
        commandByName.remove(name);
        commandByName.put(name, command);
    }

    public Class<? extends CoreCommand> getCommand(String name){
        return commandByName.get(name);
    }

    public boolean isCommandExist(String name){
        return commandByName.containsKey(name);
    }

    public int commandSize(){
        return commandByName.size();
    }
}
