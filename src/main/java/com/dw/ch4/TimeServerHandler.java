package com.dw.ch4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;


/**
 * tcp粘包
 * Created by dongwei on 2017/7/23.
 */
public class TimeServerHandler extends ChannelHandlerAdapter{
    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req,"UTF-8").substring(0,req.length - System.getProperty("line.separator").length());
        System.out.println("The time srver receive order: " + body + ";the counter is "+ ++counter);
        String currTime = "QUERY TIMER ORDER".equalsIgnoreCase(body)  ? new Date(System.currentTimeMillis()).toString() :
                "BAD ORDER";
        currTime = currTime + System.getProperty("line.separator");
        ByteBuf resp = Unpooled.copiedBuffer(currTime.getBytes());
        ctx.writeAndFlush(resp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
