package org.joychou.security;


import java.net.URI;

public class SecurityUtil {

    /**
     * 通过endsWith判断URL是否合法
     *
     * @param url
     * @return 安全url返回true，危险url返回false
     */
    public static Boolean checkURLbyEndsWith(String url, String[] urlwhitelist) {
        try {
            URI uri = new URI(url);
            // 判断是否是http(s)协议
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                return false;
            }

            // 使用uri获取host
            String host = uri.getHost().toLowerCase();
            for (String whitelist: urlwhitelist){
                if (host.endsWith("." + whitelist)) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }



}