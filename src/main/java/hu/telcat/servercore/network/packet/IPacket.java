package hu.telcat.servercore.network.packet;

import org.json.JSONObject;

public interface IPacket {

    public void loadData(JSONObject json);
    public JSONObject serializePacket();
}
