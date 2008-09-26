package net.sourceforge.fenixedu.applicationTier.Servico.alumni;

import java.util.UUID;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.alumni.AlumniNotificationService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniAddressBean;
import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniIdentityCheckRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniJobBean;
import net.sourceforge.fenixedu.dataTransferObject.alumni.publicAccess.AlumniPasswordBean;
import net.sourceforge.fenixedu.dataTransferObject.alumni.publicAccess.AlumniPublicAccessBean;
import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest;
import net.sourceforge.fenixedu.domain.AlumniManager;
import net.sourceforge.fenixedu.domain.Job;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactType;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;

public class RegisterAlumniData extends AlumniNotificationService {

    public Alumni run(Alumni alumni, final UUID urlRequestToken) throws FenixServiceException {

	if (alumni == null) {
	    throw new FenixServiceException("alumni.uuid.update.alumni.null");
	}

	alumni.setUrlRequestToken(urlRequestToken);
	return alumni;
    }

    public Alumni run(Alumni alumni, final Boolean registered) throws FenixServiceException {

	alumni.setRegistered(registered);
	return alumni;
    }

    public Alumni run(final Student student) {
	return new AlumniManager().registerAlumni(student);
    }

    public Alumni run(final Integer studentNumber, final String documentIdNumber, final String email) {

	final Alumni alumni = new AlumniManager().registerAlumni(studentNumber, documentIdNumber, email);
	sendPublicAccessMail(alumni, email);
	return alumni;
    }

    public void run(Alumni alumni, final String emailAddress) throws FenixServiceException {
	try {
	    if (!alumni.hasEmailAddress(emailAddress)) {
		PartyContact.createEmailAddress(alumni.getStudent().getPerson(), PartyContactType.PERSONAL, Boolean.FALSE,
			emailAddress);
	    }
	} catch (DomainException e) {
	    throw new FenixServiceException(e.getMessage());
	}
    }

    public void run(final AlumniPublicAccessBean alumniBean) throws FenixServiceException {
	Person person = alumniBean.getAlumni().getStudent().getPerson();
	if (person == null) {
	    throw new FenixServiceException("alumni.partyContact.creation.person.null");
	}

	try {
	    processAlumniPhone(alumniBean, person);
	    processAlumniAddress(alumniBean, person);
	    processAlumniJob(alumniBean);
	} catch (DomainException e) {
	    throw new FenixServiceException(e.getMessage());
	}

    }

    public void run(final AlumniIdentityCheckRequestBean bean) {

	final Alumni alumni = new AlumniManager().checkAlumniIdentity(bean.getDocumentIdNumber(), bean.getContactEmail());
	if (!alumni.hasAnyPendingIdentityRequests()) {

	    AlumniIdentityCheckRequest identityRequest = new AlumniIdentityCheckRequest(bean.getContactEmail(), bean
		    .getDocumentIdNumber(), bean.getFullName(), bean.getDateOfBirthYearMonthDay(), bean.getDistrictOfBirth(),
		    bean.getDistrictSubdivisionOfBirth(), bean.getParishOfBirth(), bean.getSocialSecurityNumber(), bean
			    .getNameOfFather(), bean.getNameOfMother(), bean.getRequestType());

	    identityRequest.setAlumni(alumni);
	    if (identityRequest.isValid()) {
		identityRequest.validate(Boolean.TRUE);
		sendIdentityCheckEmail(identityRequest, Boolean.TRUE);
	    }

	} else {
	    throw new DomainException("alumni.has.pending.identity.requests");
	}
    }

    public void run(final AlumniPasswordBean bean) {

	bean.getAlumni().setRegistered(Boolean.TRUE);
	if (!bean.getAlumni().hasAnyPendingIdentityRequests()) {

	    AlumniIdentityCheckRequest identityRequest = new AlumniIdentityCheckRequest(bean.getAlumni().getPersonalEmail()
		    .getValue(), bean.getAlumni().getStudent().getPerson().getDocumentIdNumber(), bean.getFullName(), bean
		    .getDateOfBirthYearMonthDay(), bean.getDistrictOfBirth(), bean.getDistrictSubdivisionOfBirth(), bean
		    .getParishOfBirth(), bean.getSocialSecurityNumber(), bean.getNameOfFather(), bean.getNameOfMother(), bean
		    .getRequestType());

	    identityRequest.setAlumni(bean.getAlumni());
	    if (identityRequest.isValid()) {
		identityRequest.validate(Boolean.TRUE);
		sendIdentityCheckEmail(identityRequest, Boolean.TRUE);
	    }
	}
    }

    private void processAlumniJob(final AlumniPublicAccessBean alumniBean) {

	if (alumniBean.getCurrentJob() == null) {
	    final AlumniJobBean jobBean = alumniBean.getJobBean();
	    new Job(jobBean.getAlumni().getStudent().getPerson(), jobBean.getEmployerName(), jobBean.getCity(), jobBean
		    .getCountry(), jobBean.getChildBusinessArea(), jobBean.getPosition(), jobBean.getBeginDateAsLocalDate(),
		    jobBean.getEndDateAsLocalDate(), jobBean.getContractType());
	} else {
	    final AlumniJobBean jobBean = alumniBean.getJobBean();
	    alumniBean.getCurrentJob().setEmployerName(jobBean.getEmployerName());
	    alumniBean.getCurrentJob().setCity(jobBean.getCity());
	    alumniBean.getCurrentJob().setCountry(jobBean.getCountry());
	    alumniBean.getCurrentJob().setBusinessArea(jobBean.getChildBusinessArea());
	    alumniBean.getCurrentJob().setPosition(jobBean.getPosition());
	    alumniBean.getCurrentJob().setBeginDate(jobBean.getBeginDateAsLocalDate());
	    alumniBean.getCurrentJob().setEndDate(jobBean.getEndDateAsLocalDate());
	    alumniBean.getCurrentJob().setContractType(jobBean.getContractType());
	}
    }

    private void processAlumniAddress(final AlumniPublicAccessBean alumniBean, final Person person) {

	final AlumniAddressBean addressBean = alumniBean.getAddressBean();
	if (alumniBean.getCurrentPhysicalAddress() == null) {

	    final PhysicalAddress address = new PhysicalAddress(person, PartyContactType.PERSONAL, Boolean.FALSE, addressBean
		    .getAddress(), addressBean.getAreaCode(), addressBean.getAreaOfAreaCode(), null, null, null, null,
		    addressBean.getCountry());

	    person.addPartyContacts(address);
	} else {
	    PhysicalAddress address = alumniBean.getCurrentPhysicalAddress();
	    address.setAddress(addressBean.getAddress());
	    address.setAreaCode(addressBean.getAreaCode());
	    address.setAreaOfAreaCode(addressBean.getAreaOfAreaCode());
	    address.setCountryOfResidence(addressBean.getCountry());
	}
    }

    private void processAlumniPhone(final AlumniPublicAccessBean alumniBean, final Person person) {

	if (alumniBean.getCurrentPhone() == null) {
	    person.addPartyContacts(new Phone(person, PartyContactType.PERSONAL, Boolean.TRUE, alumniBean.getPhone()));
	} else {
	    alumniBean.getCurrentPhone().setNumber(alumniBean.getPhone());
	}
    }

}
