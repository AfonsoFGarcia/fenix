package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.serviceRequests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.RectorateSubmissionBatch;
import net.sourceforge.fenixedu.domain.serviceRequests.RectorateSubmissionState;
import net.sourceforge.fenixedu.domain.serviceRequests.RegistryCode;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaSupplementRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(path = "/rectorateDocumentSubmission", module = "academicAdminOffice")
@Forwards( { @Forward(name = "index", path = "/academicAdminOffice/rectorateDocumentSubmission/batchIndex.jsp"),
	@Forward(name = "viewBatch", path = "/academicAdminOffice/rectorateDocumentSubmission/showBatch.jsp") })
public class RectorateDocumentSubmissionDispatchAction extends FenixDispatchAction {
    public ActionForward index(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	AdministrativeOffice office = getLoggedPerson(request).getEmployee().getAdministrativeOffice();
	request.setAttribute("unsent", office.getRectorateSubmissionBatchesByState(RectorateSubmissionState.UNSENT));
	request.setAttribute("closed", office.getRectorateSubmissionBatchesByState(RectorateSubmissionState.CLOSED));
	request.setAttribute("sent", office.getRectorateSubmissionBatchesByState(RectorateSubmissionState.SENT));
	request.setAttribute("received", office.getRectorateSubmissionBatchesByState(RectorateSubmissionState.RECEIVED));
	return mapping.findForward("index");
    }

