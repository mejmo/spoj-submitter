package com.mejmo.spoj.submitter.service;

import com.mejmo.spoj.submitter.domain.JobInfo;
import com.mejmo.spoj.submitter.domain.SubmitResult;
import com.mejmo.spoj.submitter.exceptions.SPOJSubmitterException;
import com.mejmo.spoj.submitter.toolbars.SubmitterToolWindowFactory;

/**
 * @author Martin Formanko 2015
 */
public class SubmitThread implements Runnable {

    private JobInfo jobInfo;
    private SubmitterToolWindowFactory submitterToolWindowFactory;

    public SubmitThread(JobInfo jobInfo, SubmitterToolWindowFactory factory) {
        this.jobInfo = jobInfo;
        this.submitterToolWindowFactory = factory;
    }

    @Override
    public void run() {

        try {

            submitterToolWindowFactory.getProgressBar().setVisible(true);
            submitterToolWindowFactory.getProgressBar().setValue(30);
            submitterToolWindowFactory.setLabelText("Logging in ...");
            SpojService.getInstance().login(jobInfo);
            submitterToolWindowFactory.getProgressBar().setValue(60);
            submitterToolWindowFactory.setLabelText("Submitting solution ...");
            SpojService.getInstance().submitSolution(jobInfo);
            submitterToolWindowFactory.getProgressBar().setValue(80);
            submitterToolWindowFactory.setLabelText("Getting result ...");
            SubmitResult result = SpojService.getInstance().getSubmitResult(jobInfo, new StatusChangeListener() {
                @Override
                public void changeStatus(String value) {
                    submitterToolWindowFactory.setLabelText(value);
                }
            });
            submitterToolWindowFactory.addResult(result);
            submitterToolWindowFactory.setLabelText("Result: "+result.getStatus());
            submitterToolWindowFactory.getProgressBar().setVisible(false);

        } catch (SPOJSubmitterException ex) {
            submitterToolWindowFactory.setLabelText("Error while submitting solution");
        }

    }

    public JobInfo getJobInfo() {
        return jobInfo;
    }

    public void setJobInfo(JobInfo jobInfo) {
        this.jobInfo = jobInfo;
    }
}
