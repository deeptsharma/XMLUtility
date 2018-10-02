package com.xml;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLUtil {
	public static Document convertStringToDocument(String inputString) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			return documentBuilder.parse(new InputSource(new StringReader(inputString)));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String convertDocumentToString(Document xmlString) {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			StringWriter stringWriter = new StringWriter();
			transformer.transform(new DOMSource(xmlString), new StreamResult(stringWriter));
			return stringWriter.getBuffer().toString();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<String> getAttributeValue(Document document, String tagName, String attribute) {
		List<String> attributeVaueList=new ArrayList<>();
		XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			XPathExpression expr = xPath.compile("//" + tagName + "[@" + attribute + "]");
			NodeList nl = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

			for (int i = 0; i < nl.getLength(); i++) {
				Node currentItem = nl.item(i);
				String key = currentItem.getAttributes().getNamedItem(attribute).getNodeValue();
				attributeVaueList.add(key);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return attributeVaueList;
	}

	public static  List<String> getTagElementValue(Document document, String parentTag, String childTag){
		List<String> tagValueList=new ArrayList<>();
		
		
		
		return tagValueList;
	}
	
	public static String getFormattedXml(Document document) {
		Transformer transformer;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(document);
			transformer.transform(source, result);
			return result.getWriter().toString();
		} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return null;
	}
}
