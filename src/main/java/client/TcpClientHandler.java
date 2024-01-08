package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TcpClientHandler extends ChannelInboundHandlerAdapter {
    static final String SHUTDOWN_MSG = "Server has been shutdown. Press enter to quit.";
    static final String AN_ERROR_OCCURRED_THERE_IS_POSSIBLE_DATA_LOSS = "An error occurred. There is possible data loss.";


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String message = (String) msg;
        if (!isQuitMessage(message)) {
            System.out.println(message);
        } else {
            System.out.println(SHUTDOWN_MSG);
            ctx.close();
        }
    }
    private static boolean isQuitMessage(String message) {
        return message.equalsIgnoreCase("quit");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (TcpClient.channels.isEmpty()) {
            System.out.println(SHUTDOWN_MSG);
        } else {
            System.out.println(AN_ERROR_OCCURRED_THERE_IS_POSSIBLE_DATA_LOSS);
        }
        ctx.close();
    }

}
