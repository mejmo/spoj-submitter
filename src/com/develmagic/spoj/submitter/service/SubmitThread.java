package com.develmagic.spoj.submitter.service;

import com.develmagic.spoj.submitter.domain.JobInfo;
import com.develmagic.spoj.submitter.domain.SubmitResult;
import com.develmagic.spoj.submitter.exceptions.SPOJSubmitterException;
import com.develmagic.spoj.submitter.toolbars.SubmitterToolWindowFactory;

import javax.swing.*;

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

            disableButtons(true);
            showProgressBar(true);
            setProgressBarValue(30);
            setLabelText("Logging in ...");
            SpojService.getInstance().login(jobInfo);
            setProgressBarValue(60);
            setLabelText("Submitting solution ...");
            SpojService.getInstance().submitSolution(jobInfo);
            setProgressBarValue(80);
            setLabelText("Getting result ...");
            SubmitResult result = SpojService.getInstance().getSubmitResult(jobInfo, new StatusChangeListener() {
                @Override
                public void changeStatus(String value) {
                    try {
                        setLabelText(value);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            });
            submitterToolWindowFactory.addResult(result);
            setLabelText("Result: " + result.getStatus());

        } catch (SPOJSubmitterException ex) {
            setLabelText("Error while submitting solution");
        }

        disableButtons(false);
        showProgressBar(false);
        showStatusLabel(true);

    }

    private void disableButtons(final boolean b) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                submitterToolWindowFactory.getSubmitBtn().setEnabled(!b);
            }
        });

    }

    private void setProgressBarValue(final int value) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                submitterToolWindowFactory.getProgressBar().setValue(value);
            }
        });
    }

    private void setLabelText(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                submitterToolWindowFactory.setLabelText(text);
            }
        });
    }

    private void showProgressBar(final boolean visible) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                submitterToolWindowFactory.getProgressBar().setVisible(visible);
            }
        });
    }

    private void showStatusLabel(final boolean visible) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                submitterToolWindowFactory.getStatusLabel().setVisible(visible);
            }
        });
    }

    public JobInfo getJobInfo() {
        return jobInfo;
    }

    public void setJobInfo(JobInfo jobInfo) {
        this.jobInfo = jobInfo;
    }
}
