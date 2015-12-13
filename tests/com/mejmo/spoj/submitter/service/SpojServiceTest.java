package com.mejmo.spoj.submitter.service;

import com.mejmo.spoj.submitter.domain.JobInfo;
import com.mejmo.spoj.submitter.domain.LanguageInfo;
import com.mejmo.spoj.submitter.exceptions.SPOJSubmitterException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.spi.LoggerFactory;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Service class for all requests to SPOJ webpage
 *
 * @author Martin Formanko 2015
 */
public class SpojServiceTest {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger("Test");

    @Test
    public void testService() throws IOException, SPOJSubmitterException {

        String file = FileUtils.readFileToString(new File("resources/main.py"));

        JobInfo jobInfo = new JobInfo();

        SpojService.getInstance().login(jobInfo);
        SpojService.getInstance().submitSolution(jobInfo);
        SpojService.getInstance().getSubmitResult(jobInfo, new StatusChangeListener() {
            @Override
            public void changeStatus(String value) {
                logger.info(value);
            }
        });

    }

    @Test
    public void testAvailableLanguages() throws IOException, SPOJSubmitterException {

        LanguageInfo[] map = SpojService.getInstance().getAvailableLanguages();
        assertThat(map[0].getId(), equalTo("Python (python 2.7.10)"));

    }


}
