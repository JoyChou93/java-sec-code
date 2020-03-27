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

    private static Logger logger= LoggerFactory.getLogger(SafeDomainParser.class);

    public SafeDomainParser(){

        String safeTag = "safedomain";
        String domainSafeTag = "domain";
        String safeDomainClassPath = "url" + File.separator + "safe_domain.xml";
        ArrayList<String> safeDomains = new ArrayList<>();

        try {
            // 读取resources目录下的文件
            ClassPathResource resource = new ClassPathResource(safeDomainClassPath);
            File file = resource.getFile();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);  // parse xml

            NodeList rootNode = doc.getElementsByTagName(safeTag);
            Node domainsNode = rootNode.item(0);
            NodeList child = domainsNode.getChildNodes();
            
            for (int i = 0; i < child.getLength(); i++){
                Node node = child.item(i);
                if (node.getNodeName().equals(domainSafeTag)) {
                    safeDomains.add(node.getTextContent());
                }
            }

        }catch (Exception e){
            logger.error(e.toString());
        }

        WebConfig wc = new WebConfig();
        wc.setSafeDomains(safeDomains);
    }
}




