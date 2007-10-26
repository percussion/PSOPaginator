/*
 * com.percussion.pso.paginator PaginatorUtils.java
 *  
 * @author DavidBenua
 *
 */
package com.percussion.pso.paginator;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 *
 * @author DavidBenua
 *
 */
public class PaginatorUtils
{
   /**
    * Static methods only
    */
   private PaginatorUtils()
   {
     
   }
   
   private static final Log log = LogFactory.getLog(Paginator.class);
   
   public static List<String> createLocationList(String location, int pagecount)
   {
      List<String> outList = new ArrayList<String>();
      for(int i = 1; i <= pagecount; i++)
      {
         outList.add(createNewLocation(location,i)); 
      }
      
      return outList; 
   }
   /**
    * Takes the old location and creates a new location with the given page number.
    * The context is passed as 1 by default
    * @param location the location never <code>null</code>.
    * @param pageno
    * @return location, never null
    */
   public static String createNewLocation(String location, int pageno) 
   {
	   if(StringUtils.isBlank(location))
       {
          String emsg = "Location must not be blank"; 
          log.error(emsg); 
          throw new IllegalArgumentException(emsg); 
       }
	   
	   if(pageno < 0)
	   {
		   String emsg = "Page number must be greater than zero: " + pageno;
		   log.error(emsg);
		   throw new IllegalArgumentException(emsg);
	   }
	  
      return createNewLocation(location, pageno, 1); 
   }
   /**
    * Takes the old location and creates a new location with the given page number.
    * Passing the context will allow you to get a specific URL either Rhythmyx or publish URL.
    * @param location the location never <code>null</code>.
    * @param pageno
    * @return location, never null
    */
   public static String createNewLocation(String location, int pageno, int context) 
   {  	    	   	  
   	  if (context == 0)
   	  {
   		  log.debug("creating preview url for context: " + context);
   		  if(location.contains("pageno"))
   		  {
   			String baseurl = StringUtils.substringBeforeLast(location, "pageno");
   			log.debug("baseurl is: " + baseurl);
   			String resturl = StringUtils.substringAfterLast(location, "pageno");
   			log.debug("resturl is: " + resturl);
   			if(resturl.contains("&"))
   			{
   				log.debug("resturl contains &");
   				String remainingurl = StringUtils.substringAfter(resturl, "&");
   				log.debug("remaining url: " + remainingurl);
   				log.debug("The returned url is: " + baseurl + "pageno=" + pageno + remainingurl);
   				return baseurl + "pageno=" + pageno + remainingurl; 
   			}
   			else if(!resturl.contains("&"))
   			{
   				log.debug("pageno was the last param");
   				log.debug("The returned url is: " + baseurl + "pageno=" + pageno);
   				return baseurl + "pageno=" + pageno;
   			}	   			  
   		  }
   		  else if (!location.contains("pageno"))
   		  {
   			  log.debug("pageno was not in the url");
   			  log.debug("The returned url is: " + location + "&amp;pageno=" + pageno);
   			  return location + "&amp;pageno=" + pageno;  
   		  }
   	  }
      else
      {    	  	   
    	  log.debug("creating publish url for context:" + context);
	      String basename = StringUtils.substringBeforeLast(location,".");
	      String ext = StringUtils.substringAfterLast(location, ".");
	      String old_pageno = StringUtils.substringAfterLast(basename, "_");
	      String prefix = StringUtils.substringBeforeLast(basename,"_");
	      if (StringUtils.isNotBlank(ext)) {
	         return prefix + "_" + pageno + "." + ext;
	      } 
	      
	      return prefix + "_" + pageno; 
      }
   	  
   	  return location;
   }

}
