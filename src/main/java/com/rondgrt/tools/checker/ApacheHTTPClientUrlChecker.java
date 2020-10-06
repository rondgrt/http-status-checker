package com.rondgrt.tools.checker;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class ApacheHTTPClientUrlChecker {

    public void checkUrls(List<String> urls) {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials("todo", "todo");
        provider.setCredentials(AuthScope.ANY, credentials);

        BasicCookieStore cookieStore = new BasicCookieStore();

        BasicClientCookie cookie = new BasicClientCookie("authId", "todo");
        cookie.setDomain("myprivacy.dpgmedia.be");
        cookie.setPath("/");

        BasicClientCookie cookie2 = new BasicClientCookie("xAuthId", "todo");
        cookie.setDomain("myprivacy.dpgmedia.be");
        cookie.setPath("/");

        cookieStore.addCookie(cookie);

        HttpClient client = getHttpClient(provider, cookieStore);

        List<String> non200StatusUrls = new ArrayList<>();

        for (String url : urls) {

            try {
                HttpResponse response = client.execute(new HttpGet(url));

                System.out.println(response.getStatusLine().getStatusCode() + " --> " + url);

                if(response.getStatusLine().getStatusCode() != 200) {
                    non200StatusUrls.add(response.getStatusLine().getStatusCode() + " --> " + url);
                }
            } catch(Exception ex) {
                non200StatusUrls.add("ERROR --> " + url);
            }

        }

        System.out.println("Ready: " + non200StatusUrls.size() + " urls have a non 200 response code. Total urls checked = ");
        // TODO write non200StatusUrls to file
    }

    private static HttpClient getHttpClient(final CredentialsProvider provider, final BasicCookieStore cookieStore) {

        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");

            sslContext.init(null,
                    new TrustManager[]{new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {

                            return null;
                        }

                        public void checkClientTrusted(
                                X509Certificate[] certs, String authType) {

                        }

                        public void checkServerTrusted(
                                X509Certificate[] certs, String authType) {

                        }
                    }}, new SecureRandom());

            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpClient httpClient = HttpClientBuilder.create()
                    .setSSLSocketFactory(socketFactory)
                    .setDefaultCredentialsProvider(provider)
                    .setDefaultCookieStore(cookieStore)
                    .build();

            return httpClient;

        } catch (Exception e) {
            e.printStackTrace();
            return HttpClientBuilder.create().build();
        }
    }
}




