package com.mejmo.spoj.submitter.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.mejmo.spoj.submitter.PluginPersistence;
import com.mejmo.spoj.submitter.Utils;
import com.mejmo.spoj.submitter.domain.Language;
import com.mejmo.spoj.submitter.exceptions.SPOJSubmitterException;
import com.mejmo.spoj.submitter.service.SpojService;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

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

        Language[] langs = null;
        try {
            langs = SpojService.getInstance().getAvailableLanguages();
        } catch (SPOJSubmitterException ex) {
            Utils.showError("Cannot get available languages list!");
            return contentPane;
        }

        comboLanguages.setModel(new DefaultComboBoxModel());
        for (Language lang : langs) {
            comboLanguages.addItem(lang);
            if (lang.getId().equalsIgnoreCase(PluginPersistence.getLanguageId()))
                comboLanguages.setSelectedItem(lang);
        }

        return contentPane;
    }

    public Language getResult() {
        return (Language) comboLanguages.getSelectedItem();
    }

}
