package com.rondgrt.tools;

import com.rondgrt.tools.checker.ApacheHTTPClientUrlChecker;
import com.rondgrt.tools.checker.Java11HTTPClientUrlChecker;
import com.rondgrt.tools.checker.UrlConnectionUrlChecker;
import com.rondgrt.tools.reader.AccessLogReader;
import com.rondgrt.tools.urlparser.UrlParser;

import java.util.List;

public class HttpStatusChecker {

    public static void main(String[] args) {
        System.out.println("Starting...");

        AccessLogReader accessLogReader = new AccessLogReader();

        List<String> uniqueLines = accessLogReader.getUniqueLinesCount();

        System.out.println("Number of lines in log is: " + uniqueLines.size());
        System.out.println(uniqueLines.get(0) + "\n");
        System.out.println(uniqueLines.get(1) + "\n");
        System.out.println(uniqueLines.get(2) + "\n");
        System.out.println(uniqueLines.get(3) + "\n");
        System.out.println(uniqueLines.get(4) + "\n");

        List<String> uniqueUrls = new UrlParser().urlsToCheck(uniqueLines);

        System.out.println("Number of lines in urls is: " + uniqueUrls.size());
        System.out.println(uniqueUrls.get(0) + "\n");
        System.out.println(uniqueUrls.get(1) + "\n");
        System.out.println(uniqueUrls.get(2) + "\n");
        System.out.println(uniqueUrls.get(3) + "\n");
        System.out.println(uniqueUrls.get(4) + "\n");

        Java11HTTPClientUrlChecker checker = new Java11HTTPClientUrlChecker();
//        UrlConnectionUrlChecker checker = new UrlConnectionUrlChecker();
        checker.checkUrls(uniqueUrls);

        System.out.println("Done!!");
    }
}
