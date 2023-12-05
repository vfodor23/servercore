package hu.telcat.servercore.listener;

import hu.telcat.servercore.ServerCore;
import hu.telcat.servercore.entity.list.UserList;
import hu.telcat.servercore.network.packet.spigot.S02PlayerJoinServer;
import hu.telcat.servercore.network.packet.spigot.S03PlayerLeaveServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.json.JSONObject;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event){
        Player player = event.getPlayer();
        JSONObject json = new JSONObject();
        json.put("uuid", player.getUniqueId().toString());
        S03PlayerLeaveServer playerLeaveServer = new S03PlayerLeaveServer();
        playerLeaveServer.loadData(json);
        //ServerCore.getInstance().getCerberusQueue().queuePacket(playerLeaveServer);
        UserList.removeUser(player.getUniqueId());
    }
}
