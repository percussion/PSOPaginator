/*
 * com.percussion.pso.paginator.servlet PSAbstractPageExpander.java
 *  
 * @author DavidBenua
 *
 */
package com.percussion.pso.paginator.servlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.percussion.pso.paginator.PaginatorUtils;
import com.percussion.services.assembly.IPSAssemblyErrors;
import com.percussion.services.assembly.IPSAssemblyItem;
import com.percussion.services.assembly.IPSAssemblyResult;
import com.percussion.services.assembly.IPSAssemblyService;
import com.percussion.services.assembly.PSAssemblyException;
import com.percussion.services.assembly.PSAssemblyServiceLocator;
import com.percussion.services.assembly.data.PSAssemblyWorkItem;
import com.percussion.services.guidmgr.IPSGuidManager;
import com.percussion.services.guidmgr.PSGuidManagerLocator;
import com.percussion.services.guidmgr.data.PSLegacyGuid;
import com.percussion.util.IPSHtmlParameters;
import com.percussion.utils.exceptions.PSExceptionHelper;

/**
 * 
 *
 * @author DavidBenua
 *
 */
public abstract class PSAbstractPageExpander
{
   /**
    * Logger for this class
    */
   private static final Log log = LogFactory.getLog(PSAbstractPageExpander.class);
   
   private static IPSGuidManager gmgr = PSGuidManagerLocator.getGuidMgr(); 
   private static IPSAssemblyService asm = PSAssemblyServiceLocator.getAssemblyService();

   protected String pageParamName = "pageno"; 

   private static void initStatics()
   {
      if(gmgr == null)
      {
         gmgr = PSGuidManagerLocator.getGuidMgr();
      }
      if(asm == null)
      {
         asm = PSAssemblyServiceLocator.getAssemblyService();
      }
   }
   /**
    * 
    */
   public PSAbstractPageExpander() 
   {
      super();
   }
   
   /**
    * Counts the pages for this item.  
    * @param item the content list item to expand. 
    * @return the number of pages.  Implementations should return -1
    * when the pages cannot be counted. 
    * @throws PSAssemblyException
    */
   protected abstract int countPages(IPSContentListItem item) throws PSAssemblyException; 
   
   public List<IPSContentListItem> expand(List<IPSContentListItem> items) throws PSAssemblyException 
   {
      initStatics();
      List<IPSContentListItem> nl = new ArrayList<IPSContentListItem>();
      for (IPSContentListItem item : items)
      {
         int total_pages = countPages(item);
         if (total_pages > 0)
         {            
            String oldLocation = item.getLocation();
            for (int page = 1; page <= total_pages; page++)
            {
               IPSContentListItem ni = item.clone();
               String newLocation = PaginatorUtils.createNewLocation(oldLocation, page, 1);
               log.debug("Page " + page + " new location " + newLocation);
               ni.setLocation(newLocation);
               ni.getExtraParams().put(pageParamName,
                     new String[]{String.valueOf(page)});
               nl.add(ni);
            }
         } else
         {
            log.debug("Item is not paginated: location = "
                        + item.getLocation());
            nl.add(item);
         }
      }
      return nl;
   }
   
   protected IPSAssemblyResult assembleItem(IPSContentListItem item,Map<String,String[]> moreParams)
      throws PSAssemblyException
   {
      initStatics();
      Map<String,String[]> params = new HashMap<String,String[]>();
      PSLegacyGuid cid = (PSLegacyGuid)item.getContentId(); //TODO: use the GUID Manager in 6.01
      params.put(IPSHtmlParameters.SYS_CONTENTID, new String[]{String.valueOf(cid.getContentId())});
      params.put(IPSHtmlParameters.SYS_REVISION, new String[]{String.valueOf(cid.getRevision())});
      if (item.getFolderId() != null) {
          params.put(IPSHtmlParameters.SYS_FOLDERID, new String[]{String.valueOf(item.getFolderId().longValue())});
      }
      else {
          log.debug("Content List item " + item.getContentId() + "was missing it's folderid");
      }
      params.put(IPSHtmlParameters.SYS_TEMPLATE, new String[]{String.valueOf(item.getTemplateId().longValue())});
      params.put(IPSHtmlParameters.SYS_CONTEXT, new String[]{String.valueOf(item.getContext())});
      params.put(IPSHtmlParameters.SYS_SITEID, new String[]{String.valueOf(item.getSiteId().longValue())});
      params.put(IPSHtmlParameters.SYS_ITEMFILTER, new String[]{item.getFilter().getName()});
      params.put(IPSHtmlParameters.SYS_PUBLICATIONID, new String[]{item.getPublicationid()}); 
      
      params.putAll(item.getExtraParams()); 
      
      params.putAll(moreParams); 
      long jobid = 0;      
      if(StringUtils.isNotBlank(item.getPublicationid()))
         {
         jobid = Long.parseLong(item.getPublicationid());
         }
      String path = null; 
      if(item.getExtraParams().containsKey(IPSHtmlParameters.SYS_PATH))
      {
         path = item.getExtraParams().get(IPSHtmlParameters.SYS_PATH)[0]; 
      }
 
      if(moreParams.containsKey(IPSHtmlParameters.SYS_PATH))
      {
         path = moreParams.get(IPSHtmlParameters.SYS_PATH)[0]; 
      }
 
      try 
      {
         PSAssemblyWorkItem work = (PSAssemblyWorkItem) asm.createAssemblyItem(
               path, jobid, 0, null, null, params, null, false);
         
         List<IPSAssemblyItem> wl = new ArrayList<IPSAssemblyItem>();
         work.setPublish(item.isPublish()); 
         wl.add(work);
         List<IPSAssemblyResult> results = asm.assemble(wl);
         if (results.size() == 0)
         {
            return null;
         }
         return results.get(0);
         
      }
      catch (PSAssemblyException e)
      {
         log.error("Unexpected Exception " + e.getLocalizedMessage(), e); 
         // Rethrow assembly exceptions
         throw e;
      }
      catch (Exception e)
      {
         Throwable cause = PSExceptionHelper.findRootCause(e, true);
         log.error("Failure while processing assembly item", cause);
         throw new PSAssemblyException(IPSAssemblyErrors.UNKNOWN_ERROR, cause,
               e.getLocalizedMessage());
      }
   }

 
   /**
    * @return Returns the pageParamName.
    */
   public String getPageParamName()
   {
      return pageParamName;
   }

   /**
    * @param pageParamName The pageParamName to set.
    */
   public void setPageParamName(String pageParamName)
   {
      this.pageParamName = pageParamName;
   }
   /**
    * @return Returns the asm.
    */
   public static IPSAssemblyService getAsm()
   {
      return asm;
   }
   /**
    * @param asm The asm to set.
    */
   public static void setAsm(IPSAssemblyService asm)
   {
      PSAbstractPageExpander.asm = asm;
   }
   /**
    * @return Returns the gmgr.
    */
   public static IPSGuidManager getGmgr()
   {
      return gmgr;
   }
   /**
    * @param gmgr The gmgr to set.
    */
   public static void setGmgr(IPSGuidManager gmgr)
   {
      PSAbstractPageExpander.gmgr = gmgr;
   }
}
