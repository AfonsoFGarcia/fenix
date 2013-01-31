/*
 * Created on Apr 3, 2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.dataTransferObject.InfoExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomExamsMap;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew.renderers.ExamsMapContentRenderer;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew.renderers.ExamsMapSlotContentRenderer;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class RenderExamsMapTag extends TagSupport {

	// Name of atribute containing ExamMap
	private String name;

	private String user;

	private String mapType;

	private ExamsMapSlotContentRenderer examsMapSlotContentRenderer = new ExamsMapContentRenderer();

	@Override
	public int doStartTag() throws JspException {
		// Obtain InfoExamMap
		InfoExamsMap infoExamsMap = null;
		InfoRoomExamsMap infoRoomExamsMap = null;
		ExamsMap examsMap = null;
		IExamsMapRenderer renderer = null;
		String typeUser = "";
		String typeMapType = "";
		Locale locale = (Locale) pageContext.findAttribute(Globals.LOCALE_KEY);

		try {
			infoExamsMap = (InfoExamsMap) pageContext.findAttribute(name);
			typeUser = user;
			typeMapType = mapType;
			examsMap = new ExamsMap(infoExamsMap, locale);
			renderer = new ExamsMapRenderer(examsMap, this.examsMapSlotContentRenderer, typeUser, typeMapType, locale);
		} catch (ClassCastException e) {
			infoExamsMap = null;
		}
		try {
			infoRoomExamsMap = (InfoRoomExamsMap) pageContext.findAttribute(name);

			typeUser = user;
			examsMap = new ExamsMap(infoRoomExamsMap, locale);
			renderer = new ExamsMapForRoomRenderer(examsMap, this.examsMapSlotContentRenderer, typeUser);
		} catch (ClassCastException e) {
			infoRoomExamsMap = null;
		}
		if (infoExamsMap == null && infoRoomExamsMap == null) {
			throw new JspException(messages.getMessage("generateExamsMap.infoExamsMap.notFound", name));
		}

		// Generate Map from infoExamsMap
		JspWriter writer = pageContext.getOut();
		// ExamsMap examsMap = new ExamsMap(infoExamsMap);

		// ExamsMapRenderer renderer =
		// new ExamsMapRenderer(
		// examsMap,
		// this.examsMapSlotContentRenderer,
		// typeUser);

		try {
			writer.print(renderer.render(locale, pageContext));
		} catch (IOException e) {
			e.printStackTrace();
			throw new JspException(messages.getMessage("generateExamsMap.io", e.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (SKIP_BODY);
	}

	@Override
	public int doEndTag() {
		return (EVAL_PAGE);
	}

	@Override
	public void release() {
		super.release();
	}

	// Error Messages
	protected static MessageResources messages = MessageResources.getMessageResources("ApplicationResources");

	public String getName() {
		return (this.name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String string) {
		user = string;
	}

	public String getMapType() {
		return mapType;
	}

	public void setMapType(String string) {
		mapType = string;
	}
}