package com.mejmo.spoj.submitter.toolbars.listeners;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.mejmo.spoj.submitter.Utils;
import com.mejmo.spoj.submitter.exceptions.SPOJSubmitterException;
import com.mejmo.spoj.submitter.service.SpojService;
import com.mejmo.spoj.submitter.toolbars.SubmitterToolWindowFactory;
import org.slf4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mejmo on 12/13/15.
 */
public class SubmitListener implements ActionListener {

    private SubmitterToolWindowFactory submitterToolWindowFactory;
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(SubmitListener.class);

    public SubmitListener(SubmitterToolWindowFactory submitterToolWindowFactory) {
        this.submitterToolWindowFactory = submitterToolWindowFactory;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.info("Solution submit requested");

//                btnSubmit.setEnabled(false);
        try {

            Project[] openedProjects = ProjectManager.getInstance().getOpenProjects();

            if (openedProjects.length != 1) {
                logger.error("Cannot get current opened project");
                Utils.showError("Cannot get current opened project");
                return;
            }

            FileEditorManager editorManager = FileEditorManager.getInstance(openedProjects[0]);
            Editor editor = editorManager.getSelectedTextEditor();
            String code = editor.getDocument().getText();

            submitterToolWindowFactory.getProgressBar().setVisible(true);
            submitterToolWindowFactory.getProgressBar().setValue(30);
//                    SpojService.getInstance().login();
            submitterToolWindowFactory.getProgressBar().setValue(60);
//                    SpojService.getInstance().submitSolution(code);
            submitterToolWindowFactory.getProgressBar().setValue(80);
            submitterToolWindowFactory.addResult(SpojService.getInstance().getSubmitResult());
            submitterToolWindowFactory.getProgressBar().setVisible(false);

        } catch (SPOJSubmitterException ex) {

        }
    }
}
