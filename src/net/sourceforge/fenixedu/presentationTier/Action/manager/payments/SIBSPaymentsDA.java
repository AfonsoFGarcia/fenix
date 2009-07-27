package net.sourceforge.fenixedu.presentationTier.Action.manager.payments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.accounting.sibsPaymentFileProcessReport.SibsPaymentFileProcessReportDTO;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeMapping;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeState;
import net.sourceforge.fenixedu.domain.accounting.SibsPaymentFileProcessReport;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.sibs.incomming.SibsIncommingPaymentFile;
import net.sourceforge.fenixedu.util.sibs.incomming.SibsIncommingPaymentFileDetailLine;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

@Mapping(path = "/SIBSPayments", module = "manager")
@Forwards( { @Forward(name = "prepareUploadSIBSPaymentFiles", path = "/manager/payments/prepareUploadSIBSPaymentFiles.jsp") })
public class SIBSPaymentsDA extends FenixDispatchAction {

    static private final String[] SUPPORTED_FILE_EXTENSIONS = new String[] { "INP", "ZIP" };

    static public class UploadBean implements Serializable {
	private static final long serialVersionUID = 3625314688141697558L;

	private transient InputStream inputStream;

	private String filename;

	public InputStream getInputStream() {
	    return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
	    this.inputStream = inputStream;
	}

	public String getFilename() {
	    return filename;
	}

	public void setFilename(String filename) {
	    this.filename = StringNormalizer.normalize(filename);
	}
    }

    private class ProcessResult {

	private final HttpServletRequest request;
	private boolean processFailed = false;

	public ProcessResult(HttpServletRequest request) {
	    this.request = request;
	}

	public void addMessage(String message, String... args) {
	    addActionMessage("message", request, message, args);
	}

	public void reportFailure() {
	    processFailed = true;
	}

	public boolean hasFailed() {
	    return processFailed;
	}
    }

    public ActionForward prepareUploadSIBSPaymentFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	UploadBean bean = (UploadBean) getRenderedObject("uploadBean");
	RenderUtils.invalidateViewState("uploadBean");
	if (bean == null) {
	    bean = new UploadBean();
	}

