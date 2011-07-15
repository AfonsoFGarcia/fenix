package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "publico", path = "/files")
public class FileDownload extends FenixAction {
    public static final String ACTION_PATH = "/conteudos-publicos/ficheiros?oid=";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	String oid = request.getParameter("oid");
	File file = rootDomainObject.readFileByOID(Integer.parseInt(oid));
	if (!file.isPrivate() || file.isPersonAllowedToAccess(AccessControl.getPerson())) {
	    try {
		response.setContentType(file.getMimeType());
		final DataOutputStream dos = new DataOutputStream(response.getOutputStream());
		response.addHeader("Content-Disposition", "attachment; filename=" + file.getFilename());
		response.setContentLength(file.getSize());
		dos.write(file.getContents());
		dos.close();
	    } catch (IOException e) {
	    }
	}
	return null;
    }
}
