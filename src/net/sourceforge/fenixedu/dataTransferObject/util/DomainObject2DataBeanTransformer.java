/*
 * Created on 2003/08/24
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.util;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.DomainObject;

import org.apache.commons.lang.StringUtils;

/**
 * @author Luis Cruz
 *  
 */
public class DomainObject2DataBeanTransformer extends ObjectBeanTransformer {

    /**
     * @param toClass
     * @param fromClass
     */
    public DomainObject2DataBeanTransformer() {
        super(InfoObject.class, DomainObject.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see dataBean.util.ObjectBeanTransformer#getHashKey(java.lang.Object)
     */
    protected Object getHashKey(Object fromObject) {
        return fromObject.getClass().getName() + ((DomainObject) fromObject).getIdInternal();
    }

    /*
     * (non-Javadoc)
     * 
     * @see dataBean.util.ObjectBeanTransformer#destinationObjectConstructor(java.lang.Object)
     */
    protected Object destinationObjectConstructor(Object fromObject) {
        String classToCreate = InfoObject.class.getPackage().getName()
                + ".Info"
                + StringUtils.stripStart(fromObject.getClass().getName(), fromObject.getClass()
                        .getPackage().getName()
                        + ".");
        return objectConstructor(classToCreate);
    }

}