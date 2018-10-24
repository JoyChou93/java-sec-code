package org.joychou.utils;

import com.google.common.net.InternetDomainName;
import java.net.URL;

public class Security {
    /**
     * @param url
     * @return 安全url返回true，危险url返回false
     */
    public static Boolean checkSafeUrl(String url, String[] urlwhitelist){
        try{
            URL u = new URL(url);
            // 判断是否是http(s)协议
            if (!u.getProtocol().startsWith("http") && !u.getProtocol().startsWith("https")) {
                System.out.println("The protocol of url is not http or https.");
                return false;
            }
            String host = u.getHost().toLowerCase();
            // 如果非顶级域名后缀会报错
            String rootDomain = InternetDomainName.from(host).topPrivateDomain().toString();

            for (String whiteurl: urlwhitelist){
                if (rootDomain.equals(whiteurl)) {
                    return true;
                }
            }

            System.out.println("Url is not safe.");
            return false;
        }catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return false;
        }
    }
}
