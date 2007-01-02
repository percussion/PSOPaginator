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

/**
 * Represents a content list item with all of the parameters needed 
 * to assemble it.  
 * 
 * 
 *
 * @author DavidBenua
 *
 */
public interface IPSContentListItem
{
   /**
    * Gets the Content ID for this item.
    * @return Returns the contentId.
    */
   public IPSGuid getContentId();

   /**
    * Returns the folder Id. 
    * @return Returns the folderId.
    */
   public IPSGuid getFolderId();

   /**
    * Returns the Template Id. 
    * @return Returns the templateId.
    */
   public IPSGuid getTemplateId();
  
   /**
    * Returns the Site ID.
    * 
    * @return Returns the siteId.
    */
   public IPSGuid getSiteId();
 

   /**
    * Returns the assembly context. 
    * @return Returns the context.
    */
   public int getContext();
 
   /**
    * Returns the delivery location. 
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
  
   /**
    * Sets the assembly URL.
    * @param baseURL the URL to set.
    * @throws PSRequestParsingException
    */
   public void setAssemblyURL(String baseURL) throws PSRequestParsingException;
   
   public String getAssemblyURL();
   
   /**
    * Clones the content list item.  
    * @return the new content list item. 
    */
   public IPSContentListItem clone();
   /**
    * Returns the extra parameters that will be added to the URL 
    * at assembly time. 
    * @return Returns the extraParams.
    */
   public Map<String, String[]> getExtraParams();
}