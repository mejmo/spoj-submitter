package com.mejmo.spoj.submitter.toolbars.listeners;

import com.intellij.diff.DiffManager;
import com.intellij.diff.chains.DiffRequestChain;
import com.intellij.diff.contents.DiffContent;
import com.intellij.diff.requests.ContentDiffRequest;
import com.intellij.diff.requests.DiffRequest;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.openapi.Disposable;
import com.intellij.diff.DiffRequestFactory;
import com.intellij.diff.contents.SimpleContentBuilder;
import com.intellij.diff.DiffContentFactory;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.mejmo.spoj.submitter.Utils;
import com.mejmo.spoj.submitter.exceptions.SPOJSubmitterException;
import com.mejmo.spoj.submitter.service.SpojService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

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
