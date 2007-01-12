/*
 * com.percussion.pso.paginator.revision.impl PSORevisionCorrectorImpl.java
 *  
 * @author DavidBenua
 *
 */
package com.percussion.pso.paginator.revision.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.percussion.cms.objectstore.PSComponentSummary;
import com.percussion.error.PSException;
import com.percussion.pso.paginator.revision.PSORevisionCorrector;
import com.percussion.pso.workflow.PSOWorkflowInfoFinder;
import com.percussion.services.guidmgr.data.PSLegacyGuid;
import com.percussion.services.system.data.PSState;
import com.percussion.utils.guid.IPSGuid;

/**
 * 
 *
 * @author DavidBenua
 *
 */
public class PSORevisionCorrectorImpl implements PSORevisionCorrector
{
   /**
    * Logger for this class
    */
    private static final Log log = LogFactory.getLog(PSORevisionCorrectorImpl.class);

    private Map<Integer, PSComponentSummary> summaryMap = new HashMap<Integer, PSComponentSummary>();
    
    private boolean correctable = false;
    
    private PSOWorkflowInfoFinder wfFinder = null;
    
    private List<String> correctableStates = new ArrayList<String>();
   /**
    * 
    */
   PSORevisionCorrectorImpl()
   {
      super();
   }
   
   /**
    * @see com.percussion.pso.paginator.revision.PSORevisionCorrector#correct(com.percussion.utils.guid.IPSGuid)
    */
   public IPSGuid correct(IPSGuid inguid) throws PSException
   {
      if(!correctable)
      {
          return inguid;
      }
      PSLegacyGuid lguid = (PSLegacyGuid)inguid;
      log.debug("original revision is " + lguid.getRevision());
      return getCorrectGuid(lguid.getContentId());
   }
   
   /**
    * @see com.percussion.pso.paginator.revision.PSORevisionCorrector#getCorrectGuid(int)
    */
   public IPSGuid getCorrectGuid(int contentId) throws PSException
   {
      if(!correctable)
      {
         return null;
      }
      Integer key = new Integer(contentId);
      PSComponentSummary sum = summaryMap.get(key);
      if(sum == null)
      {
         String emsg = "Cannot find guid for content id " + contentId;
         log.error(emsg); //should never happen. 
         throw new RuntimeException(emsg);
      }
      int revision = -1;
      PSState state = wfFinder.findWorkflowState(sum.getWorkflowAppId(), sum.getContentStateId());
      if(state == null)
      {
         String emsg = "item is not in a valid workflow state " + contentId; 
         log.error(emsg);
         throw new RuntimeException(emsg);
      }
      log.trace("state is " + state.getName());
      if(correctableStates.contains(state.getName()))
      {
         revision = sum.getCurrentLocator().getRevision();
         log.debug("corrected revision is " + revision );
      }
      else
      {
         revision = sum.getPublicOrCurrentRevision();
         log.debug("public or current revision is " + revision);  
      }
      return new PSLegacyGuid(contentId, revision);
   }

   /**
    * @see com.percussion.pso.paginator.revision.PSORevisionCorrector#isCorrectable()
    */
   public boolean isCorrectable()
   {
      return correctable;
   }

   /**
    * @param correctable The correctable to set.
    */
   public void setCorrectable(boolean correctable)
   {
      this.correctable = correctable;
   }

   /**
    * @return Returns the correctableStates.
    */
   public List<String> getCorrectableStates()
   {
      return correctableStates;
   }

   /**
    * @param correctableStates The correctableStates to set.
    */
   public void setCorrectableStates(List<String> correctableStates)
   {
      this.correctableStates = correctableStates;
   }

   /**
    * @return Returns the summaryMap.
    */
   public Map<Integer, PSComponentSummary> getSummaryMap()
   {
      return summaryMap;
   }

   /**
    * @param summaryMap The summaryMap to set.
    */
   public void setSummaryMap(Map<Integer, PSComponentSummary> summaryMap)
   {
      this.summaryMap = summaryMap;
   }

   /**
    * @return Returns the wfFinder.
    */
   public PSOWorkflowInfoFinder getWfFinder()
   {
      return wfFinder;
   }

   /**
    * @param wfFinder The wfFinder to set.
    */
   public void setWfFinder(PSOWorkflowInfoFinder wfFinder)
   {
      this.wfFinder = wfFinder;
   }
}
