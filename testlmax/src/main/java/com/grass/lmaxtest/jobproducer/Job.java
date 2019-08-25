package com.grass.lmaxtest.jobproducer;

import com.grass.lmaxtest.requestsender.ResponseModel;
import com.grass.lmaxtest.requestsender.router.RouterCodes;

import java.util.Objects;

public class Job {
    private RouterCodes assignedRouter;
    private JobState state;
    private ResponseModel lastResponse;
    private final int jobNumber;

    public Job(JobState state, RouterCodes assignedRouter, ResponseModel lastResponse, int jobNumber) {
        this.state = state;
        this.assignedRouter = assignedRouter;
        this.lastResponse = lastResponse;
        this.jobNumber = jobNumber;
    }

    public RouterCodes getAssignedRouter() {
        return assignedRouter;
    }

    public JobState getState() {
        return state;
    }

    public void setState(JobState state) {
        this.state = state;
    }

    public ResponseModel getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(ResponseModel lastResponse) {
        this.lastResponse = lastResponse;
    }

    public int getJobNumber() {
        return jobNumber;
    }

    public boolean isNew() {
        return this.getState() == JobState.NEW && this.lastResponse == null;
    }

    public void cloneFromOther(Job that) {
        this.state = that.state;
        this.lastResponse = that.lastResponse;
        this.assignedRouter = that.assignedRouter;
    }

    public Job copy() {
        return new Job(state, assignedRouter, lastResponse, jobNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Job job = (Job) o;
        return assignedRouter == job.assignedRouter && state == job.state && Objects.equals(lastResponse,
                job.lastResponse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assignedRouter, state, lastResponse);
    }

    @Override
    public String toString() {
        return "Job{" + "assignedRouter=" + assignedRouter + ", state=" + state + ", lastResponse='" + lastResponse + '\'' + '}';
    }
}
