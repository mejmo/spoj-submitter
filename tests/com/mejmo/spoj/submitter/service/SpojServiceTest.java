package com.mejmo.spoj.submitter.service;

import com.mejmo.spoj.submitter.domain.Language;
import com.mejmo.spoj.submitter.exceptions.SPOJSubmitterException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by mejmo on 12/11/15.
 */
public class SpojServiceTest {


    @Test
    public void testService() throws IOException {

        String file = FileUtils.readFileToString(new File("resources/main.py"));

//        spojService.login();
//        spojService.submitSolution();
//        spojService.getSubmitResult()
    }

    @Test
    public void testAvailableLanguages() throws IOException, SPOJSubmitterException {

        Language[] map = SpojService.getInstance().getAvailableLanguages();
        assertThat(map[0].getId(), equalTo("Python (python 2.7.10)"));

    }


}
