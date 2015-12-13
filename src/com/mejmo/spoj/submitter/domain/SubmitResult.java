package com.mejmo.spoj.submitter.domain;

/**
 * Created by mejmo on 12/13/15.
 */
public class SubmitResult {
    private String submitId;
    private String mem;
    private String time;
    private String status;

    public SubmitResult(String submitId, String mem, String time, String status) {
        this.submitId = submitId;
        this.mem = mem;
        this.time = time;
        this.status = status;
    }

    public String getSubmitId() {
        return submitId;
    }

    public void setSubmitId(String submitId) {
        this.submitId = submitId;
    }

    public String getMem() {
        return mem;
    }

    public void setMem(String mem) {
        this.mem = mem;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
