/*
 * Created on 29/Jun/2004
 *
 */
package DataBeans;

import Dominio.ICoordinator;

/**
 * @author T�nia Pous�o
 *
 */
public class InfoCoordinatorWithInfoPerson extends InfoCoordinator {

    /* (non-Javadoc)
     * @see DataBeans.InfoCoordinator#copyFromDomain(Dominio.ICoordinator)
     */
    public void copyFromDomain(ICoordinator coordinator) {
        super.copyFromDomain(coordinator);
        if(coordinator != null) {
            setInfoTeacher(InfoTeacherWithPerson.newInfoFromDomain(coordinator.getTeacher()));
        }
    }
    
    public static InfoCoordinator newInfoFromDomain (ICoordinator coordinator) {
        InfoCoordinatorWithInfoPerson infoCoordinator = null;
        if(coordinator != null) {
            infoCoordinator = new InfoCoordinatorWithInfoPerson();
            infoCoordinator.copyFromDomain(coordinator);        
        }
        return infoCoordinator;
    }
}
