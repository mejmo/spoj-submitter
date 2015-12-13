package com.mejmo.spoj.submitter.toolbars.listeners;

import com.intellij.openapi.ui.Messages;
import com.mejmo.spoj.submitter.PluginPersistence;
import com.mejmo.spoj.submitter.toolbars.SubmitterToolWindowFactory;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by mejmo on 12/13/15.
 */
public class ChooseProblemListener implements MouseListener {

    private SubmitterToolWindowFactory submitterToolWindowFactory;

    public ChooseProblemListener(SubmitterToolWindowFactory submitterToolWindowFactory) {
        this.submitterToolWindowFactory = submitterToolWindowFactory;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        String problem = Messages.showInputDialog("Choose the problem id (ex. POKER)", "Problem id", Messages.getQuestionIcon());
        if (problem != null) {
            PluginPersistence.setProblemId(problem);
            PluginPersistence.save();
            submitterToolWindowFactory.updateLabels();
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