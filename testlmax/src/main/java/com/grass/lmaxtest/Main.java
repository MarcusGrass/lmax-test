package com.grass.lmaxtest;

public class Main {

    public static void main(String[] args) {
        RequestDisruptor disruptor = RequestDisruptor.init();
        disruptor.start();
    }
}
