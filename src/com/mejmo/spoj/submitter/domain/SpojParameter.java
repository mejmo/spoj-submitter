package com.mejmo.spoj.submitter.domain;

/**
 * Created by mejmo on 12/12/15.
 */
public abstract class SpojParameter {
    private String id;
    private String value;

    public SpojParameter(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
