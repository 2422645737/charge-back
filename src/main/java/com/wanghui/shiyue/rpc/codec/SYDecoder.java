package com.wanghui.shiyue.rpc.codec;

import cn.hutool.json.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.wanghui.shiyue.rpc.message.Message;
import com.wanghui.shiyue.rpc.message.Request;
import com.wanghui.shiyue.rpc.message.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SYDecoder extends LengthFieldBasedFrameDecoder {


    public SYDecoder() {
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

        if(Message.MessageTyepe.REQUEST.getCode().equals(messageType)){
            return deserializeRequest(body);
        }
        if(Message.MessageTyepe.RESPONSE.getCode().equals(messageType)){
            return deserializeResponse(body);
        }

        throw new IllegalArgumentException("消息类型不支持,messageType" + messageType);
    }

    private Response deserializeResponse(byte[] body) {
        return JSONObject.parseObject(new String(body, StandardCharsets.UTF_8), Response.class);
    }

    private Request deserializeRequest(byte[] body) {
        return JSONObject.parseObject(new String(body, StandardCharsets.UTF_8), Request.class);
    }
}