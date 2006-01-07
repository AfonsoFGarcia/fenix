/*
 * Created on 27/Out/2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Coordinator;

/**
 * fenix-head Dominio
 * 
 * @author Jo�o Mota 27/Out/2003
 *  
 */
public class InfoCoordinator extends InfoObject {

    private InfoTeacher infoTeacher;

    private InfoExecutionDegree infoExecutionDegree;

    private Boolean responsible;

    /**
     * @return
     */
    public InfoExecutionDegree getInfoExecutionDegree() {
        return infoExecutionDegree;
    }

    /**
     * @param infoExecutionDegree
     */
    public void setInfoExecutionDegree(InfoExecutionDegree infoExecutionDegree) {
        this.infoExecutionDegree = infoExecutionDegree;
    }

    /**
     * @return
     */
    public InfoTeacher getInfoTeacher() {
        return infoTeacher;
    }

    /**
     * @param infoTeacher
     */
    public void setInfoTeacher(InfoTeacher infoTeacher) {
        this.infoTeacher = infoTeacher;
    }

    /**
     * @return
     */
    public Boolean getResponsible() {
        return responsible;
    }

    /**
     * @param responsible
     */
    public void setResponsible(Boolean responsible) {
        this.responsible = responsible;
    }

    public void copyFromDomain(Coordinator coordinator) {
        super.copyFromDomain(coordinator);
        if (coordinator != null) {
            setResponsible(coordinator.getResponsible());
        }
    }

    public static InfoCoordinator newInfoFromDomain(Coordinator coordinator) {
        InfoCoordinator infoCoordinator = null;
        if (coordinator != null) {
            infoCoordinator = new InfoCoordinator();
            infoCoordinator.copyFromDomain(coordinator);
        }
        return infoCoordinator;
    }
}