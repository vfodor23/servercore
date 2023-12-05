package hu.telcat.servercore.network;

import hu.telcat.servercore.ServerCore;
import hu.telcat.servercore.log.Log;
import hu.telcat.servercore.network.packet.IPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.bukkit.Bukkit;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;

public class CerberusQueue{

    private final ArrayList<String> packetQueue = new ArrayList<>();
    private Channel channel;

    public void startQueue() {
        Log.info("Starting the packet queue system");
        Bukkit.getScheduler().runTaskTimerAsynchronously(ServerCore.getInstance(), () -> {
            if(packetQueue.size() == 0 || channel == null){
                return;
            }
            Log.info("§dSending out " + packetQueue.size() + " packet(s)");
            long start = ZonedDateTime.now().toInstant().toEpochMilli();
            Iterator it = packetQueue.iterator();
            while(it.hasNext()){
                final ByteBuf msg = Unpooled.buffer();
                String out = (String) it.next();
                Log.info("A : " + out);
                msg.writeCharSequence((String) out, StandardCharsets.UTF_8);
                channel.writeAndFlush(msg);
                it.remove();
            }
            long finish = ZonedDateTime.now().toInstant().toEpochMilli();
            Log.info("§dPackets were sent out in " + (finish - start) + " ms");
            Log.info("§dPackets stuck in the system: " + packetQueue.size());
        }, 0, 10L);
    }

    public void queuePacket(IPacket packet){
        this.packetQueue.add(packet.serializePacket().toString());
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
