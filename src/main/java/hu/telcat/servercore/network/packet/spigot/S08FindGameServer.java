package hu.telcat.servercore.network.packet.spigot;

import hu.telcat.servercore.network.packet.IPacket;
import org.json.JSONObject;

public class S08FindGameServer implements IPacket {

    private JSONObject json;
    @Override
    public void loadData(JSONObject json) {
        this.json = json;
        json.put("id", 8);
    }

    @Override
    public JSONObject serializePacket() {
        return this.json;
    }
}
