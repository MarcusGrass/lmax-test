package com.grass.lmaxtest.restendpoints;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class controller {
    private AtomicInteger completeCount = new AtomicInteger();
    private LocalDateTime startTime;
    private LocalDateTime lastAnnouncement = LocalDateTime.now();
    private boolean clockStarted = false;

    @GetMapping("/stage_one")
    public String getStageOneConfirmation() {
        if (!clockStarted && completeCount.get() == 0) {
            System.out.println("Starting clock.");
            clockStarted = true;
            startTime = LocalDateTime.now();
        }
        return "{ \"response\": \"stage one complete.\" }";
    }

    @GetMapping("/stage_two")
    public String getStageTwoConfirmation() {
        completeCount.incrementAndGet();
        announceJobsPerSecond();
        return "{ \"response\": \"stage two complete.\" }";
    }

    private void announceJobsPerSecond() {
        LocalDateTime now = LocalDateTime.now();
        if (now.minusSeconds(5).isAfter(lastAnnouncement)) {
            int completed = completeCount.get();
            long seconds = startTime.until(now, ChronoUnit.SECONDS);
            float perSec = (float)completed/seconds;
            System.out.println("Has completed " + completed + " jobs in " + seconds + " seconds. " +
                    "Averaging " + perSec + " completed jobs per second.");
            lastAnnouncement = now;
        }
    }
}
