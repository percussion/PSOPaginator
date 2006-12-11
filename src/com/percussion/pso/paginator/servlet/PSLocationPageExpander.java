/*
 * com.percussion.pso.paginator.servlet PSLocationPageExpander.java
 *  
 * @author DavidBenua
 *
 */
package com.percussion.pso.paginator.servlet;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 *
 * @author DavidBenua
 *
 */
public class PSLocationPageExpander extends PSAbstractPageExpander implements IPSPageExpander
{
   /**
    * Logger for this class
    */
   private static final Log log = LogFactory.getLog(PSLocationPageExpander.class);
   
   /**
    * 
    */
   public PSLocationPageExpander()
   {
      super();
   }
  
   /**
    * Parses the location for the total number of pages.
    * The format of the location string should be 
    * "xxx_n.xxx" or "xxxxxx_n" where n is a digit.
    * 
    * @param location
    * @return number of pages, -1 if n is not found, never null.  
    */
   @Override
   protected int countPages(IPSContentListItem item) 
   {
       String location = item.getLocation();
       String basename = StringUtils.substringBeforeLast(location,".");
       String pageno = StringUtils.substringAfterLast(basename, "_");
       if (StringUtils.isNotBlank(pageno) && StringUtils.isNumeric(pageno)) 
       {
           log.trace("Page count " + pageno); 
           return Integer.parseInt(pageno);
       } 
      return -1;      
   }
   
   
   
}
