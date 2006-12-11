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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;
import com.percussion.extension.IPSJexlExpression;
import com.percussion.extension.IPSJexlMethod;
import com.percussion.extension.IPSJexlParam;
import com.percussion.extension.PSJexlUtilBase;
import com.percussion.pso.jexl.PSOSlotTools;
import com.percussion.services.assembly.IPSAssemblyItem;
import com.percussion.services.assembly.IPSAssemblyResult;
import com.percussion.services.assembly.IPSAssemblyService;
import com.percussion.services.assembly.PSAssemblyServiceLocator;


public class Paginator extends PSJexlUtilBase implements IPSJexlExpression
{
   /**
    * Logger for this class
    */
   private static final Log log = LogFactory.getLog(Paginator.class);
   
   private static IPSAssemblyService asm = null;
   
   private static void initServices()
   {
      if(asm == null)
        asm = PSAssemblyServiceLocator.getAssemblyService();
   }
   /**
    * 
    */
   public Paginator()
   {
      
   }
   @IPSJexlMethod(description="Count the number of page breaks", params={
           @IPSJexlParam(name="fieldContent", description="Page content") } )
           @Deprecated
        public String getPageCount(String fieldContent)
        {
            return String.valueOf(getBodyPageCount(fieldContent)); 
        }
   
   @IPSJexlMethod(description="Count the number of page breaks", params={
		   @IPSJexlParam(name="fieldContent", description="Page content") } ) 
	    public int getBodyPageCount(String fieldContent)
	    {
	    	try
	    	{
	    		InputSource in = createInputStreamXML(wrapString(fieldContent));

	    		int pageCount = HTMLPageCounter.getCount(in);	    		
	    		return pageCount;
	       	} 
	    	catch (Exception e)
	       {
	          log.error("Error getting content", e); 
	          return 0; 
	       }
	    }
   
    @IPSJexlMethod(description="Get content from specified page number", params={
          @IPSJexlParam(name="fieldContent", description="Page content"),
          @IPSJexlParam(name="pageNumber", description="Page number") })
    @Deprecated      
    public String getPage(String fieldContent, String pageNumber)
    {
    	if (StringUtils.isBlank(pageNumber) || !StringUtils.isNumeric(pageNumber) )
		{
			return fieldContent;
		}
        return getBodyPageN(fieldContent, Long.valueOf(fieldContent)); 
    	 
    }
    
    @IPSJexlMethod(description="Get content from specified page number", params={
          @IPSJexlParam(name="fieldContent", description="Page content"),
          @IPSJexlParam(name="pageNumber", description="Page number") }) 
    public String getBodyPage(String fieldContent, String pageNumber)
    {       
       Long pno = new Long(0); 
       if(StringUtils.isNotBlank(pageNumber))
       {
          if(!StringUtils.isNumeric(pageNumber))
          {
             String emsg = "Page Number must be numeric - " + pageNumber;
             log.error(emsg); 
             throw new IllegalArgumentException(emsg); 
          }
          pno = new Long(pageNumber);
       }           
       return getBodyPageN(fieldContent,pno);
    }
    
    @IPSJexlMethod(description="Get content from specified page number", params={
          @IPSJexlParam(name="fieldContent", description="Page content"),
          @IPSJexlParam(name="pageNumber", description="Page number") }) 
    public String getBodyPageN(String fieldContent, Long pageNumber)
    {
        if (pageNumber == null || pageNumber == 0 )
        {
            return fieldContent;
        }
        try
        {
            InputSource in = createInputStreamXML(wrapString(fieldContent));            
            String pageHTML = HTMLPaginator.getHTMLPage(in, pageNumber.intValue());
            return unwrapString(pageHTML);
        }
        catch (Exception e)
       {
          log.error("Error getting content = ", e);    
          return "";
       }
         
    }

