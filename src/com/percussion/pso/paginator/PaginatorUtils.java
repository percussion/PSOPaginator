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
    * 
    * @param location, should not be null.
    * @param pageno
    * @return location, never null
    */
   public static String createNewLocation(String location, int pageno) 
   {
      String basename = StringUtils.substringBeforeLast(location,".");
      String ext = StringUtils.substringAfterLast(location, ".");
      String old_pageno = StringUtils.substringAfterLast(basename, "_");
      String prefix = StringUtils.substringBeforeLast(basename,"_");
      if (StringUtils.isNotBlank(ext)) {
         return prefix + "_" + pageno + "." + ext;
      } 
      
      return prefix + "_" + pageno; 
   }
}
