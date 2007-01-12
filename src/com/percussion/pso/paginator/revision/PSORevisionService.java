/*
 * com.percussion.pso.paginator.revision.impl PSORevisionService.java
 *  
 * @author DavidBenua
 *
 */
package com.percussion.pso.paginator.revision;
import java.util.List;
import com.percussion.utils.guid.IPSGuid;
public interface PSORevisionService
{
   public PSORevisionCorrector getRevisionCorrector(String filterName,
         List<IPSGuid> guidList);
   public boolean isCorrectable(String itemFilterName);
}