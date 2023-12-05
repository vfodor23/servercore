package hu.telcat.servercore.network;

import hu.telcat.servercore.ServerCore;
import hu.telcat.servercore.log.Log;
import hu.telcat.servercore.network.packet.IPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

public class CerberusHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf buffer) throws Exception {
        String income = buffer.readCharSequence(buffer.readableBytes(), StandardCharsets.UTF_8).toString();
        if(!isValidJSON(income)){
            Log.error("Packet error: Not valid JSON Message: " + income);
        }
        JSONObject json = new JSONObject(income);
        if(!json.has("id")){
            Log.error("Packet error: Not valid JSON Message: " + income);
        }
        int id = json.getInt("id");
        if(!ServerCore.getPacketHandler().packetExist(id)){
            Log.error("Packet with ID: " + id + " do NOT exist");
            return;
        }
        try {
            IPacket packet = ServerCore.getPacketHandler().getPacketById(id).getDeclaredConstructor().newInstance();
            packet.loadData(json);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidJSON(String json) {
        try {
            new JSONObject(json);
        } catch (JSONException e) {
            try {
                new JSONArray(json);
            } catch (JSONException ne) {
                return false;
            }
        }
        return true;
    }
}
