package com.grass.lmaxtest.jobproducer;
import com.grass.lmaxtest.requestsender.router.RouterCodes;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class JobProducer {
    private static final Queue<Job> JOB_QUEUE = new LinkedBlockingQueue<>();
    private static final AtomicInteger JOB_COUNT = new AtomicInteger();
    private static final AtomicInteger COMPLETED_JOBS = new AtomicInteger();
    private static final LocalDateTime START_TIME = LocalDateTime.now();


    private JobProducer() {

    }

    public static void pushToQueue(Job job) {
        if (!JOB_QUEUE.add(job)) {
            throw new RuntimeException("Failed to push job=" + job + " to queue.");
        }
    }

    public static Job getNextJob() {
        if (JOB_QUEUE.isEmpty()) {
            System.out.println("Creating new job number " + JOB_COUNT.get());
            return generateNewJob();

        }
        return JOB_QUEUE.remove();
    }

    private static Job generateNewJob() {
        int count = JOB_COUNT.incrementAndGet();
        if (count % 2 == 0) {
            return new Job(JobState.NEW, RouterCodes.FAST_ROUTER, null, count);
        } else {
            return new Job(JobState.NEW, RouterCodes.SLOW_ROUTER, null, count);
        }
    }

    public static void announceCompletionStatistics() {
        int completed = COMPLETED_JOBS.incrementAndGet();
        long seconds = START_TIME.until(LocalDateTime.now(), ChronoUnit.SECONDS);
        float perSec = (float)completed/seconds;
        // System.out.println("Has completed " + completed + " jobs in " + seconds + " seconds. Averaging " + perSec + " jobs per second.");
    }
}
