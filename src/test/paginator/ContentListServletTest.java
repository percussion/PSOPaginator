package test.paginator;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.percussion.cms.objectstore.PSComponentSummary;
import com.percussion.pso.paginator.servlet.ContentListServlet;
import com.percussion.pso.paginator.servlet.IPSContentListItem;
import com.percussion.pso.paginator.servlet.IPSContentListItemLocationGenerator;
import com.percussion.services.assembly.IPSAssemblyService;
import com.percussion.services.assembly.IPSAssemblyTemplate;
import com.percussion.services.catalog.PSTypeEnum;
import com.percussion.services.filter.IPSItemFilter;
import com.percussion.services.guidmgr.data.PSGuid;
import com.percussion.services.guidmgr.data.PSLegacyGuid;
import com.percussion.services.legacy.IPSCmsObjectMgr;
import com.percussion.services.memory.IPSCacheAccess;
import com.percussion.services.publisher.IPSContentList;
import com.percussion.services.publisher.IPSPublisherService;
import com.percussion.services.publisher.data.PSContentListItem;
import com.percussion.util.IPSHtmlParameters;
import com.percussion.utils.guid.IPSGuid;

@RunWith(JMock.class)
public class ContentListServletTest {

    Mockery context = new JUnit4Mockery();
    ContentListServlet servlet;
    HttpServletRequest request;
    HttpServletResponse response;
    IPSPublisherService pub = null;
    IPSCmsObjectMgr cms = null;
    IPSAssemblyService asm = null;  
    IPSCacheAccess access = null;
    IPSCacheAccess cache;
    IPSContentList contentList;
    IPSItemFilter itemFilter;
    IPSGuid templateId;
    IPSAssemblyTemplate assemblyTemplate;
    
    @SuppressWarnings("static-access")
    @Before
    public void setUp() throws Exception {
        request = context.mock(HttpServletRequest.class);
        response = context.mock(HttpServletResponse.class);
        pub = context.mock(IPSPublisherService.class);
        cms = context.mock(IPSCmsObjectMgr.class);
        asm = context.mock(IPSAssemblyService.class);
        cache = context.mock(IPSCacheAccess.class);
        contentList = context.mock(IPSContentList.class);
        assemblyTemplate = context.mock(IPSAssemblyTemplate.class);
        templateId = new PSGuid(PSTypeEnum.TEMPLATE, 1L);
        servlet = new ContentListServlet();
        servlet.setPub(pub);
        servlet.setCms(cms);
        servlet.setAsm(asm);
        servlet.setCache(cache);
        servlet.setItemLocationGenerator(
                new IPSContentListItemLocationGenerator () {

                    public String makeLocation(IPSContentListItem item) {
                        return "test_location";
                    }
                    
                });
        
    }
    
    public void setupRequest(final Map<String, String> params) {
        
        final Map<String,String[]> paramMap = new HashMap<String, String[]>();
        context.checking(new Expectations () {{
            for (Entry<String, String> entry : params.entrySet()) {
                atLeast(1).of(request).getParameter(entry.getKey());
                will(returnValue(entry.getValue()));
                if( entry.getValue() != null) 
                    paramMap.put(entry.getKey(), new String [] {entry.getValue()});
            }
            atLeast(1).of(request).getParameterMap();
            will(returnValue(paramMap));
            
            atLeast(1).of(request).getMethod();
            will(returnValue("GET"));
        }});
        
    }
    
    public StringWriter setupResponse() throws Exception {
        StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        context.checking(new Expectations () {{
            one(response).setContentType("text/xml");
            one(response).getWriter();
            will(returnValue(pw));
        }});
        return sw;
    }
    
    public Map<String,String> makeParams() {
        //sys_deliverytype=ftp&sys_assembly_context=301&sys_contentlist=perc_paginate&sys_siteid=305&sys_authtype=101&sys_context=1
        Map<String,String> params = new HashMap<String, String>();
        params.put(IPSHtmlParameters.SYS_HOST, null);
        params.put(IPSHtmlParameters.SYS_PROTOCOL, null);
        params.put(IPSHtmlParameters.SYS_PORT, null);
        params.put(IPSHtmlParameters.SYS_SITEID,"305");
        params.put(IPSHtmlParameters.SYS_PUBLISH, null);
        params.put(IPSHtmlParameters.SYS_PUBLICATIONID, null);
        params.put(IPSHtmlParameters.MAXRESULTSPERPAGE, null);
        params.put(IPSHtmlParameters.SYS_PAGE, null);
        params.put(IPSHtmlParameters.SYS_DELIVERYTYPE,"ftp");
        params.put(IPSHtmlParameters.SYS_CONTENTLIST,"test_contentlist");
        params.put(IPSHtmlParameters.SYS_CONTEXT,"1");
        params.put("sys_assembly_context","301");
        return params;
    }
    
    public class TestContentListItem extends PSContentListItem {

        public TestContentListItem(IPSGuid cid, IPSGuid fid, IPSGuid tid, IPSGuid siteid, int contextid) {
            super(cid, fid, tid, siteid, contextid);
        }
        
        /**
         * @return Returns the location.
         */
        public String getLocation()
        {
            return "";
        }
        
    }
    public PSContentListItem makeItem(final int contentId, final int context) throws Exception {
        final PSContentListItem item = new TestContentListItem(new PSLegacyGuid(contentId),
                new PSLegacyGuid(1),
                templateId,
                new PSLegacyGuid(1), context );
        final PSComponentSummary summary = new PSComponentSummary();
        summary.setContentId(contentId);
        summary.setName("test_name");
        this.context.checking(new Expectations () {{
            one(cms).loadComponentSummaries(Arrays.asList(1));
            will(returnValue(Arrays.asList(summary)));
        }});
        return item;
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void shouldUseAssemblyContextForContextInRenderUrl() throws Exception {
        final Map<String,String> params = makeParams();
        setupRequest(params);
        StringWriter responseWriter = setupResponse();
        servlet.setPageExpander(null);
        
       
        //IPSContentList list = pub.findContentListByName(contentlistname);
        //pub.executeContentList(list, overrides, publish, Integer.parseInt(context))
//        String baseURL = pub.constructAssemblyUrl(host, port, protocol, item.getSiteId(), 
//                item.getContentId(), folderguid, template, item.getFilter(),
//                  assemblyContext, item.isPublish());
        context.checking(new Expectations () {{
            one(pub).findContentListByName("test_contentlist");
            will(returnValue(contentList));
            
            one(pub).executeContentList(with(equal(contentList)), with(any(Map.class)), 
                    with(any(Boolean.class)), 
                    with(equal(1)));
            will(returnValue(Arrays.asList(makeItem(1,1))));
            
            one(contentList).getFilter();
            will(returnValue(itemFilter));
            
            one(asm).loadTemplate(templateId, false);
            will(returnValue(assemblyTemplate));
            //constructAssemblyUrl(String, int, String, IPSGuid, IPSGuid, IPSGuid, 
            //IPSAssemblyTemplate, IPSItemFilter, int, boolean)
            one(pub).constructAssemblyUrl(with(any(String.class)), with(any(Integer.class)), 
                    with(any(String.class)), with(any(IPSGuid.class)), with(any(IPSGuid.class)),
                    with(any(IPSGuid.class)),
                    with(any(IPSAssemblyTemplate.class)), with(any(IPSItemFilter.class)),
                    //NOTICE the context is the assembly context!!!
                    with(equal(301)), 
                    with(any(Boolean.class)));
            will(returnValue("test_assembly_url"));

            
        }});
        servlet.handleRequest(request, response);
        System.out.println(responseWriter.toString());
    }

}
