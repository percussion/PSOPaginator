/*
 * test.paginator PSTemplatePageExpanderTest.java
 *  
 * @author DavidBenua
 *
 */
package test.paginator;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmock.MockObjectTestCase;
import com.percussion.pso.paginator.servlet.IPSContentListItem;
import com.percussion.pso.paginator.servlet.PSContentListItemExtended;
import com.percussion.pso.paginator.servlet.PSTemplatePageExpander;
import com.percussion.services.PSBaseServiceLocator;
import com.percussion.services.assembly.IPSAssemblyItem;
import com.percussion.services.assembly.PSAssemblyException;
import com.percussion.services.assembly.PSAssemblyServiceLocator;
import com.percussion.services.assembly.data.PSAssemblyWorkItem;
import com.percussion.services.filter.IPSItemFilter;
import com.percussion.services.guidmgr.data.PSLegacyGuid;
import com.percussion.utils.guid.IPSGuid;

public class PSTemplatePageExpanderTest extends MockObjectTestCase
{
   /**
    * Logger for this class
    */
   private static final Log log = LogFactory.getLog(PSTemplatePageExpanderTest.class);
   
   private MockAssemblyService asm = null;
   
   private List<IPSContentListItem> itemList = new ArrayList<IPSContentListItem>();
   
   private PSTemplatePageExpander expander = null;
   
   private PSAssemblyWorkItem workItem = null; 
   /**
    * @see junit.framework.TestCase#setUp()
    */
   @Override
   protected void setUp() throws Exception
   {      
      super.setUp();
      
      URL beanfile = this.getClass().getResource("local-beans.xml"); 
      PSBaseServiceLocator.initCtx(new String[]{beanfile.toExternalForm()});      
      log.info("Initializing Spring " + beanfile.toString()); 
      
      asm = (MockAssemblyService)PSAssemblyServiceLocator.getAssemblyService();
      
      expander = new PSTemplatePageExpander();
      expander.setPageParamName("pageno");
      expander.setPageCountParameterName("pagecount");
      expander.setPageCountBindingName("$pagecount"); 
      
      IPSGuid cid = new PSLegacyGuid(1);
      IPSGuid fid = new PSLegacyGuid(2);
      IPSGuid tid = new PSLegacyGuid(3); 
      IPSGuid siteid = new PSLegacyGuid(4);
      IPSItemFilter filter = new MockItemFilter("filter","test filter"); 
      IPSContentListItem newItem = new PSContentListItemExtended(cid,fid,tid,siteid,5,filter,true,"12367"); 
      newItem.setLocation("/foo/bar/xyz.html");
      
      workItem = new PSAssemblyWorkItem();
      List <IPSAssemblyItem> workList = new ArrayList<IPSAssemblyItem>(); 
      workList.add(workItem); 
      asm.setItems(workList); 
      
      workItem.getBindings().put("$pagecount", "4");
      asm.getResults().add(workItem); 
      
      itemList.add(newItem);
      
   }
   public static void main(String[] args)
   {
      junit.textui.TestRunner.run(PSTemplatePageExpanderTest.class);
   }
  
   /*
    * Test method for 'com.percussion.pso.paginator.servlet.PSAbstractPageExpander.expand(List<IPSContentListItem>)'
    */
   public void testExpand()
   {
          
       try
      {
         List<IPSContentListItem> expandedList = expander.expand(itemList);
         assertTrue(workItem.isPublish());
         
         log.info("Expanded List is "  + expandedList);
         assertNotNull(expandedList); 
         assertEquals(4, expandedList.size());
         IPSContentListItem testItem = expandedList.get(0);
         String location = testItem.getLocation(); 
         log.info("Location is " + location);
         assertTrue(location.indexOf("xyz_1.html") > 1); 
         assertTrue(testItem.getExtraParams().containsKey("pageno")); 
         assertEquals(1,Integer.parseInt(testItem.getExtraParams().get("pageno")[0])); 

         workItem.getBindings().put("$pagecount", new Long(3));
         expandedList = expander.expand(itemList); 
         assertNotNull(expandedList);
         assertEquals(3, expandedList.size());
         
      } catch (PSAssemblyException e)
      {
         String emsg = "Unexpected Exception " + e.getMessage();
         log.error(emsg, e);
      } 
   }
}
