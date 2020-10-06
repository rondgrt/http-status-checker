package com.rondgrt.tools.urlparser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlParser {

    private static final String NEW_URL = "https://beta.hln.be";
    private static final String REGEX_PATTERN = "\"GET\\s(.*?)\\s";

    public List<String> urlsToCheck(final List<String> rawAccessLogLines) {
        // get relative url from log line
        Set<String> uniqueUrls = new HashSet<>();

        Pattern pattern = Pattern.compile(REGEX_PATTERN);

        for(String line : rawAccessLogLines) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                uniqueUrls.add(NEW_URL + matcher.group(1));
            }
        }

        return new ArrayList<>(uniqueUrls);
    }
}
