package com.rondgrt.tools.checker;

import org.apache.commons.codec.binary.Base64;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Authenticator;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UrlConnectionUrlChecker {

    public void checkUrls(List<String> urls) {
        List<String> non200StatusUrls = new ArrayList<>();

        for (String url : urls) {
            try {
                URL theUrl = new URL(url);

                String auth = "todo" + ":" + "todo";
                byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
                String authHeaderValue = "Basic " + new String(encodedAuth);

                HttpURLConnection huc = (HttpURLConnection) theUrl.openConnection();
                huc.setRequestProperty("Set-Cookie", "authId=todo; Domain=myprivacy.dpgmedia.be; Path=/");
                huc.setRequestProperty("Set-Cookie", "xAuthId=todo; Domain=myprivacy.dpgmedia.be; Path=/");
                huc.setRequestProperty("Authorization", authHeaderValue);
                huc.setInstanceFollowRedirects(true);

                System.out.println(huc.getResponseCode() +  " --> " + url);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Ready: " + non200StatusUrls.size() + " urls have a non 200 response code. Total urls checked = " + urls.size());
        // TODO write non200StatusUrls to file
        try {
            writeToFile(non200StatusUrls);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void writeToFile(final List<String> lines) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new FileOutputStream("/Users/groot100/Documents/logs/output.txt"));
        for (String line : lines)
            pw.println(line);
        pw.close();
    }

}
