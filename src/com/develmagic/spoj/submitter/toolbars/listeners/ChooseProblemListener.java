package com.develmagic.spoj.submitter.toolbars.listeners;

import com.develmagic.spoj.submitter.PluginPersistence;
import com.intellij.openapi.ui.Messages;
import com.develmagic.spoj.submitter.toolbars.SubmitterToolWindowFactory;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Martin Formanko 2015
 */
public class ChooseProblemListener implements MouseListener {

    private SubmitterToolWindowFactory submitterToolWindowFactory;

    public ChooseProblemListener(SubmitterToolWindowFactory submitterToolWindowFactory) {
        this.submitterToolWindowFactory = submitterToolWindowFactory;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        String problem = Messages.showInputDialog("Choose the problem id (ex. POKER)", "ProblemInfo id", Messages.getQuestionIcon());
        if (problem != null) {
            PluginPersistence.setProblemId(problem);
            PluginPersistence.save();
            submitterToolWindowFactory.updateLabels();
            submitterToolWindowFactory.updateJobsTable();
            submitterToolWindowFactory.getStatusLabel().setVisible(false);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
