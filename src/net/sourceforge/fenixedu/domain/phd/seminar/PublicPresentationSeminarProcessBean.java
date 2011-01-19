package net.sourceforge.fenixedu.domain.phd.seminar;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;

import org.joda.time.LocalDate;

public class PublicPresentationSeminarProcessBean implements Serializable {

    private static final long serialVersionUID = -7837778662130742070L;

    private String remarks;

    private PhdProgramDocumentUploadBean document;

    private LocalDate presentationDate;

    private Boolean generateAlert;

    private PublicPresentationSeminarProcess process;
    
    private PublicPresentationSeminarProcessStateType processState;

    public PublicPresentationSeminarProcessBean() {
	this.document = new PhdProgramDocumentUploadBean();
    }

    public PublicPresentationSeminarProcessBean(final PhdIndividualProgramProcess process) {
	this();
	setProcess(process.getSeminarProcess());
	setGenerateAlert(process.getPhdConfigurationIndividualProgramProcess().getGenerateAlert());

	setPresentationDate(process.getSeminarProcess().getPresentationDate());
    }

    public String getRemarks() {
	return remarks;
    }

    public void setRemarks(String remarks) {
	this.remarks = remarks;
    }

    public PhdProgramDocumentUploadBean getDocument() {
	return document;
    }

    public void setDocument(PhdProgramDocumentUploadBean document) {
	this.document = document;
    }

    public LocalDate getPresentationDate() {
	return presentationDate;
    }

    public void setPresentationDate(LocalDate presentationDate) {
	this.presentationDate = presentationDate;
    }

    public Boolean getGenerateAlert() {
	return generateAlert;
    }

    public void setGenerateAlert(Boolean generateAlert) {
	this.generateAlert = generateAlert;
    }

    public PublicPresentationSeminarProcess getProcess() {
	return process;
    }

    public void setProcess(PublicPresentationSeminarProcess process) {
	this.process = process;
    }

    public PublicPresentationSeminarProcessStateType getProcessState() {
	return processState;
    }

    public void setProcessState(PublicPresentationSeminarProcessStateType processState) {
	this.processState = processState;
    }
}
