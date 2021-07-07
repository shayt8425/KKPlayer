package com.zhao.KKPlayer.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

/**
 * 提供了访问xml文件的相关方法
 * @author Administrator
 *
 */
public class XmlUtil {

	public static Document loadXml(String xmlFile,URL schemaUrl)throws Exception{
		SAXBuilder builder =new SAXBuilder();
		builder.setFeature(
				"http://apache.org/xml/features/validation/schema",	true);
		builder.setFeature(
				"http://xml.org/sax/features/validation",  true);
		builder.setFeature(
				"http://xml.org/sax/features/namespaces", true);
		builder.setProperty(
				"http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation",
				schemaUrl.toString());
		builder.setFeature(
				"http://apache.org/xml/features/validation/schema-full-checking",
                true);
		FileInputStream fis = new FileInputStream(xmlFile);
		Document document = builder.build(fis);
		fis.close();
		return document;
	}
	
	public static int getAutoId(String type,Document doc) throws JDOMException {
		String xpath = null;
		int id = 0;
		if ("PlayList".equals(type)) {
			xpath = "//PlayList";
		} else if ("Music".equals(type)) {
			xpath = "//Music";
		}
		XPath path = XPath.newInstance(xpath);
		List<?> elements = path.selectNodes(doc.getRootElement());
		for (Iterator<?> i = elements.iterator(); i.hasNext();) {
			Element e = (Element) i.next();
			int temp = Integer.parseInt(e.getAttributeValue("id")) ;
			id = Math.max(temp, id);
		}
		return id + 1;
	}

	public static Element findElement(String xpath,Document doc) throws JDOMException {
             XPath xPath = XPath.newInstance(xpath);
             return (Element) xPath.selectSingleNode(doc.getRootElement());
	}
	
	public static void writeXMLFile(Document doc,String xmlPath) throws IOException{
		OutputStream out = new FileOutputStream(xmlPath);
		Format format = Format.getPrettyFormat();
		format.setEncoding("UTF-8");
		XMLOutputter outputter = new XMLOutputter(format);
		outputter.output(doc, out);
		out.flush();
		out.close();
	}
}
