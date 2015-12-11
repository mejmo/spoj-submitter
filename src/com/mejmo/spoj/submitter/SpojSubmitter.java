package com.mejmo.spoj.submitter;

import com.intellij.icons.AllIcons;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.mejmo.spoj.submitter.exceptions.SPOJSubmitterException;
import com.yourkit.util.FileUtil;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.sanselan.util.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MFO on 11.12.2015.
 */
public class SpojSubmitter extends AnAction {

    private static final String SPOJ_LOGIN_URL = "http://www.spoj.com/login";
    private static final String SPOJ_SUBMIT_URL = "http://www.spoj.com/submit/complete/";
    private static final String SPOJ_STATUS_URL = "http://www.spoj.com/status/";
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final int MAX_TRIES = 5;
    private static final int WAIT_SECS = 5;

    private static String cookie = null;

    private static final Logger logger = LoggerFactory.getLogger(SpojSubmitter.class);

    public void login() {

        logger.info("Logging in ...");

        HttpResponse response = null;

        try {

            response = Request.Post(SPOJ_LOGIN_URL)
                    .bodyForm(Form.form()
                            .add("login_user", "mejmo")
                            .add("password", "")
                            .add("next_raw", "/")
                            .add("autologin", "1")
                            .build())
                    .execute()
                    .returnResponse();

        } catch (IOException e) {
            throw new SPOJSubmitterException(e);
        }

        if (!(response.getStatusLine().getStatusCode() == 302))
            throw new SPOJSubmitterException("Login page returned non-302 return code: "+
                    response.getStatusLine().getStatusCode()+": "+response.getStatusLine().getReasonPhrase());

        List<String> loginCookies = new ArrayList<>();
        for (Header header : response.getAllHeaders()) {
            if (header.getName().equalsIgnoreCase("Set-Cookie")) {
                logger.debug("Got cookie: "+header.getValue());
                loginCookies.add(header.getValue());
            }
        }
        StringBuilder builder = new StringBuilder();
        for (String cookie : loginCookies)
            builder.append(cookie);
        cookie = builder.toString();

        logger.info("Logged in!");

    }

    public void submitSolution() {

        logger.info("Submitting solution ...");

        HttpResponse response = null;

        try {

            response = Request.Post(SPOJ_SUBMIT_URL)
                    .addHeader("Cookie", cookie)
                    .bodyForm(Form.form()
                            .add("problemcode", "POKER")
                            .add("lang", "4")
                            .add("submit", "Submit!")
                            .add("file", FileUtil.readFileContentAsUtf8(new File("resources/main.py")))
                            .build())
                    .execute()
                    .returnResponse();

            if (!(response.getStatusLine().getStatusCode() == 200))
                throw new SPOJSubmitterException("Solution submit returned non-200 return code: "+
                        response.getStatusLine().getStatusCode()+": "+response.getStatusLine().getReasonPhrase());

        } catch (IOException e) {
            throw new SPOJSubmitterException(e);
        }

        logger.info("Solution submitted!");

    }

    public String getSubmitResult() {

        logger.info("Getting submit result ...");

        HttpResponse response = null;

        try {

//            String result = FileUtil.readFileContentAsUtf8(new File("resources/result.html"));
            String finalResult = null;

            for (int i = 0; i < MAX_TRIES; i++) {

                response = Request.Get(SPOJ_STATUS_URL)
                        .addHeader("Cookie", cookie)
                        .execute()
                        .returnResponse();


                if (!(response.getStatusLine().getStatusCode() == 200))
                    throw new SPOJSubmitterException("Getting status returned non-200 return code: "+
                            response.getStatusLine().getStatusCode()+": "+response.getStatusLine().getReasonPhrase());

                String result = EntityUtils.toString(response.getEntity());
                String status = parseStatus(result);

                if (status == null)
                    throw new SPOJSubmitterException("Cannot parse status from status page "+SPOJ_STATUS_URL);

                logger.info("Parsed status: "+status);
                if (status.equalsIgnoreCase("waiting..")) {
                    logger.info("Waiting for processing to be done ...");
                    try {
                        Thread.sleep(WAIT_SECS * 1000);
                    } catch (InterruptedException e) {
                        throw new SPOJSubmitterException("Waiting for status was interrupted");
                    }
                } else {
                    finalResult = status;
                    break;
                }
            }

            if (finalResult == null)
                finalResult = "unknown";

            logger.info("STATUS: "+finalResult);
            return finalResult;

        } catch (IOException e) {
            throw new SPOJSubmitterException(e);
        }

    }

    private String parseStatus(String html) {

        Document doc = Jsoup.parse(html);
        Elements a = doc.getElementsByAttributeValueStarting("class", "kol");

        for (Element el : a) {

            Elements aHrefs = el.getElementsByAttributeValue("href", "/users/mejmo/");

            if (aHrefs.size() > 0) {
                String jobId = el.getElementsByAttributeValue("class", "suser text-center").attr("id").replaceAll("^.+_(.+)$", "$1");
                Element element = doc.getElementById("statusres_"+jobId);

                if (element.text().contains("accepted"))
                    return "accepted";

                return element.ownText();
            }

        }
        return null;

    }

    public void actionPerformed(AnActionEvent event) {
//        Project project = event.getData(PlatformDataKeys.PROJECT);
//        String txt= Messages.showInputDialog(project, "What is your name?", "Input your name", Messages.getQuestionIcon());
//        Messages.showMessageDialog(project, "Hello, " + txt + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());


    }
}
