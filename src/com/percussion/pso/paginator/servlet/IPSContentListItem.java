/*
 * com.percussion.pso.paginator.servlet IPSContentListItem.java
 *  
 * @author DavidBenua
 *
 */
package com.percussion.pso.paginator.servlet;
import java.util.Map;
import com.percussion.server.PSRequestParsingException;
import com.percussion.services.filter.IPSItemFilter;
import com.percussion.utils.guid.IPSGuid;
public interface IPSContentListItem
{
   /**
    * @return Returns the contentId.
    */
   public IPSGuid getContentId();

   /**
    * @return Returns the folderId.
    */
   public IPSGuid getFolderId();

   /**
    * @return Returns the templateId.
    */
   public IPSGuid getTemplateId();
  
   /**
    * @return Returns the siteId.
    */
   public IPSGuid getSiteId();
 

   /**
    * @return Returns the context.
    */
   public int getContext();
 
   /**
    * @return Returns the location.
    */
   public String getLocation();

   public void setLocation(String location); 
   /**
    * @return Returns the filter.
    */
   public IPSItemFilter getFilter();
   /**
    * @return Returns the publicationid.
    */
   public String getPublicationid();
   /**
    * @return Returns the publish.
    */
   public boolean isPublish();
  
   public void setAssemblyURL(String baseURL) throws PSRequestParsingException;
   
   public String getAssemblyURL();
   
   public IPSContentListItem clone();
   /**
    * @return Returns the extraParams.
    */
   public Map<String, String[]> getExtraParams();
}