    public ActionForward viewBatch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
	Set<String> actions = new HashSet<String>();
	Set<String> confirmActions = new HashSet<String>();
	switch (batch.getState()) {
	case UNSENT:
	    if (batch.hasAnyDocumentRequest()) {
		confirmActions.add("closeBatch");
	    }
	    break;
	case CLOSED:
	    actions.add("generateMetadataForRegistry");
	    actions.add("generateMetadataForDiplomas");
	    actions.add("zipDocuments");
	    confirmActions.add("markAsSent");
	    break;
	case SENT:
	    actions.add("generateMetadataForRegistry");
	    actions.add("generateMetadataForDiplomas");
	    actions.add("zipDocuments");
	    if (batch.allDocumentsReceived()) {
		confirmActions.add("markAsReceived");
	    }
	case RECEIVED:
	    actions.add("generateMetadataForRegistry");
	    actions.add("generateMetadataForDiplomas");
	    actions.add("zipDocuments");
	}
	request.setAttribute("batch", batch);
	// Filter out canceled document requests, ticket: #248539
	Set<DocumentRequest> requests = new HashSet<DocumentRequest>();
	for (DocumentRequest docRequest : batch.getDocumentRequestSet()) {
	    if (!docRequest.isCancelled()) {
		requests.add(docRequest);
	    }
	}
	request.setAttribute("requests", requests);
	request.setAttribute("actions", actions);
	request.setAttribute("confirmActions", confirmActions);
	return mapping.findForward("viewBatch");
    }

    @Service
    public ActionForward closeBatch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
	batch.closeBatch();
	return viewBatch(mapping, actionForm, request, response);
    }

    @Service
    public ActionForward markAsSent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
	batch.markAsSent();
	return viewBatch(mapping, actionForm, request, response);
    }

    @Service
    public ActionForward markAsReceived(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
	batch.markAsReceived();
	return viewBatch(mapping, actionForm, request, response);
    }

    public ActionForward generateMetadataForRegistry(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
	Set<DocumentRequest> docs = new HashSet<DocumentRequest>();
	for (DocumentRequest document : batch.getDocumentRequestSet()) {
	    // Filter out canceled document requests, ticket: #248539
	    if (!(document instanceof DiplomaRequest) && !document.isCancelled()) {
		docs.add(document);
	    }
	}
	return generateMetadata(docs, "registos-", request, response);
    }

    public ActionForward generateMetadataForDiplomas(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
	Set<DocumentRequest> docs = new HashSet<DocumentRequest>();
	for (DocumentRequest document : batch.getDocumentRequestSet()) {
	    // Filter out canceled document requests, ticket: #248539
	    if (document instanceof DiplomaRequest && !document.isCancelled()) {
		docs.add(document);
	    }
	}
	return generateMetadata(docs, "cartas-", request, response);
    }

    private ActionForward generateMetadata(Set<DocumentRequest> documents, String prefix, HttpServletRequest request,
	    HttpServletResponse response) {
	SortedSet<DocumentRequest> sorted = new TreeSet<DocumentRequest>(DocumentRequest.COMPARATOR_BY_REGISTRY_NUMBER);
	sorted.addAll(documents);
	Set<RegistryCode> codes = new HashSet<RegistryCode>();
	for (DocumentRequest document : documents) {
	    codes.add(document.getRegistryCode());
	}
	Integer min = null;
	Integer max = null;
	for (RegistryCode code : codes) {
	    Integer codeNumber = code.getCodeNumber();
	    if (min == null || codeNumber.intValue() < min.intValue()) {
		min = codeNumber;
	    }
	    if (max == null || codeNumber.intValue() > max.intValue()) {
		max = codeNumber;
	    }
	}
	SheetData<DocumentRequest> data = new SheetData<DocumentRequest>(sorted) {
	    @Override
	    protected void makeLine(DocumentRequest document) {
		ResourceBundle enumeration = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
		addCell("C�digo", document.getRegistryCode().getCode());
		addCell("Tipo de Documento", enumeration.getString(document.getDocumentRequestType().name()));
		switch (document.getDocumentRequestType()) {
		case REGISTRY_DIPLOMA_REQUEST:
		    addCell("Ciclo", enumeration.getString(((RegistryDiplomaRequest) document).getRequestedCycle().name()));
		    break;
		case DIPLOMA_REQUEST:
		    CycleType cycle = ((DiplomaRequest) document).getWhatShouldBeRequestedCycle();
		    addCell("Ciclo", cycle != null ? enumeration.getString(cycle.name()) : null);
		    break;
		case DIPLOMA_SUPPLEMENT_REQUEST:
		    addCell("Ciclo", enumeration.getString(((DiplomaSupplementRequest) document).getRequestedCycle().name()));
		    break;
		default:
		    addCell("Ciclo", null);
		}
		addCell("Tipo de Curso", enumeration.getString(document.getDegreeType().name()));
		addCell("N� de Aluno", document.getRegistration().getNumber());
		addCell("Nome", document.getPerson().getName());
		if (!(document instanceof DiplomaRequest)) {
		    addCell("Ficheiro", document.getLastGeneratedDocument().getFilename());
		}
	    }
	};
	try {
	    response.setContentType("application/vnd.ms-excel");
	    response.setHeader("Content-disposition", "attachment; filename=" + prefix + min + "-" + max + "(" + codes.size()
		    + ")" + ".xls");
	    final ServletOutputStream writer = response.getOutputStream();
	    new SpreadsheetBuilder().addSheet("lote", data).build(WorkbookExportFormat.EXCEL, writer);
	    writer.flush();
	    response.flushBuffer();
	} catch (IOException e) {
	    throw new DomainException("error.rectorateSubmission.errorGeneratingMetadata");
	}

	return null;
    }

    public ActionForward zipDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	try {
	    ByteArrayOutputStream bout = new ByteArrayOutputStream();
	    ZipOutputStream zip = new ZipOutputStream(bout);
	    RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
	    for (DocumentRequest document : batch.getDocumentRequestSet()) {
		// Filter out canceled document requests, ticket: #248539
		if (!(document instanceof DiplomaRequest) && !document.isCancelled()) {
		    zip.putNextEntry(new ZipEntry(document.getLastGeneratedDocument().getFilename()));
		    zip.write(document.getLastGeneratedDocument().getContents());
		    zip.closeEntry();
		}
	    }
	    zip.close();
	    response.setContentType("application/zip");
	    response.addHeader("Content-Disposition", "attachment; filename=documentos-" + batch.getRange() + ".zip");
	    ServletOutputStream writer = response.getOutputStream();
	    writer.write(bout.toByteArray());
	    writer.flush();
	    writer.close();
	    response.flushBuffer();
	    return null;
	} catch (IOException e) {
	    throw new DomainException("error.rectorateSubmission.errorGeneratingMetadata");
	}
    }

    @Service
    public ActionForward delayRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	DocumentRequest document = getDomainObject(request, "academicServiceRequestOid");
	request.setAttribute("batchOid", document.getRectorateSubmissionBatch().getExternalId());
	if (document.isPiggyBackedOnRegistry()) {
	    addActionMessage(request, "error.rectorateSubmissionBatch.cannotDelayPiggyBackedDocument");
	} else {
	    RectorateSubmissionBatch next = document.getRectorateSubmissionBatch().getNextRectorateSubmissionBatch();
	    if (document.isRegistryDiploma()) {
		RegistryDiplomaRequest registry = (RegistryDiplomaRequest) document;
		registry.getDiplomaSupplement().setRectorateSubmissionBatch(next);
	    }
	    document.setRectorateSubmissionBatch(next);
	}
	return viewBatch(mapping, actionForm, request, response);
    }
}
