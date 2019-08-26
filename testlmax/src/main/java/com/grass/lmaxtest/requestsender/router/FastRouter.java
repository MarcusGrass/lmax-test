package com.grass.lmaxtest.requestsender.router;

import com.grass.lmaxtest.jobproducer.Job;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

public class FastRouter extends AbstractRouter {

    public FastRouter(RouterCodes type) {
        super(type);
    }

    @Override
    public HttpUriRequest getPreparedRequest(Job job) {
        System.out.println("Fast Router processing job with state=" + job.getState());
        switch(job.getState()) {
            case NEW:
                return generateNewStateGet();
            case RUNNING:
                return generateRunningStateGet();
            default:
                throw new IllegalStateException("Invalid jobstate for job=" + job);
        }
    }

    private HttpUriRequest generateNewStateGet() {
        return new HttpGet(firstEndPoint);
    }

    private HttpUriRequest generateRunningStateGet() {
        return new HttpGet(secondEndPoint);
    }

    @Override
    public String getName() {
        return RouterCodes.FAST_ROUTER.name();
    }
}
