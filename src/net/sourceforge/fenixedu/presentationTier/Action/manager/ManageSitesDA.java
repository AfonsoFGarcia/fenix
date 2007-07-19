package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DegreeSite;
import net.sourceforge.fenixedu.domain.DepartmentSite;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.PedagogicalCouncilSite;
import net.sourceforge.fenixedu.domain.ResearchUnitSite;
import net.sourceforge.fenixedu.domain.ScientificCouncilSite;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.SiteTemplate;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Struts dispatch actions used to control the templates for the various types
 * of websites.
 * 
 * @author cfgi
 */
public class ManageSitesDA extends SiteManagementDA {

    public static Class[] SITE_TYPES = {
        Homepage.class,
        ExecutionCourseSite.class,
        DegreeSite.class,
        DepartmentSite.class,
        ResearchUnitSite.class,
        PedagogicalCouncilSite.class,
        ScientificCouncilSite.class
    };
    
    public ActionForward listSites(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("types", Arrays.asList(SITE_TYPES));
        
        return mapping.findForward("listSites");
    }

    public ActionForward manageSite(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Class type = getType(request);
        
        if (type == null) {
            return listSites(mapping, actionForm, request, response);
        }
        else {
            Site site = (Site) ServiceUtils.executeService(getUserView(request), "CreateSiteTemplateInstance", type);
            request.setAttribute("site", site);
            
            return mapping.findForward("sectionsManagement");
        }
    }

    private Class getType(HttpServletRequest request) {
        String value = request.getParameter("type");
        
        if (value == null) {
            return null;
        }
        else {
            try {
                return Class.forName(value);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
    }

    @Override
    protected String getAuthorNameForFile(HttpServletRequest request, Item item) {
        return UnitUtils.readInstitutionUnit().getName();
    }

    @Override
    protected String getItemLocationForFile(HttpServletRequest request, Item item, Section section) {
        return null;
    }

    public ActionForward prepareCreateFunctionalitySection(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Section section = selectSection(request);
        
        if (section == null) {
            Site site = getSite(request);
            
            request.setAttribute("creator", new FunctionalitySectionCreator(site));
        } else {
            request.setAttribute("creator", new FunctionalitySectionCreator(section));
        }

        return mapping.findForward("chooseFunctionality");
    }
    
    public ActionForward functionalitySection(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        section(mapping, actionForm, request, response);
        
        return mapping.findForward("functionalitySection");
    }

    public ActionForward editFunctionalitySection(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        selectSection(request);
        
        return mapping.findForward("editFunctionalitySection");
    }

    @Override
    protected Site getSite(HttpServletRequest request) {
        Site site = super.getSite(request);
        
        if (site != null) {
            return site;
        }
        else {
            String type = request.getParameter("type");
            if (type == null) {
                return null;
            }
            else {
                try {
                    return SiteTemplate.getTemplateForType(Class.forName(type));
                } catch (ClassNotFoundException e) {
                    return null;
                }
            }
        }
    }

    public ActionForward prepareChooseBaseModule(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return mapping.findForward("chooseBaseModule");
    }
}
