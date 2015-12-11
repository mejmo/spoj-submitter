package com.mejmo.spoj.submitter;

import com.mejmo.spoj.submitter.domain.Language;
import com.mejmo.spoj.submitter.service.SpojService;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;

public class ChooseLanguageDialog extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboLanguages;
    private Language result;

    public ChooseLanguageDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        ComboBoxModel comboBoxModel = new DefaultComboBoxModel();
        HashMap<String, String> langs = new SpojService().getAvailableLanguages();

        for (String langId : langs.keySet()) {
            comboLanguages.addItem(new Language(langId, langs.get(langId)));
        }

        this.pack();
        this.setVisible(true);
        this.

    }

    private void onOK() {
        Language lang = (Language) comboLanguages.getSelectedItem();
        if (lang != null)
            result = lang;
    }

    private void onCancel() {
        dispose();
    }

    public Language getResult() {
        return result;
    }

    public void setResult(Language result) {
        this.result = result;
    }
}
