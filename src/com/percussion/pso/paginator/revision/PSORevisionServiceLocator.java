/*
 * com.percussion.pso.paginator.revision PSORevisionServiceLocator.java
 *  
 * @author DavidBenua
 *
 */
package com.percussion.pso.paginator.revision;

import com.percussion.services.PSBaseServiceLocator;

/**
 * 
 *
 * @author DavidBenua
 *
 */
public class PSORevisionServiceLocator extends PSBaseServiceLocator
{
   private static final String REVISION_SERVICE_NAME = "PSORevisionService";
   /**
    * 
    */
   public PSORevisionServiceLocator()
   {
     
   }
   
   public static PSORevisionService getRevisionService()
   {
      return (PSORevisionService) getBean(REVISION_SERVICE_NAME);
   }
}
