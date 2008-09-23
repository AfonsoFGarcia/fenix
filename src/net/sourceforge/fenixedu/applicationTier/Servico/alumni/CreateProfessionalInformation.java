package net.sourceforge.fenixedu.applicationTier.Servico.alumni;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniJobBean;
import net.sourceforge.fenixedu.domain.Job;

public class CreateProfessionalInformation extends FenixService {

    public Job run(final AlumniJobBean bean) {

	return new Job(bean.getAlumni().getStudent().getPerson(), bean.getEmployerName(), bean.getCity(), bean.getCountry(), bean
		.getChildBusinessArea(), bean.getPosition(), bean.getBeginDateAsLocalDate(), bean.getEndDateAsLocalDate(), bean
		.getContractType());
    }

}
