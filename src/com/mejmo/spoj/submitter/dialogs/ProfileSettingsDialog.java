package com.mejmo.spoj.submitter.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.mejmo.spoj.submitter.PluginPersistence;
import com.mejmo.spoj.submitter.exceptions.SPOJSubmitterException;
import com.mejmo.spoj.submitter.service.SpojService;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProfileSettingsDialog extends DialogWrapper {

    private JPanel contentPane;
    private JTextField textUsername;
    private JButton testButton;
    private JTextField textPassword;
    private JLabel lblTest;
    private JComboBox comboLanguages;

    public ProfileSettingsDialog(@Nullable Project project) {
        super(project);
        super.init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        textUsername.setText(PluginPersistence.getUsername());
        textPassword.setText(PluginPersistence.getPassword());

        testButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SpojService.getInstance().login(textUsername.getText(), textPassword.getText());
                    lblTest.setText("Login successful");
                    lblTest.setForeground(Color.GREEN);
                } catch (SPOJSubmitterException ex) {
                    lblTest.setText("Login failed!");
                    lblTest.setForeground(Color.RED);
                }
            }
        });

        return contentPane;

    }

    public String getUsername() {
        return textUsername.getText();
    }

    public String getPassword() {
        return textPassword.getText();
    }


}
