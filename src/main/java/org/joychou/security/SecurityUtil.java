package org.joychou.security;



import java.net.URI;

public class SecurityUtil {

    /**
     * 通过endsWith判断URL是否合法
     *
     * @param url 需要check的url
     * @param urlwhitelist url白名单list
     * @return 安全url返回true，危险url返回false
     */
    public static Boolean checkURLbyEndsWith(String url, String[] urlwhitelist) {
        if (null == url) {
            return false;
        }
        try {
            URI uri = new URI(url);

            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                return false;
            }

            String host = uri.getHost().toLowerCase();
            for (String whitelist: urlwhitelist){
                if (host.endsWith("." + whitelist)) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 解析url的ip，判断ip是否是内网ip，所以TTL设置为0的情况不适用。
     *
     * @param url check的url
     * @return 安全返回true，危险返回false
     */
    public static Boolean checkSSRF(String url) {
        if (SSRFChecker.checkSSRF(url)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Suitable for: TTL isn't set to 0 & Redirect is forbidden.
     *
     * @param url The url that needs to check.
     * @return Safe url returns true. Dangerous url returns false.
     */
    public static boolean checkSSRFWithoutRedirect(String url) {
        return !SSRFChecker.isInnerIPByUrl(url);
    }

    /**
     * Check SSRF by host white list.
     * This is the simplest and most effective method to fix ssrf vul.
     *
     * @param url The url that needs to check.
     * @param hostWlist host whitelist
     * @return Safe url returns true. Dangerous url returns false.
     */
    public static boolean checkSSRFByHostWlist(String url, String[] hostWlist) {
        return checkURLbyEndsWith(url, hostWlist);
    }

}