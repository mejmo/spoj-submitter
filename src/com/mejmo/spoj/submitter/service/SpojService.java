package com.mejmo.spoj.submitter.service;

import com.google.common.collect.Lists;
import com.intellij.psi.search.scope.packageSet.ParsingException;
import com.mejmo.spoj.submitter.Constants;
import com.mejmo.spoj.submitter.SpojSubmitter;
import com.mejmo.spoj.submitter.Utils;
import com.mejmo.spoj.submitter.domain.Language;
import com.mejmo.spoj.submitter.domain.Problem;
import com.mejmo.spoj.submitter.exceptions.SPOJSubmitterException;
import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.spi.ThrowableInformation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by mejmo on 12/11/15.
 */

public class SpojService implements Constants {

    private static final Logger logger = LoggerFactory.getLogger(SpojService.class);

    private static String cookie = null;


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

    public void submitSolution(String solution) {

        logger.info("Submitting solution ...");

        HttpResponse response = null;

        try {

            response = Request.Post(SPOJ_SUBMIT_URL)
                    .addHeader("Cookie", cookie)
                    .bodyForm(Form.form()
                            .add("problemcode", "POKER")
                            .add("lang", "4")
                            .add("submit", "Submit!")
                            .add("file", solution)
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

    public Language[] getAvailableLanguages() {

        logger.debug("Getting available languages ...");

        HttpResponse response = null;
        List<Language> resultLangs = new ArrayList<>();

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
                    resultLangs.add(new Language(lang.attr("value"), lang.ownText()));
                }
            } else {
                throw new ParseException("No languages present");
            }
            return resultLangs.toArray(new Language[resultLangs.size()]);

        }  catch (Throwable e) {
            Utils.showError("Cannot get languages list");
            throw new SPOJSubmitterException(e);
        }

    }


    public Problem[]  getAvailableProblems() {

        logger.debug("Getting available problems ...");

        HttpResponse response = null;
        List<Problem> resultProbs = new ArrayList<>();

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
                        resultProbs.add(new Problem(prob[0], prob[1]));
                    } else {
                        throw new ParseException("Error in parsing available problems");
                    }
                }
            } else {
                throw new ParseException("No problems present");
            }
            return resultProbs.toArray(new Problem[resultProbs.size()]);

        }  catch (Throwable e) {
            Utils.showError("Cannot get languages list");
            throw new SPOJSubmitterException(e);
        }

    }
}
