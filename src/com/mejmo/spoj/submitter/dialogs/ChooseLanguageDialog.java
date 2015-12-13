package com.mejmo.spoj.submitter.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.mejmo.spoj.submitter.PluginPersistence;
import com.mejmo.spoj.submitter.Utils;
import com.mejmo.spoj.submitter.domain.LanguageInfo;
import com.mejmo.spoj.submitter.exceptions.SPOJSubmitterException;
import com.mejmo.spoj.submitter.service.SpojService;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Simple dialog which gets language id and name from SPOJ website
 *
 * @author Martin Formanko 2015
 */
public class ChooseLanguageDialog extends DialogWrapper {

    private JPanel contentPane;
    private JComboBox comboLanguages;

    public ChooseLanguageDialog(@Nullable Project project) {
        super(project);
        super.init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        LanguageInfo[] langs = null;
        try {
            langs = SpojService.getInstance().getAvailableLanguages();
        } catch (SPOJSubmitterException ex) {
            Utils.showError("Cannot get available languages list!");
            return contentPane;
        }

        comboLanguages.setModel(new DefaultComboBoxModel());
        for (LanguageInfo lang : langs) {
            comboLanguages.addItem(lang);
            if (lang.getId().equalsIgnoreCase(PluginPersistence.getLanguageId()))
                comboLanguages.setSelectedItem(lang);
        }

        return contentPane;
    }

    public LanguageInfo getResult() {
        return (LanguageInfo) comboLanguages.getSelectedItem();
    }

}
