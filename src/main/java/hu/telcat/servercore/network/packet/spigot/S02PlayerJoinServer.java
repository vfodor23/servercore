package hu.telcat.servercore.network.packet.spigot;

import hu.telcat.servercore.network.packet.IPacket;
import hu.telcat.servercore.utils.ServerConstants;
import org.json.JSONObject;

public class S02PlayerJoinServer implements IPacket {

    private JSONObject json;

    @Override
    public void loadData(JSONObject json) {
        this.json = json;
        json.put("id", 2);
        this.json.put("server", ServerConstants.getServerName());
    }

    @Override
    public JSONObject serializePacket() {
        return this.json;
    }
}
