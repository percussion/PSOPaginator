package com.percussion.pso.paginator;

import java.util.ListIterator;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HTMLPaginator extends DefaultHandler
{
	
	/**
	    * Logger for this class
	*/
	private static final Log log = LogFactory.getLog(HTMLPaginator.class);

	
	// Initial values
	private static int numPages = 1;
	private static String resultHTML = "";
	private static Stack<HTMLTag> htmlTags = new Stack<HTMLTag>();	
	private static int currentPage;
	
    public static String getHTMLPage(InputSource in, int page)
    {        
    	
    	setResultHTML("");
    	currentPage = page;
    	htmlTags = new Stack<HTMLTag>();
    	numPages = 1;
    	
    	// New event handler
        DefaultHandler saxHandler = new HTMLPaginator();
               
        try 
        {
            // Create SAX parser
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            
            // Parse the html converted to xhtml
            saxParser.parse( in, saxHandler);
            
            return getResultHTML();
        } 
        catch (Throwable t) 
        {        	
            log.error("Sax Parser main process: " + t.toString());
            return null;
        }
    }
    
    //===========================================================
    // SAX DocumentHandler methods
    //===========================================================
    public void startDocument()
    	throws SAXException
    {
    	setResultHTML("");
    }

    public void endDocument()
    throws SAXException
    {       
        try 
        {        	        	
         	while ( !htmlTags.empty()  )
        	{        		
        		HTMLTag h = htmlTags.pop();
        		
        		// First condition is for text nodes 
        		// Second is for all the tags opened before or during current page being
        		// closed during or after current page (both conditions are checked)
    			if ( ( h.getTagName().equals("") && h.getClosedInPage() == currentPage )  || 
    					( h.getOpenedInPage() <= currentPage && h.getClosedInPage() >= currentPage ) )
    				setResultHTML( h.getTagText() + getResultHTML() ) ;
        	}
        } 
        catch (Exception e) 
        {
            throw new SAXException("On End Document: ", e);
        }
    }

    public void processingInstruction(String target, String data)
    throws SAXException
    {    	
        if ( target.equals("pageBreak") )
        	numPages++; 
    }

    
    public void startElement(String namespaceURI,
                             String lName, 
                             String qName, 
                             Attributes attrs)
    throws SAXException
    {
    	try
    	{    	
    		// Look for tag and attributes
			String eName = lName;
			eName = qName; 		        
			String resTag = "<" + eName;		        
			if (attrs != null) 
			{
			    for (int i = 0; i < attrs.getLength(); i++) {
			        String aName = attrs.getLocalName(i); // Attr name 
			        if ("".equals(aName)) 
			        	aName = attrs.getQName(i);
			        // Set text with attributes
			        resTag += " " + aName + "=\"" + attrs.getValue(i) + "\"";
			    }
			}                	
			resTag += ">";

		   // Save in stack
		   addNewHTMLTag(eName, resTag, numPages, 0);
    	}
    	catch(Exception e)
    	{
    		log.error(e.toString());
    	}
    }
    public void endElement(String namespaceURI,
                           String sName, 
                           String qName
                          )
    throws SAXException
    {   
    	// Page default for closing tag
    	int openedInPage = numPages;
    	
    	// Look for the most recent not-yet-closed tag inserted.
        ListIterator it = htmlTags.listIterator(htmlTags.size());
        
        while ( it.hasPrevious() )
        {
        	HTMLTag h = (HTMLTag) it.previous();    	
        	
    		// Check if is already closed
	    	if ( h.getClosedInPage() == 0 ) // Not yet closed
	    	{
	    		// Ser current page as the page where tag was closed
	    		h.setClosedInPage(numPages);
	    		// Save the value for the closing tag
	    		openedInPage = h.getOpenedInPage();  
	    		break;
	    	}
        }
        
        // Closing tag
        addNewHTMLTag(qName, "</" + qName + ">", openedInPage, numPages);
    }

    public void characters(char buf[], int offset, int len)
    throws SAXException
    {
    	// Text nodes
    	String s = new String(buf, offset, len);
    	addNewHTMLTag("", s, numPages, numPages);    	
	 }

    private void addNewHTMLTag(String tagName, String tagText, int openedInPage, int closedInPage)
    {
    	// Add a new tag to the stack
    	HTMLTag h1 =  new HTMLTag();
		h1.setTagName(tagName);
		h1.setTagText(tagText);
		h1.setOpenedInPage(openedInPage);
		h1.setClosedInPage(closedInPage); 
		htmlTags.push(h1);
    }
    
	/**
	 * @return Returns the resultHTML.
	 */
	private static String getResultHTML() 
	{
		return resultHTML;
	}
	/**
     * Sets the HTML result. 
	 * @param res The resultHTML to set.
	 */
	private static void setResultHTML(String res) 
	{
		resultHTML = res;
	}		
}
