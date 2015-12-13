package com.mejmo.spoj.submitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.ide.util.PropertiesComponent;
import com.mejmo.spoj.submitter.domain.ResultsDataBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Martin Formanko 2015
 */
public class PluginPersistence {

    private static String languageId;
    private static String languageName;
    private static String problemName;
    private static String problemId;
    private static String username;
    private static String password;

    private static final Logger logger = LoggerFactory.getLogger(PluginPersistence.class);

    public static String getLanguageId() {
        return languageId;
    }

    public static void setLanguageId(String languageId) {
        PluginPersistence.languageId = languageId;
    }

    public static String getLanguageName() {
        return languageName;
    }

    public static void setLanguageName(String languageName) {
        PluginPersistence.languageName = languageName;
    }

    public static String getProblemName() {
        return problemName;
    }

    public static void setProblemName(String problemName) {
        PluginPersistence.problemName = problemName;
    }

    public static void readConfiguration() {
        languageName = PropertiesComponent.getInstance().getValue("SpojSubmitter-language-name");
        languageId = PropertiesComponent.getInstance().getValue("SpojSubmitter-language-id");
        problemId = PropertiesComponent.getInstance().getValue("SpojSubmitter-problem-id");
        problemName = PropertiesComponent.getInstance().getValue("SpojSubmitter-problem-name");
        username = PropertiesComponent.getInstance().getValue("SpojSubmitter-username");
        password = PropertiesComponent.getInstance().getValue("SpojSubmitter-password");
    }

    public static String getProblemId() {
        return problemId;
    }

    public static void setProblemId(String problemId) {
        PluginPersistence.problemId = problemId;
    }

    public static void save() {
        PropertiesComponent.getInstance().setValue("SpojSubmitter-language-name", languageName);
        PropertiesComponent.getInstance().setValue("SpojSubmitter-language-id", languageId);
        PropertiesComponent.getInstance().setValue("SpojSubmitter-problem-id", problemId);
        PropertiesComponent.getInstance().setValue("SpojSubmitter-problem-name", problemName);
        PropertiesComponent.getInstance().setValue("SpojSubmitter-username", username);
        PropertiesComponent.getInstance().setValue("SpojSubmitter-password", password);
        logger.debug("Configuration saved");
    }

    public static void saveProblemResults(String problem, ResultsDataBean bean) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(bean);
            PropertiesComponent.getInstance().setValue("SpojSubmitter-"+problem+"-results", json);
            logger.debug("Results for problem "+problem+" saved");
        } catch (JsonProcessingException e) {
            logger.error("Cannot write results for problem "+problem);
        }
    }

    public static ResultsDataBean loadProblemResults(String problem) {
        String json = PropertiesComponent.getInstance().getValue("SpojSubmitter-"+problem+"-results");
        ObjectMapper objectMapper = new ObjectMapper();
        if (json == null)
            return new ResultsDataBean();
        try {
            return objectMapper.readValue(json, ResultsDataBean.class);
        } catch (IOException e) {
            logger.error("Cannot read results for problem "+problem);
            return new ResultsDataBean();
        }
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        PluginPersistence.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        PluginPersistence.password = password;
    }


}
