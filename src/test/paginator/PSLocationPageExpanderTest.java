/*
 * test.paginator PSLocationPageExpanderTest.java
 *  
 * @author DavidBenua
 *
 */
package test.paginator;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.percussion.pso.paginator.servlet.IPSContentListItem;
import com.percussion.pso.paginator.servlet.PSContentListItemExtended;
import com.percussion.pso.paginator.servlet.PSLocationPageExpander;
import com.percussion.services.filter.IPSItemFilter;
import com.percussion.services.guidmgr.data.PSLegacyGuid;
import com.percussion.utils.guid.IPSGuid;

public class PSLocationPageExpanderTest extends TestCase
{
   /**
    * Logger for this class
    */
   private static final Log log = LogFactory.getLog(PSLocationPageExpanderTest.class);
   
   List<IPSContentListItem> itemList = new ArrayList<IPSContentListItem>();
   
   PSLocationPageExpander expander = new PSLocationPageExpander();
   
   public static void main(String[] args)
   {
      junit.textui.TestRunner.run(PSLocationPageExpanderTest.class);
   }
   protected void setUp() throws Exception
   {
      super.setUp();
    
      expander.setPageParamName("pageno"); 
      
      IPSGuid cid = new PSLegacyGuid(1);
      IPSGuid fid = new PSLegacyGuid(2);
      IPSGuid tid = new PSLegacyGuid(3); 
      IPSGuid siteid = new PSLegacyGuid(4);
      IPSItemFilter filter = new MockItemFilter("filter","test filter"); 
      IPSContentListItem newItem = new PSContentListItemExtended(cid,fid,tid,siteid,5,filter,true,"12367"); 
     
      itemList.add(newItem);        
      
   }
   
  
   /*
    * Test method for 'com.percussion.pso.paginator.servlet.PSAbstractPageExpander.expand(List<IPSContentListItem>)'
    */
   public void testExpand()
   {
      IPSContentListItem original = itemList.get(0); 
      original.setLocation("/foo/bar/xyz_4.html");
      
      try
      {
         List<IPSContentListItem> expandedList = expander.expand(itemList);
         log.info("Expanded List is "  + expandedList);
         assertNotNull(expandedList); 
         assertEquals(4, expandedList.size());
         IPSContentListItem testItem = expandedList.get(0);
         String location = testItem.getLocation(); 
         assertTrue(location.indexOf("xyz_1.html") > 1); 
         assertTrue(testItem.getExtraParams().containsKey("pageno")); 
         assertEquals(1,Integer.parseInt(testItem.getExtraParams().get("pageno")[0])); 
         
         testItem = expandedList.get(3); 
         log.info("Test Item is "  + testItem); 
         location = testItem.getLocation();
         assertTrue(location.indexOf("xyz_4.html") > 1); 
         assertTrue(testItem.getExtraParams().containsKey("pageno")); 
         assertEquals(4,Integer.parseInt(testItem.getExtraParams().get("pageno")[0]));
         
         original.setLocation("/foo/bar/xyz.html");
         expandedList = expander.expand(itemList); 
         assertNotNull(expandedList); 
         assertEquals(1,expandedList.size()); 
         testItem = expandedList.get(0);
         location = testItem.getLocation();
         assertTrue(location.indexOf("xyz.html") > 1);
      } catch (NumberFormatException e)
      {
         fail("Unexpected Exception"); 
         e.printStackTrace();
      } catch (Exception e)
      {
         fail("Unexpected Exception"); 
         e.printStackTrace();
      } 
   }
}
