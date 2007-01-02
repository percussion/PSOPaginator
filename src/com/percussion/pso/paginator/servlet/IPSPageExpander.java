/*
 * com.percussion.pso.paginator.servlet IPSPageExpander.java
 *  
 * @author DavidBenua
 *
 */
package com.percussion.pso.paginator.servlet;

import java.util.List;
import com.percussion.services.assembly.PSAssemblyException;

/**
 * The page expander creates multiple page items when a item has 
 * more than one page. 
 *  
 *
 * @author DavidBenua
 *
 */
public interface IPSPageExpander
{
   /**
    * Expands a content list entry into multiple pages
    * @param items a list of items to expand.
    * @return the list of items expanded. 
    * @throws PSAssemblyException
    */
   public List<IPSContentListItem> expand(List<IPSContentListItem> items)
      throws PSAssemblyException;
}