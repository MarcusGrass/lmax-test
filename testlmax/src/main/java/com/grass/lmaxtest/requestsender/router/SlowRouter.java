package com.grass.lmaxtest.requestsender.router;

import com.grass.lmaxtest.jobproducer.Job;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import sun.plugin.dom.exception.InvalidStateException;

public class SlowRouter extends AbstractRouter {

    public SlowRouter(RouterCodes type) {
        super(type);
    }

    @Override
    public HttpUriRequest getPreparedRequest(Job job) {
        System.out.println("Slow Router processing job with state=" + job.getState());
        switch(job.getState()) {
            case NEW:
                return generateNewStateGet();
            case RUNNING:
                return generateRunningStateGet();
            default:
                throw new InvalidStateException("Invalid jobstate");
        }
    }

    private HttpUriRequest generateNewStateGet() {
        try {
            Thread.sleep(3000);
            return new HttpGet(firstEndPoint);
        } catch (Exception e) {
            throw new RuntimeException("Thread sleep or runtime exception=" + e.getMessage());
        }
    }

    private HttpUriRequest generateRunningStateGet() {
        try {
            Thread.sleep(4000);
            return new HttpGet(secondEndPoint);
        } catch (Exception e) {
            throw new RuntimeException("Thread sleep or runtime exception");
        }
    }

    @Override
    public String getName() {
        return RouterCodes.SLOW_ROUTER.name();
    }
}
