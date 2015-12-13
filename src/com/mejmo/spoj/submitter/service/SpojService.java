package com.mejmo.spoj.submitter.service;

import com.intellij.diff.DiffManager;
import com.intellij.openapi.vcs.ProjectLevelVcsManager;
import com.intellij.openapi.vcs.changes.Change;
import com.mejmo.spoj.submitter.Constants;
import com.mejmo.spoj.submitter.PluginPersistence;
import com.mejmo.spoj.submitter.domain.JobInfo;
import com.mejmo.spoj.submitter.domain.LanguageInfo;
import com.mejmo.spoj.submitter.domain.ProblemInfo;
import com.mejmo.spoj.submitter.domain.SubmitResult;
import com.mejmo.spoj.submitter.exceptions.SPOJSubmitterException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for all requests to SPOJ webpage
 *
 * @author Martin Formanko 2015
 */
public class SpojService implements Constants {

    private static final Logger logger = LoggerFactory.getLogger(SpojService.class);
    private static String cookie = null;
    private static SpojService instance = null;

    public synchronized static SpojService getInstance() {
        if (instance == null)
            instance = new SpojService();
        return instance;
    }

    public synchronized void login() throws SPOJSubmitterException {
        login(new JobInfo(PluginPersistence.getUsername(), PluginPersistence.getPassword(), null, null, null));
    }

