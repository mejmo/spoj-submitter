package com.develmagic.spoj.submitter.domain;

import java.io.Serializable;

/**
 * @author Martin Formanko 2015
 */
public abstract class SpojParameter implements Serializable {
    private String id;
    private String value;

    public SpojParameter() {

    }

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

