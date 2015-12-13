package com.mejmo.spoj.submitter.domain;

import com.fasterxml.jackson.databind.JsonSerializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Formanko 2015
 */
public class ResultsDataBean implements Serializable {
    private List<SubmitResult> results = new ArrayList<>();

    public List<SubmitResult> getResults() {
        return results;
    }

    public void setResults(List<SubmitResult> results) {
        this.results = results;
    }
}
