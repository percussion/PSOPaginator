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
    * Default constructor.
    */
   public Paginator()
   {
      
   }
   
   /**
    * Counts the number of pages in a body field.  The page breaks will be 
    * one more than the number of pages, so a field with 1 page break is
    * published as 2 pages. 
    * @deprecated use getBodyPageCount() instead.
    * @param fieldContent
    * @return the page count.
    */
   @IPSJexlMethod(description="Count the number of page breaks", params={
           @IPSJexlParam(name="fieldContent", description="Page content") } )
           @Deprecated
        public String getPageCount(String fieldContent)
        {
            return String.valueOf(getBodyPageCount(fieldContent)); 
        }
   
   /**
    * Counts the number of pages in a body field.  The page breaks will be 
    * one more than the number of pages, so a field with 1 page break is
    * published as 2 pages. 
    * 
    * @param fieldContent the field content
    * @return the page count.
    */
   @IPSJexlMethod(description="Count the number of page breaks", params={
		   @IPSJexlParam(name="fieldContent", description="Page content") } ) 
	    public Number getBodyPageCount(String fieldContent)
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
   
    /**
     * Gets the specified page.
     * @deprecated use the getBodyPage instead
     * @param fieldContent the field content. 
     * @param pageNumber the page number. 
     * @return the body page. Never <code>null</code>
     */
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
    
    /**
     * Convenience method for getBodyPageN.  Used when the page number
     * parameter is available as a <code>String</code>
     * @param fieldContent the body field contents
     * @param pageNumber the page number. Must be numeric and greater than 0. 
     * @return the body page. Never <code>null</code>
     */
    @IPSJexlMethod(description="Get content from specified page number", params={
          @IPSJexlParam(name="fieldContent", description="Page content"),
          @IPSJexlParam(name="pageNumber", description="Page number") }) 
    public String getBodyPage(String fieldContent, String pageNumber)
    {       
       Number pno = new Long(0); 
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
    
    /**
     * Gets the specified page.  
     * @param fieldContent the contents of the body field. 
     * @param pageNumber the page number to retrieve. Must not be 
     * <code>null</code>.  Must be greater than zero.  
     * @return the body page. Never <code>null</code>
     */
    @IPSJexlMethod(description="Get content from specified page number", params={
          @IPSJexlParam(name="fieldContent", description="Page content"),
          @IPSJexlParam(name="pageNumber", description="Page number") }) 
    public String getBodyPageN(String fieldContent, Number pageNumber)
    {
        if (pageNumber == null || pageNumber.intValue() == 0 )
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

    /**
     * Convenience method for getSlotPageN.  
     * Gets the contents of the slot paginated. 
     * @param item the parent item which contains the slot.  Usually
     * <code>$sys.item</code>
     * @param slot the slot name to paginate
     * @param params a parameters used to evaluate the slot. 
     * @param pageSize the maximum number of slot items per page. 
     * @param pageNumber the number of the page. 
     * @return the list of items returned in the slot. 
     * Never <code>null</code>.  May be <code>empty</code>.
     * @throws Throwable
     */
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
    
    /**
     * Gets the contents of the slot paginated. 
     * @param item the parent item which contains the slot.  Usually
     * <code>$sys.item</code>
     * @param slot the slot name to paginate
     * @param params a parameters used to evaluate the slot. 
     * @param pageSize the maximum number of slot items per page. 
     * @param pageNumber the number of the page. 
     * @return the list of items returned in the slot. 
     * Never <code>null</code>.  May be <code>empty</code>.
     * @throws Throwable
     */
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
    
    /**
     * Convenience method for getSlotPageCountN. 
     * Used when the page size is available as a String. 
     * Gets the page count on a slot. 
     * @param item the parent item that contains the slot.
     * @param slot the name of the slot.
     * @param params the parameters used to assemble the slot. 
     * @param pageSize the number of items per page. 
     * @return the list of items in the slot. 
     * Never <code>null</code>.  May be <code>empty</code>. 
     * @throws Throwable
     */
    @IPSJexlMethod(description="Get slot page count", params={
          @IPSJexlParam(name="item", description="current item"),
          @IPSJexlParam(name="slot", description="slot to paginate"),
          @IPSJexlParam(name="params", description="assembly parameters for slot"), 
          @IPSJexlParam(name="pageSize", description="number of items per page") }) 
    public Number getSlotPageCount(IPSAssemblyItem item, String slot,
          Map<String,Object> params, String pageSize) throws Throwable
       {
       if(StringUtils.isBlank(pageSize))
          {
          log.debug("page size is blank");
          return new Integer(0); 
          }
       Number pagesz = new Integer(pageSize); 
       return getSlotPageCountN(item, slot, params, pagesz); 
       }

    /**
     * Gets the page count on a slot. 
     * @param item the parent item that contains the slot.
     * @param slot the name of the slot.
     * @param params the parameters used to assemble the slot. 
     * @param pageSize the number of items per page. 
     * @return the list of items in the slot. 
     * Never <code>null</code>.  May be <code>empty</code>. 
     * @throws Throwable
     */
    @IPSJexlMethod(description="Get slot page count", params={
          @IPSJexlParam(name="item", description="current item"),
          @IPSJexlParam(name="slot", description="slot to paginate"),
          @IPSJexlParam(name="params", description="assembly parameters for slot"), 
          @IPSJexlParam(name="pageSize", description="number of items per page") }) 
    public Number  getSlotPageCountN(IPSAssemblyItem item, String slot,
          Map<String,Object> params, Number pageSize) throws Throwable
    {
      
       int pagesz; 
       if(pageSize == null)
       {
          String emsg = "page size must not be null"; 
          log.error(emsg);
          throw new IllegalArgumentException(emsg);
       }
      
       pagesz = pageSize.intValue(); 
       
       if(pagesz <= 0)
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
          return new Integer(1); 
       }
       int pgcount = ((icount-1)/psize)+1;
       log.trace("page count " + pgcount); 
       return new Integer(pgcount);  
    }
    
    /**
     * Convenience methods for createLocationListN().  
     * Used when the page count is available when the page count is 
     * available as a String. 
     * Creates a list of locations.  Appends the "_N" to the based location
     * and produces an expanded list.  
     * @param location the base location.
     * @param pagecount the number of pages. 
     * @return the list of locations. Never <code>null</code>
     */
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
    
    /**
     * Creates a list of locations.  Appends the "_N" to the based location
     * and produces an expanded list.  
     * @param location the base location.
     * @param pagecount the number of pages. 
     * @return the list of locations. Never <codenull</code>
     */
    @IPSJexlMethod(description="create the list of locations", params={
          @IPSJexlParam(name="location", description="base location for item"),
          @IPSJexlParam(name="pagecount", description="number of pages") }) 
    public List<String> createLocationListN(String location, Number pagecount)
    {
       return PaginatorUtils.createLocationList(location, pagecount.intValue()); 
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
    
    /**
     * Wraps a string within <code>&lt;div&gt;</code> tag. Used to 
     * insure that the page content is well formed. 
     * @param in the raw string to be wrapped.      
     * @return the wrapped string. 
     */
    private static String wrapString(String in)
    {
       StringBuilder builder = new StringBuilder();
       builder.append(DIV_START);
       builder.append(in); 
       builder.append(DIV_END); 
       return builder.toString(); 
    }

    /**
     * Unwraps a string which was originally wrapped.  
     * @param in the wrapped string
     * @return the string unwrapped. 
     */
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

  
