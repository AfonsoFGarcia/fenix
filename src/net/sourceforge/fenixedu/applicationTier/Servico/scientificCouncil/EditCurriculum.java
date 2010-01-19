/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Curriculum;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Jo�o Mota
 * 
 *         23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 * 
 */
public class EditCurriculum extends FenixService {

    @Checked("RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE")
    @Service
    public static Boolean run(Integer curriculumId, String program, String programEn, String operacionalObjectives,
	    String operacionalObjectivesEn, String generalObjectives, String generalObjectivesEn, Boolean basic)
	    throws FenixServiceException {

	Curriculum curriculum = rootDomainObject.readCurriculumByOID(curriculumId);
	if (curriculum.getCurricularCourse().getBasic().equals(basic)) {
	    curriculum.setProgram(program);
	    curriculum.setProgramEn(programEn);
	    curriculum.setOperacionalObjectives(operacionalObjectives);
	    curriculum.setOperacionalObjectivesEn(operacionalObjectivesEn);
	    curriculum.setGeneralObjectives(generalObjectives);
	    curriculum.setGeneralObjectivesEn(generalObjectivesEn);
	    return new Boolean(true);
	}
	return new Boolean(false);

	// TODO: KEEP HISTORY OF CURRICULAR INFORMATION
    }

}