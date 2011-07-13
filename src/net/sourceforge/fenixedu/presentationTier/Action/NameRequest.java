package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

import com.lowagie.text.DocumentException;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/NameResolution", module = "external")
public class NameRequest extends FenixDispatchAction {

    private static final String storedPassword;
    private static final String storedUsername;

    static {
	storedUsername = PropertiesManager.getProperty("nameresolution.name");
	storedPassword = PropertiesManager.getProperty("nameresolution.password");
    }

    public ActionForward resolve(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    final HttpServletResponse httpServletResponse) throws Exception {

	String digest = DigestUtils.shaHex(storedPassword);
	String providedUsername = request.getParameter("username");
	String providedDigest = request.getParameter("password");

	if (storedUsername.equals(providedUsername) && digest.equals(providedDigest)) {
	    String id = request.getParameter("id");
	    User user = User.readUserByUserUId(id);

	    String name = user.getPerson().getName();
	    String nickName = user.getPerson().getNickname();
	    httpServletResponse.setHeader("Content-Type", "application/json; charset=utf-8");
	    String message = "{\n" + "\"name\" : \"" + name + "\",\n" + "\"nickName\" : \"" + nickName + "\"\n" + "}";
	    httpServletResponse.getOutputStream().write(message.getBytes("UTF-8"));
	    httpServletResponse.getOutputStream().close();
	    return null;

	} else {
	    throw new DocumentException("invalid.authentication");
	}

    }

}
