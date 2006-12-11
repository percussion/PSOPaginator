/*
 * test.paginator PSPaginatorTest.java
 *  
 * @author DavidBenua
 *
 */
package test.paginator;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import com.percussion.pso.paginator.Paginator;
import com.percussion.services.PSBaseServiceLocator;
import com.percussion.services.assembly.IPSAssemblyItem;
import com.percussion.services.assembly.IPSAssemblyResult;
import com.percussion.services.assembly.IPSSlotContentFinder;
import com.percussion.services.assembly.IPSTemplateSlot;
import com.percussion.services.assembly.PSAssemblyServiceLocator;
import com.percussion.services.guidmgr.data.PSLegacyGuid;
import com.percussion.utils.guid.IPSGuid;

public class PSPaginatorTest extends MockObjectTestCase
{
   /**
    * Logger for this class
    */
   private static final Log log = LogFactory.getLog(PSPaginatorTest.class);

   Paginator paginator = new Paginator();
   
   MockAssemblyService asm = null;
   
   Mock mockFinder = new Mock(IPSSlotContentFinder.class);
   Mock mockSlot = new Mock(IPSTemplateSlot.class);
   Mock mockItem = new Mock(IPSAssemblyItem.class);

   IPSAssemblyItem item = (IPSAssemblyItem)(mockItem.proxy()); 
   IPSTemplateSlot slot = (IPSTemplateSlot)(mockSlot.proxy()); 
   String slotName = "Alice";
   
   public static void main(String[] args)
   {
      junit.textui.TestRunner.run(PSPaginatorTest.class);
   }
   protected void setUp() throws Exception
   {
      super.setUp();
      URL beanfile = this.getClass().getResource("local-beans.xml"); 
      PSBaseServiceLocator.initCtx(new String[]{beanfile.toExternalForm()});
      
      asm =(MockAssemblyService)PSAssemblyServiceLocator.getAssemblyService();
      log.info("Mock Assembly Service " + asm); 
      
      mockSlot.stubs().method("getFinderName").will(returnValue("Fred"));
      
      mockFinder.stubs().method("find").will(returnValue(buildMockItems(10)));
      
      IPSSlotContentFinder finder = (IPSSlotContentFinder)(mockFinder.proxy());
      asm.setFinder(finder);
      
      item = (IPSAssemblyItem)(mockItem.proxy()); 
      slot = (IPSTemplateSlot)(mockSlot.proxy());
      asm.setSlot(slot);

   }
   /*
    * Test method for 'com.percussion.pso.paginator.Paginator.getBodyPageCount(String)'
    */
   public void testGetBodyPageCount()
   {
      String body = "<div>Page1<?pageBreak ?>Page2<?pageBreak ?>Page3</div>"; 
      int count = paginator.getBodyPageCount(body); 
      assertEquals(3,count); 
      body = "<p>Page1<?pageBreak ?>Page2</p><h1>Title</h1>"; 
      count = paginator.getBodyPageCount(body); 
      assertEquals(2,count); 
      body = "";
      count = paginator.getBodyPageCount(body); 
      assertEquals(1,count); 
      
   }
   /*
    * Test method for 'com.percussion.pso.paginator.Paginator.getBodyPage(String, Long)'
    */
   public void testGetBodyPage()
   {
      String body = "<div>Page1<?pageBreak ?>Page2<?pageBreak ?>Page3</div>"; 
      String page = paginator.getBodyPageN(body,new Long(1)); 
      assertTrue(page.indexOf("Page1") > 0);
      assertFalse(page.indexOf("Page3") > 0); 
      page = paginator.getBodyPageN(body, new Long(3)); 
      assertTrue(page.indexOf("Page3") > 0); 
   }
   /*
    * Test method for 'com.percussion.pso.paginator.Paginator.getSlotPage(IPSAssemblyItem, IPSTemplateSlot, Map<String, Object>, Long, Long)'
    */
   public void testGetSlotPage()
   {
      Map<String,Object> params = new HashMap<String,Object>();
     
      try
      {
         List<IPSAssemblyResult> results = paginator.getSlotPageN(item, slotName, params, 20L, 1L);
         
         List<IPSAssemblyItem> items = asm.getItems();
         log.info("Returned Items " + items); 
         assertEquals(10,items.size()); 
         
         results = paginator.getSlotPageN(item, slotName, params, 3L, 1L);
         items = asm.getItems(); 
         assertEquals(3,items.size()); 
         
         IPSGuid guid = items.get(0).getId();
         log.info("First Guid is " + guid); 
         assertEquals(1L,guid.longValue());
         
         results = paginator.getSlotPageN(item, slotName, params, 3L, 2L);
         items = asm.getItems(); 
         assertEquals(3,items.size()); 
         
         guid = items.get(0).getId();
         log.info("First Guid is " + guid); 
         assertEquals(4L,guid.longValue());
         
         
      } catch (Throwable e)
      {
         log.error("Unexpected Exception " + e.getMessage(), e); 
         fail("Unexpected Exception " + e.getMessage()); 
      } 
   }
   /*
    * Test method for 'com.percussion.pso.paginator.Paginator.getSlotPageCount(IPSAssemblyItem, IPSTemplateSlot, Map<String, Object>, Long)'
    */
   public void testGetSlotPageCount()
   {
      
      Map<String,Object> params = new HashMap<String,Object>();
      
      try
      {
         int pages = paginator.getSlotPageCountN(item, slotName, params, 10L);
         log.info("Pages = " + pages); 
         assertEquals(1,pages);
         
         pages = paginator.getSlotPageCountN(item, slotName, params, 1L);
         log.info("Pages = " + pages);
         assertEquals(10, pages); 
         
         pages = paginator.getSlotPageCountN(item, slotName, params, 7L);
         log.info("Pages = " + pages);
         assertEquals(2, pages); 
         
         pages = paginator.getSlotPageCountN(item, slotName, params, 3L);
         log.info("Pages = " + pages);
         assertEquals(4, pages); 
         
         pages = paginator.getSlotPageCountN(item, slotName, params, 100L);
         log.info("Pages = " + pages);
         assertEquals(1, pages); 
         
         mockFinder.stubs().method("find").will(returnValue(buildMockItems(0)));
         pages = paginator.getSlotPageCountN(item, slotName, params, 100L);
         log.info("Pages = " + pages);
         assertEquals(1, pages); 

         
      } catch (Throwable e)
      {
         fail("Unexpected Expection " + e.getMessage()); 
         e.printStackTrace();
      }
   }
   
   private List<IPSAssemblyItem> buildMockItems(int count)
   {
      List<IPSAssemblyItem> ourList = new ArrayList<IPSAssemblyItem>();
      for(int i=0;i < count; i++)
      {
         IPSGuid guid = new PSLegacyGuid(i+1);
         Mock item = new Mock(IPSAssemblyItem.class); 
         item.stubs().method("getId").will(returnValue(guid));
         IPSAssemblyItem it = (IPSAssemblyItem)(item.proxy()); 
         ourList.add(it); 
      }
      log.info("Built List of " + ourList.size() + " items"); 
      return ourList; 
   }
}

