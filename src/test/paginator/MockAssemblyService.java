/*
 * test.paginator MockAssemblyService.java
 *  
 * @author DavidBenua
 *
 */
package test.paginator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import com.percussion.services.assembly.IPSAssemblyItem;
import com.percussion.services.assembly.IPSAssemblyResult;
import com.percussion.services.assembly.IPSAssemblyService;
import com.percussion.services.assembly.IPSAssemblyTemplate;
import com.percussion.services.assembly.IPSSlotContentFinder;
import com.percussion.services.assembly.IPSTemplateSlot;
import com.percussion.services.assembly.PSAssemblyException;
import com.percussion.services.assembly.PSTemplateNotImplementedException;
import com.percussion.services.assembly.IPSAssemblyTemplate.OutputFormat;
import com.percussion.services.assembly.IPSAssemblyTemplate.TemplateType;
import com.percussion.services.catalog.IPSCatalogSummary;
import com.percussion.services.catalog.PSCatalogException;
import com.percussion.services.catalog.PSTypeEnum;
import com.percussion.services.filter.PSFilterException;
import com.percussion.utils.guid.IPSGuid;

/**
 * 
 *
 * @author DavidBenua
 *
 */
public class MockAssemblyService implements IPSAssemblyService
{
   private List<IPSAssemblyResult> results = new ArrayList<IPSAssemblyResult>();
   
   private List<IPSAssemblyItem> items = new ArrayList<IPSAssemblyItem>(); 
   
   private IPSSlotContentFinder finder = null;
   