	request.setAttribute("uploadBean", bean);
	return mapping.findForward("prepareUploadSIBSPaymentFiles");
    }

    public ActionForward uploadSIBSPaymentFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	UploadBean bean = (UploadBean) getRenderedObject("uploadBean");
	RenderUtils.invalidateViewState("uploadBean");
	if (bean == null) {
	    return prepareUploadSIBSPaymentFiles(mapping, form, request, response);
	}
	if (bean.getFilename().toUpperCase().endsWith(SUPPORTED_FILE_EXTENSIONS[1])) {
	    File zipFile = pt.utl.ist.fenix.tools.util.FileUtils.copyToTemporaryFile(bean.getInputStream());
	    File unzipDir = null;
	    try {
		unzipDir = pt.utl.ist.fenix.tools.file.utils.FileUtils.unzipFile(zipFile);
		if (!unzipDir.isDirectory()) {
		    addActionMessage("error", request, "error.manager.SIBS.zipException", bean.getFilename());
		    return prepareUploadSIBSPaymentFiles(mapping, form, request, response);
		}
	    } catch (Exception e) {
		addActionMessage("error", request, "error.manager.SIBS.zipException", e.getMessage());
		return prepareUploadSIBSPaymentFiles(mapping, form, request, response);
	    } finally {
		zipFile.delete();
	    }
	    ProcessResult result = new ProcessResult(request);
	    recursiveZipProcess(unzipDir, result);
	    result.addMessage("label.manager.SIBS.allDone");

	} else if (bean.getFilename().toUpperCase().endsWith(SUPPORTED_FILE_EXTENSIONS[0])) {
	    File file = pt.utl.ist.fenix.tools.util.FileUtils.copyToTemporaryFile(bean.getInputStream(), bean.getFilename());
	    ProcessResult result = new ProcessResult(request);
	    result.addMessage("label.manager.SIBS.processingFile", file.getName());
	    try {
		processFile(file, result);
	    } catch (FileNotFoundException e) {
		addActionMessage("error", request, "error.manager.SIBS.zipException", e.getMessage());
	    } catch (IOException e) {
		addActionMessage("error", request, "error.manager.SIBS.IOException", e.getMessage());
	    } catch (Exception e) {
		addActionMessage("error", request, "error.manager.SIBS.fileException", e.getMessage());
	    } finally {
		file.delete();
	    }
	} else {
	    addActionMessage("error", request, "error.manager.SIBS.notSupportedExtension", bean.getFilename());
	}
	return prepareUploadSIBSPaymentFiles(mapping, form, request, response);
    }

    private void recursiveZipProcess(File unzipDir, ProcessResult result) {
	for (File file : unzipDir.listFiles()) {
	    if (file.isDirectory()) {
		recursiveZipProcess(file, result);
	    } else {
		if (!file.getName().toUpperCase().endsWith(SUPPORTED_FILE_EXTENSIONS[0])) {
		    file.delete();
		    continue;
		}
		result.addMessage("label.manager.SIBS.processingFile", file.getName());
		try {
		    processFile(file, result);
		} catch (FileNotFoundException e) {
		    result.addMessage("error.manager.SIBS.zipException", e.getMessage());
		} catch (IOException e) {
		    result.addMessage("error.manager.SIBS.IOException", e.getMessage());
		} catch (Exception e) {
		    result.addMessage("error.manager.SIBS.fileException", e.getMessage());
		} finally {
		    file.delete();
		}
	    }
	}
	unzipDir.delete();
    }

    private void processFile(File file, ProcessResult result) throws IOException {
	FileInputStream fileInputStream = null;
	try {
	    fileInputStream = new FileInputStream(file);
	    final Person person = AccessControl.getPerson();
	    final SibsIncommingPaymentFile sibsFile = SibsIncommingPaymentFile.parse(file.getName(), fileInputStream);

	    result.addMessage("label.manager.SIBS.linesFound", String.valueOf(sibsFile.getDetailLines().size()));
	    result.addMessage("label.manager.SIBS.startingProcess");

	    for (final SibsIncommingPaymentFileDetailLine detailLine : sibsFile.getDetailLines()) {
		try {
		    processCode(detailLine, person.getIdInternal(), result);
		} catch (Exception e) {
		    result.addMessage("error.processException", detailLine.getCode());
		}
	    }

	    result.addMessage("label.manager.SIBS.creatingReport");
	    if (!result.hasFailed()) {
		if (SibsPaymentFileProcessReport.hasAny(sibsFile.getWhenProcessedBySibs(), sibsFile.getVersion())) {
		    result.addMessage("warning.manager.SIBS.reportAlreadyProcessed");
		} else {
		    try {
			createSibsFileReport(sibsFile, result);
		    } catch (Exception ex) {
			result.addMessage("error.manager.SIBS.reportException", ex.getMessage());
		    }
		}
	    } else {
		result.addMessage("error.manager.SIBS.nonProcessedCodes");
	    }
	    result.addMessage("label.manager.SIBS.done");
	} finally {
	    if (fileInputStream != null) {
		fileInputStream.close();
	    }
	}
    }

    private void processCode(SibsIncommingPaymentFileDetailLine detailLine, Integer personId, ProcessResult result)
	    throws Exception {
	final Person person = (Person) rootDomainObject.readPartyByOID(personId);
	final PaymentCode paymentCode = getPaymentCode(detailLine, result);

	if (paymentCode == null) {
	    result.addMessage("error.manager.SIBS.codeNotFound", detailLine.getCode());
	    throw new Exception();
	}

	final PaymentCode codeToProcess = getPaymentCodeToProcess(paymentCode, ExecutionYear.readByDateTime(detailLine
		.getWhenOccuredTransaction()), result);

	if (codeToProcess.getState() == PaymentCodeState.INVALID) {
	    result.addMessage("warning.manager.SIBS.invalidCode", codeToProcess.getCode());
	}

	if (codeToProcess.isProcessed() && codeToProcess.getWhenUpdated().isBefore(detailLine.getWhenOccuredTransaction())) {
	    result.addMessage("warning.manager.SIBS.codeAlreadyProcessed", codeToProcess.getCode());
	}

	codeToProcess.process(person, detailLine.getAmount(), detailLine.getWhenOccuredTransaction(), detailLine
		.getSibsTransactionId(), StringUtils.EMPTY);

    }

    private void createSibsFileReport(SibsIncommingPaymentFile sibsIncomingPaymentFile, ProcessResult result) throws Exception {
	final SibsPaymentFileProcessReportDTO reportDTO = new SibsPaymentFileProcessReportDTO(sibsIncomingPaymentFile);
	for (final SibsIncommingPaymentFileDetailLine detailLine : sibsIncomingPaymentFile.getDetailLines()) {
	    reportDTO.addAmount(detailLine, getPaymentCode(detailLine, result));
	}
	SibsPaymentFileProcessReport.create(reportDTO);
	result.addMessage("label.manager.SIBS.reportCreated");
    }

    private PaymentCode getPaymentCodeToProcess(final PaymentCode paymentCode, ExecutionYear executionYear, ProcessResult result) {
	final PaymentCodeMapping mapping = paymentCode.getOldPaymentCodeMapping(executionYear);

	final PaymentCode codeToProcess;
	if (mapping != null) {
	    result.addMessage("warning.manager.SIBS.foundMapping", paymentCode.getCode(), mapping.getNewPaymentCode().getCode());
	    result.addMessage("warning.manager.SIBS.invalidating", paymentCode.getCode());

	    codeToProcess = mapping.getNewPaymentCode();
	    paymentCode.setState(PaymentCodeState.INVALID);

	} else {
	    codeToProcess = paymentCode;
	}
	return codeToProcess;
    }

    private PaymentCode getPaymentCode(final SibsIncommingPaymentFileDetailLine detailLine, ProcessResult result) {
	return getPaymentCode(detailLine.getCode(), result);
    }

    private PaymentCode getPaymentCode(final String code, ProcessResult result) {
	/*
	 * TODO:
	 * 
	 * 09/07/2009 - Payments are not related only to students. readAll() may
	 * be heavy to get the PaymentCode.
	 * 
	 * 
	 * Ask Nadir and Joao what is best way to deal with PaymentCode
	 * retrieval.
	 */

	return PaymentCode.readByCode(code);
    }
}
