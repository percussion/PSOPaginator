/*
 * com.percussion.pso.paginator.servlet PSContentListItemExtended.java
 *  
 * @author DavidBenua
 *
 */
package com.percussion.pso.paginator.servlet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.percussion.pso.utils.PSOMutableUrl;
import com.percussion.server.PSRequestParsingException;
import com.percussion.services.filter.IPSItemFilter;
import com.percussion.services.publisher.data.PSContentListItem;
import com.percussion.utils.guid.IPSGuid;

/**
 * 
 *
 * @author DavidBenua
 *
 */
public class PSContentListItemExtended extends PSContentListItem implements IPSContentListItem
{
   /**
    * Logger for this class
    */
   private static final Log log = LogFactory
         .getLog(PSContentListItemExtended.class);
   
   private IPSItemFilter filter; 
   
   private boolean publish; 
   
   private String publicationid; 
   
   private Map<String, String[]> extraParams = new HashMap<String, String[]>(); 
   
   private PSOMutableUrl assemblyURL = null; 
      
   /**
    * Creates a new content list item
    * @param cid the content GUID
    * @param fid the folder GUID
    * @param tid the template GUID
    * @param siteid the site GUID
    * @param contextid the context 
    * @param filter the item filter
    * @param publish the publish flag
    * @param publicationid the id of this publication run 
    */
   public PSContentListItemExtended(IPSGuid cid, IPSGuid fid, IPSGuid tid, IPSGuid siteid, int contextid, IPSItemFilter filter, boolean publish, String publicationid)
   {
      super(cid, fid, tid, siteid, contextid);
      log.trace("creating list item"); 
      this.filter = filter;
      this.publish = publish; 
      this.publicationid = publicationid;
   }

   /**
    * @see com.percussion.pso.paginator.servlet.IPSContentListItem#getFilter()
    */
   public IPSItemFilter getFilter()
   {
      return filter;
   }

   /**
    * @see com.percussion.pso.paginator.servlet.IPSContentListItem#getPublicationid()
    */
   public String getPublicationid()
   {
      return publicationid;
   }

   /**
    * @see com.percussion.pso.paginator.servlet.IPSContentListItem#isPublish()
    */
   public boolean isPublish()
   {
      return publish;
   }
   
   /**
    * @see com.percussion.pso.paginator.servlet.IPSContentListItem#clone()
    */
   public IPSContentListItem clone()
   {
      PSContentListItemExtended item = new PSContentListItemExtended(
            this.getContentId(), this.getFolderId(), this.getTemplateId(), this.getSiteId(),
            this.getContext(),this.getFilter(), this.isPublish(), this.getPublicationid());
      item.extraParams.putAll(this.extraParams);    
      try
      {
         item.setAssemblyURL(this.getAssemblyURL());
      } catch (PSRequestParsingException e)
      {
         log.error("Error parsing URL " + e.getMessage(), e); 
      } 
      item.setLocation(this.getLocation()); 
      return item;      
   }

   /**
    * @see com.percussion.pso.paginator.servlet.IPSContentListItem#getExtraParams()
    */
   public Map<String, String[]> getExtraParams()
   {
      return extraParams;
   }
   
   
   /**
    * @return Returns the assemblyURL.
    */
   public String getAssemblyURL()
   { 
      if(assemblyURL == null)
      {
         return null;
      }
      return assemblyURL.toString();
   }

   /**
    * @param assemblyURL The assemblyURL to set.
    */
   public void setAssemblyURL(PSOMutableUrl assemblyURL)
   {
      this.assemblyURL = assemblyURL;
   }

   
   public void setAssemblyURL(String baseURL) throws PSRequestParsingException
   {
      if(StringUtils.isBlank(baseURL))
      {
         this.assemblyURL = null;
         return;
      }
      this.assemblyURL = new PSOMutableUrl(baseURL);
      for(String key : extraParams.keySet())
      {
         String[] values = extraParams.get(key);
         String val = values[0]; 
         assemblyURL.setParam(key, val); 
      }
   }   
  

   public static List<IPSContentListItem> wrapContentList(List<PSContentListItem> original, IPSItemFilter filter,
         boolean publish, String publicationid)
   {
      List<IPSContentListItem> list = new ArrayList<IPSContentListItem>();
      for(PSContentListItem old : original)
      {
         IPSContentListItem newItem = new PSContentListItemExtended(old.getContentId(), old.getFolderId(), 
               old.getTemplateId(), old.getSiteId(), old.getContext(), filter, publish, publicationid);
         list.add(newItem); 
      }
      return list; 
   }
   
   
}
