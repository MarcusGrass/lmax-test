package com.grass.lmaxtest.jobproducer;

public class JobEvent {
    private Job job;

    public Job getJob() {
        return job;
    }

    public void set(Job job) {
        this.job = job;
    }

    public void clear() {
        job = null;
    }

    public boolean isEmpty() {
        return job == null;
    }
}
