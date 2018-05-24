package org.joychou.controller;

import com.sun.corba.se.impl.ior.OldJIDLObjectKeyTemplate;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import org.w3c.dom.Document;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.XMLReader;
import java.io.StringReader;
import java.net.URL;

import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.helpers.DefaultHandler;
import org.apache.commons.digester3.Digester;

/**
 * author: JoyChou (joychou@joychou.org)
 * date:   2017.12.22
 * desc:   java xxe vuls fix code
 */

@Controller
@RequestMapping("/xxe")
public class XMLInjection {

    @RequestMapping("/xmlReader")
    @ResponseBody
    public static String xxe_xmlReader(HttpServletRequest request) {
        try {
            String xml_con = request.getParameter("xml").toString();
            System.out.println(xml_con);
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            xmlReader.setFeature("http://xml.org/sax/features/external-general-entities", false);
            xmlReader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            xmlReader.parse( new InputSource(new StringReader(xml_con)) );
            return "ok";
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }


    @RequestMapping("/SAXParser")
    @ResponseBody
    public static String xxe_SAXParser(HttpServletRequest request) {
        try {
            String xml_con = request.getParameter("xml").toString();
            System.out.println(xml_con);
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            spf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            SAXParser parser = spf.newSAXParser();
            // parse xml
            parser.parse(new InputSource(new StringReader(xml_con)), new DefaultHandler());
            return "test";
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }

    @RequestMapping("/Digester")
    @ResponseBody
    public static String xxe_Digester(HttpServletRequest request) {
        try {
            String xml_con = request.getParameter("xml").toString();
            System.out.println(xml_con);
            Digester digester = new Digester();
            digester.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            digester.setFeature("http://xml.org/sax/features/external-general-entities", false);
            digester.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            digester.parse(new StringReader(xml_con));
            return "test";
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }


    @RequestMapping("/DocumentBuilder")
    @ResponseBody
    public static String xxe_DocumentBuilder(HttpServletRequest request) {
        try {
            String xml_con = request.getParameter("xml").toString();
            System.out.println(xml_con);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(xml_con);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);
            sr.close();
            return "test";
        } catch (Exception e) {
            System.out.println(e);
            return "except";
        }
    }



}
