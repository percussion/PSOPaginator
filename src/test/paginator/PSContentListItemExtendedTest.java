/*
 * test.paginator PSContentListItemExtendedTest.java
 *  
 * @author DavidBenua
 *
 */
package test.paginator;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import com.percussion.pso.paginator.servlet.IPSContentListItem;
import com.percussion.pso.paginator.servlet.PSContentListItemExtended;
import com.percussion.server.PSRequestParsingException;
import com.percussion.services.filter.IPSItemFilter;
import com.percussion.services.guidmgr.data.PSLegacyGuid;
import com.percussion.services.publisher.data.PSContentListItem;
import com.percussion.utils.guid.IPSGuid;

public class PSContentListItemExtendedTest extends TestCase
{
   PSContentListItem oldItem;
   PSContentListItemExtended newItem;
   IPSItemFilter filter; 
   
   public static void main(String[] args)
   {
      junit.textui.TestRunner.run(PSContentListItemExtendedTest.class);
   }
   protected void setUp() throws Exception
   {
      IPSGuid cid = new PSLegacyGuid(1);
      IPSGuid fid = new PSLegacyGuid(2);
      IPSGuid tid = new PSLegacyGuid(3); 
      IPSGuid siteid = new PSLegacyGuid(4);
      oldItem = new PSContentListItem(cid,fid,tid,siteid,5);
      oldItem.setLocation("Test Location"); 
      filter = new MockItemFilter("filter","test filter"); 
      newItem = new PSContentListItemExtended(cid,fid,tid,siteid,5,filter,true,"12367");
      newItem.setLocation("Test Location 2"); 
   }
   protected void tearDown() throws Exception
   {
      super.tearDown();
   }
   /*
    * Test method for 'com.percussion.pso.paginator.servlet.PSContentListItemExtended.clone()'
    */
   public void testClone()
   {
      IPSContentListItem testItem = newItem.clone(); 
      assertNotNull(testItem); 
      assertEquals(newItem.getContentId(),testItem.getContentId()); 
      assertEquals(newItem.getLocation(), testItem.getLocation()); 
      assertEquals(newItem.getFilter().getName(), testItem.getFilter().getName()); 
   }
   /*
    * Test method for 'com.percussion.pso.paginator.servlet.PSContentListItemExtended.setAssemblyURL(String)'
    */
   public void testSetAssemblyURLString()
   {
       newItem.getExtraParams().put("foo", new String[]{"bar"});
       try
      {
         newItem.setAssemblyURL("http://www.xyzzy.com/index.html?param1=1&param2=2");
      } catch (PSRequestParsingException e)
      {
         fail("should not get parsing exception"); 
         e.printStackTrace();
      } 
      String url = newItem.getAssemblyURL(); 
      //System.out.println("Url " + url); 
      assertTrue(url.indexOf("foo=bar") > 1);
   }
   /*
    * Test method for 'com.percussion.pso.paginator.servlet.PSContentListItemExtended.wrapContentList(List<PSContentListItem>, IPSItemFilter, boolean, String)'
    */
   public void testWrapContentList()
   {
      List<PSContentListItem> olist = new ArrayList<PSContentListItem>();
      List<IPSContentListItem> nlist;
      
      olist.add(oldItem);
      nlist = PSContentListItemExtended.wrapContentList(olist, filter, false, "12345");
      assertEquals(1,nlist.size()); 
      IPSContentListItem nitem = nlist.get(0); 
      assertEquals(oldItem.getContentId(), nitem.getContentId()); 
      assertEquals("12345",nitem.getPublicationid()); 
      assertEquals(filter.getName(), nitem.getFilter().getName());
      
   }
}
