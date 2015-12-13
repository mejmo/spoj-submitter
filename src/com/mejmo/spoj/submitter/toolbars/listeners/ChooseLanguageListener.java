package com.mejmo.spoj.submitter.toolbars.listeners;

import com.intellij.openapi.ui.DialogWrapper;
import com.mejmo.spoj.submitter.PluginPersistence;
import com.mejmo.spoj.submitter.dialogs.ChooseLanguageDialog;
import com.mejmo.spoj.submitter.domain.Language;
import com.mejmo.spoj.submitter.toolbars.SubmitterToolWindowFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by mejmo on 12/13/15.
 */
public class ChooseLanguageListener implements MouseListener {

    private SubmitterToolWindowFactory submitterToolWindowFactory;
    private Logger logger = LoggerFactory.getLogger(ChooseLanguageListener.class);

    public ChooseLanguageListener(SubmitterToolWindowFactory submitterToolWindowFactory) {
        this.submitterToolWindowFactory = submitterToolWindowFactory;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        logger.debug("Creating choose language dialog");

        ChooseLanguageDialog dialog = new ChooseLanguageDialog(null);
        dialog.show();

        if (dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
            Language lang = dialog.getResult();
            PluginPersistence.setLanguageName(lang.getValue());
            PluginPersistence.setLanguageId(lang.getId());
            PluginPersistence.save();
            submitterToolWindowFactory.updateLabels();
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
}
