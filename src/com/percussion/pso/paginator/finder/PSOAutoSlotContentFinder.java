/*
 * com.percussion.pso.paginator.finder PSOAutoSlotContentFinder.java
 *  
 * @author DavidBenua
 *
 */
package com.percussion.pso.paginator.finder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.jcr.ItemNotFoundException;
import javax.jcr.RepositoryException;
import com.percussion.error.PSException;
import com.percussion.pso.paginator.revision.PSORevisionCorrector;
import com.percussion.pso.paginator.revision.PSORevisionService;
import com.percussion.pso.paginator.revision.PSORevisionServiceLocator;
import com.percussion.services.assembly.IPSAssemblyItem;
import com.percussion.services.assembly.IPSSlotContentFinder;
import com.percussion.services.assembly.IPSTemplateSlot;
import com.percussion.services.assembly.impl.finder.PSAutoSlotContentFinder;
import com.percussion.services.assembly.impl.finder.PSBaseSlotContentFinder.SlotItem;
import com.percussion.services.filter.IPSItemFilter;
import com.percussion.services.filter.PSFilterException;
import com.percussion.utils.guid.IPSGuid;

/**
 * 
 *
 * @author DavidBenua
 *
 */
public class PSOAutoSlotContentFinder extends PSAutoSlotContentFinder
      implements
         IPSSlotContentFinder
{
   /**
* Logger for this class
 */private static final Log log = LogFactory.getLog(PSOAutoSlotContentFinder.class);

   /**
    * 
    */
   public PSOAutoSlotContentFinder()
   {
      super();
   }

   /**
    * @see com.percussion.services.assembly.impl.finder.PSAutoSlotContentFinder#getSlotItems(com.percussion.services.assembly.IPSAssemblyItem, com.percussion.services.assembly.IPSTemplateSlot, java.util.Map)
    */
   @Override
   protected Set<SlotItem> getSlotItems(IPSAssemblyItem parentItem, IPSTemplateSlot slot, Map<String, Object> params) throws ItemNotFoundException
   {
      log.debug("getting Slot items... " ); 
      PSORevisionService rev = PSORevisionServiceLocator.getRevisionService();      
      Set<SlotItem> results = new LinkedHashSet<SlotItem>();
      try
      {
         results = super.getSlotItems(parentItem, slot, params);
         IPSItemFilter filter = parentItem.getFilter();
         if(filter == null)
         {
            log.debug("filter is null");
            return results;
         }
         log.debug("filter name " + filter.getName());
         if(rev.isCorrectable(filter.getName()))
         {
            List<IPSGuid> glist = new ArrayList<IPSGuid>();
            for(SlotItem si : results)
            {
               glist.add(si.getItemId());
            }
            log.debug("processed " + glist.size() + " guids"); 
            PSORevisionCorrector corr = rev.getRevisionCorrector(filter.getName(), glist);
            for(SlotItem si : results)
            {               
               si.setItemId(corr.correct(si.getItemId()));
               log.debug("Correct GUID is " + si.getItemId());
            }
            log.debug("finished correcting guids");
         }
      } catch (PSFilterException ex)
      {
         String emsg = "Filter returned exception " + ex.getMessage();
         log.error(emsg, ex);
         throw new ItemNotFoundException(emsg,ex);
      } catch (PSException ex)
      {
         String emsg = "unknown exception " + ex.getMessage();
         log.error(emsg, ex);
         throw new ItemNotFoundException(emsg,ex);
       } catch (RepositoryException ex)
      {
          String emsg = "repository exception " + ex.getMessage();
          log.error(emsg, ex);
          throw new ItemNotFoundException(emsg,ex);
      }
      return results;
   }
   
   
}
