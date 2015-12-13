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
import com.mejmo.spoj.submitter.toolbars.listeners.ChooseLanguageListener;
import com.mejmo.spoj.submitter.toolbars.listeners.ChooseProblemListener;
import com.mejmo.spoj.submitter.toolbars.listeners.ShowSettingsListener;
import com.mejmo.spoj.submitter.toolbars.listeners.SubmitListener;
import com.mejmo.spoj.submitter.toolbars.renderer.CustomRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

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
        resultsTable.getColumnModel().getColumn(0).setCellRenderer(new CustomRenderer());

    }

    /**
     * JTable does not have BeanModel..
     * @param resultsDataBean
     */
    private void bindToBean(ResultsDataBean resultsDataBean) {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Status", "Lang", "Time", "Mem"}, 0);
        for (SubmitResult submitResult : resultsDataBean.getResults()) {
            model.addRow(new Object[] { submitResult.getStatus(), submitResult.getLanguage().getValue(), submitResult.getTime(), submitResult.getMem()});
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
        updateJobsTable();

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

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setLabelText(String value) {
        lblStatus.setText(value);
    }

}

