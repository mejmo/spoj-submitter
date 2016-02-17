package com.develmagic.spoj.submitter.toolbars.listeners;

import com.develmagic.spoj.submitter.Utils;
import com.intellij.diff.DiffManager;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.diff.DiffContentFactory;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.develmagic.spoj.submitter.service.SpojService;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by mejmo on 12/13/15.
 */
public class ResultsMouseAdapter extends MouseAdapter {

    @Override
    public void mouseClicked(MouseEvent e) {
        try {
            JTable target = (JTable)e.getSource();
            int row = target.getSelectedRow();
            int column = target.getSelectedColumn();
            if (column == 3) {
                String submitId = (String)target.getModel().getValueAt(row, column);
                if (submitId != null && submitId.trim().length() > 0) {
                    String code = SpojService.getInstance().getCodeForSubmit(submitId);
                    Project[] openedProjects = ProjectManager.getInstance().getOpenProjects();
                    FileEditorManager editorManager = FileEditorManager.getInstance(openedProjects[0]);
                    Editor editor = editorManager.getSelectedTextEditor();
                    String currentCode = editor.getDocument().getText();
                    DiffManager.getInstance().showDiff(openedProjects[0], new SimpleDiffRequest("XXX",
                            DiffContentFactory.getInstance().create(currentCode),
                            DiffContentFactory.getInstance().create(code),
                            "Editor file",
                            "Earlier submitted solution"));
                }
            }
        } catch (Throwable e1) {
            Utils.showError("Cannot get source code for the submission");
        }
    }

}
