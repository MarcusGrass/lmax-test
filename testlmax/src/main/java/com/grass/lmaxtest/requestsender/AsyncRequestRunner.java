package com.grass.lmaxtest.requestsender;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grass.lmaxtest.jobproducer.Job;
import com.grass.lmaxtest.jobproducer.JobProducer;
import com.grass.lmaxtest.requestsender.router.AbstractRouter;
import com.grass.lmaxtest.requestsender.ResponseModel;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

public class AsyncRequestRunner implements Callable<Object> {
    private final AbstractRouter router;
    private final HttpClient client;
    private final Job job;
    private final ObjectMapper json;

    public AsyncRequestRunner(AbstractRouter router, HttpClient client, Job job, ObjectMapper json) {
        super();
        this.router = router;
        this.client = client;
        this.job = job;
        this.json = json;
    }

    @Override
    public Object call() throws Exception {
        try {
            HttpUriRequest request = router.getPreparedRequest(job);
            HttpResponse response = client.execute(request);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),
                    StandardCharsets.UTF_8));

            StringBuilder responseBuilder = new StringBuilder();
            String inputString;
            while ((inputString = reader.readLine()) != null) {
                responseBuilder.append(inputString);
            }
            ResponseModel model = json.readValue(responseBuilder.toString(), ResponseModel.class);
            job.setLastResponse(model);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception in thread.");
        }
        return new Object();
    }
}
