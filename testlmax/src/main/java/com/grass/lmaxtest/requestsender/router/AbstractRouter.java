package com.grass.lmaxtest.requestsender.router;
import com.grass.lmaxtest.jobproducer.Job;
import com.grass.lmaxtest.jobproducer.JobState;
import org.apache.http.client.methods.HttpUriRequest;

public abstract class AbstractRouter {
    protected final String firstEndPoint = "http://127.0.0.1:8080/stage_one";
    protected final String secondEndPoint = "http://127.0.0.1:8080/stage_two";
    private final RouterCodes type;

    public AbstractRouter(RouterCodes type) {
        this.type = type;
    }

    public abstract HttpUriRequest getPreparedRequest(Job job);
    public abstract String getName();

    public boolean isComplete(Job job) {
        if (job.getLastResponse() == null) {
            return false;
        }
        if (job.getState() == JobState.COMPLETED) {
            return true;
        }
        if (job.getLastResponse().getResponse().equals("stage one complete.")) {
            job.setState(JobState.RUNNING);
            return false;
        }
        return false;
    }

    public void collectData(Job job) {
        job.setState(JobState.COMPLETED);
        System.out.println(getName() + " completed job=" + job);
    }

    public RouterCodes getType() {
        return type;
    }

}
