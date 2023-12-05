package hu.telcat.servercore.command.core;

import hu.telcat.servercore.ServerCore;
import hu.telcat.servercore.network.packet.spigot.S08FindGameServer;
import org.bukkit.entity.Player;
import org.json.JSONObject;

public class GameCommand implements CoreCommand {

    @Override
    public void executeCommand(Player player, String[] args) {
        String group = args[0];
        JSONObject json = new JSONObject();
        json.put("uuid", player.getUniqueId());
        json.put("group", group);
        S08FindGameServer findGameServer = new S08FindGameServer();
        findGameServer.loadData(json);
        ServerCore.getInstance().getCerberusQueue().queuePacket(findGameServer);
    }
}
