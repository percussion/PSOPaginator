package com.percussion.pso.paginator.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import com.percussion.cms.objectstore.PSComponentSummary;
import com.percussion.server.PSRequestParsingException;
import com.percussion.server.PSServer;
import com.percussion.services.assembly.IPSAssemblyService;
import com.percussion.services.assembly.IPSAssemblyTemplate;
import com.percussion.services.assembly.PSAssemblyException;
import com.percussion.services.assembly.PSAssemblyServiceLocator;
import com.percussion.services.catalog.PSTypeEnum;
import com.percussion.services.guidmgr.data.PSGuid;
import com.percussion.services.guidmgr.data.PSLegacyGuid;
import com.percussion.services.legacy.IPSCmsObjectMgr;
import com.percussion.services.legacy.PSCmsObjectMgrLocator;
import com.percussion.services.memory.IPSCacheAccess;
import com.percussion.services.memory.PSCacheAccessLocator;
import com.percussion.services.publisher.IPSContentList;
import com.percussion.services.publisher.IPSPublisherService;
import com.percussion.services.publisher.IPSPublisherServiceErrors;
import com.percussion.services.publisher.PSPublisherException;
import com.percussion.services.publisher.PSPublisherServiceLocator;
import com.percussion.util.IPSHtmlParameters;
import com.percussion.util.PSUrlUtils;
import com.percussion.utils.exceptions.PSExceptionHelper;
import com.percussion.utils.guid.IPSGuid;
import com.percussion.utils.timing.PSStopwatch;


public class ContentListServlet extends AbstractController {
    
    /**
     * Cache region in use
     */
    private static final String ms_region = "contentlist";

    private static IPSPublisherService pub = null;
    
    private static IPSCmsObjectMgr cms = null;
    
    private static IPSAssemblyService asm = null;  
    
    private static Log log = LogFactory.getLog(ContentListServlet.class);

    /**
     * Date format used in content lists
     */
    private static final SimpleDateFormat ms_datefmt = new SimpleDateFormat(
          "yyyy-MM-dd HH:mm:ss");
    /**
     * 
     */
    private static final long serialVersionUID = 1289502L;

    private IPSPageExpander pageExpander = null;
    
