package com.mejmo.spoj.submitter;

import com.intellij.ide.util.PropertiesComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mejmo on 12/12/15.
 */
public class Configuration {

    private static String languageId;
    private static String languageName;
    private static String problemName;
    private static String problemId;

    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);

    public static String getLanguageId() {
        return languageId;
    }

    public static void setLanguageId(String languageId) {
        Configuration.languageId = languageId;
    }

    public static String getLanguageName() {
        return languageName;
    }

    public static void setLanguageName(String languageName) {
        Configuration.languageName = languageName;
    }

    public static String getProblemName() {
        return problemName;
    }

    public static void setProblemName(String problemName) {
        Configuration.problemName = problemName;
    }

    public static void readConfiguration() {
        languageName = PropertiesComponent.getInstance().getValue("SpojSubmitter-language-name");
        languageId = PropertiesComponent.getInstance().getValue("SpojSubmitter-language-id");
        problemId = PropertiesComponent.getInstance().getValue("SpojSubmitter-problem-id");
        problemName = PropertiesComponent.getInstance().getValue("SpojSubmitter-problem-name");
    }

    public static String getProblemId() {
        return problemId;
    }

    public static void setProblemId(String problemId) {
        Configuration.problemId = problemId;
    }

    public static void save() {
        PropertiesComponent.getInstance().setValue("SpojSubmitter-language-name", languageName);
        PropertiesComponent.getInstance().setValue("SpojSubmitter-language-id", languageId);
        PropertiesComponent.getInstance().setValue("SpojSubmitter-problem-id", problemId);
        PropertiesComponent.getInstance().setValue("SpojSubmitter-problem-name", problemName);
        logger.debug("Configuration saved");
    }
}
