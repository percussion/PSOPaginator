package com.percussion.pso.paginator;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HTMLPageCounter extends DefaultHandler
{
	
	/**
	    * Logger for this class
	*/
	private static final Log log = LogFactory.getLog(HTMLPageCounter.class);

	
	// Initial values
	private static int numPages = 1;

    public static int getCount(InputSource in)
    {           	
    	// Reset
    	numPages = 1;
    	
    	// New event handler
        DefaultHandler saxHandler = new HTMLPageCounter();
               
        try 
        {
            // Create SAX parser
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            
            // Parse the html converted to xhtml
            saxParser.parse( in, saxHandler);
            
            return numPages;
        } 
        catch (Throwable t) 
        {        	
            log.error("Counter - Sax Parser main process: " + t.toString());
            return 0;
        }
    }
    
    //===========================================================
    // SAX DocumentHandler methods
    //===========================================================

    public void processingInstruction(String target, String data)
    throws SAXException
    {    	
        if ( target.equals("pageBreak") )
        	numPages++; 
    }
}
