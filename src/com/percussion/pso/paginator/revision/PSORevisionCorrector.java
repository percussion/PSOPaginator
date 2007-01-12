/*
 * com.percussion.pso.paginator.revision.impl PSORevisionCorrector.java
 *  
 * @author DavidBenua
 *
 */
package com.percussion.pso.paginator.revision;
import com.percussion.error.PSException;
import com.percussion.utils.guid.IPSGuid;
public interface PSORevisionCorrector
{
   public IPSGuid correct(IPSGuid inguid) throws PSException;
   public IPSGuid getCorrectGuid(int contentId) throws PSException;
   /**
    * @return Returns the correctable.
    */
   public boolean isCorrectable();
}