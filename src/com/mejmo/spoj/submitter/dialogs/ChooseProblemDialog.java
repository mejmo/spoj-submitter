package com.mejmo.spoj.submitter.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.mejmo.spoj.submitter.Configuration;
import com.mejmo.spoj.submitter.domain.Language;
import com.mejmo.spoj.submitter.domain.Problem;
import com.mejmo.spoj.submitter.service.SpojService;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashMap;

public class ChooseProblemDialog extends DialogWrapper {

    private JPanel contentPane;
    private JComboBox comboProblems;

    public ChooseProblemDialog(@Nullable Project project) {
        super(project);
        super.init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        Problem[] probs = new SpojService().getAvailableProblems();
        comboProblems.setModel(new DefaultComboBoxModel());
        for (Problem prob : probs) {
            comboProblems.addItem(prob);
            if (prob.getId().equalsIgnoreCase(Configuration.getLanguageId()))
                comboProblems.setSelectedItem(prob);
        }

        return contentPane;
    }

    public Problem getResult() {
        return (Problem) comboProblems.getSelectedItem();
    }

}