    protected static void initStatics()
    {
       if(asm == null)
       {
       pub = PSPublisherServiceLocator.getPublisherService();       
       cms = PSCmsObjectMgrLocator.getObjectManager();       
       asm = PSAssemblyServiceLocator.getAssemblyService();
       }
    }
    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {
       
        initStatics();
        IPSCacheAccess cache = PSCacheAccessLocator.getCacheAccess();
        PSStopwatch sw = new PSStopwatch();
        sw.start();
        String siteid = request.getParameter(IPSHtmlParameters.SYS_SITEID);
        String delivery = request
              .getParameter(IPSHtmlParameters.SYS_DELIVERYTYPE);
        String publishstr = request.getParameter(IPSHtmlParameters.SYS_PUBLISH);
        String context = request.getParameter(IPSHtmlParameters.SYS_CONTEXT);
        String assemblycontext = request.getParameter("sys_assembly_context");
        String contentlistname = request
              .getParameter(IPSHtmlParameters.SYS_CONTENTLIST);
        String publicationid = request
              .getParameter(IPSHtmlParameters.SYS_PUBLICATIONID);
        String maxresultsstr = request
              .getParameter(IPSHtmlParameters.MAXRESULTSPERPAGE);
        int maxresults = !StringUtils.isBlank(maxresultsstr) ? Integer
              .parseInt(maxresultsstr) : 0;
        String pagestr = request.getParameter(IPSHtmlParameters.SYS_PAGE);
        int page = !StringUtils.isBlank(pagestr) ? Integer.parseInt(pagestr) : 0;

        String host = request.getParameter(IPSHtmlParameters.SYS_HOST);
        String protocol = request.getParameter(IPSHtmlParameters.SYS_PROTOCOL);
        String portstr = request.getParameter(IPSHtmlParameters.SYS_PORT);
       
        int port = 0;
        if (!StringUtils.isBlank(portstr))
        {
           port = Integer.parseInt(portstr);
        }
        boolean publish = publishstr == null
              || !publishstr.equalsIgnoreCase("unpublish");
        try
        {
           requiredParam(delivery, IPSHtmlParameters.SYS_DELIVERYTYPE);
           requiredParam(context, IPSHtmlParameters.SYS_CONTEXT);
           requiredParam(contentlistname, IPSHtmlParameters.SYS_CONTENTLIST);
           
           int icontext = 0;
           if (!StringUtils.isBlank(assemblycontext))
           {
              icontext = Integer.parseInt(assemblycontext);
           }
           else
           {
              icontext = Integer.parseInt(context);   
           }

           StringWriter writer = new StringWriter();
           XMLOutputFactory ofact = XMLOutputFactory.newInstance();
           XMLStreamWriter f = beginDocument(delivery, context, writer, ofact);

           IPSContentList list = pub.findContentListByName(contentlistname);
           Map<String, String> overrides = new HashMap<String, String>();
           Map<String, String[]> params = request.getParameterMap();
           for (Map.Entry<String, String[]> e : params.entrySet())
           {
              if (e.getValue().length > 0)
              {
                 overrides.put(e.getKey(), e.getValue()[0]);
              }
           }
           if (siteid != null)
           {
              overrides.put("siteid", siteid);
           }
           List<IPSContentListItem> items = null;
           if (maxresults > 0 && !StringUtils.isBlank(publicationid))
           {
              items = (List<IPSContentListItem>) cache.get(publicationid, ms_region);
           }

           if (items == null)
           {
              items = PSContentListItemExtended.wrapContentList(pub.executeContentList(list, overrides, publish, 
                    Integer.parseInt(context)),  list.getFilter(), publish, publicationid);
              if (!StringUtils.isBlank(publicationid) && maxresults > 0)
              {
                    cache.save(publicationid, (Serializable) items, ms_region);
              }
           }

           // If we're limiting by results, create a sublist
           boolean done = false;
           if (maxresults > 0)
           {
              int start = page * maxresults;
              int end = start + maxresults;
              if (end >= items.size())
              {
                 done = true;
                 end = items.size();
              }
              items = items.subList(start, end);
           }
           if(pageExpander != null)
           {
              items = pageExpander.expand(items); 
           }
           
           
           for (IPSContentListItem item : items)
           {
              formatContentListItem( f,  host, protocol,
                    port, list, item);
           }
           f.writeCharacters("\n");
           if (maxresults > 0)
           {
              // No next if done
              if (!done)
              {
                 // Generate the element for the next page
                 f.writeStartElement("PSXNextPage");
                 StringBuffer urlbuf = request.getRequestURL();
                 urlbuf.append('?');
                 urlbuf.append(request.getQueryString());
                 String url = urlbuf.toString();
                 if (url.contains(IPSHtmlParameters.SYS_PAGE))
                 {
                    url = PSUrlUtils.replaceUrlParameterValue(url,
                          IPSHtmlParameters.SYS_PAGE, Integer.toString(page + 1));
                 }
                 else
                 {
                    url += "&" + IPSHtmlParameters.SYS_PAGE + "=" + (page + 1);
                 }
                 f.writeCharacters(url);
              }
              else
              {
                 // Clear the cached data
                 if (!StringUtils.isBlank(publicationid))
                 {
                    cache.evict(publicationid, ms_region);
                 }
              }
              // Create new url with the right page. If
           }

           f.writeEndDocument();
           response.setContentType("text/xml");
           PrintWriter w = response.getWriter();
           f.flush();
           f.close();
           writer.close();
           w.print(writer.toString());
           sw.stop();
           log.info("Processing content list for " + items.size()
                 + " items took " + sw);
        }
        catch (Exception e)
        {
           response.setContentType("text/plain");
           PrintWriter w;
           try
           {
              Throwable orig = PSExceptionHelper.findRootCause(e, true);
              log.error("Content list failure", orig);
              w = response.getWriter();
              w.println(e.getLocalizedMessage());
           }
           catch (IOException e1)
           {
              throw new RuntimeException(e1);
           }
        }
        return null;
    }
    
