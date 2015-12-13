package com.mejmo.spoj.submitter.toolbars;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.mejmo.spoj.submitter.PluginPersistence;
import com.mejmo.spoj.submitter.domain.SubmitResult;
import com.mejmo.spoj.submitter.toolbars.listeners.ChooseProblemListener;
import com.mejmo.spoj.submitter.toolbars.listeners.ShowSettingsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by MFO on 11.12.2015.
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

    private ToolWindow myToolWindow;

    private static final Logger logger = LoggerFactory.getLogger(SubmitterToolWindowFactory.class);

    class CustomRenderer extends DefaultTableCellRenderer
    {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            switch (value.toString()) {
                case "accepted":
                    c.setForeground(Color.GREEN);
                    break;
                case "wrong answer":
                    c.setForeground(Color.RED);
                    break;
                default:
                    break;
                //TODO! How to get the default color of IDEA ?
//                    c.setForeground(Color.LIGHT_GRAY);
            }
            return c;
        }
    }

    public void getAvailableLanguages() {



    }

    public void createJobsTable() {

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Status", "Lang", "Time", "Mem"}, 0);
        model.addRow(new Object[]{"accepted", "python", "0.2", "7MB"});
        resultsTable.setModel(model);
        resultsTable.getColumnModel().getColumn(0).setCellRenderer(new CustomRenderer());

    }

    public void updateLabels() {

        lblChooseLanguage.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblChooseProblem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (PluginPersistence.getLanguageName() == null)
            lblLanguage.setText("<not set>");
        else
            lblLanguage.setText(PluginPersistence.getLanguageName());
        if (PluginPersistence.getProblemId() == null)
            lblProblem.setText("<not set>");
        else
            lblProblem.setText(PluginPersistence.getProblemId());

    }

    public void setActions() {

        lblChooseLanguage.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

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
        });

        lblChooseProblem.addMouseListener(new ChooseProblemListener(this));
        btnSettings.addActionListener(new ShowSettingsListener(this));

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

    }

    public void addResult(SubmitResult submitResult) {

        ((DefaultTableModel)resultsTable.getModel()).insertRow(0,
                new Object[] {
                        submitResult.getStatus(),
                        PluginPersistence.getLanguageName(),
                        submitResult.getMem(),
                        submitResult.getTime()
                });
        if (resultsTable.getModel().getRowCount() > 20) {
            ((DefaultTableModel) resultsTable.getModel()).removeRow(resultsTable.getRowCount()-1);
        }
//        PluginPersistence.

    }

    public SubmitterToolWindowFactory() {

        PluginPersistence.readConfiguration();
        createJobsTable();
        updateLabels();
        setActions();

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


}

