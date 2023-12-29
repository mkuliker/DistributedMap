package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TcpClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) throws InterruptedException, IOException {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new TcpClientInitializer());

            Channel channel = b.connect(SERVER_HOST, SERVER_PORT).sync().channel();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                String message = reader.readLine();
                if (message.equalsIgnoreCase("quit")) {
                    System.out.println("BYE");
                    break;
                }

                channel.writeAndFlush(message + "\n");
            }
        } finally {
            group.shutdownGracefully();
        }
    }
}
