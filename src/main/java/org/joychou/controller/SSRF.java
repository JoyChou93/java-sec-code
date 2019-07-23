package org.joychou.controller;

import com.google.common.io.Files;
import com.squareup.okhttp.OkHttpClient;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.joychou.security.SecurityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;


/**
 * @author  JoyChou (joychou@joychou.org)
 * @date    2017.12.28
 * @desc    Java ssrf vuls code.
 */

@Controller
@RequestMapping("/ssrf")
public class SSRF {

    @RequestMapping("/urlConnection")
    @ResponseBody
    public static String ssrf_URLConnection(HttpServletRequest request)
    {
        try {
            String url = request.getParameter("url");
            URL u = new URL(url);
            URLConnection urlConnection = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); //send request
            String inputLine;
            StringBuffer html = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                html.append(inputLine);
            }
            in.close();
            return html.toString();
        }catch(Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }


    @RequestMapping("/HttpURLConnection")
    @ResponseBody
    public static String ssrf_httpURLConnection(HttpServletRequest request)
    {
        try {
            String url = request.getParameter("url");
            URL u = new URL(url);
            URLConnection urlConnection = u.openConnection();
            HttpURLConnection httpUrl = (HttpURLConnection)urlConnection;
            BufferedReader in = new BufferedReader(new InputStreamReader(httpUrl.getInputStream())); //send request
            String inputLine;
            StringBuffer html = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                html.append(inputLine);
            }
            in.close();
            return html.toString();
        }catch(Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }


    @RequestMapping("/Request")
    @ResponseBody
    public static String ssrf_Request(HttpServletRequest request)
    {
        try {
            String url = request.getParameter("url");
            return Request.Get(url).execute().returnContent().toString();
        }catch(Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }


    /**
     * Download the url file.
     * http://localhost:8080/ssrf/openStream?url=file:///etc/passwd
     *
     * new URL(String url).openConnection()
     * new URL(String url).openStream()
     * new URL(String url).getContent()
     */
    @RequestMapping("/openStream")
    @ResponseBody
    public static void ssrf_openStream (HttpServletRequest request, HttpServletResponse response) throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String url = request.getParameter("url");
        try {
            String downLoadImgFileName = Files.getNameWithoutExtension(url) + "." + Files.getFileExtension(url);
            // download
            response.setHeader("content-disposition", "attachment;fileName=" + downLoadImgFileName);

            URL u = new URL(url);
            int length;
            byte[] bytes = new byte[1024];
            inputStream = u.openStream(); // send request
            outputStream = response.getOutputStream();
            while ((length = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, length);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }

        }
    }


    @RequestMapping("/ImageIO")
    @ResponseBody
    public static void ssrf_ImageIO(HttpServletRequest request) {
        String url = request.getParameter("url");
        try {
            URL u = new URL(url);
            ImageIO.read(u); // send request
        } catch (Exception e) {
        }
    }


    @RequestMapping("/okhttp")
    @ResponseBody
    public static void ssrf_okhttp(HttpServletRequest request) throws IOException {
        String url = request.getParameter("url");
        OkHttpClient client = new OkHttpClient();
        com.squareup.okhttp.Request ok_http = new com.squareup.okhttp.Request.Builder().url(url).build();
        client.newCall(ok_http).execute();
    }


    /**
     * http://localhost:8080/ssrf/HttpClient?url=http://www.baidu.com
     *
     * @return The response of url param.
     */
    @RequestMapping("/HttpClient")
    @ResponseBody
    public static String ssrf_HttpClient(HttpServletRequest request) {

        String url = request.getParameter("url");
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse httpResponse = client.execute(httpGet); // send request
            BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        }catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }

    }


    /**
     * Safe code.
     * http://localhost:8080/ssrf/commonsHttpClient?url=http://www.baidu.com
     *
     */
    @RequestMapping("/commonsHttpClient")
    @ResponseBody
    public static String commonsHttpClient(HttpServletRequest request) {

        String url = request.getParameter("url");

        // Security check
        if (!SecurityUtil.checkSSRFWithoutRedirect(url)) {
            return "Bad man. I got u.";
        }
        // Create an instance of HttpClient.
        HttpClient client = new HttpClient();

        // Create a method instance.
        GetMethod method = new GetMethod(url);

        // forbid 302 redirection
        method.setFollowRedirects(false);

        try {
            // Send http request.
            int status_code = client.executeMethod(method);

            // Only allow the url that status_code is 200.
            if (status_code != HttpStatus.SC_OK) {
                return "Method failed: " + method.getStatusLine();
            }

            // Read the response body.
            byte[] resBody = method.getResponseBody();
            return new String(resBody);

        } catch (IOException e) {
            return "Error: " + e.getMessage();
        } finally {
            // Release the connection.
            method.releaseConnection();
        }


    }


    /**
     * Safe code.
     * http://localhost:8080/ssrf/ImageIO_safe?url=http://www.baidu.com
     *
     */
    @RequestMapping("/ImageIO_safe")
    @ResponseBody
    public static String ssrf_ImageIO_safecode(HttpServletRequest request) {
        String url = request.getParameter("url");
        try {
            URL u = new URL(url);
            if (!SecurityUtil.checkSSRF(url)) {
                return "SSRF check failed.";
            }
            ImageIO.read(u); // send request
        } catch (Exception e) {
            return e.toString();
        }

        return "ImageIO ssrf safe code.";
    }
}
