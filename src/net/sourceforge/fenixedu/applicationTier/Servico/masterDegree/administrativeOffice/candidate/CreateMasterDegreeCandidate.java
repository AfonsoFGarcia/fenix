/*
 * Created on 14/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;

/**
 * @author <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo </a>
 * @author <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali </a>
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 */
public class CreateMasterDegreeCandidate extends Service {

    public InfoMasterDegreeCandidate run(Specialization degreeType, Integer executionDegreeID,
            String name, String identificationDocumentNumber, IDDocumentType identificationDocumentType)
            throws Exception {

        // Read the Execution of this degree in the current execution Year
        final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeID);
        Person person = Person.readByDocumentIdNumberAndIdDocumentType(identificationDocumentNumber, identificationDocumentType);
        
        MasterDegreeCandidate masterDegreeCandidate = person.getMasterDegreeCandidateByExecutionDegree(executionDegree);
        if (masterDegreeCandidate != null) {
            throw new ExistingServiceException();
        }

        // Set the Candidate's Situation
        CandidateSituation candidateSituation = DomainFactory.makeCandidateSituation();
        // First candidate situation
        candidateSituation.setRemarks("Pr�-Candidatura. Pagamento da candidatura por efectuar.");
        candidateSituation.setSituation(new SituationName(SituationName.PRE_CANDIDATO));
        candidateSituation.setValidation(new State(State.ACTIVE));
        Calendar actualDate = Calendar.getInstance();
        candidateSituation.setDate(actualDate.getTime());

        // Create the Candidate
        masterDegreeCandidate = DomainFactory.makeMasterDegreeCandidate();
        masterDegreeCandidate.addSituations(candidateSituation);
        masterDegreeCandidate.setSpecialization(degreeType);
        masterDegreeCandidate.setExecutionDegree(executionDegree);

        // Generate the Candidate's number
        Integer number = persistentSupport.getIPersistentMasterDegreeCandidate()
                .generateCandidateNumber(executionDegree.getExecutionYear().getYear(),
                        executionDegree.getDegreeCurricularPlan().getDegree().getSigla(), degreeType);
        masterDegreeCandidate.setCandidateNumber(number);

        Role personRole = Role.getRoleByRoleType(RoleType.PERSON);
        if (person == null) {
            // Create the new Person
            person = DomainFactory.makePerson(name, identificationDocumentNumber,
                    identificationDocumentType, Gender.MALE, "T" + System.currentTimeMillis());
        }

        if (!person.hasRole(RoleType.PERSON)) {
            person.addPersonRoles(personRole);
        }

        masterDegreeCandidate.setPerson(person);

        if (!person.hasRole(RoleType.MASTER_DEGREE_CANDIDATE)) {
            person.addPersonRoles(Role.getRoleByRoleType(RoleType.MASTER_DEGREE_CANDIDATE));
        }

        // Return the new Candidate
        InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
                .newInfoFromDomain(masterDegreeCandidate);
        InfoExecutionDegree infoExecutionDegree = InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan
                .newInfoFromDomain(masterDegreeCandidate.getExecutionDegree());
        infoMasterDegreeCandidate.setInfoExecutionDegree(infoExecutionDegree);
        return infoMasterDegreeCandidate;
    }
}
