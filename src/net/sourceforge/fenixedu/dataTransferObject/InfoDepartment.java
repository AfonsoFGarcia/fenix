/*
 * Created on 4/Jul/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Department;

/**
 * @author jpvl
 */
public class InfoDepartment extends InfoObject {
    private String name;
    private String realName;
    private String code;

    public InfoDepartment() {
    }

    /**
     * @return
     */
    public String getCode() {
        return this.code;
    }

    /**
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof InfoDepartment) {
            InfoDepartment infoDepartment = (InfoDepartment) obj;
            return ((this.getCode().equals(infoDepartment.getCode())) || (this.getName()
                    .equals(infoDepartment.getName())));
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain(Dominio.DomainObject)
     */
    public void copyFromDomain(Department department) {
        super.copyFromDomain(department);
        if (department != null) {
            setCode(department.getCode());
            setName(department.getName());
            setRealName(department.getRealName());
        }
    }

    public static InfoDepartment newInfoFromDomain(Department department) {
        InfoDepartment infoDepartment = null;
        if (department != null) {
            infoDepartment = new InfoDepartment();
            infoDepartment.copyFromDomain(department);
        }
        return infoDepartment;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}