package com.mejmo.spoj.submitter;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Created by MFO on 11.12.2015.
 */
public class SubmitterToolWindowFactory implements ToolWindowFactory {

    private JPanel myToolWindowContent;
    private JTable resultsTable;
    private JButton submitBtn;
    private JButton settingsBtn;
    private JProgressBar progressBar1;

    private ToolWindow myToolWindow;

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

    public SubmitterToolWindowFactory() {
//        hideToolWindowButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                myToolWindow.hide(null);
//            }
//        });
//        refreshToolWindowButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                SubmitterToolWindowFactory.this.currentDateTime();
//            }
//        });

//        JTableHeader header = new JTableHeader();
//        TableColumn statusColumn = new TableColumn();
//        TableColumn timeColumn = new TableColumn();
//        TableColumn problemColumn = new TableColumn();
//        TableColumn memColumn = new TableColumn();
//        TableColumn langColumn = new TableColumn();
//
//        problemColumn.setHeaderValue("Problem");
//        statusColumn.setHeaderValue("Status");
//        langColumn.setHeaderValue("Lang");
//        timeColumn.setHeaderValue("Time");
//        memColumn.setHeaderValue("Mem");
//
//        header.getColumnModel().addColumn(statusColumn);
//        header.getColumnModel().addColumn(problemColumn);
//        header.getColumnModel().addColumn(langColumn);
//        header.getColumnModel().addColumn(timeColumn);
//        header.getColumnModel().addColumn(memColumn);

//        resultsTable.setTableHeader(header);

//        resultsTable.setDefaultRenderer(String.class, new CustomRenderer());

//        DefaultTableModel model = (DefaultTableModel) resultsTable.getModel();

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Problem", "Status", "Lang", "Time", "Mem"}, 0);

        model.addRow(new Object[]{"accepted", "POKER", "python", "0.2", "7MB"});

        resultsTable.setModel(model);
        resultsTable.getColumnModel().getColumn(1).setCellRenderer(new CustomRenderer());



    }

    // Create the tool window content.
    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {

        myToolWindow = toolWindow;

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(myToolWindowContent, "", false);
        toolWindow.getContentManager().addContent(content);

    }

//    public void currentDateTime() {
//        // Get current date and time
//        Calendar instance = Calendar.getInstance();
//        currentDate.setText(String.valueOf(instance.get(Calendar.DAY_OF_MONTH)) + "/"
//                + String.valueOf(instance.get(Calendar.MONTH) + 1) + "/" + String.valueOf(instance.get(Calendar.YEAR)));
//        currentDate.setIcon(new ImageIcon(getClass().getResource("resources/myToolWindow/Calendar-icon.png")));
//        int min = instance.get(Calendar.MINUTE);
//        String strMin;
//        if (min < 10) {
//            strMin = "0" + String.valueOf(min);
//        } else {
//            strMin = String.valueOf(min);
//        }
//        currentTime.setText(instance.get(Calendar.HOUR_OF_DAY) + ":" + strMin);
//        currentTime.setIcon(new ImageIcon(getClass().getResource("resources/myToolWindow/Time-icon.png")));
//        // Get time zone
//        long gmt_Offset = instance.get(Calendar.ZONE_OFFSET); // offset from GMT in milliseconds
//        String str_gmt_Offset = String.valueOf(gmt_Offset / 3600000);
//        str_gmt_Offset = (gmt_Offset > 0) ? "GMT + " + str_gmt_Offset : "GMT - " + str_gmt_Offset;
//        timeZone.setText(str_gmt_Offset);
//        timeZone.setIcon(new ImageIcon(getClass().getResource("resources/myToolWindow/Time-zone-icon.png")));
//
//
//    }

}
