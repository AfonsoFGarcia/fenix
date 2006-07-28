package net.sourceforge.fenixedu.domain.material;

import java.util.Comparator;

import org.apache.commons.beanutils.BeanComparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class Material extends Material_Base {
    
    public static final Comparator COMPARATOR_BY_CLASS_NAME = new BeanComparator("class.simpleName");
        
    protected Material() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(getClass().getName());
    }   
}
