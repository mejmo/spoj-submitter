package com.mejmo.spoj.submitter.toolbars.listeners;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.mejmo.spoj.submitter.PluginPersistence;
import com.mejmo.spoj.submitter.Utils;
import com.mejmo.spoj.submitter.domain.JobInfo;
import com.mejmo.spoj.submitter.domain.LanguageInfo;
import com.mejmo.spoj.submitter.domain.ProblemInfo;
import com.mejmo.spoj.submitter.service.SubmitThread;
import com.mejmo.spoj.submitter.toolbars.SubmitterToolWindowFactory;
import org.slf4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Martin Formanko 2015
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

//        btnSubmit.setEnabled(false);

        Project[] openedProjects = ProjectManager.getInstance().getOpenProjects();

        if (openedProjects.length != 1) {
            logger.error("Cannot get current opened project");
            Utils.showError("Cannot get current opened project");
            return;
        }

        FileEditorManager editorManager = FileEditorManager.getInstance(openedProjects[0]);
        Editor editor = editorManager.getSelectedTextEditor();
        String code = editor.getDocument().getText();

        JobInfo jobInfo = new JobInfo();
        jobInfo.setUsername(PluginPersistence.getUsername());
        jobInfo.setPassword(PluginPersistence.getPassword());
        jobInfo.setProblem(new ProblemInfo(PluginPersistence.getProblemId(), PluginPersistence.getProblemName()));
        jobInfo.setLanguage(new LanguageInfo(PluginPersistence.getLanguageId(), PluginPersistence.getLanguageName()));
        jobInfo.setSolution(code);

        Thread submitThread = new Thread(new SubmitThread(jobInfo, submitterToolWindowFactory));
        logger.debug("Starting submit thread");
        submitThread.start();

    }
}
