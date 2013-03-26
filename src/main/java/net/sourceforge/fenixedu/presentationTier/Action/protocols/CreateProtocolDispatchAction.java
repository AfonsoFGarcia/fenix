package net.sourceforge.fenixedu.presentationTier.Action.protocols;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits.CreateExternalUnitByNameAndCountry;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ExecuteFactoryMethod;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.InsertExternalPerson;
import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory;
import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolSearch;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.protocols.Protocol;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class CreateProtocolDispatchAction extends FenixDispatchAction {

    public ActionForward prepareCreateProtocolData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("protocolFactory", new ProtocolFactory());
        return mapping.findForward("prepareCreate-protocol-data");
    }

    public ActionForward prepareCreateProtocolResponsibles(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = getRenderedObject();
        RenderUtils.invalidateViewState();
        request.setAttribute("protocolFactory", protocolFactory);
        if (isCancelled(request)) {
            List<Protocol> protocolList = new ArrayList<Protocol>();
            ProtocolSearch protocolSearch = new ProtocolSearch();
            for (Protocol protocol : rootDomainObject.getProtocols()) {
                if (protocolSearch.satisfiedActivity(protocol)) {
                    protocolList.add(protocol);
                }
            }
            request.setAttribute("protocolSearch", protocolSearch);
            request.setAttribute("protocols", protocolList);
            return mapping.findForward("show-protocols");
        }
        if (isProtocolNumberValid(protocolFactory)) {
            protocolFactory.resetSearches();
            if (validateDates(protocolFactory, request)) {
                return mapping.findForward("prepareCreate-protocol-responsibles");
            } else {
                return mapping.findForward("prepareCreate-protocol-data");
            }
        } else {
            validateDates(protocolFactory, request);
            setError(request, "errorMessage", new ActionMessage("error.protocol.number.alreadyExists"));
            return mapping.findForward("prepareCreate-protocol-data");
        }
    }

    private boolean validateDates(ProtocolFactory protocolFactory, HttpServletRequest request) {
        if (protocolFactory.getBeginDate() != null && protocolFactory.getEndDate() != null) {
            if (!protocolFactory.getBeginDate().isBefore(protocolFactory.getEndDate())) {
                setError(request, "errorMessage", new ActionMessage("error.protocol.dates.notContinuous"));
                return false;
            }
        }
        return true;
    }

    public ActionForward invalidProtocolData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("protocolFactory", getRenderedObject());
        return mapping.findForward("prepareCreate-protocol-data");
    }

    public ActionForward insertResponsible(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = getRenderedObject();
        if (request.getParameter("back") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-data");
        }
        if (request.getParameter("next") != null) {
            if ((protocolFactory.getResponsibles() == null || protocolFactory.getResponsibles().size() == 0)
                    && (protocolFactory.getResponsibleFunctions() == null || protocolFactory.getResponsibleFunctions().size() == 0)) {
                setError(request, "errorMessage", new ActionMessage("error.protocol.empty.istResponsibles"));
                request.setAttribute("protocolFactory", protocolFactory);
                return mapping.findForward("prepareCreate-protocol-responsibles");
            }
            return prepareCreateProtocolUnits(mapping, actionForm, request, response);
            // request.setAttribute("protocolFactory", protocolFactory);
            // return mapping.findForward("prepareCreate-protocol-units");
        }

        if (request.getParameter("cancel") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-responsibles");
        }
        if (request.getParameter("createNew") != null) {
            request.setAttribute("createExternalPerson", "true");
            protocolFactory.setInternalUnit(false);
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-responsibles");
        }

        if (protocolFactory.getPartnerResponsible() == null
                && (protocolFactory.getResponsible() == null || !protocolFactory.getIstResponsibleIsPerson())
                && (protocolFactory.getResponsibleFunction() == null || !protocolFactory.getIstResponsible())) {
            if (StringUtils.isEmpty(protocolFactory.getResponsibleName()) || protocolFactory.getIstResponsible()) {
                setError(request, "errorMessage", new ActionMessage("error.protocol.person.selectFromList"));
            } else if (!protocolFactory.getIstResponsible()) {
                request.setAttribute("needToCreatePerson", "true");
            }
        } else {
            if (protocolFactory.getIstResponsible()) {
                if (protocolFactory.getIstResponsibleIsPerson()) {
                    if (!protocolFactory.addISTResponsible()) {
                        setError(request, "errorMessage", new ActionMessage("error.protocol.duplicated.responsible"));
                    }
                } else {
                    if (!protocolFactory.addISTResponsibleFunction()) {
                        setError(request, "errorMessage", new ActionMessage("error.protocol.duplicated.responsibleFunction"));
                    }
                }
            } else {
                if (!protocolFactory.addPartnerResponsible()) {
                    setError(request, "errorMessage", new ActionMessage("error.protocol.duplicated.responsible"));
                }
            }
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-responsibles");
    }

    public ActionForward createExternalResponsible(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = getRenderedObject();
        if (request.getParameter("cancel") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-responsibles");
        }
        if (protocolFactory.getUnitObject() != null) {
            ExternalContract externalContract =
                    InsertExternalPerson.run(new InsertExternalPerson.ServiceArguments(protocolFactory.getResponsibleName(),
                            protocolFactory.getUnitObject().getUnit()));
            protocolFactory.addPartnerResponsible(externalContract.getPerson());
        } else if (request.getParameter("createNew") != null) {
            request.setAttribute("createExternalUnit", "true");
        } else {
            request.setAttribute("createExternalPerson", "true");
            request.setAttribute("needToCreateUnit", "true");
        }
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-responsibles");
    }

    public ActionForward createExternalPersonAndUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = getRenderedObject();
        if (request.getParameter("cancel") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            request.setAttribute("createExternalPerson", "true");
            return mapping.findForward("prepareCreate-protocol-responsibles");
        }

        ExternalContract externalContract =
                InsertExternalPerson.run(protocolFactory.getResponsibleName(), protocolFactory.getUnitName(),
                        protocolFactory.getCountry());
        protocolFactory.addPartnerResponsible(externalContract.getPerson());
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-responsibles");
    }

    public ActionForward prepareCreateProtocolUnits(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = getRenderedObject();
        protocolFactory.resetSearches();
        Unit institutionalUnit = UnitUtils.readInstitutionUnit();
        if (protocolFactory.getUnits() == null || !protocolFactory.getUnits().contains(institutionalUnit)) {
            protocolFactory.addISTUnit(institutionalUnit);
        }
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-units");
    }

    public ActionForward insertUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolFactory protocolFactory = getRenderedObject();
        if (request.getParameter("back") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-responsibles");
        }
        if (request.getParameter("next") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-files");
        }
        if (request.getParameter("cancel") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-units");
        }
        if (request.getParameter("createNew") != null) {
            request.setAttribute("createExternalUnit", "true");
            protocolFactory.setInternalUnit(false);
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-units");
        }
        if (request.getParameter("createProtocol") != null) {
            return createProtocol(mapping, request, protocolFactory);
        }
        if (protocolFactory.getUnitObject() == null) {
            if (StringUtils.isEmpty(protocolFactory.getUnitName()) || protocolFactory.getInternalUnit()) {
                setError(request, "errorMessage", new ActionMessage("error.protocol.unit.selectFromList"));
            } else if (!protocolFactory.getInternalUnit()) {
                request.setAttribute("needToCreateUnit", "true");
            }
        } else {
            if (protocolFactory.getInternalUnit()) {
                if (!protocolFactory.addISTUnit()) {
                    setError(request, "errorMessage", new ActionMessage("error.protocol.duplicated.unit"));
                }
            } else {
                if (!protocolFactory.addPartnerUnit()) {
                    setError(request, "errorMessage", new ActionMessage("error.protocol.duplicated.unit"));
                }
            }
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-units");
    }

    private ActionForward createProtocol(ActionMapping mapping, HttpServletRequest request, ProtocolFactory protocolFactory)
            throws Exception {
        if (protocolFactory.getPartnerUnits() == null || protocolFactory.getPartnerUnits().size() == 0) {
            setError(request, "errorMessage", new ActionMessage("error.protocol.empty.partnerUnits"));
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-units");
        } else if (protocolFactory.getUnits() == null || protocolFactory.getUnits().size() == 0) {
            setError(request, "errorMessage", new ActionMessage("error.protocol.empty.units"));
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-units");
        } else {
            Protocol protocol = (Protocol) ExecuteFactoryMethod.run(protocolFactory);
            request.setAttribute("protocolFactory", new ProtocolFactory(protocol));
            return mapping.findForward("view-protocol");
        }
    }

    public ActionForward createExternalUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = getRenderedObject();
        if (request.getParameter("cancel") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-units");
        }

        Unit externalUnit = CreateExternalUnitByNameAndCountry.run(protocolFactory.getUnitName(), protocolFactory.getCountry());
        protocolFactory.addPartnerUnit(externalUnit);
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-units");
    }

    public ActionForward removeISTResponsible(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = getRenderedObject();
        Person responsible =
                (Person) RootDomainObject.readDomainObjectByOID(Person.class,
                        getInteger((DynaActionForm) actionForm, "responsibleID"));
        protocolFactory.getResponsibles().remove(responsible);
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-responsibles");
    }

    public ActionForward removeISTResponsibleFunction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = getRenderedObject();
        Function responsibleFunction =
                (Function) RootDomainObject.readDomainObjectByOID(Function.class,
                        getInteger((DynaActionForm) actionForm, "responsibleID"));
        protocolFactory.getResponsibleFunctions().remove(responsibleFunction);
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-responsibles");
    }

    public ActionForward removePartnerResponsible(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = getRenderedObject();
        Person partnerResponsible =
                (Person) RootDomainObject.readDomainObjectByOID(Person.class,
                        getInteger((DynaActionForm) actionForm, "responsibleID"));
        protocolFactory.getPartnerResponsibles().remove(partnerResponsible);
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-responsibles");
    }

    public ActionForward removeISTUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = getRenderedObject();
        Unit unit = (Unit) RootDomainObject.readDomainObjectByOID(Unit.class, getInteger((DynaActionForm) actionForm, "unitID"));
        protocolFactory.getUnits().remove(unit);
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-units");
    }

    public ActionForward removePartnerUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = getRenderedObject();
        Unit partnerUnit =
                (Unit) RootDomainObject.readDomainObjectByOID(Unit.class, getInteger((DynaActionForm) actionForm, "unitID"));
        protocolFactory.getPartnerUnits().remove(partnerUnit);
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-units");
    }

    public ActionForward prepareCreateProtocolFiles(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("protocolFactory", getRenderedObject());
        return mapping.findForward("prepareCreate-protocol-files");
    }

    public ActionForward addFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = getRenderedObject();
        if (request.getParameter("back") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-units");
        }
        if (request.getParameter("createProtocol") != null) {
            Protocol protocol = (Protocol) ExecuteFactoryMethod.run(protocolFactory);
            request.setAttribute("protocolFactory", new ProtocolFactory(protocol));
            return mapping.findForward("view-protocol");
        }
        if (protocolFactory.getInputStream() != null) {
            protocolFactory.addFile();
            protocolFactory.resetFile();
            RenderUtils.invalidateViewState();
        }
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-files");
    }

    public ActionForward removeFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = getRenderedObject();
        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        protocolFactory.removeFile(dynaActionForm.getString("fileName"));
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-files");
    }

    private boolean isProtocolNumberValid(ProtocolFactory protocolFactory) {
        for (Protocol protocol : rootDomainObject.getProtocols()) {
            if (protocol.getProtocolNumber().equals(protocolFactory.getProtocolNumber())) {
                return false;
            }
        }
        return true;
    }

    private void setError(HttpServletRequest request, String error, ActionMessage actionMessage) {
        ActionMessages actionMessages = getMessages(request);
        actionMessages.add(error, actionMessage);
        saveMessages(request, actionMessages);
    }
}