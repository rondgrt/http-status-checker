package com.rondgrt.tools.checker;

import org.apache.commons.codec.binary.Base64;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;

public class Java11HTTPClientUrlChecker {

    public void checkUrls(List<String> urls) {
        System.setProperty("jdk.httpclient.redirects.retrylimit", "15");

        CookieManager cookieManager = new CookieManager();

        HttpCookie cookie = new HttpCookie("authId", "todo");
        cookie.setDomain("myprivacy.dpgmedia.be");
        cookie.setPath("/");

        HttpCookie cookie2 = new HttpCookie("xAuthId", "todo");
        cookie.setDomain("myprivacy.dpgmedia.be");
        cookie.setPath("/");

        cookieManager.getCookieStore().add(null, cookie);
        cookieManager.getCookieStore().add(null, cookie2);

        String auth = "todo" + ":" + "todo";
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
        String authHeaderValue = "Basic " + new String(encodedAuth);

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .cookieHandler(cookieManager)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .executor(Executors.newFixedThreadPool(20))
                .build();

        List<String> non200StatusUrls = new CopyOnWriteArrayList<>();

        for (String url : urls) {
            HttpRequest request = HttpRequest.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .uri(URI.create(url))
                    .headers("Accept-Encoding", "gzip, deflate")
                    .header("Authorization", authHeaderValue)
//                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                    .build();

            try {
                System.out.println("getting response for url: " + url);

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                System.out.println(response.statusCode() + " --> " + url);

                if(response.statusCode() != 200) {
                    non200StatusUrls.add(response.statusCode() + " --> " + url);
                }
            } catch(Exception ex) {
                System.out.println("???" + " --> " + url);
                non200StatusUrls.add("ERR --> " + url);
                ex.printStackTrace();
            }
        }

        System.out.println("Ready: " + non200StatusUrls.size() + " urls have a non 200 response code. Total urls checked = " + urls.size());
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
