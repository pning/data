package data.util;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ReadXML {
	public static String readvalue(String name) {
		String value = null;
		File inputXml = new File("resource/main/java/parameter.xml");
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(inputXml);
			Element parameters = document.getRootElement();
			for (Iterator i = parameters.elementIterator(); i.hasNext();) {
				Element parameter = (Element) i.next();
				for (Iterator j = parameter.elementIterator(); j.hasNext();) {
					Element node = (Element) j.next();
					if (name.equals(node.getText())) {
						Element n = (Element) j.next();
						value = n.getText();
					}
				}
			}
		} catch (DocumentException e) {
			System.out.println(e.getMessage());
		}
		return value;
	}
	// public static void main(String[] args) {
	// System.out.println(readvalue("PRIVATE_KEY"));
	// }
}
