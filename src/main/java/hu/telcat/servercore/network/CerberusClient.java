package hu.telcat.servercore.network;

import hu.telcat.servercore.ServerCore;
import hu.telcat.servercore.log.Log;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import network.ycc.raknet.RakNet;
import network.ycc.raknet.client.RakNetClient;
import network.ycc.raknet.pipeline.UserDataCodec;

public class CerberusClient extends Thread {

    private final String host;
    private final int port;
    private Channel connection;

    public CerberusClient (String host, int port){
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        super.run();
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(RakNetClient.CHANNEL)
                    .option(RakNet.MTU, 150)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .handler(new ChannelInitializer<Channel>(){
                        @Override
                        protected void initChannel(Channel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(UserDataCodec.NAME, new UserDataCodec(0xFE));
                            pipeline.addLast(new CerberusHandler());
                        }
                    });
            ChannelFuture future = b.connect(this.host, this.port);
            if(!future.sync().isSuccess()){
                Log.error("Couldn't connect to Cerberus system!");
                return;
            }
            Log.info("Connected to the Cerberus system");
            connection = future.sync().channel();
            ServerCore.getInstance().getCerberusQueue().setChannel(this.connection);
            ServerCore.getInstance().getCerberusQueue().startQueue();
            //Wait until the connection is closed
            future.channel().closeFuture().sync();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            //Shutdown the event loop to terminate all Threads
            group.shutdownGracefully();
        }
    }

    public Channel getConnection() {
        return connection;
    }
}
