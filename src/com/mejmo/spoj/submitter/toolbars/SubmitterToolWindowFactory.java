package com.mejmo.spoj.submitter.toolbars;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.mejmo.spoj.submitter.PluginPersistence;
import com.mejmo.spoj.submitter.domain.LanguageInfo;
import com.mejmo.spoj.submitter.domain.ResultsDataBean;
import com.mejmo.spoj.submitter.domain.SubmitResult;
import com.mejmo.spoj.submitter.toolbars.listeners.*;
import com.mejmo.spoj.submitter.toolbars.renderer.DiffLabelRenderer;
import com.mejmo.spoj.submitter.toolbars.renderer.StatusRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The main toolbar
 *
 * @author Martin Formanko 2015
 */
public class SubmitterToolWindowFactory implements ToolWindowFactory {

    private JPanel myToolWindowContent;
    private JTable resultsTable;
    private JButton btnSubmit;
    private JButton btnSettings;
    private JLabel lblChooseProblem;
    private JLabel lblChooseLanguage;
    private JProgressBar progressBar;
    private JLabel lblProblem;
    private JLabel lblLanguage;
    private JLabel lblStatus;
    private ResultsDataBean resultsData;

    private ToolWindow myToolWindow;

    private static final Logger logger = LoggerFactory.getLogger(SubmitterToolWindowFactory.class);

    public void updateJobsTable() {

        ResultsDataBean resultsDataBean = PluginPersistence.loadProblemResults(PluginPersistence.getProblemId());
        this.resultsData = resultsDataBean;
        bindToBean(resultsDataBean);
        resultsTable.getColumnModel().getColumn(0).setCellRenderer(new StatusRenderer());
        resultsTable.getColumnModel().getColumn(3).setCellRenderer(new DiffLabelRenderer());
        resultsTable.addMouseListener(new ResultsMouseAdapter());

    }

    /**
     * JTable does not have BeanModel..
     * @param resultsDataBean
     */
    private void bindToBean(ResultsDataBean resultsDataBean) {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Status", "Time", "Mem", "Diff"}, 0);
        for (SubmitResult submitResult : resultsDataBean.getResults()) {
            model.addRow(new Object[] { submitResult.getStatus(), submitResult.getTime(), submitResult.getMem(), submitResult.getSubmitId()});
        }
        resultsTable.setModel(model);
    }

    public void updateLabels() {

        lblChooseLanguage.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblChooseProblem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblLanguage.setText(PluginPersistence.getLanguageName() == null ? "<not set>": PluginPersistence.getLanguageName());
        lblProblem.setText(PluginPersistence.getProblemId() == null ? "<not set>": PluginPersistence.getProblemId());

    }

    public void setListeners() {

        lblChooseLanguage.addMouseListener(new ChooseLanguageListener(this));
        lblChooseProblem.addMouseListener(new ChooseProblemListener(this));
        btnSettings.addActionListener(new ShowSettingsListener(this));
        btnSubmit.addActionListener(new SubmitListener(this));

    }

    public void addResult(SubmitResult submitResult) {

        resultsData.getResults().add(0, new SubmitResult(
                submitResult.getSubmitId(),
                submitResult.getMem(),
                submitResult.getTime(),
                submitResult.getStatus(),
                new LanguageInfo(PluginPersistence.getLanguageId(), PluginPersistence.getLanguageName())
        ));
        PluginPersistence.saveProblemResults(PluginPersistence.getProblemId(), resultsData);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                updateJobsTable();
            }
        });

    }

    public SubmitterToolWindowFactory() {

        PluginPersistence.readConfiguration();
        updateJobsTable();
        updateLabels();
        setListeners();

    }

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {

        myToolWindow = toolWindow;
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(myToolWindowContent, "", false);
        toolWindow.getContentManager().addContent(content);

    }

    public JButton getSubmitBtn() {
        return btnSubmit;
    }

    public JLabel getStatusLabel() {
        return lblStatus;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setLabelText(String value) {
        lblStatus.setText(value);
    }

}

