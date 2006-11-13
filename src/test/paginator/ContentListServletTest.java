package test.paginator;

import com.percussion.pso.scripps.paginator.ContentListServlet;

import junit.framework.TestCase;

public class ContentListServletTest extends TestCase {

    public void testPageNoFromLocation() {
        ContentListServlet sv = new ContentListServlet();
        int single = sv.pageNoFromLocation("test_9.html");
        assertEquals(single,9);
        
        int noExt = sv.pageNoFromLocation("test_10");
        assertEquals(10,noExt);
        
        int blank = sv.pageNoFromLocation("");
        assertEquals(blank,-1);
    }
    
    public void testCreateLocation() {
        ContentListServlet sv = new ContentListServlet();
        String newLocation = sv.createNewLocation("test_9.html", 10);
        assertEquals("test_10.html",newLocation);
        String double_under = sv.createNewLocation("one_234_two_45.html", 10);
        assertEquals("one_234_two_10.html", double_under);
        String no_ext = sv.createNewLocation("test_22", 10);
        assertEquals("test_10",no_ext);
        String emptyLocation = sv.createNewLocation("", 10);
        assertEquals(emptyLocation,"");
    }
}
