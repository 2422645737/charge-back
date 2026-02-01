package com.wanghui.shiyue.rpc;

import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Consumer consumer = new Consumer();
        System.out.println(consumer.add(1, 2));
        System.out.println(consumer.add(1, 20));
    }
}