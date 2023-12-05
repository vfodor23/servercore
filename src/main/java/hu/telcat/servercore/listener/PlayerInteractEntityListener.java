package hu.telcat.servercore.listener;

import hu.telcat.servercore.ServerCore;
import hu.telcat.servercore.command.CommandType;
import hu.telcat.servercore.entity.list.ItemList;
import hu.telcat.servercore.entity.npc.NPC;
import hu.telcat.servercore.entity.npc.NPCList;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractEntityListener implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if(entity.hasMetadata("CORE#CUSTOM_COMMAND")){
            event.setCancelled(true);
            String name = entity.getMetadata("CORE#CUSTOM_COMMAND").get(0).asString();
            if(!NPCList.isNPCExist(name)){
                player.sendMessage("§4Nem található ilyen NPC");
                return;
            }
            NPC npc = NPCList.getNPC(name);
            if(!npc.isCommandEnabled()){
                player.sendMessage("§4A parancs nincs engedélyezve");
                return;
            }
            CommandType commandType = npc.getCommandType();
            String command = npc.getCommand();
            String commandIdentifier = command;
            String[] args;
            if(command.contains(" ")){
                String[] splitedCommand = command.split(" ");
                commandIdentifier = splitedCommand[0];
                args = new String[splitedCommand.length-1];
                for(int i = 0; i< args.length; i++){
                    args[i] = splitedCommand[i+1];
                }
            }else{
                args = new String[0];
            }
            switch (commandType){
                case CORE -> ServerCore.getInstance().getCoreCommand(commandIdentifier).executeCommand(player, args);
                case BUKKIT -> {

                }
            }
        }
    }
}
