package com.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.StringTokenizer;

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
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class XMLUtil {

	/**
	 * Function to convert String to Document
	 * @param xmlString
	 * @return Document
	 */
	public static Document getXMLDocument(String xmlString) {
		try {
			if (xmlString != null) {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				/**				documentBuilderFactory.setValidating(true);
				documentBuilderFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
				documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities ",false);
				documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);*/
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());
				return documentBuilder.parse(inputStream);
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Function to Convert Document to String
	 * @param xmlString
	 * @return String
	 */
	public static String getXMLText(Document xmlString) {
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

	/**
	 * Function to get tag/attribute value from Document
	 * parent/tag to get tag value	or  parent@attribute to get attribute value 
	 * @param document
	 * @param pathString
	 * @return value
	 */
	public static String getValueFromDom(Document document, String inputString) {
		try {
			StringTokenizer stringTokenizer=null;
			stringTokenizer=new StringTokenizer(inputString,"/");
			if(stringTokenizer.countTokens()>1) {
				return getTagElementValue(document,stringTokenizer.nextToken(),stringTokenizer.nextToken());
			}
			
			stringTokenizer=new StringTokenizer(inputString,"@");
			if(stringTokenizer.countTokens()>1) {
				return getAttributeValue(document,stringTokenizer.nextToken(),stringTokenizer.nextToken());
			}
		}
		catch(Exception ex) {
			
		}
		return null;
	}
	
	/**
	 * Function to modify tag or attribute value
	 * parent/tag to modify tag element value  or parent@attribute to modify attribute value
	 * @param document
	 * @param pathString
	 * @param pathValue
	 */
	public static void setValueInDom(Document document, String pathString, String pathValue) {
		try {
			StringTokenizer stringTokenizer=null;
			stringTokenizer=new StringTokenizer(pathString,"/");
			if(stringTokenizer.countTokens()>1) {
				setTagElementValue(document,stringTokenizer.nextToken(),stringTokenizer.nextToken(),pathValue);
				return;
			}
			
			stringTokenizer=new StringTokenizer(pathString,"@");
			if(stringTokenizer.countTokens()>1) {
				setAttributeValue(document,stringTokenizer.nextToken(),stringTokenizer.nextToken(),pathValue);
				return;
			}
		}
		catch(Exception ex) {
			
		}
	}

	public static void addAttribute(Document document, String parentTag, String attributeName,String attributeValue) {
		try {
			NodeList nodeList=document.getElementsByTagName(parentTag);
			Node node;
			if(nodeList.getLength()>0) {
				node=nodeList.item(nodeList.getLength()-1);
			}
			else {
				node=nodeList.item(0);
			}	

			String attrVal="";
			if(attributeValue!=null) {
				attrVal=attributeValue;
			}
			Element nodeElement = (Element)node;
			nodeElement.setAttribute(attributeName, attrVal);
		}
		catch(Exception ex) {
			
		}
	}
	
	/**
	 * Function to get Attribute value
	 * @param document
	 * @param Parent tagName
	 * @param attributeName
	 * @return value
	 */
	public static String getAttributeValue(Document document, String tagName, String attributeName) {
		String retVal=null;
		NodeList list= document.getElementsByTagName(tagName);
		if(list!=null) {
			Node node= list.item(0);
			if(node!=null) {
				NamedNodeMap namedNodeMap=node.getAttributes();
				Node tempNode=namedNodeMap.getNamedItem(attributeName);
				if(tempNode!=null) {
					return tempNode.getNodeValue();
				}
			}
		}
		return retVal;
	}
	
	/**
	 * Function to modify attribute value in Document
	 * @param document
	 * @param tagName
	 * @param attributeName
	 * @param attributeVal
	 */
	public static void setAttributeValue(Document document, String tagName, String attributeName, String attributeVal) {
		try {
			NodeList nodeList=document.getElementsByTagName(tagName);
			Node node=nodeList.item(0);
			NamedNodeMap nodeMap = node.getAttributes();
			Node attr=nodeMap.getNamedItem(attributeName);
			if(attr!=null) {
				attr.setNodeValue(attributeVal);
			}
		}
		catch(Exception ex) {
			
		}
	}
	
	/**
	 * Function to remove Attribute from Document
	 * @param document
	 * @param tagName
	 * @param attributeName
	 */
	public static void removeAttribute(Document document, String tagName, String attributeName) {
		try {
			Element element = (Element) document.getElementsByTagName(tagName).item(0);
			boolean hasAttribute=element.hasAttribute(attributeName);
			if(hasAttribute) {
				element.removeAttribute(attributeName);
			}
		}
		catch(Exception ex) {
			
		}
	}
	
	/**
	 * Function to add tag to Document
	 * @param document
	 * @param parentTag
	 * @param nodeName
	 * @param nodeValue
	 */
	public static void addTagElement(Document document, String parentTag, String nodeName, String nodeValue) {
		try {
			NodeList nodeList=document.getElementsByTagName(parentTag);
			
			if(nodeList==null) {
				return ;
			}
			
			Node node=nodeList.item(0);
			Element element = document.createElement(nodeName);
			
			String newNodeValue;
			
			if(nodeValue==null) {
				newNodeValue="";
			}
			else {
				newNodeValue=nodeValue;
			}
			
			Text text=document.createTextNode(newNodeValue);
			element.appendChild(text);
			node.appendChild(element);
		}
		catch(Exception ex ) {
			
		}
	}
	
	/**
	 * Function to get tag element value from document
	 * @param document
	 * @param parentTag
	 * @param childTag
	 * @return value
	 */
	public static String getTagElementValue(Document document, String parentTag, String childTag) {
		if (parentTag == null || childTag == null) {
			return null;
		}

		XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			XPathExpression expr = xPath.compile("//" + parentTag + "/" + childTag);
			NodeList nl = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
			if (nl.getLength() > 0) {
				Node currentItem = nl.item(0);
				return currentItem.getTextContent();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Function to modify tag element value
	 * @param document
	 * @param parentTag
	 * @param tagName
	 * @param tagValue
	 */
	public static void setTagElementValue(Document document, String parentTag, String tagName, String tagValue) {
		try {
			NodeList nodeList=document.getElementsByTagName(parentTag);
			Node node=nodeList.item(0);
			NodeList childNodeList=node.getChildNodes();
			
			for(int i=0;i<childNodeList.getLength();i++) {
				Node childNode=childNodeList.item(i);
				if(childNode.getNodeName().equalsIgnoreCase(tagName)) {
					childNode.setTextContent(tagValue);
					//childNode.setNodeValue(tagValue);
					break;
				}
			}
		}
		catch(Exception ex) {
			
		}
	}

	/**
	 * Function to remove tag from Document
	 * @param document
	 * @param parentTag
	 * @param childTag
	 */
	public static void removeTagElement(Document document, String parentTag, String childTag) {
		if (parentTag == null || childTag == null) {
			return;
		}

		XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			XPathExpression expr = xPath.compile("//" + parentTag + "/" + childTag);
			NodeList nl = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
			if (nl.getLength() > 0) {
				Node currentItem = nl.item(0);
				currentItem.getParentNode().removeChild(currentItem);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * Function to get Formatted XML with indentation 2
	 * @param document
	 * @return formatted XML
	 */
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

/**public static String getAttributesValue(Document document, String tagName, String attribute) {
	XPath xPath = XPathFactory.newInstance().newXPath();
	try {
		XPathExpression expr = xPath.compile("//" + tagName + "[@" + attribute + "]");
		NodeList nl = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

		if (nl.getLength() > 0) {
			Node currentItem = nl.item(0);
			return currentItem.getAttributes().getNamedItem(attribute).getNodeValue();
		}
	} catch (Exception exception) {
		exception.printStackTrace();
	}
	return null;
}
public static void setAttributeValue(Document document, String tagName, String attribute, String nodeValue) {
	XPath xPath = XPathFactory.newInstance().newXPath();
	try {
		XPathExpression expr = xPath.compile("//" + tagName + "[@" + attribute + "]");
		NodeList nl = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

		if (nl.getLength() > 0) {
			Node currentItem = nl.item(0);
			currentItem.getAttributes().getNamedItem(attribute).setNodeValue(nodeValue);
		}
	} catch (Exception exception) {
		exception.printStackTrace();
	}
}

public static void removeAttribute(Document document, String tagName, String attribute) {
	XPath xPath = XPathFactory.newInstance().newXPath();
	try {
		XPathExpression expr = xPath.compile("//" + tagName + "[@" + attribute + "]");
		NodeList nl = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

		if (nl.getLength() > 0) {
			Node currentItem = nl.item(0);
			currentItem.getAttributes().removeNamedItem(attribute);
		}
	} catch (Exception exception) {
		exception.printStackTrace();
	}
}*/