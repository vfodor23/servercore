package hu.telcat.servercore.command.core;

import hu.telcat.servercore.ServerCore;
import hu.telcat.servercore.network.packet.spigot.S04FindLobbyServer;
import org.bukkit.entity.Player;
import org.json.JSONObject;

public class LobbyCommand  implements CoreCommand{

    @Override
    public void executeCommand(Player player, String[] args) {
        String group = args[0];
        JSONObject json = new JSONObject();
        json.put("uuid", player.getUniqueId());
        json.put("group", group);
        S04FindLobbyServer findLobbyServer = new S04FindLobbyServer();
        findLobbyServer.loadData(json);
        ServerCore.getInstance().getCerberusQueue().queuePacket(findLobbyServer);
    }
}
