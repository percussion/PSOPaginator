/*
 * com.percussion.pso.paginator.servlet PSBaseFilteringPageExpander.java
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
 * 
 *
 * @author DavidBenua
 *
 */
public class PSBaseFilteringPageExpander implements IPSPageExpander
{
   /**
    * Logger for this class
    */
   private static final Log log = LogFactory.getLog(PSBaseFilteringPageExpander.class);

   IPSPageExpander wrappedPageExpander = null;
   /**
    * 
    */
   protected PSBaseFilteringPageExpander()
   {
      super();
   }

   /**
    * @see com.percussion.pso.paginator.servlet.IPSPageExpander#expand(java.util.List)
    */
   public List<IPSContentListItem> expand(List<IPSContentListItem> items) throws PSAssemblyException
   {
      if(items == null)
      {
         String emsg = "item list must not be null";
         log.error(emsg);
         throw new IllegalArgumentException(emsg); 
      }
      if(getWrappedPageExpander() == null)
      {
         String emsg = "Initialization error: wrappedPageExpander must not be null";
         log.error(emsg);
         throw new IllegalStateException(emsg);
      }      
      log.trace("filtered items size " + items.size());
      return getWrappedPageExpander().expand(items);
   }

   /**
    * @return Returns the wrappedPageExpander.
    */
   public final IPSPageExpander getWrappedPageExpander()
   {
      return wrappedPageExpander;
   }

   /**
    * @param wrappedPageExpander The wrappedPageExpander to set.
    */
   public final void setWrappedPageExpander(IPSPageExpander wrappedPageExpander)
   {
      this.wrappedPageExpander = wrappedPageExpander;
   }
}
