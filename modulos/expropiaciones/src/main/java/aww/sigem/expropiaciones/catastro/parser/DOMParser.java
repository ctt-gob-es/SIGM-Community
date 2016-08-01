package aww.sigem.expropiaciones.catastro.parser;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import aww.sigem.expropiaciones.rule.xmlcatastro.ComprobarXMLCatastroRule;

public class DOMParser
{
	public static final Logger logger = Logger.getLogger(DOMParser.class);
	private Document doc = null;
	
	public DOMParser()
	{
		try
		{
			doc = parserXML(new File("SCLTI140071691101 Buena.XML"));
			
			visit(doc, 0);
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
	}
	
	public void visit(Node node, int level)
	{
		NodeList nl = node.getChildNodes();
		
		for(int i=0, cnt=nl.getLength(); i<cnt; i++)
		{
			logger.warn("["+nl.item(i)+"]");
			
			visit(nl.item(i), level+1);
		}
	}
	
	public Document parserXML(File file) throws SAXException, IOException, ParserConfigurationException
	{
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
	}
	
	public static void main(String[] args)
	{
		new DOMParser();
	}
}

