/*
 * Created on Feb 18, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.student.IDelegate;
import net.sourceforge.fenixedu.util.DelegateYearType;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 *  
 */
public class InfoDelegate extends InfoObject {
    private DelegateYearType yearType;

    private InfoStudent infoStudent;

    private InfoDegree infoDegree;

    private InfoExecutionYear infoExecutionYear;

    private Boolean type;

    /**
     *  
     */
    public InfoDelegate() {
        super();
    }

    /**
     * @param idInternal
     */
    public InfoDelegate(Integer idInternal) {
        super(idInternal);
    }

    /**
     * @return Returns the infoDegree.
     */
    public InfoDegree getInfoDegree() {
        return infoDegree;
    }

    /**
     * @param infoDegree
     *            The infoDegree to set.
     */
    public void setInfoDegree(InfoDegree infoDegree) {
        this.infoDegree = infoDegree;
    }

    /**
     * @return Returns the infoExecutionYear.
     */
    public InfoExecutionYear getInfoExecutionYear() {
        return infoExecutionYear;
    }

    /**
     * @param infoExecutionYear
     *            The infoExecutionYear to set.
     */
    public void setInfoExecutionYear(InfoExecutionYear infoExecutionYear) {
        this.infoExecutionYear = infoExecutionYear;
    }

    /**
     * @return Returns the infoStudent.
     */
    public InfoStudent getInfoStudent() {
        return infoStudent;
    }

    /**
     * @param infoStudent
     *            The infoStudent to set.
     */
    public void setInfoStudent(InfoStudent infoStudent) {
        this.infoStudent = infoStudent;
    }

    /**
     * @return Returns the type.
     */
    public DelegateYearType getYearType() {
        return yearType;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setYearType(DelegateYearType type) {
        this.yearType = type;
    }

    /**
     * @return Returns the type.
     */
    public Boolean getType() {
        return type;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setType(Boolean type) {
        this.type = type;
    }

    public void copyFromDomain(final IDelegate delegate) {
        super.copyFromDomain(delegate);
        if (delegate != null) {
            setType(delegate.getType());
            setYearType(delegate.getYearType());
        }
    }

    public static InfoDelegate newInfoFromDomain(final IDelegate delegate) {
        if (delegate != null) {
            final InfoDelegate infoDelegate = new InfoDelegate();
            infoDelegate.copyFromDomain(delegate);
            return infoDelegate;
        }

        return null;
    }

}