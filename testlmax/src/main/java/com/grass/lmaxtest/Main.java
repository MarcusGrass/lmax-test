package com.grass.lmaxtest;

import com.grass.lmaxtest.jobproducer.Job;
import com.grass.lmaxtest.jobproducer.JobEvent;
import com.grass.lmaxtest.jobproducer.JobProducer;
import com.grass.lmaxtest.requestsender.RequestSender;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import java.util.concurrent.ThreadFactory;

public class Main {

    public static void main(String[] args) {
        ThreadFactory threadFactory = DaemonThreadFactory.INSTANCE;

        WaitStrategy waitStrategy = new BusySpinWaitStrategy();

        Disruptor<JobEvent> disruptor = new Disruptor<>(
                JobEvent::new,
                8,
                threadFactory, ProducerType.SINGLE,
                waitStrategy
        );

        int numHandlers = 5;
        RequestSender[] handlers = new RequestSender[numHandlers];
        for (int i = 0; i < numHandlers; i++) {
            handlers[i] = new RequestSender();
        }

        disruptor.handleEventsWith(handlers);

        RingBuffer<JobEvent> ringBuffer = disruptor.start();

        int i = 0;
        while(true) {
            long sequenceId = ringBuffer.next();
            try {
                JobEvent newEvent = ringBuffer.get(sequenceId);

                Job toStart = JobProducer.getNextJob();
                newEvent.set(toStart);
            } finally {
                ringBuffer.publish(sequenceId);
            }
        }
    }
}
