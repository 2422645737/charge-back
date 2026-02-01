package com.wanghui.shiyue.rpc.handler;

import com.alibaba.fastjson.JSONObject;
import com.wanghui.shiyue.rpc.message.Message;
import com.wanghui.shiyue.rpc.message.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

public class MessageEncoder extends MessageToByteEncoder<Response> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Response o, ByteBuf byteBuf) throws Exception {
        byte[] logic = Message.LOGIC;

        byte messageType = Message.MessageTyepe.REQUEST.code;

        byte[] body = encodeBody(o);

        int length = logic.length + Byte.BYTES + body.length;

        byteBuf.writeInt(length);
        byteBuf.writeBytes(logic);
        byteBuf.writeByte(messageType);
        byteBuf.writeBytes(body);
    }

    private byte[] encodeBody(Object body){
        return JSONObject.toJSONString(body).getBytes(StandardCharsets.UTF_8);
    }
}