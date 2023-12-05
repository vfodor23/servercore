package hu.telcat.servercore.network.packet;


import hu.telcat.servercore.network.packet.common.C01ServerConnectionStart;
import hu.telcat.servercore.network.packet.spigot.S02PlayerJoinServer;
import hu.telcat.servercore.network.packet.spigot.S03PlayerLeaveServer;
import hu.telcat.servercore.network.packet.spigot.S04FindLobbyServer;
import hu.telcat.servercore.network.packet.spigot.S05TeleportPlayerToServer;

import java.util.HashMap;

public class PacketHandler {

    private HashMap<Integer, Class<? extends IPacket>> idToPacket = new HashMap<>();

    public void registerPackets() {
        idToPacket.put(1, C01ServerConnectionStart.class);
        idToPacket.put(2, S02PlayerJoinServer.class);
        idToPacket.put(3, S03PlayerLeaveServer.class);
        idToPacket.put(4, S04FindLobbyServer.class);
        idToPacket.put(5, S05TeleportPlayerToServer.class);
    }

    public Class<? extends IPacket> getPacketById(int i){
        return this.idToPacket.get(i);
    }

    public boolean packetExist(int i){
        return idToPacket.containsKey(i);
    }
}
