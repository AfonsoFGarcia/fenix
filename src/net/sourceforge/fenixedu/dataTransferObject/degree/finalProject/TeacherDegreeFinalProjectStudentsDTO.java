/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.degree.finalProject;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;

/**
 * @author jpvl
 */
public class TeacherDegreeFinalProjectStudentsDTO extends DataTranferObject {
    private InfoExecutionPeriod infoExecutionPeriod;

    private InfoTeacher infoTeacher;

    private List infoTeacherDegreeFinalProjectStudentList;

    /**
     * @return Returns the infoExecutionYear.
     */
    public InfoExecutionPeriod getInfoExecutionPeriod() {
        return this.infoExecutionPeriod;
    }

    /**
     * @return Returns the infoTeacher.
     */
    public InfoTeacher getInfoTeacher() {
        return this.infoTeacher;
    }

    /**
     * @return Returns the infoTeacherDegreeFinalProjectStudentList.
     */
    public List getInfoTeacherDegreeFinalProjectStudentList() {
        return this.infoTeacherDegreeFinalProjectStudentList;
    }

    /**
     * @param infoExecutionYear
     *            The infoExecutionYear to set.
     */
    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }

    /**
     * @param infoTeacher
     *            The infoTeacher to set.
     */
    public void setInfoTeacher(InfoTeacher infoTeacher) {
        this.infoTeacher = infoTeacher;
    }

    /**
     * @param infoTeacherDegreeFinalProjectStudentList
     *            The infoTeacherDegreeFinalProjectStudentList to set.
     */
    public void setInfoTeacherDegreeFinalProjectStudentList(List infoTeacherDegreeFinalProjectStudentList) {
        this.infoTeacherDegreeFinalProjectStudentList = infoTeacherDegreeFinalProjectStudentList;
    }

}