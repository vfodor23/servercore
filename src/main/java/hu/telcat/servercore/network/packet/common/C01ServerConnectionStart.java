package hu.telcat.servercore.network.packet.common;

import hu.telcat.servercore.ServerCore;
import hu.telcat.servercore.network.packet.IPacket;
import hu.telcat.servercore.utils.ServerConstants;
import org.bukkit.Bukkit;
import org.json.JSONObject;

public class C01ServerConnectionStart implements IPacket {

    @Override
    public void loadData(JSONObject json) {}

    @Override
    public JSONObject serializePacket() {
        JSONObject json = new JSONObject();
        JSONObject extra = new JSONObject();
        extra.put("maxPlayers", Bukkit.getMaxPlayers());
        json.put("extra", extra);
        json.put("serverType", ServerConstants.getServerType());
        json.put("group", ServerConstants.getServerGroup());
        json.put("name", ServerConstants.getServerName());
        json.put("id", 1);
        return json;
    }
}