   private IPSTemplateSlot slot = null; 
   /**
    * 
    */
   public MockAssemblyService()
   {
      super();
     
   }
   /**
    * @see com.percussion.services.assembly.IPSAssemblyService#createAssemblyItem(java.lang.String, long, int, com.percussion.services.assembly.IPSAssemblyTemplate, java.util.Map, java.util.Map, javax.jcr.Node, boolean)
    */
   public IPSAssemblyItem createAssemblyItem(String arg0, long arg1, int arg2,
         IPSAssemblyTemplate arg3, Map<String, String> arg4,
         Map<String, String[]> arg5, Node arg6, boolean arg7)
         throws PSAssemblyException
   {
      return items.get(0);
   }
   /**
    * @see com.percussion.services.assembly.IPSAssemblyService#processServletRequest(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
    */
   public IPSAssemblyResult processServletRequest(HttpServletRequest request,
         String template, String variantid) throws PSAssemblyException
   {
      return null;
   }
   /**
    * @see com.percussion.services.assembly.IPSAssemblyService#loadFinder(java.lang.String)
    */
   public IPSSlotContentFinder loadFinder(String finderName)
         throws PSAssemblyException
   {
      return finder;
   }
   /**
    * @see com.percussion.services.assembly.IPSAssemblyService#getLandingPageLink(com.percussion.services.assembly.IPSAssemblyItem, javax.jcr.Node, com.percussion.utils.guid.IPSGuid)
    */
   public String getLandingPageLink(IPSAssemblyItem parentItem,
         Node landingPage, IPSGuid templateId) throws PSAssemblyException
   {
      return null;
   }
   /**
    * @see com.percussion.services.assembly.IPSAssemblyService#setCurrentAssemblyItem(com.percussion.services.assembly.IPSAssemblyItem)
    */
   public void setCurrentAssemblyItem(IPSAssemblyItem item)
   {
      
   }
   /**
    * @see com.percussion.services.assembly.IPSAssemblyService#getCurrentAssemblyItem()
    */
   public IPSAssemblyItem getCurrentAssemblyItem()
   {
      return null;
   }
   /**
    * @see com.percussion.services.assembly.IPSAssembler#assemble(java.util.List)
    */
   public List<IPSAssemblyResult> assemble(List<IPSAssemblyItem> arg0)
         throws ItemNotFoundException, RepositoryException,
         PSTemplateNotImplementedException, PSAssemblyException,
         PSFilterException
   {
      this.setItems(arg0); 
      return results;
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#createTemplate()
    */
   public IPSAssemblyTemplate createTemplate()
   {
      return null;
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#loadTemplate(com.percussion.utils.guid.IPSGuid, boolean)
    */
   public IPSAssemblyTemplate loadTemplate(IPSGuid id, boolean loadSlots)
         throws PSAssemblyException
   {
      return null;
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#loadTemplate(java.lang.String, boolean)
    */
   public IPSAssemblyTemplate loadTemplate(String guid, boolean loadSlots)
         throws PSAssemblyException
   {
      return null;
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#findAllTemplates()
    */
   public Set<IPSAssemblyTemplate> findAllTemplates()
         throws PSAssemblyException
   {
      return null;
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#findAllGlobalTemplates()
    */
   public Set<IPSAssemblyTemplate> findAllGlobalTemplates()
         throws PSAssemblyException
   {
      return null;
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#findTemplatesBySlot(com.percussion.services.assembly.IPSTemplateSlot)
    */
   public List<IPSAssemblyTemplate> findTemplatesBySlot(IPSTemplateSlot slot)
         throws PSAssemblyException
   {
      return null;
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#findTemplatesByAssemblyUrl(java.lang.String, boolean)
    */
   public List<IPSAssemblyTemplate> findTemplatesByAssemblyUrl(String pattern,
         boolean loadSlot) throws PSAssemblyException
   {
      return null;
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#findTemplatesByContentType(com.percussion.utils.guid.IPSGuid)
    */
   public List<IPSAssemblyTemplate> findTemplatesByContentType(
         IPSGuid contenttype) throws PSAssemblyException
   {
      return null;
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#saveTemplate(com.percussion.services.assembly.IPSAssemblyTemplate)
    */
   public void saveTemplate(IPSAssemblyTemplate var) throws PSAssemblyException
   {
      
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#loadTemplateNameMap()
    */
   public Map<String, Map<IPSGuid, IPSGuid>> loadTemplateNameMap()
   {
      return null;
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#findTemplateByName(java.lang.String)
    */
   public IPSAssemblyTemplate findTemplateByName(String name)
         throws PSAssemblyException
   {
      return null;
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#findTemplateByNameAndType(java.lang.String, com.percussion.utils.guid.IPSGuid)
    */
   public IPSAssemblyTemplate findTemplateByNameAndType(String name,
         IPSGuid contenttype) throws PSAssemblyException
   {
      return null;
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#findTemplates(java.lang.String, java.lang.String, java.util.Set, com.percussion.services.assembly.IPSAssemblyTemplate.TemplateType, java.lang.Boolean, java.lang.Boolean, java.lang.String)
    */
   public List<IPSAssemblyTemplate> findTemplates(String name,
         String contentType, Set<OutputFormat> outputFormats,
         TemplateType type, Boolean globalFilter, Boolean legacyFilter,
         String assembler) throws PSAssemblyException
   {
      
      return null;
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#deleteTemplate(com.percussion.utils.guid.IPSGuid)
    */
   public void deleteTemplate(IPSGuid id) throws PSAssemblyException
   {
      // TODO Auto-generated method stub
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#createSlot()
    */
   public IPSTemplateSlot createSlot()
   {
      // TODO Auto-generated method stub
      return null;
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#loadSlot(com.percussion.utils.guid.IPSGuid)
    */
   public IPSTemplateSlot loadSlot(IPSGuid id) throws PSAssemblyException
   {
      // TODO Auto-generated method stub
      return null;
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#loadSlot(java.lang.String)
    */
   public IPSTemplateSlot loadSlot(String idstr) throws PSAssemblyException
   {
      // TODO Auto-generated method stub
      return null;
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#loadSlots(java.util.List)
    */
   public List<IPSTemplateSlot> loadSlots(List<IPSGuid> arg0)
   {
      // TODO Auto-generated method stub
      return null;
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#saveSlot(com.percussion.services.assembly.IPSTemplateSlot)
    */
   public void saveSlot(IPSTemplateSlot var) throws PSAssemblyException
   {
      // TODO Auto-generated method stub
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#findSlotByName(java.lang.String)
    */
   public IPSTemplateSlot findSlotByName(String name)
         throws PSAssemblyException
   {      
      return this.slot;
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#findSlotsByName(java.lang.String)
    */
   public List<IPSTemplateSlot> findSlotsByName(String name)
   {
      // TODO Auto-generated method stub
      return null;
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#findSlotsByNames(java.util.List)
    */
   public List<IPSTemplateSlot> findSlotsByNames(List<String> arg0)
   {
      // TODO Auto-generated method stub
      return null;
   }
   /**
    * @see com.percussion.services.assembly.IPSTemplateService#deleteSlot(com.percussion.utils.guid.IPSGuid)
    */
   public void deleteSlot(IPSGuid id) throws PSAssemblyException
   {
      // TODO Auto-generated method stub
   }
   /**
    * @see com.percussion.services.catalog.IPSCataloger#getTypes()
    */
   public PSTypeEnum[] getTypes()
   {
      // TODO Auto-generated method stub
      return null;
   }
   /**
    * @see com.percussion.services.catalog.IPSCataloger#getSummaries(com.percussion.services.catalog.PSTypeEnum)
    */
   public List<IPSCatalogSummary> getSummaries(PSTypeEnum type)
         throws PSCatalogException
   {
      // TODO Auto-generated method stub
      return null;
   }
   /**
    * @see com.percussion.services.catalog.IPSCataloger#loadByType(com.percussion.services.catalog.PSTypeEnum, java.lang.String)
    */
   public void loadByType(PSTypeEnum type, String item)
         throws PSCatalogException
   {
      // TODO Auto-generated method stub
   }
   /**
    * @see com.percussion.services.catalog.IPSCataloger#saveByType(com.percussion.utils.guid.IPSGuid)
    */
   public String saveByType(IPSGuid id) throws PSCatalogException
   {
      // TODO Auto-generated method stub
      return null;
   }
   /**
    * @return Returns the finders.
    */
   public IPSSlotContentFinder getFinder()
   {
      return finder;
   }
   /**
    * @param finders The finders to set.
    */
   public void setFinder(IPSSlotContentFinder finder)
   {
      this.finder = finder;
   }
   /**
    * @return Returns the results.
    */
   public List<IPSAssemblyResult> getResults()
   {
      return results;
   }
   /**
    * @param results The results to set.
    */
   public void setResults(List<IPSAssemblyResult> results)
   {
      this.results = results;
   }
   /**
    * @return Returns the items.
    */
   public List<IPSAssemblyItem> getItems()
   {
      return items;
   }
   /**
    * @param items The items to set.
    */
   public void setItems(List<IPSAssemblyItem> items)
   {
      this.items = items;
   }
   /**
    * @param slot The slot to set.
    */
   public void setSlot(IPSTemplateSlot slot)
   {
      this.slot = slot;
   }
}
