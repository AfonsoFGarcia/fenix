package DataBeans;

import Dominio.IMasterDegreeCandidate;

/**
 * @author Fernanda Quit�rio Created on 1/Jul/2004
 *  
 */
public class InfoMasterDegreeCandidateWithInfoPerson extends InfoMasterDegreeCandidate {

    public void copyFromDomain(IMasterDegreeCandidate masterDegreeCandidate) {
        super.copyFromDomain(masterDegreeCandidate);
        if (masterDegreeCandidate != null) {
            setInfoPerson(InfoPerson.newInfoFromDomain(masterDegreeCandidate.getPerson()));
        }
    }

    public static InfoMasterDegreeCandidate newInfoFromDomain(
            IMasterDegreeCandidate masterDegreeCandidate) {
        InfoMasterDegreeCandidateWithInfoPerson infoMasterDegreeCandidateWithInfoPerson = null;
        if (masterDegreeCandidate != null) {
            infoMasterDegreeCandidateWithInfoPerson = new InfoMasterDegreeCandidateWithInfoPerson();
            infoMasterDegreeCandidateWithInfoPerson.copyFromDomain(masterDegreeCandidate);
        }
        return infoMasterDegreeCandidateWithInfoPerson;
    }

}