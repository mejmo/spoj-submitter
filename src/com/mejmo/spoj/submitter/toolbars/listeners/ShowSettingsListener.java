package com.mejmo.spoj.submitter.toolbars.listeners;

import com.intellij.openapi.ui.DialogWrapper;
import com.mejmo.spoj.submitter.PluginPersistence;
import com.mejmo.spoj.submitter.dialogs.ProfileSettingsDialog;
import com.mejmo.spoj.submitter.toolbars.SubmitterToolWindowFactory;
import org.slf4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mejmo on 12/13/15.
 */
public class ShowSettingsListener implements ActionListener {

    private SubmitterToolWindowFactory submitterToolWindowFactory;
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ShowSettingsListener.class);

    public ShowSettingsListener(SubmitterToolWindowFactory submitterToolWindowFactory) {
        this.submitterToolWindowFactory = submitterToolWindowFactory;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ProfileSettingsDialog settingsDialog = new ProfileSettingsDialog(null);
        settingsDialog.setTitle("SPOJ profile settings");
        settingsDialog.show();
        if (settingsDialog.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
            PluginPersistence.setUsername(settingsDialog.getUsername());
            PluginPersistence.setPassword(settingsDialog.getPassword());
            PluginPersistence.save();
            logger.debug("Profile settings saved");
        }
    }
}
