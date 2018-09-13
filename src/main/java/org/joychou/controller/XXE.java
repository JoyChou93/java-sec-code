package org.joychou.controller;


import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.XMLReader;
import java.io.*;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.commons.digester3.Digester;

/**
 * @author: JoyChou (joychou@joychou.org)
 * @date:   2017.12.22
 * @desc:   Java XXE 漏洞代码，修复代码在注释里
 */

@Controller
@RequestMapping("/xxe")
public class XXE {

    @RequestMapping(value = "/xmlReader", method = RequestMethod.POST)
    @ResponseBody
    public  String xxe_xmlReader(HttpServletRequest request) {
        try {
            String xml_con = getBody(request);
            System.out.println(xml_con);
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();

            // fix code start

//            xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
//            xmlReader.setFeature("http://xml.org/sax/features/external-general-entities", false);
//            xmlReader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

            //fix code end

            xmlReader.parse( new InputSource(new StringReader(xml_con)) );  // parse xml
            return "ok";
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }

    @RequestMapping(value = "/SAXParser", method = RequestMethod.POST)
    @ResponseBody
    public String xxe_SAXParser(HttpServletRequest request) {
        try {
            String xml_con = getBody(request);
            System.out.println(xml_con);
            SAXParserFactory spf = SAXParserFactory.newInstance();

            // fix code start

//            spf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
//            spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
//            spf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

            // fix code end

            SAXParser parser = spf.newSAXParser();
            parser.parse(new InputSource(new StringReader(xml_con)), new DefaultHandler());  // parse xml
            return "test";
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }

    @RequestMapping(value = "/Digester", method = RequestMethod.POST)
    @ResponseBody
    public String xxe_Digester(HttpServletRequest request) {
        try {
            String xml_con = getBody(request);
            System.out.println(xml_con);
            Digester digester = new Digester();

            // fix code start

//            digester.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
//            digester.setFeature("http://xml.org/sax/features/external-general-entities", false);
//            digester.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

            // fix code end

            digester.parse(new StringReader(xml_con));  // parse xml
            return "test";
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }


    @RequestMapping(value = "/DocumentBuilder", method = RequestMethod.POST)
    @ResponseBody
    public String xxe_DocumentBuilder(HttpServletRequest request) {
        try {
            String xml_con = getBody(request);
            System.out.println(xml_con);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            // fix code start

//            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
//            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
//            dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

            // fix code end

            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(xml_con);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);  // parse xml
            sr.close();
            return "test";
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }


    @RequestMapping(value = "/DocumentBuilder_xinclude", method = RequestMethod.POST)
    @ResponseBody
    public String xxe_xinclude_DocumentBuilder(HttpServletRequest request) {
        try {
            String xml_con = getBody(request);
            System.out.println(xml_con);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            dbf.setXIncludeAware(true);   // 支持XInclude
            dbf.setNamespaceAware(true);  // 支持XInclude

            // fix code start

//            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
//            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
//            dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

            // fix code end

            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(xml_con);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);  // parse xml

            NodeList rootNodeList = document.getChildNodes();

            for (int i = 0; i < rootNodeList.getLength(); i++) {
                Node rootNode = rootNodeList.item(i);
                NodeList xxe = rootNode.getChildNodes();
                for (int j = 0; j < xxe.getLength(); j++) {
                    Node xxeNode = xxe.item(j);
                    System.out.println("xxeNode: " + xxeNode.getNodeValue());  // 回显
                }

            }

            sr.close();
            return "test";
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }

    // 获取body数据
    private String getBody(HttpServletRequest request) throws IOException {
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