    /**
     * Login does not deal with captchas
     * @param jobInfo
     * @throws SPOJSubmitterException
     */
    public synchronized void login(JobInfo jobInfo) throws SPOJSubmitterException {

        logger.info("Logging in ...");

        String username = jobInfo.getUsername();
        String password = jobInfo.getPassword();

        HttpResponse response = null;
        if (username == null || username.trim().length() == 0 ||
                password == null || password.trim().length() == 0) {
            String msg = "No SPOJ username or password set!";
            throw new SPOJSubmitterException(msg);
        }

        try {

            response = Request.Post(SPOJ_LOGIN_URL)
                    .bodyForm(Form.form()
                            .add("login_user", username)
                            .add("password", password)
                            .add("next_raw", "/")
                            .add("autologin", "1")
                            .build())
                    .execute()
                    .returnResponse();

        } catch (IOException e) {
            logger.error("Error while logging in", e);
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

    /**
     * Submits solution when captcha is not present, otherwise returns exception (hopefully)
     * @param jobInfo
     * @throws SPOJSubmitterException
     */
    public synchronized void submitSolution(JobInfo jobInfo) throws SPOJSubmitterException {

        logger.info("Submitting solution ...");

        HttpResponse response = null;

        if (jobInfo.getLanguage().getId() == null || jobInfo.getLanguage().getId().trim().length() == 0 ||
                jobInfo.getProblem().getId() == null || jobInfo.getProblem().getId().trim().length() == 0) {
            String msg = "No problem id or language set!";
            throw new SPOJSubmitterException(msg);
        }

        try {

            response = Request.Post(SPOJ_SUBMIT_URL)
                    .addHeader("Cookie", cookie)
                    .bodyForm(Form.form()
                            .add("problemcode", jobInfo.getProblem().getId())
                            .add("lang", jobInfo.getLanguage().getId())
                            .add("submit", "Submit!")
                            .add("file", jobInfo.getSolution())
                            .build())
                    .execute()
                    .returnResponse();

            if (!(response.getStatusLine().getStatusCode() == 200))
                throw new SPOJSubmitterException("Solution submit returned non-200 return code: "+
                        response.getStatusLine().getStatusCode()+": "+response.getStatusLine().getReasonPhrase());

        } catch (IOException e) {
            logger.error("Error while submittin", e);
            throw new SPOJSubmitterException(e);
        }

        logger.info("Solution submitted!");

    }

    /**
     * Parses the result from SPOJ table
     * @param jobInfo
     * @param listener
     * @return
     * @throws SPOJSubmitterException
     */
    public synchronized SubmitResult getSubmitResult(JobInfo jobInfo, StatusChangeListener listener) throws SPOJSubmitterException {

        logger.info("Getting submit result ...");

        SubmitResult submitResult = null;
        HttpResponse response = null;

        try {

//            String result = FileUtils.readFileToString(new File("/tmp/"));

            for (int i = 0; i < MAX_TRIES; i++) {

                response = Request.Get(SPOJ_STATUS_URL)
                        .execute()
                        .returnResponse();


                if (!(response.getStatusLine().getStatusCode() == 200))
                    throw new SPOJSubmitterException("Getting status returned non-200 return code: "+
                            response.getStatusLine().getStatusCode()+": "+response.getStatusLine().getReasonPhrase());

                SubmitResult status = parseStatus(jobInfo, EntityUtils.toString(response.getEntity()));
//                SubmitResult status = parseStatus(jobInfo, result);

                if (status == null)
                    throw new SPOJSubmitterException("Cannot parse status from status page "+SPOJ_STATUS_URL);

                logger.info("Parsed status: "+status.getStatus());
                if (!status.getStatus().contains("..")) {
                    submitResult = status;
                    break;
                }

                logger.info("Waiting for processing to be done ...");
                try {
                    listener.changeStatus("Waiting for status ("+(i+1)+") ...");
                    Thread.sleep(WAIT_SECS * 1000);
                } catch (InterruptedException e) {
                    throw new SPOJSubmitterException("Waiting for status was interrupted");
                }
            }

            if (submitResult == null)
                submitResult = new SubmitResult(String.valueOf(RandomUtils.nextDouble()), "-", "-", "unknown", jobInfo.getLanguage());

            logger.info("STATUS: "+submitResult.getStatus());
            return submitResult;

        } catch (IOException e) {
            logger.info("Cannot get result", e);
            throw new SPOJSubmitterException(e);
        }

    }

    /**
     * Helper method for parsing status
     * @param jobInfo
     * @param html
     * @return
     */
    private synchronized SubmitResult parseStatus(JobInfo jobInfo, String html) {

        Document doc = Jsoup.parse(html);
        Elements a = doc.getElementsByAttributeValueStarting("class", "kol");

        for (Element el : a) {

            Elements aHrefs = el.getElementsByAttributeValue("href", "/users/"+ jobInfo.getUsername()+"/");

            if (aHrefs.size() > 0) {
                String jobId = el.getElementsByAttributeValue("class", "suser text-center").attr("id").replaceAll("^.+_(.+)$", "$1");
                Element element = doc.getElementById("statusres_"+jobId);

                Element elMem = doc.getElementById("statusmem_"+jobId);
                String memory = elMem == null ? "-" : elMem.text();

                Element elTimeA = doc.getElementById("statustime_"+jobId);
                String time = null;
                if (elTimeA != null) {
                    if (elTimeA.getAllElements().size() != 0) {
                        time = elTimeA.children().get(0).ownText();
                    }
                } else {
                    time = "-";
                }

                String status = element.ownText();
                if (element.ownText().trim().length() == 0)
                    if (element.children().get(0).tag().toString().equalsIgnoreCase("a"))
                        status = element.children().get(0).ownText();

                if (element.text().contains("accepted"))
                    return new SubmitResult(jobId, memory, time, "accepted", jobInfo.getLanguage());

                return new SubmitResult(jobId, memory, time, status, jobInfo.getLanguage());
            }
        }
        return null;

    }

    /**
     * Gets all available language from combo box
     * @return
     * @throws SPOJSubmitterException
     */
    public synchronized LanguageInfo[] getAvailableLanguages() throws SPOJSubmitterException {

        logger.debug("Getting available languages ...");

        HttpResponse response = null;
        List<LanguageInfo> resultLangs = new ArrayList<>();

        try {

            response = Request.Get(SPOJ_LANGUAGE_URL)
                    .execute()
                    .returnResponse();


            if (!(response.getStatusLine().getStatusCode() == 200))
                throw new SPOJSubmitterException("Getting status returned non-200 return code: " +
                        response.getStatusLine().getStatusCode() + ": " + response.getStatusLine().getReasonPhrase());

            String result = EntityUtils.toString(response.getEntity());

            if (result.trim().length() > 0) {
                Document doc = Jsoup.parse(EntityUtils.toString(response.getEntity()));
                Element el = doc.getElementById("lang");
                Elements langs = el.getElementsByTag("option");
                for (Element lang : langs) {
                    resultLangs.add(new LanguageInfo(lang.attr("value"), lang.ownText()));
                }
            } else {
                throw new ParseException("No languages present");
            }
            return resultLangs.toArray(new LanguageInfo[resultLangs.size()]);

        }  catch (Throwable e) {
            throw new SPOJSubmitterException(e);
        }

    }


    /**
     * Used for getting all available problems from remote site (replaces by code)
     * @return
     * @throws SPOJSubmitterException
     */
    @Deprecated
    public synchronized ProblemInfo[]  getAvailableProblems() throws SPOJSubmitterException {

        logger.debug("Getting available problems ...");

        HttpResponse response = null;
        List<ProblemInfo> resultProbs = new ArrayList<>();

        try {

            response = Request.Get(SPOJ_PROBLEMS_URL)
                    .execute()
                    .returnResponse();

            if (!(response.getStatusLine().getStatusCode() == 200))
                throw new SPOJSubmitterException("Getting status returned non-200 return code: " +
                        response.getStatusLine().getStatusCode() + ": " + response.getStatusLine().getReasonPhrase());

            String result = EntityUtils.toString(response.getEntity());

            if (result.trim().length() > 0) {
                String[] lines = result.split("\\r?\\n");
                for (String line : lines) {
                    String[] prob = line.split("\\r?\\n");
                    if (prob.length == 2) {
                        resultProbs.add(new ProblemInfo(prob[0], prob[1]));
                    } else {
                        throw new ParseException("Error in parsing available problems");
                    }
                }
            } else {
                throw new ParseException("No problems present");
            }
            return resultProbs.toArray(new ProblemInfo[resultProbs.size()]);

        }  catch (Throwable e) {
            throw new SPOJSubmitterException(e);
        }

    }

    public String getCodeForSubmit(String statusId) throws SPOJSubmitterException {
        logger.debug("Getting available problems ...");

        if (cookie == null)
            login();

        HttpResponse response = null;
        List<ProblemInfo> resultProbs = new ArrayList<>();

        try {

            response = Request.Get(SPOJ_VIEW_FILE_URL + "/" + statusId)
                    .addHeader("Cookie", cookie)
                    .execute()
                    .returnResponse();

            if (!(response.getStatusLine().getStatusCode() == 200))
                throw new SPOJSubmitterException("Getting status returned non-200 return code: " +
                        response.getStatusLine().getStatusCode() + ": " + response.getStatusLine().getReasonPhrase());

            return EntityUtils.toString(response.getEntity());

        } catch (Throwable t) {
            throw new SPOJSubmitterException(t);
        }

    }
}
