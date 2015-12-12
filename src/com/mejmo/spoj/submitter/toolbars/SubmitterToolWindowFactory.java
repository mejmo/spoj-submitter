package com.mejmo.spoj.submitter.toolbars;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.mejmo.spoj.submitter.Configuration;
import com.mejmo.spoj.submitter.dialogs.ChooseLanguageDialog;
import com.mejmo.spoj.submitter.domain.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by MFO on 11.12.2015.
 */
public class SubmitterToolWindowFactory implements ToolWindowFactory {

    private JPanel myToolWindowContent;
    private JTable resultsTable;
    private JButton submitBtn;
    private JButton settingsBtn;
    private JLabel lblChooseProblem;
    private JLabel lblChooseLanguage;
    private JProgressBar progressBar1;
    private JLabel lblProblem;
    private JLabel lblLanguage;

    private ToolWindow myToolWindow;

    private static final Logger logger = LoggerFactory.getLogger(SubmitterToolWindowFactory.class);

    class CustomRenderer extends DefaultTableCellRenderer
    {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value.toString().equalsIgnoreCase("accepted"))
                c.setForeground(Color.GREEN);

            if (value.toString().equalsIgnoreCase("wrong answer"))
                c.setForeground(Color.RED);

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
        if (Configuration.getLanguageName() == null)
            lblLanguage.setText("<not set>");
        else
            lblLanguage.setText(Configuration.getLanguageName());
        if (Configuration.getProblemName() == null)
            lblProblem.setText("<not set>");
        else
            lblProblem.setText(Configuration.getProblemId());

    }

    public SubmitterToolWindowFactory() {

        Configuration.readConfiguration();
        createJobsTable();
        updateLabels();

        lblChooseLanguage.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logger.debug("Creating choose language dialog");

                ChooseLanguageDialog dialog = new ChooseLanguageDialog(null);
                dialog.show();

                if (dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
                    Language lang = dialog.getResult();
                    Configuration.setLanguageName(lang.getValue());
                    Configuration.setLanguageId(lang.getId());
                    Configuration.save();
                    updateLabels();
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
        });

    }


    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {

        myToolWindow = toolWindow;
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(myToolWindowContent, "", false);
        toolWindow.getContentManager().addContent(content);

    }
}

//                Project project = event.getData(PlatformDataKeys.PROJECT);
//                String txt = Messages.showInputDialog(project, "What is your name?", "Input your name", Messages.getQuestionIcon());
//                Messages.showMessageDialog(project, "Hello, " + txt + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());
//                Messages.
