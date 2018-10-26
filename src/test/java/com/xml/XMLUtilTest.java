package com.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

public class XMLUtilTest {
	private static final String TAG_PATH="catalog_item/item_number";
	private static final String  TAG_NAME="item_number";
	private static final String  ATTRIBUTE_NAME="gender";
	private static final String  PARENT_TAG="catalog_item";
	private static final String  ATTRIBUTE_PATH="catalog_item@gender";
	private static final String  VALUE="Modified Value";
	
	private Document document;
	@Before
	public void setup() {
		document=XMLUtil.getXMLDocument(getMessage());
	}
	
	@Test
	public  void testGetXMLDocument() {
		assertNotNull(XMLUtil.getXMLDocument(getMessage()));
	}

	@Test
	public  void testGetXMLText() {
		assertNotNull(XMLUtil.getXMLText(document));
	}

	@Test
	public  void testGetValueFromDomTag() {
		assertNotNull(XMLUtil.getValueFromDom(document, TAG_PATH));
	}
	
	@Test
	public  void testGetValueFromDomAttribute() {
		assertNotNull(XMLUtil.getValueFromDom(document, ATTRIBUTE_PATH));
	}
	
	@Test
	public  void testSetValueInDomTag() {
		XMLUtil.setValueInDom(document, TAG_PATH, VALUE);
		assertEquals(XMLUtil.getValueFromDom(document, TAG_PATH), VALUE);	
	}
	
	@Test
	public  void testSetValueInDomAttribute() {
		XMLUtil.setValueInDom(document, ATTRIBUTE_PATH, VALUE);
		assertEquals(XMLUtil.getValueFromDom(document, ATTRIBUTE_PATH), VALUE);	
	}

	@Test
	public  void testAddAttribute() {
		String attrName="NewAttribute";
		XMLUtil.addAttribute(document, PARENT_TAG, attrName, VALUE);
		assertNotNull(XMLUtil.getAttributeValue(document, PARENT_TAG, attrName));
	}
	
	@Test
	public  void testGetAttributeValue() {
		assertNotNull(XMLUtil.getAttributeValue(document, PARENT_TAG, ATTRIBUTE_NAME));
	}
	
	@Test
	public  void testSetAttributeValue() {
		XMLUtil.setAttributeValue(document, PARENT_TAG, ATTRIBUTE_NAME, VALUE);
		assertEquals(XMLUtil.getValueFromDom(document, ATTRIBUTE_PATH), VALUE);	
	}

	@Test
	public  void testRemoveAttribute() {
		XMLUtil.removeAttribute(document, PARENT_TAG, ATTRIBUTE_NAME);
		assertNull(XMLUtil.getValueFromDom(document, ATTRIBUTE_PATH));	
	}
	
	@Test
	public  void testAddTagElement() {
		String newTag="NewTag";
		XMLUtil.addTagElement(document, PARENT_TAG, newTag, VALUE);
		assertNotNull(XMLUtil.getValueFromDom(document, PARENT_TAG+"/"+newTag));
	}
	
	@Test
	public  void testGetTagElementValue() {
		assertNotNull(XMLUtil.getTagElementValue(document, PARENT_TAG, TAG_NAME));
	}
	
	@Test
	public  void testSetTagEelementValue() {
		XMLUtil.setTagElementValue(document, PARENT_TAG, TAG_NAME, VALUE);
		assertEquals(XMLUtil.getTagElementValue(document, PARENT_TAG, TAG_NAME),VALUE);
	}

	@Test
	public  void testRemoveTagElement() {
		XMLUtil.removeTagElement(document, PARENT_TAG, TAG_NAME);
		assertNull(XMLUtil.getTagElementValue(document, PARENT_TAG, TAG_NAME));
	}
	
	@Test
	public  void testGetFormattedXml() {
		assertNotNull(XMLUtil.getFormattedXml(document));
	}
	
	public String getMessage() {
	return "<catalog>"+
			   "<product description=\"Cardigan Sweater\" product_image=\"cardigan.jpg\">"+
			      "<catalog_item gender=\"Mens\">"+
			         "<item_number no=\"10\">QWZ5671</item_number>"+
			         "<price>39.95</price>"+
			         "<size description=\"Medium\">"+
			            "<color_swatch image=\"red_cardigan.jpg\">Red</color_swatch>"+
			            "<color_swatch image=\"burgundy_cardigan.jpg\">Burgundy</color_swatch>"+
			         "</size>"+
			      "</catalog_item>"+
			   "</product>"+
			"</catalog>";
	}
}