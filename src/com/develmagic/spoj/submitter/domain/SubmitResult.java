package com.develmagic.spoj.submitter.domain;

import java.io.Serializable;

/**
 * @author Martin Formanko 2015
 */
public class SubmitResult implements Serializable {
    private String submitId;
    private String mem;
    private String time;
    private String status;
    private LanguageInfo language;

    public SubmitResult() {

    }

    public SubmitResult(String submitId, String mem, String time, String status, LanguageInfo language) {
        this.submitId = submitId;
        this.mem = mem;
        this.time = time;
        this.status = status;
        this.language = language;
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

    public LanguageInfo getLanguage() {
        return language;
    }

    public void setLanguage(LanguageInfo language) {
        this.language = language;
    }
}
