package com.wanghui.shiyue.rpc.message;

import java.nio.charset.StandardCharsets;

public class Message {
    public static final byte[] LOGIC = "shiyue".getBytes(StandardCharsets.UTF_8);


    private byte[] logic;

    private byte[] body;

    private byte messageType;

    public enum MessageTyepe {
        REQUEST(1),
        RESPONSE(2);

        public final byte code;

        MessageTyepe(int code) {
            this.code = (byte)code;
        }

        public Byte getCode(){
            return this.code;
        }
    }
}