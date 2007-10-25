/*
 * test.paginator MockItemFilter.java
 *  
 * @author DavidBenua
 *
 */
package test.paginator;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.xml.sax.SAXException;
import com.percussion.services.filter.IPSFilterItem;
import com.percussion.services.filter.IPSItemFilter;
import com.percussion.services.filter.IPSItemFilterRuleDef;
import com.percussion.services.filter.PSFilterException;
import com.percussion.utils.guid.IPSGuid;

/**
 * 
 *
 * @author DavidBenua
 *
 */
public class MockItemFilter implements IPSItemFilter
{
   private String name;
   private String description; 
   /**
    * 
    */
   public MockItemFilter(String name, String description)
   {
      super();
      this.name = name;
      this.description = description; 
   }
   /**
    * @see com.percussion.services.filter.IPSItemFilter#filter(java.util.Set, java.util.Map)
    */
   public Set<IPSFilterItem> filter(Set<IPSFilterItem> arg0,
         Map<String, String> arg1) throws PSFilterException
   {
      // TODO Auto-generated method stub
      return null;
   }
   /**
    * @see com.percussion.services.filter.IPSItemFilter#getName()
    */
   public String getName()
   {
      return name;
   }
   /**
    * @see com.percussion.services.filter.IPSItemFilter#setName(java.lang.String)
    */
   public void setName(String name) throws PSFilterException
   {
     this.name=name; 
   }
   /**
    * @see com.percussion.services.filter.IPSItemFilter#getParentFilter()
    */
   public IPSItemFilter getParentFilter()
   {
      return null;
   }
   /**
    * @see com.percussion.services.filter.IPSItemFilter#setParentFilter(com.percussion.services.filter.IPSItemFilter)
    */
   public void setParentFilter(IPSItemFilter parentFilter)
   {
   }
   /**
    * @see com.percussion.services.filter.IPSItemFilter#getDescription()
    */
   public String getDescription()
   {
      return this.description;
   }
   /**
    * @see com.percussion.services.filter.IPSItemFilter#setDescription(java.lang.String)
    */
   public void setDescription(String description)
   {
      this.description = description;
   }
   /**
    * @see com.percussion.services.filter.IPSItemFilter#getLegacyAuthtypeId()
    */
   public Integer getLegacyAuthtypeId()
   {
      // TODO Auto-generated method stub
      return null;
   }
   /**
    * @see com.percussion.services.filter.IPSItemFilter#setLegacyAuthtypeId(java.lang.Integer)
    */
   public void setLegacyAuthtypeId(Integer authTypeId)
   {
      // TODO Auto-generated method stub
   }
   /**
    * @see com.percussion.services.filter.IPSItemFilter#getRuleDefs()
    */
   public Set<IPSItemFilterRuleDef> getRuleDefs()
   {
      return null;
   }
   /**
    * @see com.percussion.services.filter.IPSItemFilter#setRuleDefs(java.util.Set)
    */
   public void setRuleDefs(Set<IPSItemFilterRuleDef> arg0)
   {
   }
   /**
    * @see com.percussion.services.filter.IPSItemFilter#addRuleDef(com.percussion.services.filter.IPSItemFilterRuleDef)
    */
   public void addRuleDef(IPSItemFilterRuleDef def)
   {
   }
   /**
    * @see com.percussion.services.filter.IPSItemFilter#removeRuleDef(com.percussion.services.filter.IPSItemFilterRuleDef)
    */
   public void removeRuleDef(IPSItemFilterRuleDef def)
   {
   }
   /**
    * @see com.percussion.services.catalog.IPSCatalogItem#toXML()
    */
   public String toXML() throws IOException, SAXException
   {
      return null;
   }
   /**
    * @see com.percussion.services.catalog.IPSCatalogItem#fromXML(java.lang.String)
    */
   public void fromXML(String xmlsource) throws IOException, SAXException
   {
   }
   /**
    * @see com.percussion.services.catalog.IPSCatalogItem#getGUID()
    */
   public IPSGuid getGUID()
   {
      return null;
   }
   /**
    * @see com.percussion.services.catalog.IPSCatalogItem#setGUID(com.percussion.utils.guid.IPSGuid)
    */
   public void setGUID(IPSGuid newguid) throws IllegalStateException
   {
   }
   
   public List<IPSFilterItem> filter(List<IPSFilterItem> items, Map<String, String> params) throws PSFilterException {
    // TODO Auto-generated method stub
    //return null;
    throw new UnsupportedOperationException("filter is not yet supported");
   }
}
