/*
 * 
 * com.percussion.pso.paginator Paginator.java
 *  
 * @author Jesus Garcia
 *
 * This extension creates pages from content fields, using the 
 * <?pageBreak?> tag as page delimiter.
 * <br><br>
 * In Ephox edit control, <?pageBreak?> tags are inserted inside the html. 
 * <br><br>
 * This extension assumes the html being sent as parameter to its methods
 * is well formed.
 * <br><br>
 * Two methods are provided: One for counting the pages and another for getting the 
 * specified page.
 * <br><br>
 * Both methods use SAX event handlers, but the counting method only looks for the 
 * number of  <?pageBreak?> tags to calculate the number of pages.
 * 
 */

package com.percussion.pso.paginator;

import java.io.StringReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;

import com.percussion.extension.IPSJexlExpression;
import com.percussion.extension.IPSJexlMethod;
import com.percussion.extension.IPSJexlParam;
import com.percussion.extension.PSJexlUtilBase;
import org.apache.commons.lang.StringUtils;


public class Paginator extends PSJexlUtilBase implements IPSJexlExpression
{
   /**
    * Logger for this class
    */
   private static final Log log = LogFactory.getLog(Paginator.class);
   
   /**
    * 
    */
   public Paginator()
   {
      
   }

   @IPSJexlMethod(description="Count the number of page breaks", params={
		   @IPSJexlParam(name="fieldContent", description="Page content") } ) 
	    public String getPageCount(String fieldContent)
	    {
	    	try
	    	{
	    		InputSource in = createInputStreamXML(fieldContent);
	    		int pageCount = HTMLPageCounter.getCount(in);	    		
	    		return String.valueOf(pageCount);
	       	} 
	    	catch (Exception e)
	       {
	          log.error("Error getting content", e); 
	          return ""; 
	       }
	    }
   
    @IPSJexlMethod(description="Get content from specified page number", params={
          @IPSJexlParam(name="fieldContent", description="Page content"),
          @IPSJexlParam(name="pageNumber", description="Page number") }) 
    public String getPage(String fieldContent, String pageNumber)
    {
    	if (StringUtils.isBlank(pageNumber) || !StringUtils.isNumeric(pageNumber) )
		{
			return fieldContent;
		}
    	try
    	{
    		InputSource in = createInputStreamXML(fieldContent);    		
    		String pageHTML = HTMLPaginator.getHTMLPage(in, Integer.parseInt(pageNumber));
    		return pageHTML;
       	}
    	catch (Exception e)
       {
          log.error("Error getting content = ", e);    
          return "";
       }
    	 
    }
    /*
     *  To create an Input source for the SAX parser
     */
    private InputSource createInputStreamXML(String htmlString)
    {
    	try
    	{     		
    		// Create InputSource, specify encoding
    		InputSource in = new InputSource(new StringReader(htmlString));
    		in.setEncoding("UTF-8");
    		return in;
    	}
    	catch (Exception e)
    	{
    		log.error("Error creating InputSource: " + e.toString());
    		return null;
    	}
    }
  }


