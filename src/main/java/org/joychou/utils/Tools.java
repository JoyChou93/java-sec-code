package org.joychou.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Tools {

    // get body
    public static String getBody(HttpServletRequest request) throws IOException {
        InputStream in = request.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuffer sb = new StringBuffer("");
        String temp;
        while ((temp = br.readLine()) != null) {
            sb.append(temp);
        }
        if (in != null) {
            in.close();
        }
        if (br != null) {
            br.close();
        }
        return sb.toString();
    }

}
