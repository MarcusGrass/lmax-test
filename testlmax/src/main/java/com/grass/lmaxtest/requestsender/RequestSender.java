package com.grass.lmaxtest.requestsender;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grass.lmaxtest.jobproducer.Job;
import com.grass.lmaxtest.jobproducer.JobEvent;
import com.grass.lmaxtest.jobproducer.JobProducer;
import com.grass.lmaxtest.requestsender.router.AbstractRouter;
import com.grass.lmaxtest.requestsender.router.Delegator;
import com.lmax.disruptor.EventHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import java.util.Collections;
import java.util.concurrent.*;

public class RequestSender implements EventHandler<JobEvent> {
    private static final Delegator DELEGATOR = new Delegator();
    private static final HttpClient CLIENT = HttpClientBuilder.create().build();
    private static final ObjectMapper JSON = new ObjectMapper();

    @Override
    public void onEvent(JobEvent jobEvent, long l, boolean b) throws Exception {
        Job job = jobEvent.getJob();
        AbstractRouter router = DELEGATOR.getAssignedRouter(job);
        if (router.isComplete(job)) {
            router.collectData(job);
            jobEvent.clear();
        } else {
            try {
                ExecutorService executorService = DELEGATOR.getAssignedThreadExecutor(router);
                executorService.submit(new AsyncRequestRunner(router, CLIENT, job, JSON));
            } catch (RejectedExecutionException e) {
                // New jobs for the slow router sleep if awaiting execution.
                Thread.sleep(1000);
            }

        }
    }
}
