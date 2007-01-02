/*
 * com.percussion.pso.paginator.servlet PSTemplatePageExpander.java
 *  
 * @author DavidBenua
 *
 */
package com.percussion.pso.paginator.servlet;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.jexl.Expression;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.percussion.pso.utils.SimplifyParameters;
import com.percussion.services.assembly.IPSAssemblyResult;
import com.percussion.services.assembly.PSAssemblyException;
import com.percussion.utils.jexl.PSJexlEvaluator;

/**
 * 
 *
 * @author DavidBenua
 *
 */
public class PSTemplatePageExpander extends PSAbstractPageExpander
      implements
         IPSPageExpander
{
   /**
    * Logger for this class
    */
   private static final Log log = LogFactory.getLog(PSTemplatePageExpander.class);
   
   private String pageCountParameterName ="pagecount"; 
   
   private String pageCountBindingName = "pagecount"; 
   
   /**
    * 
    */
   public PSTemplatePageExpander()
   {
      super();
   }
   /**
    * @throws Exception 
    * @see com.percussion.pso.paginator.servlet.PSAbstractPageExpander#countPages(com.percussion.pso.paginator.servlet.IPSContentListItem)
    */
   @Override
   protected int countPages(IPSContentListItem item) 
      throws PSAssemblyException
   {
      Map<String,String[]> moreParams = new HashMap<String, String[]>(); 
      moreParams.put(getPageCountParameterName(), new String[]{"true"}); 
      
      IPSAssemblyResult result = super.assembleItem(item, moreParams);
      PSJexlEvaluator jexl = new PSJexlEvaluator(result.getBindings());
      Object pageCount;
      try
      {
         Expression expr = PSJexlEvaluator.createExpression(getPageCountBindingName());
         pageCount = jexl.evaluate(expr);
      } catch (Exception e)
      {
         log.error("Unexpected Exception evaluating JEXL expression " + e.getMessage(), e); 
         throw new PSAssemblyException(0,e,new Object[0]); 
      }
      if(pageCount == null)
      {
         log.warn("Unable to count pages. Binding " + getPageCountBindingName() + " is null");
         return -1;
      }
      log.trace("pageCount is " + pageCount ); 
      if(pageCount instanceof Number)
      {
         Number pageno = (Number)pageCount;
         log.trace("Page count is numeric " + pageno); 
         return pageno.intValue(); 
      }
      String pageStr = SimplifyParameters.simplifyValue(pageCount);
      if(StringUtils.isBlank(pageStr) || !StringUtils.isNumeric(pageStr))
      {
         log.warn("Page count binding must be numeric - found " + pageStr);
         return -1; 
      }
      log.trace("Page count as String:" + pageStr); 
      int pageInt = Integer.parseInt(pageStr); 
      
      log.trace("Page count as int: " + pageInt); 
      return pageInt;
   }
   
   
   /**
    * @return Returns the pageCountParameterName.
    */
   public String getPageCountParameterName()
   {
      return pageCountParameterName;
   }
   /**
    * @param pageCountParameterName The pageCountParameterName to set.
    */
   public void setPageCountParameterName(String pageCountParameterName)
   {
      this.pageCountParameterName = pageCountParameterName;
   }
   /**
    * @return Returns the pageCountBindingName.
    */
   public String getPageCountBindingName()
   {
      return pageCountBindingName;
   }
   /**
    * @param pageCountBindingName The pageCountBindingName to set.
    */
   public void setPageCountBindingName(String pageCountBindingName)
   {
      this.pageCountBindingName = pageCountBindingName;
   }
}
