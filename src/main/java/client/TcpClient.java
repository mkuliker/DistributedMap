package client;

import config.Configuration.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TcpClient {

    public static void main(String[] args) throws InterruptedException, IOException {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new TcpClientInitializer());

            List<Channel> channels = createChannels(b);

            try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
                while (true) {
                    String message = reader.readLine();
                    if (message.equalsIgnoreCase("quit")) {
                        System.out.println("BYE");
                        break;
                    }
                    Channel channel = getChannel(message,channels);
                    channel.writeAndFlush(message + "\n");
                }
            }

        } finally {
            group.shutdownGracefully();
        }
    }

    private static List<Channel> createChannels(Bootstrap b) throws InterruptedException {
        List<Channel> channels = new ArrayList<>();
        for(ServerInfo s: config.Configuration.servers){
            Channel channel = b.connect(s.getHost(), s.getPort()).sync().channel();
            channels.add(channel);
        }
        return channels;
    }

    private static Channel getChannel(String message, List<Channel> channels) {
        int key = Integer.parseInt(message.split(":")[0].split(" ")[1]);
        int number = key%channels.size();
        return channels.get(number);
    }
}
