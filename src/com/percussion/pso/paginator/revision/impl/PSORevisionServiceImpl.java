/*
 * com.percussion.pso.paginator.revision.impl PSORevisionServiceImpl.java
 *  
 * @author DavidBenua
 *
 */
package com.percussion.pso.paginator.revision.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.percussion.cms.objectstore.PSComponentSummary;
import com.percussion.pso.paginator.revision.PSORevisionCorrector;
import com.percussion.pso.paginator.revision.PSORevisionService;
import com.percussion.pso.workflow.PSOWorkflowInfoFinder;
import com.percussion.services.guidmgr.data.PSLegacyGuid;
import com.percussion.services.legacy.IPSCmsContentSummaries;
import com.percussion.services.legacy.PSCmsContentSummariesLocator;
import com.percussion.utils.guid.IPSGuid;
import com.percussion.webservices.system.IPSSystemWs;

public class PSORevisionServiceImpl implements PSORevisionService
{
   private PSOWorkflowInfoFinder wfFinder = null; 
   
   private IPSCmsContentSummaries sumsvc = null; 
   
   private List<String> workflowStates = new ArrayList<String>();
   
   private List<String> itemFilters = new ArrayList<String>();
   
   public PSORevisionServiceImpl()
   {
      wfFinder = new PSOWorkflowInfoFinder();   
   }
   
   private void initServices()
   {
      if(sumsvc == null)
      {
         sumsvc = PSCmsContentSummariesLocator.getObjectManager();
      }
   }
   
   /**
    * @see com.percussion.pso.paginator.revision.PSORevisionService#getRevisionCorrector(java.lang.String, java.util.List)
    */
   public PSORevisionCorrector getRevisionCorrector(String filterName, List<IPSGuid> guidList)
   {
      PSORevisionCorrectorImpl corr = new PSORevisionCorrectorImpl();
      if(itemFilters.contains(filterName))
      {
         corr.setCorrectable(true);
         corr.setSummaryMap(buildSummaryMap(guidList));
         corr.setCorrectableStates(this.workflowStates);
         corr.setWfFinder(this.wfFinder);
      }
      else
      {
         corr.setCorrectable(false);
      }
      return corr;
   }
   
   /**
    * @see com.percussion.pso.paginator.revision.PSORevisionService#isCorrectable(java.lang.String)
    */
   public boolean isCorrectable(String itemFilterName)
   {
      return this.itemFilters.contains(itemFilterName);
   }
   
   private Map<Integer, PSComponentSummary> buildSummaryMap(List<IPSGuid> guids)
   {
      initServices();
      Map<Integer,PSComponentSummary> result = new HashMap<Integer, PSComponentSummary>(guids.size());
      List<Integer> clist = new ArrayList<Integer>();
      for(IPSGuid guid : guids)
      {
         PSLegacyGuid lg = (PSLegacyGuid)guid;
         clist.add(new Integer(lg.getContentId()));
      }
      List<PSComponentSummary> summaries = sumsvc.loadComponentSummaries(clist);
      
      for(PSComponentSummary sum : summaries)
      {
         Integer contentId = new Integer(sum.getContentId());
         result.put(contentId, sum);
      }
      
      return result;
   }

   /**
    * @return Returns the itemFilters.
    */
   public List<String> getItemFilters()
   {
      return itemFilters;
   }

   /**
    * @param itemFilters The itemFilters to set.
    */
   public void setItemFilters(List<String> itemFilters)
   {
      this.itemFilters = itemFilters;
   }

   /**
    * @return Returns the workflowStates.
    */
   public List<String> getWorkflowStates()
   {
      return workflowStates;
   }

   /**
    * @param workflowStates The workflowStates to set.
    */
   public void setWorkflowStates(List<String> workflowStates)
   {
      this.workflowStates = workflowStates;
   }

   /**
    * @param sumsvc The sumsvc to set.
    */
   public void setSumsvc(IPSCmsContentSummaries sumsvc)
   {
      this.sumsvc = sumsvc;
   }

   /**
    * @param sws The sws to set.
    */
   public void setSws(IPSSystemWs sws)
   {
   }
}
