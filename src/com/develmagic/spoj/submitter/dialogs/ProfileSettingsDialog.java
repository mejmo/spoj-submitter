package com.develmagic.spoj.submitter.dialogs;

import com.develmagic.spoj.submitter.PluginPersistence;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.develmagic.spoj.submitter.domain.JobInfo;
import com.develmagic.spoj.submitter.exceptions.SPOJSubmitterException;
import com.develmagic.spoj.submitter.service.SpojService;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Simple dialog for setting the basic configuration for SPOJ submitter
 *
 * @author Martin Formanko 2015
 */
public class ProfileSettingsDialog extends DialogWrapper {

    private JPanel contentPane;
    private JTextField textUsername;
    private JButton testButton;
    private JTextField textPassword;
    private JLabel lblTest;
    private JComboBox comboLanguages;

    private final static Logger logger = LoggerFactory.getLogger(ProfileSettingsDialog.class);

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
                    SpojService.getInstance().login(new JobInfo(textUsername.getText(),
                            textPassword.getText(),
                            null, null, null));
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

    public int showDialog() {
        this.setTitle("SPOJ profile settings");
        super.show();
        if (this.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
            PluginPersistence.setUsername(this.getUsername());
            PluginPersistence.setPassword(this.getPassword());
            PluginPersistence.save();
            logger.debug("Profile settings saved");
        }
        return this.getExitCode();
    }

    public String getUsername() {
        return textUsername.getText();
    }

    public String getPassword() {
        return textPassword.getText();
    }


}
