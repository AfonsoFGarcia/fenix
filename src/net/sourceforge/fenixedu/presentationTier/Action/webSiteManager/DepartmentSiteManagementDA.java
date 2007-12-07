package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DepartmentSite;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.MetaDomainObject;

import org.apache.struts.util.RequestUtils;

public class DepartmentSiteManagementDA extends CustomUnitSiteManagementDA {

    private Department getDepartment(final HttpServletRequest request) {
        DepartmentSite site = (DepartmentSite) getSite(request);
        return site == null ? null : site.getDepartment();
    }
    
    @Override
    protected String getAuthorNameForFile(HttpServletRequest request, Item item) {
        return getDepartment(request).getRealName();
    }

    @Override
    protected String getDirectLinkContext(HttpServletRequest request) {
        Department department = getDepartment(request);
        try {
            
            MetaDomainObject metaDomainObject = MetaDomainObject.getMeta(department.getDepartmentUnit().getSite().getClass());
            return RequestUtils.absoluteURL(request, metaDomainObject.getAssociatedPortal().getName().getContent() + "/" + department.getAcronym()).toString();
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override
    protected String getRemoveManagerServiceName() {
    	return "RemoveDepartmentSiteManager";
    }
    
    @Override
    protected String getAddManagerServiceName() {
    	return "AddDepartmentSiteManager";
    }
    
}
