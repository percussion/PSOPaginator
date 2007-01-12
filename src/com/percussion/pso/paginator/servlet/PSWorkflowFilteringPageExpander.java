/*
 * com.percussion.pso.paginator.servlet PSWorkflowFilteringPageExpander.java
 *  
 * @author DavidBenua
 *
 */
package com.percussion.pso.paginator.servlet;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.percussion.error.PSException;
import com.percussion.pso.paginator.revision.PSORevisionCorrector;
import com.percussion.pso.paginator.revision.PSORevisionService;
import com.percussion.pso.paginator.revision.PSORevisionServiceLocator;
import com.percussion.services.assembly.PSAssemblyException;
import com.percussion.utils.guid.IPSGuid;

public class PSWorkflowFilteringPageExpander
      extends
         PSBaseFilteringPageExpander implements IPSPageExpander
{
   /**
    * Logger for this class
    */
   private static final Log log = LogFactory.getLog(PSWorkflowFilteringPageExpander.class);
   
    
   
   public PSWorkflowFilteringPageExpander()
   {
      super();
   }

   /**
    * @see com.percussion.pso.paginator.servlet.PSBaseFilteringPageExpander#expand(java.util.List)
    */
   @Override
   public List<IPSContentListItem> expand(List<IPSContentListItem> items) throws PSAssemblyException
   {
      PSContentListItemExtended eItem; 
      
      List<IPSContentListItem> filteredItems = new ArrayList<IPSContentListItem>();
      if(items.isEmpty())
      {
         log.trace("expanding empty content list");
         //nothing to do
         return items;
      }
      String filterName = items.get(0).getFilter().getName();
      PSORevisionService rev = PSORevisionServiceLocator.getRevisionService();
      if(!rev.isCorrectable(filterName))
      {
         //this filter doesn't need any processing, just pass it through to super
         return super.expand(items);
      }
      List<IPSGuid> glist = new ArrayList<IPSGuid>();
      for(IPSContentListItem item : items)
      {
         glist.add(item.getContentId());
      }
      PSORevisionCorrector corr = rev.getRevisionCorrector(filterName, glist);
      for(IPSContentListItem item : items)
      {
         try
         {
            IPSGuid nguid = corr.correct(item.getContentId()); 
            eItem = new PSContentListItemExtended(
                   nguid, item.getFolderId(), item.getTemplateId(), item.getSiteId(),
                  item.getContext(),item.getFilter(), item.isPublish(), item.getPublicationid());
            filteredItems.add(eItem);
         } catch (PSException ex)
         {
            log.error("Error correcting revision " + ex.getMessage(), ex);
            log.info("Item skipped "  + item);
         }
      }
      return super.expand(filteredItems);
   }



   
}
