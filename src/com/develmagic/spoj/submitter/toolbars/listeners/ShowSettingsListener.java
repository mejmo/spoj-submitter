package com.develmagic.spoj.submitter.toolbars.listeners;

import com.develmagic.spoj.submitter.dialogs.ProfileSettingsDialog;
import com.develmagic.spoj.submitter.toolbars.SubmitterToolWindowFactory;
import org.slf4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Martin Formanko 2015
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
        settingsDialog.showDialog();
    }
}
