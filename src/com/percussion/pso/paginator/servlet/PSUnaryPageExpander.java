/*
 * com.percussion.pso.paginator.servlet PSUnaryPageExpander.java
 *  
 * @author DavidBenua
 *
 */
package com.percussion.pso.paginator.servlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import com.percussion.services.assembly.PSAssemblyException;

/**
 * A simple page expander that always returns one page. 
 *
 * @author DavidBenua
 *
 */
public class PSUnaryPageExpander implements IPSPageExpander
{
   /**
    * Logger for this class
    */
   private static final Log log = LogFactory.getLog(PSUnaryPageExpander.class);

   /**
    * 
    */
   public PSUnaryPageExpander()
   {
      super();
   }

   /**
    * @see com.percussion.pso.paginator.servlet.IPSPageExpander#expand(java.util.List)
    */
   public List<IPSContentListItem> expand(List<IPSContentListItem> items) throws PSAssemblyException
   {
      log.trace("expanding list");
      return items;
   }
   
}
