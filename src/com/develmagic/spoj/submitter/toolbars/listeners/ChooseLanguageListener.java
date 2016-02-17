package com.develmagic.spoj.submitter.toolbars.listeners;

import com.develmagic.spoj.submitter.PluginPersistence;
import com.develmagic.spoj.submitter.domain.LanguageInfo;
import com.develmagic.spoj.submitter.toolbars.SubmitterToolWindowFactory;
import com.intellij.openapi.ui.DialogWrapper;
import com.develmagic.spoj.submitter.dialogs.ChooseLanguageDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Martin Formanko 2015
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
            LanguageInfo lang = dialog.getResult();
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
