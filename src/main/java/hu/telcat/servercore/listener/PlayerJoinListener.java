package hu.telcat.servercore.listener;

import hu.telcat.servercore.ServerCore;
import hu.telcat.servercore.entity.User;
import hu.telcat.servercore.entity.list.CoreInventoryList;
import hu.telcat.servercore.entity.list.LocationList;
import hu.telcat.servercore.entity.list.UserList;
import hu.telcat.servercore.network.packet.spigot.S02PlayerJoinServer;
import hu.telcat.servercore.utils.ServerConstants;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.JSONObject;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(LocationList.isLocationExist("spawn")) player.teleport(LocationList.getLocation("spawn"));
        if(ServerConstants.clearInventoryOnJoin) player.getInventory().clear();
        if(CoreInventoryList.isInventoryExist("player")) CoreInventoryList.getInventory("player").setItemsToInventory(player.getInventory());
        JSONObject json = new JSONObject();
        json.put("uuid", player.getUniqueId().toString());
        S02PlayerJoinServer playerJoinServer = new S02PlayerJoinServer();
        playerJoinServer.loadData(json);
        //ServerCore.getInstance().getCerberusQueue().queuePacket(playerJoinServer);
        User user = UserList.getUser(player.getUniqueId());
    }
}
