package com.grass.lmaxtest;

import java.util.concurrent.ThreadFactory;

import com.grass.lmaxtest.jobproducer.Job;
import com.grass.lmaxtest.jobproducer.JobEvent;
import com.grass.lmaxtest.jobproducer.JobProducer;
import com.grass.lmaxtest.requestsender.RequestSender;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

public class RequestDisruptor {
	private final RingBuffer<JobEvent> ringBuffer;

	private RequestDisruptor() {
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

        ringBuffer = disruptor.start();
	}

	public static RequestDisruptor init() {
		return new RequestDisruptor();
	}

	public void start() {
		while(true) {
            long sequenceId = ringBuffer.next();
            try {
                JobEvent newEvent = ringBuffer.get(sequenceId);
                addNewJobIfEmptyEvent(newEvent);

            } finally {
                ringBuffer.publish(sequenceId);
            }
        }
	}

	private void addNewJobIfEmptyEvent(JobEvent event) {
		if (event.isEmpty()) {
			Job toStart = JobProducer.generateNewJob();
			event.set(toStart);
		}
	}
}
