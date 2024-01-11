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
    public static final String SHUTDOWN = "shutdown";
    public static final String QUIT = "quit";
    static List<Channel> channels;

    public static void main(String[] args) throws IOException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new TcpClientInitializer());
            channels = createChannels(b);
            handleMessageLoop(channels);
        } finally {
            beforeShutdown();
            group.shutdownGracefully();
        }
    }

    private static void beforeShutdown() {
        System.out.println("BYE");
    }

    private static void handleMessageLoop(List<Channel> channels) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (checkChannels(channels)) {
                String message = reader.readLine();
                if (isQuitMessage(message)) {
                    break;
                } else if (isBroadcastMessage(message)) {
                    broadcastMessage(message, channels);
                } else {
                    sendMessage(message, getChannel(message, channels));
                }
            }
        }
    }

    private static void sendMessage(String message, Channel channel) {
        channel.writeAndFlush(message + "\n");
    }

    private static boolean isBroadcastMessage(String message) {
        return message.equalsIgnoreCase(SHUTDOWN);
    }

    private static boolean isQuitMessage(String message) {
        return message.equalsIgnoreCase(QUIT);
    }

    private static void broadcastMessage(String message, List<Channel> channels) {
        for (Channel channel : channels) {
            channel.writeAndFlush(message + "\n");
        }
    }

    private static boolean checkChannels(List<Channel> channels) {
        channels.removeIf(c -> !c.isActive());
        return !channels.isEmpty();
    }

    private static List<Channel> createChannels(Bootstrap b) {
        List<Channel> channels = new ArrayList<>();
        for (ServerInfo s : config.Configuration.servers) {
            try{
                Channel channel = b.connect(s.getHost(), s.getPort()).sync().channel();
                channels.add(channel);
                System.out.println("Successfully connected to " + s);
            } catch (Exception e){
                System.out.println("Error while connecting to " + s + ". Connection to this worker was refused.");
            }
        }
        return channels;
    }

    private static Channel getChannel(String message, List<Channel> channels) {
        String[] leftMessage = message.split(":")[0].split(" ");
        if (channels.size() > 1 && leftMessage.length == 2) {
            int key = 0;
            try {
                key = Integer.parseInt(leftMessage[1]);
            } catch (NumberFormatException ignored) {
            }
            int number = key % channels.size();
            return channels.get(number);
        } else {
            return channels.get(0);
        }

    }
}
