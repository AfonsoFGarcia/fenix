package DataBeans;

import Dominio.IMasterDegreeThesisDataVersion;

/**
 * @author Fernanda Quit�rio Created on 6/Set/2004
 *  
 */
public class InfoMasterDegreeThesisDataVersionWithGuidersAndResp extends
        InfoMasterDegreeThesisDataVersionWithGuiders {

    public void copyFromDomain(IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion) {
        super.copyFromDomain(masterDegreeThesisDataVersion);
        if (masterDegreeThesisDataVersion != null) {
            setInfoResponsibleEmployee(InfoEmployeeWithPerson
                    .newInfoFromDomain(masterDegreeThesisDataVersion.getResponsibleEmployee()));
        }
    }

    public static InfoMasterDegreeThesisDataVersion newInfoFromDomain(
            IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion) {
        InfoMasterDegreeThesisDataVersionWithGuidersAndResp infoMasterDegreeThesisDataVersionWithGuidersAndResp = null;
        if (masterDegreeThesisDataVersion != null) {
            infoMasterDegreeThesisDataVersionWithGuidersAndResp = new InfoMasterDegreeThesisDataVersionWithGuidersAndResp();
            infoMasterDegreeThesisDataVersionWithGuidersAndResp
                    .copyFromDomain(masterDegreeThesisDataVersion);
        }
        return infoMasterDegreeThesisDataVersionWithGuidersAndResp;
    }
}