package com.grass.lmaxtest.requestsender.router;

import com.grass.lmaxtest.jobproducer.Job;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Delegator {
    private static final FastRouter fastRouter = new FastRouter(RouterCodes.FAST_ROUTER);
    private static final SlowRouter slowRouter = new SlowRouter(RouterCodes.SLOW_ROUTER);
    private static final ExecutorService SLOW_ROUTER_POOL = Executors.newFixedThreadPool(10);
    private static final ExecutorService FAST_ROUTER_POOL = Executors.newFixedThreadPool(10);

    public AbstractRouter getAssignedRouter(Job job) {
        switch(job.getAssignedRouter()) {
            case FAST_ROUTER:
                return fastRouter;
            case SLOW_ROUTER:
                return slowRouter;
            default:
                throw new IllegalStateException("Unrecognized router code");
        }
    }

    public ExecutorService getAssignedThreadExecutor(AbstractRouter router) {
        switch (router.getType()) {
            case FAST_ROUTER:
                return FAST_ROUTER_POOL;
            case SLOW_ROUTER:
                return SLOW_ROUTER_POOL;
            default:
                throw new IllegalStateException("Unrecognized router code=" + router.getType());
        }
    }
}
