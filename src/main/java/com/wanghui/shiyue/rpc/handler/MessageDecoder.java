package com.wanghui.shiyue.rpc.handler;

import com.wanghui.shiyue.rpc.message.Message;
import com.wanghui.shiyue.rpc.message.Request;
import com.wanghui.shiyue.rpc.message.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;
import java.util.Arrays;

public class MessageDecoder extends LengthFieldBasedFrameDecoder {


    public MessageDecoder() {
        super(1024 * 1024, 0, 4, 0, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);

        byte[] logic = new byte[Message.LOGIC.length];

        frame.readBytes(logic);

        if(!Arrays.equals(logic, Message.LOGIC)){
            throw new IllegalArgumentException("魔数不对，消息有问题。。。");
        }

        byte messageType = frame.readByte();
        byte[] body = new byte[frame.readableBytes()];
        frame.readBytes(body);

        if(Message.MessageTyepe.REQUEST.getClass().equals(messageType)){
            return deserializeRequest(body);
        }
        if(Message.MessageTyepe.RESPONSE.getClass().equals(messageType)){
            return deserializeResponse(body);
        }

        throw new IllegalArgumentException("消息类型不支持,messageType" + messageType);
    }

    private Response deserializeResponse(byte[] body) {
        return new Response();
    }

    private Request deserializeRequest(byte[] body) {
        return new Request();
    }
}