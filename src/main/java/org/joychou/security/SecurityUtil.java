package org.joychou.security;

import com.google.common.net.InternetDomainName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.regex.Pattern;

public class SecurityUtil {

    private static final Pattern FILTER_PATTERN = Pattern.compile("^[a-zA-Z0-9_/\\.-]+$") ;
    private static Logger logger = LoggerFactory.getLogger(SecurityUtil.class);


    /**
     * 通过endsWith判断URL是否合法
     *
     * @param url 需要check的url
     * @param urlwhitelist url白名单list
     * @return 安全url返回url，危险url返回null
     */
    public static String checkURLbyEndsWith(String url, String[] urlwhitelist) {
        if (null == url) {
            return null;
        }
        try {
            URI uri = new URI(url);

            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                return null;
            }

            String host = uri.getHost().toLowerCase();
            for (String whitelist: urlwhitelist){
                if (host.endsWith("." + whitelist)) {
                    return url;
                }
            }

            return null;
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 通过google guava判断URL是否合法。默认认为localhost合法
     *
     * @param url 需要check的url
     * @param urlwhitelist 根域名白名单
     * @return 安全url返回url，危险url返回null
     */
    public static String checkUrlByGuava(String url, String[] urlwhitelist){
        final String localhost = "localhost";

        if (null == url) {
            return null;
        }

        try {
            URI u = new URI(url);
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                return null;
            }
            String host = u.getHost().toLowerCase();
            if (localhost.equals(host)) {
                return url;
            }
            String rootDomain = InternetDomainName.from(host).topPrivateDomain().toString();

            for (String whiteurl: urlwhitelist){
                if (rootDomain.equals(whiteurl)) {
                    return url;
                }
            }
            return null;
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
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
     * @return Safe url returns url. Dangerous url returns null.
     */
    public static String checkSSRFByHostWlist(String url, String[] hostWlist) {
        return checkURLbyEndsWith(url, hostWlist);
    }

    /**
     * Filter file path to prevent path traversal vulns.
     *
     * @param filepath file path
     * @return illegal file path return null
     */
    public static String pathFilter(String filepath) {
        String temp = filepath;

        // use while to sovle multi urlencode
        while (temp.indexOf('%') != -1) {
            try {
                temp = URLDecoder.decode(temp, "utf-8");
            } catch (UnsupportedEncodingException e) {
                logger.info("Unsupported encoding exception: " + filepath);
                return null;
            } catch (Exception e) {
                logger.info(e.toString());
                return null;
            }
        }

        if (temp.indexOf("..") != -1 || temp.charAt(0) == '/') {
            return null;
        }

        return filepath;
    }


    public static String cmdFilter(String input) {
        if (!FILTER_PATTERN.matcher(input).matches()) {
            return null;
        }

        return input;
    }


    /**
     * 过滤mybatis中order by不能用#的情况。
     * 严格限制用户输入只能包含<code>a-zA-Z0-9_-.</code>字符。
     *
     * @param sql sql
     * @return 安全sql，否则返回null
     */
    public static String sqlFilter(String sql) {
        if (!FILTER_PATTERN.matcher(sql).matches()) {
            return null;
        }
        return sql;
    }

    /**
     * 将非<code>0-9a-zA-Z/-.</code>的字符替换为空
     *
     * @param str 字符串
     * @return 被过滤的字符串
     */
    public static String replaceSpecialStr(String str) {
        StringBuilder sb = new StringBuilder();
        str = str.toLowerCase();
        for(int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            // 如果是0-9
            if (ch >= 48 && ch <= 57 ){
                sb.append(ch);
            }
            // 如果是a-z
            else if(ch >= 97 && ch <= 122) {
                sb.append(ch);
            }
            else if(ch == '/' || ch == '.' || ch == '-'){
                sb.append(ch);
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.print(replaceSpecialStr("text/ html&(&(asdf"));
    }

}