package org.joychou.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

public class SafeDomainParser {

    private static Logger logger = LoggerFactory.getLogger(SafeDomainParser.class);

    public SafeDomainParser() {

        String rootTag = "domains";
        String safeDomainTag = "safedomains";
        String blockDomainTag = "blockdomains";
        String finalTag = "domain";
        String safeDomainClassPath = "url" + File.separator + "url_safe_domain.xml";
        ArrayList<String> safeDomains = new ArrayList<>();
        ArrayList<String> blockDomains = new ArrayList<>();

        try {
            // 读取resources目录下的文件
            ClassPathResource resource = new ClassPathResource(safeDomainClassPath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(resource.getInputStream());  // parse xml

            NodeList rootNode = doc.getElementsByTagName(rootTag);  // 解析根节点domains
            Node domainsNode = rootNode.item(0);
            NodeList child = domainsNode.getChildNodes();

            for (int i = 0; i < child.getLength(); i++) {
                Node node = child.item(i);
                // 解析safeDomains节点
                if (node.getNodeName().equals(safeDomainTag)) {
                    NodeList tagChild = node.getChildNodes();
                    for (int j = 0; j < tagChild.getLength(); j++) {
                        Node finalTagNode = tagChild.item(j);
                        // 解析safeDomains节点里的domain节点
                        if (finalTagNode.getNodeName().equals(finalTag)) {
                            safeDomains.add(finalTagNode.getTextContent());
                        }
                    }
                } else if (node.getNodeName().equals(blockDomainTag)) {
                    NodeList finalTagNode = node.getChildNodes();
                    for (int j = 0; j < finalTagNode.getLength(); j++) {
                        Node tagNode = finalTagNode.item(j);
                        // 解析blockDomains节点里的domain节点
                        if (tagNode.getNodeName().equals(finalTag)) {
                            blockDomains.add(tagNode.getTextContent());
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }

        WebConfig wc = new WebConfig();
        wc.setSafeDomains(safeDomains);
        logger.info(safeDomains.toString());
        wc.setBlockDomains(blockDomains);

        // 解析SSRF配置
        String ssrfRootTag = "ssrfsafeconfig";
        String ssrfSafeDomainTag = "safedomains";
        String ssrfBlockDomainTag = "blockdomains";
        String ssrfBlockIpsTag = "blockips";
        String ssrfFinalTag = "domain";
        String ssrfIpFinalTag = "ip";
        String ssrfSafeDomainClassPath = "url" + File.separator + "ssrf_safe_domain.xml";

        ArrayList<String> ssrfSafeDomains = new ArrayList<>();
        ArrayList<String> ssrfBlockDomains = new ArrayList<>();
        ArrayList<String> ssrfBlockIps = new ArrayList<>();

        try {
            // 读取resources目录下的文件
            ClassPathResource resource = new ClassPathResource(ssrfSafeDomainClassPath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            // 修复打包成jar包运行，不能读取文件的bug
            Document doc = db.parse(resource.getInputStream());  // parse xml

            NodeList rootNode = doc.getElementsByTagName(ssrfRootTag);  // 解析根节点
            Node domainsNode = rootNode.item(0);
            NodeList child = domainsNode.getChildNodes();

            for (int i = 0; i < child.getLength(); i++) {
                Node node = child.item(i);
                // 解析safeDomains节点
                if (node.getNodeName().equals(ssrfSafeDomainTag)) {
                    NodeList tagChild = node.getChildNodes();
                    for (int j = 0; j < tagChild.getLength(); j++) {
                        Node tagFinalNode = tagChild.item(j);
                        if (tagFinalNode.getNodeName().equals(ssrfFinalTag)) {
                            ssrfSafeDomains.add(tagFinalNode.getTextContent());
                        }
                    }
                } else if (node.getNodeName().equals(ssrfBlockDomainTag)) {
                    NodeList tagChild = node.getChildNodes();
                    for (int j = 0; j < tagChild.getLength(); j++) {
                        Node tagFinalNode = tagChild.item(j);
                        if (tagFinalNode.getNodeName().equals(ssrfFinalTag)) {
                            ssrfBlockDomains.add(tagFinalNode.getTextContent());
                        }
                    }
                } else if (node.getNodeName().equals(ssrfBlockIpsTag)) {
                    NodeList tagChild = node.getChildNodes();
                    for (int j = 0; j < tagChild.getLength(); j++) {
                        Node tagFinalNode = tagChild.item(j);
                        // 解析 blockIps 节点里的 ip 节点
                        if (tagFinalNode.getNodeName().equals(ssrfIpFinalTag)) {
                            ssrfBlockIps.add(tagFinalNode.getTextContent());
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }

        logger.info(ssrfBlockIps.toString());
        wc.setSsrfBlockDomains(ssrfBlockDomains);
        wc.setSsrfBlockIps(ssrfBlockIps);
        wc.setSsrfSafeDomains(ssrfSafeDomains);
    }
}