    /**
     * Formats a single content list entry for assembly and publishing. 
     * @param formatter the output stream where the entry will be written.
     * @param host the server host name
     * @param protocol the protocol. Usually <code>HTTP</code> or <code>HTTPS</code>
     * @param port the port the server listens on.
     * @param list the Content List definition. 
     * @param item the item to publish
     * @throws XMLStreamException
     * @throws PSPublisherException
     * @throws PSAssemblyException
     * @throws PSRequestParsingException
     */
    protected void formatContentListItem(
          XMLStreamWriter formatter,  String host, String protocol,
          int port, IPSContentList list, IPSContentListItem item)
          throws XMLStreamException, PSPublisherException, PSAssemblyException, PSRequestParsingException
    {
       if (pub == null)
       {
          throw new IllegalArgumentException("pub may not be null");
       }
       if (cms == null)
       {
          throw new IllegalArgumentException("cms may not be null");
       }
       if (asm == null)
       {
          throw new IllegalArgumentException("asm may not be null");
       }
       if (StringUtils.isBlank(host))
       {
          host = PSServer.getHostAddress();
       }
       if (StringUtils.isBlank(protocol))
       {
          protocol = "http";
       }
       if (list == null)
       {
          throw new IllegalArgumentException("list may not be null");
       }
       if (item == null)
       {
          throw new IllegalArgumentException("item may not be null");
       }
       if (port == 0)
       {
          if (protocol.equals("http"))
          {
             port = PSServer.getListenerPort();
          }
          else
          {
             port = PSServer.getSslListenerPort();
          }
       }

       PSLegacyGuid cid = (PSLegacyGuid) item.getContentId();
       List<Integer> ids = new ArrayList<Integer>();
       ids.add(new Integer(cid.getContentId()));
       PSComponentSummary s = cms.loadComponentSummaries(ids).get(0);
       String tid = Long.toString(item.getTemplateId().longValue());
       if (tid == null)
       {
          throw new PSPublisherException(IPSPublisherServiceErrors.RUNTIME_ERROR,
                "no template id found");
       }
       IPSGuid folderguid = item.getFolderId();
       
       IPSAssemblyTemplate template = asm.loadTemplate(new PSGuid(
               PSTypeEnum.TEMPLATE, tid), false);
       String baseURL = pub.constructAssemblyUrl(host, port, protocol, item.getSiteId(), 
             item.getContentId(), folderguid, template, item.getFilter(),
               item.getContext(), item.isPublish());
        item.setAssemblyURL(baseURL);
        formatter.writeCharacters("\n  ");
        formatter.writeStartElement("contentitem");
        formatter.writeAttribute("contentid", Integer
              .toString(cid.getContentId()));
        formatter.writeAttribute("revision", Integer.toString(cid.getRevision()));
        formatter.writeAttribute("unpublish", item.isPublish() ? "no" : "yes");
        formatter.writeAttribute("variantid", Long.toString(item.getTemplateId()
              .longValue()));
        formatter.writeCharacters("\n    ");
        
        formatter.writeCharacters("\n    ");
        formatter.writeStartElement("title");
        formatter.writeCharacters(s.getName());
        formatter.writeEndElement();
        formatter.writeCharacters("\n    ");
        formatter.writeStartElement("contenturl");

        formatter.writeCharacters(item.getAssemblyURL());
        formatter.writeEndElement();
        formatter.writeCharacters("\n    ");
        formatter.writeStartElement("delivery");
        formatter.writeCharacters("\n      ");
        formatter.writeStartElement("location");

        formatter.writeCharacters(item.getLocation());
        formatter.writeEndElement();
        formatter.writeEndElement();
        formatter.writeCharacters("\n    ");
        formatter.writeStartElement("modifydate");
        if (s.getContentLastModifiedDate() != null)
           formatter.writeCharacters(ms_datefmt.format(s
                 .getContentLastModifiedDate()));
        formatter.writeEndElement();
        formatter.writeCharacters("\n    ");
        formatter.writeStartElement("modifyuser");
        formatter.writeCharacters(s.getContentLastModifier());
        formatter.writeEndElement();
        formatter.writeCharacters("\n    ");
        formatter.writeStartElement("contenttype");
        formatter.writeCharacters(Long.toString(s.getContentTypeId()));
        formatter.writeEndElement();
        formatter.writeCharacters("\n  ");
        formatter.writeEndElement();
    }
    
    /**
    * @return Returns the pageExpander.
    */
   public IPSPageExpander getPageExpander()
   {
      return pageExpander;
   }

   /**
    * @param pageExpander The pageExpander to set.
    */
   public void setPageExpander(IPSPageExpander pageExpander)
   {
      this.pageExpander = pageExpander;
   }

   /**
     * Begin the output content list document
     * 
     * @param delivery
     * @param context
     * @param writer
     * @param ofact
     * @return the output STaX writer for the document, never <code>null</code>
     * @throws XMLStreamException
     */
    private XMLStreamWriter beginDocument(String delivery, String context,
          StringWriter writer, XMLOutputFactory ofact) throws XMLStreamException
    {
       XMLStreamWriter f = ofact.createXMLStreamWriter(writer);
       f.writeStartDocument();
       f.writeCharacters("\n");
       f.writeStartElement("contentlist");
       f.writeAttribute("context", context);
       f.writeAttribute("deliverytype", delivery);
       return f;
    }
    
    /**
     * Validate that the passed parameter value is not <code>null</code> or
     * empty.
     * 
     * @param param the param value
     * @param paramName the name of the param, assumed never <code>null</code>
     *           or empty
     */
    private void requiredParam(String param, String paramName)
    {
       if (StringUtils.isBlank(param))
       {
          throw new IllegalArgumentException(paramName
                + " is a required parameter");
       }
    }
   /**
    * @param asm The asm to set.
    */
   public static void setAsm(IPSAssemblyService asm)
   {
      log.info("Setting Assembly Service");
      ContentListServlet.asm = asm;
   }
   /**
    * @param cms The cms to set.
    */
   public static void setCms(IPSCmsObjectMgr cms)
   {
      ContentListServlet.cms = cms;
   }
   /**
    * @param pub The pub to set.
    */
   public static void setPub(IPSPublisherService pub)
   {
      ContentListServlet.pub = pub;
   }
  
}
