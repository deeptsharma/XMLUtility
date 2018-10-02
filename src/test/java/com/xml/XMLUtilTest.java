package com.xml;

import org.junit.Test;
import org.w3c.dom.Document;

public class XMLUtilTest {
	
	@Test
	public void test() {
		Document document=XMLUtil.convertStringToDocument(getMessage());
		XMLUtil.getTagElementValue(document,"name","id");
	}
	public String getMessage() {
	return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"+
            "<Emp id=\"1\"><name  id=\"10\">Pankaj</name><age>25</age>\n"+
            "<role>Developer</role><gen>Male</gen></Emp>";
	}
}
