/*
 * com.percussion.pso.paginator.servlet IPSPageExpander.java
 *  
 * @author DavidBenua
 *
 */
package com.percussion.pso.paginator.servlet;

import java.util.List;
import com.percussion.services.assembly.PSAssemblyException;

public interface IPSPageExpander
{
   public List<IPSContentListItem> expand(List<IPSContentListItem> items)
      throws PSAssemblyException;
}