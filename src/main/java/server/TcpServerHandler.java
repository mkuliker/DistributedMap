package server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import messages.*;

public class TcpServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String message = (String) msg;
        System.out.println("Received: " + message);
        message = message.replace("\n","");

        String result = handle(message);
        if (result != null){
            ctx.writeAndFlush(result);
            System.out.println("Sent: " + result);
        }
    }

    private String handle(String message) {
        Message m = MessageHandler.handle(message);
        if (m instanceof MessageWithError me && me.getError() != null){
            return me.getError();
        }
        else {
            m.handle();
            return m.getResult();
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
