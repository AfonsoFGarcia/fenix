/*
 * Created on Oct 27, 2003
 *  
 */
package DataBeans.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.beanutils.BeanUtils;

import Dominio.IDomainObject;

/**
 * @author Luis Cruz & Jo�o Luz
 *  
 */
public class CopyUtils
{

	private static ArrayList interfacesNotToCopy = null;

	static {
		interfacesNotToCopy = new ArrayList();
		interfacesNotToCopy.add(Collection.class);
		interfacesNotToCopy.add(IDomainObject.class);
	}

	public static Object copyProperties(Object destinationBean, Object sourceBean)
		throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		BeanUtils.copyProperties(destinationBean, sourceBean);
		/**
		 * FIXME: The code below have some problems with introspection. To see
		 * the problem try to use this code on use case Criar Candidato on
		 * post-graduation office...
		 * 
		 * @author jpvl
		 */
		//		PropertyDescriptor[] fields =
		// PropertyUtils.getPropertyDescriptors(sourceBean.getClass());
		//		for (int i = 0; i < fields.length; i++) {
		//			PropertyDescriptor propertyDescriptor = fields[i];
		//			Class fieldClass = propertyDescriptor.getPropertyType();
		//			Class[] interfaces = fieldClass.getInterfaces();
		//			List interfacesList = Arrays.asList(interfaces);
		//
		//			if (CollectionUtils
		//				.intersection(interfacesNotToCopy, interfacesList)
		//				.isEmpty() && !propertyDescriptor.getName().equals("class")) {
		//				Object value = PropertyUtils.getProperty(sourceBean,
		// propertyDescriptor.getName());
		//				BeanUtils.copyProperty(destinationBean,
		// propertyDescriptor.getName(), value);
		//			}
		//		}
		return destinationBean;
	}
}