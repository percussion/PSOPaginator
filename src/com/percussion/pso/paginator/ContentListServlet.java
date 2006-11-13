package com.percussion.pso.paginator;

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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.percussion.cms.objectstore.PSComponentSummary;
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
import com.percussion.services.publisher.data.PSContentListItem;
import com.percussion.util.IPSHtmlParameters;
import com.percussion.util.PSUrlUtils;
import com.percussion.utils.exceptions.PSExceptionHelper;
import com.percussion.utils.guid.IPSGuid;
import com.percussion.utils.timing.PSStopwatch;


public class ContentListServlet extends HttpServlet {
    
    /**
     * Cache region in use
     */
    private static final String ms_region = "contentlist";

    private static Log log = LogFactory.getLog(ContentListServlet.class);

    /**
     * Date format used in content lists
     */
    private static final SimpleDateFormat ms_datefmt = new SimpleDateFormat(
          "yyyy-MM-dd HH:mm:ss");

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {
        /*
         * Check if the pageno variable is set.
         */
        log.debug("Pageno variable is " + this.getHttpParamPageNo());
        
        IPSPublisherService pub = PSPublisherServiceLocator.getPublisherService();
        IPSCacheAccess cache = PSCacheAccessLocator.getCacheAccess();
        PSStopwatch sw = new PSStopwatch();
        sw.start();
        IPSCmsObjectMgr cms = PSCmsObjectMgrLocator.getObjectManager();
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
        IPSAssemblyService asm = PSAssemblyServiceLocator.getAssemblyService();
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
           List<PSContentListItem> items = null;
           if (maxresults > 0 && !StringUtils.isBlank(publicationid))
           {
              items = (List<PSContentListItem>) cache.get(publicationid, ms_region);
           }

           if (items == null)
           {
              items = pub.executeContentList(list, overrides, publish, 
                    Integer.parseInt(context));
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

           for (PSContentListItem item : items)
           {
              formatContentListItem(pub, cms, asm, f, icontext, host, protocol,
                    port, publish, list, item);
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
    }
    
    /**
     * Assemble a single content list item
     * 
     * @param pub the publishing service, never <code>null</code>
     * @param cms the legacy content service, never <code>null</code>
     * @param asm the assembly service, never <code>null</code>
     * @param formatter the xml output formatter, never <code>null</code>
     * @param context the context
     * @param host the host, if <code>null</code> or empty, defaults to the
     *           server's host
     * @param protocol the protocol as a string, defaults to server's protocol
     * @param port the port, defaults to server's port
     * @param publish the publish flag
     * @param list the content list that is being processed, never
     *           <code>null</code>
     * @param item the current item being run, never <code>null</code>
     * @throws XMLStreamException
     * @throws PSPublisherException
     * @throws PSAssemblyException
     */
    protected void formatContentListItem(IPSPublisherService pub,
          IPSCmsObjectMgr cms, IPSAssemblyService asm,
          XMLStreamWriter formatter, int context, String host, String protocol,
          int port, boolean publish, IPSContentList list, PSContentListItem item)
          throws XMLStreamException, PSPublisherException, PSAssemblyException
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
       /*
        * Get the number of pages 
        */
       List<Integer> ids = new ArrayList<Integer>();
       PSLegacyGuid cid = (PSLegacyGuid) item.getContentId();
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
         String url = pub.constructAssemblyUrl(host, port, protocol, item
               .getSiteId(), item.getContentId(), folderguid, template, list
               .getFilter(), context, publish);
         
       int total_pages = pageNoFromLocation(item.getLocation());
       
       if (total_pages != -1) {
            for (int page = 1; page <= total_pages; page++) {

                log.debug("Pageno: " + page);
                log.debug("Old Assembly url: " + url);
                url = url + "&" + this.getHttpParamPageNo() + "=" + page;
                log.debug("New Assembly url: " + url);
                formatContentListItem(url, createNewLocation(
                        item.getLocation(), page), formatter, publish, s, item);
            }
        } else {
            log.debug("Item is not paginated: location = " + item.getLocation());
            formatContentListItem(url, item.getLocation(), formatter, publish, s, item);
        }
           

    }
    
    /**
     * Helper method for the real formatContentListItem
     * 
     * @param url
     * @param location
     * @param formatter
     * @param publish
     * @param s
     * @param item
     * @throws XMLStreamException
     */
    private void formatContentListItem(String url, String location, XMLStreamWriter formatter, 
            boolean publish, PSComponentSummary s, PSContentListItem item)
            throws XMLStreamException {
        formatter.writeCharacters("\n  ");
        formatter.writeStartElement("contentitem");
        PSLegacyGuid cid = (PSLegacyGuid) item.getContentId();
        formatter.writeAttribute("contentid", Integer
              .toString(cid.getContentId()));
        formatter.writeAttribute("revision", Integer.toString(cid.getRevision()));
        formatter.writeAttribute("unpublish", publish ? "no" : "yes");
        formatter.writeAttribute("variantid", Long.toString(item.getTemplateId()
              .longValue()));
        formatter.writeCharacters("\n    ");
        
        formatter.writeCharacters("\n    ");
        formatter.writeStartElement("title");
        formatter.writeCharacters(s.getName());
        formatter.writeEndElement();
        formatter.writeCharacters("\n    ");
        formatter.writeStartElement("contenturl");

        formatter.writeCharacters(url);
        formatter.writeEndElement();
        formatter.writeCharacters("\n    ");
        formatter.writeStartElement("delivery");
        formatter.writeCharacters("\n      ");
        formatter.writeStartElement("location");

        formatter.writeCharacters(location);
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
     * Parses the location for the total number of pages.
     * The format of the location string should be 
     * "xxx_n.xxx" or "xxxxxx_n" where n is a digit.
     * 
     * @param location
     * @return number of pages, -1 if n is not found, never null.  
     */
    public int pageNoFromLocation(String location) {
        String basename = StringUtils.substringBeforeLast(location,".");
        String pageno = StringUtils.substringAfterLast(basename, "_");
        if (StringUtils.isNotBlank(pageno) && StringUtils.isNumeric(pageno)) {
            return Integer.parseInt(pageno);
        } else {
            return -1;
        }
    }
    
    /**
     * Takes the old location and creates a new location with the given page number.
     * 
     * @param location, should not be null.
     * @param pageno
     * @return location, never null
     */
    public String createNewLocation(String location, int pageno) {
        String basename = StringUtils.substringBeforeLast(location,".");
        String ext = StringUtils.substringAfterLast(location, ".");
        String old_pageno = StringUtils.substringAfterLast(basename, "_");
        String prefix = StringUtils.substringBeforeLast(basename,"_");
        if (StringUtils.isNotBlank(old_pageno) && StringUtils.isNumeric(old_pageno)) {
            if (StringUtils.isNotBlank(ext)) {
                return prefix + "_" + pageno + "." + ext;
            } else {
                return prefix + "_" + pageno;
            }
        } else {
            return location;
        }
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
    

    private String httpParamPageNo;

    /**
     * The HTTP parameter name to be passed to the assembly template to specify which 
     * page to render
     * 
     * @return The parameter name (if not setup correctly with spring it maybe null)
     */
    public String getHttpParamPageNo() {
        if (httpParamPageNo == null) {
            httpParamPageNo = getServletConfig().getInitParameter("httpParamPageNo");
        }
        return httpParamPageNo;
    }

    /**
     * Sets the HTTP parameter name for page number 
     * to be passed to the assembly template.
     * 
     * @param httpParamPageNo
     */
    public void setHttpParamPageNo(String httpParamPageNo) {
        this.httpParamPageNo = httpParamPageNo;
    }
    
    
    

}
