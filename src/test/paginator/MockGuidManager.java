/*
 * test.paginator MockGuidManager.java
 *  
 * @author DavidBenua
 *
 */
package test.paginator;

import java.util.List;
import com.percussion.design.objectstore.PSLocator;
import com.percussion.services.catalog.PSTypeEnum;
import com.percussion.services.guidmgr.IPSGuidManager;
import com.percussion.utils.guid.IPSGuid;

/**
 * 
 *
 * @author DavidBenua
 *
 */
public class MockGuidManager implements IPSGuidManager
{
   /**
    * 
    */
   public MockGuidManager()
   {
      super();
      // TODO Auto-generated constructor stub
   }
   /**
    * @see com.percussion.services.guidmgr.IPSGuidManager#createGuid(com.percussion.services.catalog.PSTypeEnum)
    */
   public IPSGuid createGuid(PSTypeEnum type)
   {
      // TODO Auto-generated method stub
      return null;
   }
   /**
    * @see com.percussion.services.guidmgr.IPSGuidManager#createGuids(com.percussion.services.catalog.PSTypeEnum, int)
    */
   public List<IPSGuid> createGuids(PSTypeEnum type, int count)
   {
      // TODO Auto-generated method stub
      return null;
   }
   /**
    * @see com.percussion.services.guidmgr.IPSGuidManager#createGuid(byte, com.percussion.services.catalog.PSTypeEnum)
    */
   public IPSGuid createGuid(byte repositoryId, PSTypeEnum type)
   {
      // TODO Auto-generated method stub
      return null;
   }
   /**
    * @see com.percussion.services.guidmgr.IPSGuidManager#createGuids(byte, com.percussion.services.catalog.PSTypeEnum, int)
    */
   public List<IPSGuid> createGuids(byte repositoryId, PSTypeEnum type,
         int count)
   {
      // TODO Auto-generated method stub
      return null;
   }
   /**
    * @see com.percussion.services.guidmgr.IPSGuidManager#createId(java.lang.String)
    */
   public int createId(String key)
   {
      // TODO Auto-generated method stub
      return 0;
   }
   /**
    * @see com.percussion.services.guidmgr.IPSGuidManager#createIdBlock(java.lang.String, int)
    */
   public int[] createIdBlock(String key, int blocksize)
   {
      // TODO Auto-generated method stub
      return null;
   }
   /**
    * @see com.percussion.services.guidmgr.IPSGuidManager#getHostId()
    */
   public long getHostId()
   {
      // TODO Auto-generated method stub
      return 0;
   }
   /**
    * @see com.percussion.services.guidmgr.IPSGuidManager#makeGuid(long, com.percussion.services.catalog.PSTypeEnum)
    */
   public IPSGuid makeGuid(long raw, PSTypeEnum type)
   {
      // TODO Auto-generated method stub
      return null;
   }
   /**
    * @see com.percussion.services.guidmgr.IPSGuidManager#makeGuid(java.lang.String, com.percussion.services.catalog.PSTypeEnum)
    */
   public IPSGuid makeGuid(String raw, PSTypeEnum type)
   {
      // TODO Auto-generated method stub
      return null;
   }
   /**
    * @see com.percussion.services.guidmgr.IPSGuidManager#makeGuid(com.percussion.design.objectstore.PSLocator)
    */
   public IPSGuid makeGuid(PSLocator loc)
   {
      // TODO Auto-generated method stub
      return null;
   }
}
