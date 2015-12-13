package com.mejmo.spoj.submitter.toolbars.renderer;

import com.intellij.ui.components.panels.HorizontalLayout;
import com.mejmo.spoj.submitter.toolbars.listeners.ResultsMouseAdapter;
import org.intellij.lang.annotations.JdkConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Martin Formanko 2015
 */
public class DiffLabelRenderer extends DefaultTableCellRenderer
{

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        JLabel diffLabel  = new JLabel("<html><u>View</u></html>");
        diffLabel.setHorizontalAlignment(SwingConstants.CENTER);
        diffLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return diffLabel;
    }

}