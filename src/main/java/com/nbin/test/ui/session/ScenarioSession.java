package com.nbin.test.ui.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope("cucumber-glue")
public class ScenarioSession {

    private static final Logger log = LoggerFactory.getLogger(ScenarioSession.class);
    private Map<String, String> sessionData = new HashMap<String, String>();

    public void putData(String key, String value) {
        log.info("Putting data with key="+key+" value="+value);
        sessionData.put(key, value);
    }

    public String getData(String key) {
        log.info("Getting data with key="+key+" value="+sessionData.get(key));
        return sessionData.get(key);
    }

    public void clearScenarioData() {
        log.info("Clearing scenario data...");
        sessionData.clear();
    }

}
