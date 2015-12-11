package com.mejmo.spoj.submitter;

import org.junit.Test;

/**
 * Created by MFO on 11.12.2015.
 */
public class SpojSubmitterTest {

    @Test
    public void testLogin() {
        SpojSubmitter spojSubmitter = new SpojSubmitter();
        spojSubmitter.login();
        spojSubmitter.submitSolution();
        spojSubmitter.getSubmitResult();
    }

}
