package com.grass.lmaxtest.jobproducer;
import com.grass.lmaxtest.requestsender.router.RouterCodes;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class JobProducer {
    private static final AtomicInteger JOB_COUNT = new AtomicInteger();

    private JobProducer() {

    }

    public static Job generateNewJob() {
        int count = JOB_COUNT.incrementAndGet();
        if (count % 2 == 0) {
            return new Job(JobState.NEW, RouterCodes.FAST_ROUTER, null, count);
        } else {
            return new Job(JobState.NEW, RouterCodes.SLOW_ROUTER, null, count);
        }
    }
}
