package hu.telcat.servercore.network.packet.spigot;

import hu.telcat.servercore.ServerCore;
import hu.telcat.servercore.network.packet.IPacket;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class S05TeleportPlayerToServer implements IPacket {

    @Override
    public void loadData(JSONObject json) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeUTF("Connect");
                out.writeUTF(json.getString("server"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            UUID uuid = UUID.fromString(json.getString("uuid"));
            Bukkit.getPlayer(uuid).sendPluginMessage(ServerCore.getInstance(), "BungeeCord", b.toByteArray());
        } catch (org.bukkit.plugin.messaging.ChannelNotRegisteredException e) {
            Bukkit.getLogger().warning(" ERROR - Usage of bungeecord connect effects is not possible. Your server is not having bungeecord support (Bungeecord channel is not registered in your minecraft server)!");
        }
    }

    @Override
    public JSONObject serializePacket() {
        return null;
    }
}
