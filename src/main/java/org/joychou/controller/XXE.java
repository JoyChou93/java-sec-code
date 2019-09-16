package org.joychou.controller;

import org.dom4j.io.SAXReader;
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
import org.jdom2.input.SAXBuilder;
import org.joychou.util.WebUtils;

/**
 * Java xxe vul and safe code.
 *
 * @author JoyChou @2017-12-22
 */

@RestController
@RequestMapping("/xxe")
public class XXE {

    @RequestMapping(value = "/xmlReader", method = RequestMethod.POST)
    public String xxe_xmlReader(HttpServletRequest request) {
        try {
            String xml_con = WebUtils.getRequestBody(request);
            System.out.println(xml_con);
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.parse(new InputSource(new StringReader(xml_con)));  // parse xml
            return "ok";
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }


    @RequestMapping(value = "/xmlReader_fix", method = RequestMethod.POST)
    public String xxe_xmlReader_fix(HttpServletRequest request) {
        try {
            String xml_con = WebUtils.getRequestBody(request);
            System.out.println(xml_con);

            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            // fix code start
            xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            xmlReader.setFeature("http://xml.org/sax/features/external-general-entities", false);
            xmlReader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            //fix code end
            xmlReader.parse(new InputSource(new StringReader(xml_con)));  // parse xml

            return "ok";
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }


    @RequestMapping(value = "/SAXBuilder", method = RequestMethod.POST)
    public String xxe_SAXBuilder(HttpServletRequest request) {
        try {
            String xml_con = WebUtils.getRequestBody(request);
            System.out.println(xml_con);

            SAXBuilder builder = new SAXBuilder();
            org.jdom2.Document document = builder.build(new InputSource(new StringReader(xml_con)));  // cause xxe
            return "ok";
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }

    @RequestMapping(value = "/SAXBuilder_fix", method = RequestMethod.POST)
    public String xxe_SAXBuilder_fix(HttpServletRequest request) {
        try {
            String xml_con = WebUtils.getRequestBody(request);
            System.out.println(xml_con);

            SAXBuilder builder = new SAXBuilder();
            builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            builder.setFeature("http://xml.org/sax/features/external-general-entities", false);
            builder.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            org.jdom2.Document document = builder.build(new InputSource(new StringReader(xml_con)));

            return "ok";
        } catch (Exception e) {
            return "except";
        }
    }

    @RequestMapping(value = "/SAXReader", method = RequestMethod.POST)
    public String xxe_SAXReader(HttpServletRequest request) {
        try {
            String xml_con = WebUtils.getRequestBody(request);
            System.out.println(xml_con);

            SAXReader reader = new SAXReader();
            org.dom4j.Document document = reader.read(new InputSource(new StringReader(xml_con))); // cause xxe

            return "ok";
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }

    @RequestMapping(value = "/SAXReader_fix", method = RequestMethod.POST)
    public String xxe_SAXReader_fix(HttpServletRequest request) {
        try {
            String xml_con = WebUtils.getRequestBody(request);
            System.out.println(xml_con);

            SAXReader reader = new SAXReader();
            reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
            reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            org.dom4j.Document document = reader.read(new InputSource(new StringReader(xml_con)));

            return "ok";
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }

    @RequestMapping(value = "/SAXParser", method = RequestMethod.POST)
    public String xxe_SAXParser(HttpServletRequest request) {
        try {
            String xml_con = WebUtils.getRequestBody(request);
            System.out.println(xml_con);

            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            parser.parse(new InputSource(new StringReader(xml_con)), new DefaultHandler());  // parse xml

            return "test";
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }


    @RequestMapping(value = "/SAXParser_fix", method = RequestMethod.POST)
    public String xxe_SAXParser_fix(HttpServletRequest request) {
        try {
            String xml_con = WebUtils.getRequestBody(request);
            System.out.println(xml_con);

            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            spf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            SAXParser parser = spf.newSAXParser();
            parser.parse(new InputSource(new StringReader(xml_con)), new DefaultHandler());  // parse xml
            return "test";
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }


    @RequestMapping(value = "/Digester", method = RequestMethod.POST)
    public String xxe_Digester(HttpServletRequest request) {
        try {
            String xml_con = WebUtils.getRequestBody(request);
            System.out.println(xml_con);

            Digester digester = new Digester();
            digester.parse(new StringReader(xml_con));  // parse xml

            return "test";
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }

    @RequestMapping(value = "/Digester_fix", method = RequestMethod.POST)
    public String xxe_Digester_fix(HttpServletRequest request) {
        try {
            String xml_con = WebUtils.getRequestBody(request);
            System.out.println(xml_con);

            Digester digester = new Digester();
            digester.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            digester.setFeature("http://xml.org/sax/features/external-general-entities", false);
            digester.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            digester.parse(new StringReader(xml_con));  // parse xml

            return "test";
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }


    // 有回显的XXE
    @RequestMapping(value = "/DocumentBuilder_return", method = RequestMethod.POST)
    public String xxeDocumentBuilderReturn(HttpServletRequest request) {
        try {
            String xml_con = WebUtils.getRequestBody(request);
            System.out.println(xml_con);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(xml_con);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);  // parse xml

            // 遍历xml节点name和value
            StringBuffer buf = new StringBuffer();
            NodeList rootNodeList = document.getChildNodes();
            for (int i = 0; i < rootNodeList.getLength(); i++) {
                Node rootNode = rootNodeList.item(i);
                NodeList child = rootNode.getChildNodes();
                for (int j = 0; j < child.getLength(); j++) {
                    Node node = child.item(j);
                    buf.append(node.getNodeName() + ": " + node.getTextContent() + "\n");
                }
            }
            sr.close();
            System.out.println(buf.toString());
            return buf.toString();
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }


    @RequestMapping(value = "/DocumentBuilder", method = RequestMethod.POST)
    public String DocumentBuilder(HttpServletRequest request) {
        try {
            String xml_con = WebUtils.getRequestBody(request);
            System.out.println(xml_con);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(xml_con);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);  // parse xml

            // 遍历xml节点name和value
            StringBuffer result = new StringBuffer();
            NodeList rootNodeList = document.getChildNodes();
            for (int i = 0; i < rootNodeList.getLength(); i++) {
                Node rootNode = rootNodeList.item(i);
                NodeList child = rootNode.getChildNodes();
                for (int j = 0; j < child.getLength(); j++) {
                    Node node = child.item(j);
                    // 正常解析XML，需要判断是否是ELEMENT_NODE类型。否则会出现多余的的节点。
                    if (child.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        result.append(node.getNodeName() + ": " + node.getFirstChild().getNodeValue() + "\n");
                    }
                }
            }
            sr.close();
            System.out.println(result.toString());
            return result.toString();
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }


    @RequestMapping(value = "/DocumentBuilder_fix", method = RequestMethod.POST)
    public String xxe_DocumentBuilder_fix(HttpServletRequest request) {
        try {
            String xml_con = WebUtils.getRequestBody(request);
            System.out.println(xml_con);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
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
    public String xxe_xinclude_DocumentBuilder(HttpServletRequest request) {
        try {
            String xml_con = WebUtils.getRequestBody(request);
            System.out.println(xml_con);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setXIncludeAware(true);   // 支持XInclude
            dbf.setNamespaceAware(true);  // 支持XInclude
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
                    // 测试不能blind xxe，所以强行加了一个回显
                    System.out.println("xxeNode: " + xxeNode.getNodeValue());
                }

            }

            sr.close();
            return "test";
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }


    @RequestMapping(value = "/DocumentBuilder_xinclude_fix", method = RequestMethod.POST)
    public String xxe_xinclude_DocumentBuilder_fix(HttpServletRequest request) {
        try {
            String xml_con = WebUtils.getRequestBody(request);
            System.out.println(xml_con);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            dbf.setXIncludeAware(true);   // 支持XInclude
            dbf.setNamespaceAware(true);  // 支持XInclude
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
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
                    // 测试不能blind xxe，所以强行加了一个回显
                    System.out.println("xxeNode: " + xxeNode.getNodeValue());
                }

            }

            sr.close();
            return "test";
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }


    @PostMapping("/XMLReader/vul")
    public String XMLReaderVul(HttpServletRequest request) {
        try {
            String xml_con = WebUtils.getRequestBody(request);
            System.out.println(xml_con);
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser saxParser = spf.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.parse(new InputSource(new StringReader(xml_con)));
            return "test";
        } catch (Exception e) {
            System.out.println(e.toString());
            return "except";
        }
    }


    @PostMapping("/XMLReader/fixed")
    public String XMLReaderSec(HttpServletRequest request) {
        try {
            String xml_con = WebUtils.getRequestBody(request);
            System.out.println(xml_con);
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser saxParser = spf.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            xmlReader.setFeature("http://xml.org/sax/features/external-general-entities", false);
            xmlReader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            xmlReader.parse(new InputSource(new StringReader(xml_con)));
            return "test";
        } catch (Exception e) {
            System.out.println(e.toString());
            return "except";
        }
    }


    public static void main(String[] args) throws Exception {

    }

}