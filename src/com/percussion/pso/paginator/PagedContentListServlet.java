/*
 * com.percussion.pso.scripps.paginator PagedContentListServlet.java
 *  
 * @author DavidBenua
 *
 */
package com.percussion.pso.scripps.paginator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
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
import com.percussion.services.catalog.PSTypeEnum;
import com.percussion.services.guidmgr.data.PSGuid;
import com.percussion.services.guidmgr.data.PSLegacyGuid;
import com.percussion.services.legacy.IPSCmsObjectMgr;
import com.percussion.services.publisher.IPSContentList;
import com.percussion.services.publisher.IPSPublisherService;
import com.percussion.services.publisher.IPSPublisherServiceErrors;
import com.percussion.services.publisher.PSPublisherException;
import com.percussion.services.publisher.data.PSContentListItem;
import com.percussion.services.publisher.impl.PSContentListServlet;
import com.percussion.utils.guid.IPSGuid;

public class PagedContentListServlet extends PSContentListServlet
{
   /**
    * Logger for this class
    */
   private static final Log log = LogFactory
         .getLog(PagedContentListServlet.class);
   
   
   private String httpParamPageNo;


   public PagedContentListServlet()
   {
      super();
      
   }
   
   /**
    * The HTTP parameter name to be passed to the assembly template to specify which 
    * page to render
    * 
    * @return The parameter name (if not setup correctly with spring it maybe null)
    */
   public String getHttpParamPageNo() {
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
    * Parses the location for the total number of pages.
    * The format of the location string should be 
    * "xxx_n.xxx" or "xxxxxx_n" where n is a digit.
    * 
    * @param location
    * @return number of pages, -1 if n is not found, never null.  
    */
   public int pageNoFromLocation(String location)
   {
      String basename = StringUtils.substringBeforeLast(location, ".");
      String pageno = StringUtils.substringAfterLast(basename, "_");
      if (StringUtils.isNotBlank(pageno) && StringUtils.isNumeric(pageno))
      {
         return Integer.parseInt(pageno);
      }
      return -1;
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
               } 
               return prefix + "_" + pageno;
           }    
       return location; 
   }
   
   /**
    * Date format used in content lists
    */
   private static final SimpleDateFormat ms_datefmt = new SimpleDateFormat(
         "yyyy-MM-dd HH:mm:ss");

   /**
    * 
    */
   private static final long serialVersionUID = 1L;


   /**
    * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
    */
   @Override
   public void init(ServletConfig cfg) throws ServletException
   {
      super.init(cfg);
      String pageParam = cfg.getInitParameter("httpParamPageNo"); 
      this.setHttpParamPageNo(pageParam);       
   }

}
