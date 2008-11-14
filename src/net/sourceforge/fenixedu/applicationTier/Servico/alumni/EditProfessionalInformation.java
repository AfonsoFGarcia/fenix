package net.sourceforge.fenixedu.applicationTier.Servico.alumni;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniJobBean;
import net.sourceforge.fenixedu.domain.Job;
import pt.ist.fenixWebFramework.services.Service;

public class EditProfessionalInformation extends FenixService {

    @Service
    public static void run(final AlumniJobBean jobBean) {

	Job job = rootDomainObject.readJobByOID(jobBean.getJobId());
	job.setEmployerName(jobBean.getEmployerName());
	job.setCity(jobBean.getCity());
	job.setCountry(jobBean.getCountry());
	job.setBusinessArea(jobBean.getChildBusinessArea());
	job.setPosition(jobBean.getPosition());
	job.setBeginDate(jobBean.getBeginDateAsLocalDate());
	job.setEndDate(jobBean.getEndDateAsLocalDate());
	job.setApplicationType(jobBean.getApplicationType());
	job.setContractType(jobBean.getContractType());
	job.setSalaryType(jobBean.getSalaryType());
    }
}