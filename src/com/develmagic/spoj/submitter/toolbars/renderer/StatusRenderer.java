package com.develmagic.spoj.submitter.toolbars.renderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Martin Formanko 2015
 */
public class StatusRenderer extends DefaultTableCellRenderer
{
    private Color originalColor;

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        //How to get IDEA foreground default color?
        if (originalColor == null)
            originalColor = c.getForeground();

        switch (value.toString()) {
            case "accepted":
                c.setForeground(Color.GREEN);
                break;
            case "wrong answer":
                c.setForeground(Color.RED);
                break;
            default:
                c.setForeground(originalColor);
                break;
        }
        return c;
    }
}