    @IPSJexlMethod(description="Get slot contents for a specified page number", params={
          @IPSJexlParam(name="item", description="current item"),
          @IPSJexlParam(name="slot", description="slot to paginate"),
          @IPSJexlParam(name="params", description="assembly parameters for slot"), 
          @IPSJexlParam(name="pageSize", description="number of items per page"), 
          @IPSJexlParam(name="pageNumber", description="Page number") }) 
    public List<IPSAssemblyResult> getSlotPage(IPSAssemblyItem item, String slot,
          Map<String,Object> params, String pageSize, String pageNumber) 
             throws Throwable
    {
       if(StringUtils.isBlank(pageSize)||!StringUtils.isNumeric(pageSize))
       {
          String emsg = "Pagesize must be numeric - " + pageSize; 
          log.error(emsg); 
          throw new IllegalArgumentException(emsg); 
       }
       Long pageSz = new Long(pageSize);
       
       Long pageNo = new Long(0);
       if(StringUtils.isBlank(pageNumber))
       {
          log.debug("page number is blank");       
       }
       else if(!StringUtils.isNumeric(pageSize))
       {
          String emsg = "Pagesize must be numeric - " + pageSize; 
          log.error(emsg); 
          throw new IllegalArgumentException(emsg); 
       }
       else
       {
          pageNo = new Long(pageNumber);
       }
       
       return getSlotPageN(item, slot, params, pageSz, pageNo);
    }
    @IPSJexlMethod(description="Get slot contents for a specified page number", params={
          @IPSJexlParam(name="item", description="current item"),
          @IPSJexlParam(name="slot", description="slot to paginate"),
          @IPSJexlParam(name="params", description="assembly parameters for slot"), 
          @IPSJexlParam(name="pageSize", description="number of items per page"), 
          @IPSJexlParam(name="pageNumber", description="Page number") }) 
    public List<IPSAssemblyResult> getSlotPageN(IPSAssemblyItem item, String slot,
          Map<String,Object> params, Long pageSize, Long pageNumber) 
             throws Throwable
    {
         initServices(); 
         
         PSOSlotTools tools = new PSOSlotTools();
         List<IPSAssemblyItem> relItems;
         if(pageNumber == 0)
         {
            log.debug("no page number, returning the whole slot"); 
            relItems = tools.getSlotContents(item, slot, params);
            return asm.assemble(relItems);
         }
         
         if(pageNumber < 0 || pageNumber > Integer.MAX_VALUE)
         {
            String emsg = "page number must be a positive number";
            log.error(emsg);
            throw new IllegalArgumentException(emsg);
         }
         int psize = pageSize.intValue();
         log.trace("Page size is " + psize); 
         int start = (pageNumber.intValue() - 1) * psize;
         log.trace("Start is " + start); 
         
          
         relItems = tools.getSlotContents(item, slot, params);
         if(start > relItems.size())
         {
            log.debug("page starts beyond the end of the list, returning empty slot"); 
            return new ArrayList<IPSAssemblyResult>();
         }
         int end = Math.min(relItems.size(), start+psize );
         log.trace("End is " + end); 
         relItems = relItems.subList(start, end); 
         
         return asm.assemble(relItems); 
    }
    
    @IPSJexlMethod(description="Get slot page count", params={
          @IPSJexlParam(name="item", description="current item"),
          @IPSJexlParam(name="slot", description="slot to paginate"),
          @IPSJexlParam(name="params", description="assembly parameters for slot"), 
          @IPSJexlParam(name="pageSize", description="number of items per page") }) 
    public int getSlotPageCount(IPSAssemblyItem item, String slot,
          Map<String,Object> params, String pageSize) throws Throwable
       {
       if(StringUtils.isBlank(pageSize))
          {
          log.debug("page size is blank");
          return 0; 
          }
       long pagesz = Long.parseLong(pageSize); 
       return getSlotPageCountN(item, slot, params, pagesz); 
       }

    @IPSJexlMethod(description="Get slot page count", params={
          @IPSJexlParam(name="item", description="current item"),
          @IPSJexlParam(name="slot", description="slot to paginate"),
          @IPSJexlParam(name="params", description="assembly parameters for slot"), 
          @IPSJexlParam(name="pageSize", description="number of items per page") }) 
    public int getSlotPageCountN(IPSAssemblyItem item, String slot,
          Map<String,Object> params, Long pageSize) throws Throwable
    {
      
       if(pageSize <= 0 || pageSize > Integer.MAX_VALUE)
       {
          String emsg = "page size must be a positive number";
          log.error(emsg);
          throw new IllegalArgumentException(emsg);            
       }
       int psize = pageSize.intValue();
       log.trace("page size " + psize ); 
       
       PSOSlotTools tools = new PSOSlotTools();
       List<IPSAssemblyItem> relItems = tools.getSlotContents(item, slot, params);
       int icount = relItems.size(); 
       log.trace("item count " + icount); 
       if( icount <= psize)
       {
          return 1; 
       }
       int pgcount = ((icount-1)/psize)+1;
       log.trace("page count " + pgcount); 
       return pgcount;  
    }
    @IPSJexlMethod(description="create the list of locations", params={
          @IPSJexlParam(name="location", description="base location for item"),
          @IPSJexlParam(name="pagecount", description="number of pages") }) 
    public List<String> createLocationList(String location, String pagecount)
    {
       int pagect = 0; 
       if(StringUtils.isNotBlank(pagecount))
       {
          if(StringUtils.isNumeric(pagecount))
          {
             pagect = Integer.parseInt(pagecount);
          }
          else
          {
             String emsg = "pagecount must be numeric -" + pagecount; 
             log.error(emsg);
             throw new IllegalArgumentException(emsg);
          }
       }
       return PaginatorUtils.createLocationList(location, pagect); 
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
    
    private static String wrapString(String in)
    {
       StringBuilder builder = new StringBuilder();
       builder.append(DIV_START);
       builder.append(in); 
       builder.append(DIV_END); 
       return builder.toString(); 
    }

    private static String unwrapString(String in)
    {
       String temp = in;
       if(in.startsWith(DIV_START) && in.endsWith(DIV_END))          
       {
          int len = in.length(); 
          int endindex = len - DIV_END.length(); 
          temp = in.substring(DIV_START.length(), endindex);
       }
       return temp; 
    }
    
    private static final String DIV_START = "<div>";
    private static final String DIV_END = "</div>"; 
  }

  
