/*
 * Created on Dec 30, 2003
 *  
 */
package ServidorPersistente.cache;

import java.util.Calendar;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.CurricularCourse;
import Dominio.CurricularCourseScope;
import Dominio.Enrolment;
import Dominio.ExecutionCourse;
import Dominio.Frequenta;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 *  
 */
public class DistributedCachePerformanceTest extends ObjectFenixOJB
{

	private static SuportePersistenteOJB persistentSupport;
	private static DistributedCachePerformanceTest cacheTest;

	private static Calendar startTime;
	private static Calendar endTime;
	
	private static Class classToRead = CurricularCourse.class;

	static {
		try
		{
			persistentSupport = SuportePersistenteOJB.getInstance();
			cacheTest = new DistributedCachePerformanceTest();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public DistributedCachePerformanceTest()
	{
		super();
	}

	public static void main(String[] args)
	{
		System.out.println("   ### Testing distributed cache performance ###");
		try
		{
			// Make a bogus db connection so that initialization overheads
			// don't influence test results.
			warmUp();
			
			// Do a couple of reads...
			readAlotOfObjects();
			readAlotOfObjects();
			readAlotOfObjects();
			readAlotOfObjects();
			readAlotOfObjects();
			readAlotOfObjects();
			readAlotOfObjects();
			readAlotOfObjects();
			readAlotOfObjects();
			readAlotOfObjects();
			readAlotOfObjects();
			
			clearCache();
			readAlotOfObjects();
			readAlotOfObjects();
			readAlotOfObjects();
			readAlotOfObjects();
			readAlotOfObjects();
			readAlotOfObjects();
			readAlotOfObjects();
			readAlotOfObjects();
			readAlotOfObjects();
			readAlotOfObjects();
			readAlotOfObjects();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private static void clearCache() throws ExcepcaoPersistencia
	{
		persistentSupport.iniciarTransaccao();
		persistentSupport.clearCache();
		System.out.println("Cache cleared.");
		persistentSupport.confirmarTransaccao();
	}

	/**
	 * 
	 */
	private static void warmUp() throws ExcepcaoPersistencia
	{
		persistentSupport.iniciarTransaccao();
		persistentSupport.confirmarTransaccao();
	}

	private static void readAlotOfObjects() throws ExcepcaoPersistencia
	{
		persistentSupport.iniciarTransaccao();
		startTime = Calendar.getInstance();
		List objects = cacheTest.doTheRead();
		endTime = Calendar.getInstance();
		System.out.println(
			"Read a total of "
				+ objects.size()
				+ " "
				+ classToRead.getName()
				+ " in "
				+ cacheTest.calculateServiceExecutionTime(startTime, endTime)
				+ " ms");
		persistentSupport.confirmarTransaccao();
	}

	private List doTheRead() throws ExcepcaoPersistencia
	{
		return queryList(classToRead, new Criteria());
	}

	private long calculateServiceExecutionTime(Calendar serviceStartTime, Calendar serviceEndTime)
	{
		return serviceEndTime.getTimeInMillis() - serviceStartTime.getTimeInMillis();
	}

}