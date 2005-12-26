package net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure;

import java.io.IOException;
import java.util.Locale;

import javax.faces.context.FacesContext;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.IContext;
import net.sourceforge.fenixedu.domain.degreeStructure.IDegreeModule;

public class UICurricularCourse extends UIDegreeModule {
    public static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UICurricularCourse";

    public static final String COMPONENT_FAMILY = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UICurricularCourse";

    private IContext previousContext;
    
    public UICurricularCourse() {
        super();
    }

    public UICurricularCourse(IDegreeModule curricularCourse, Boolean toEdit, int depth, String tabs, IContext previousContext) {
        super(curricularCourse, toEdit, depth, tabs);
        this.previousContext = previousContext;
    }

    public String getFamily() {
        return UICurricularCourse.COMPONENT_FAMILY;
    }

    public void encodeBegin(FacesContext facesContext) throws IOException {
        if (!isRendered()) {
            return;
        }
        
        StringBuffer buffer = new StringBuffer();
        buffer.append(tabs);
        buffer.append("[LEVEL ").append(new Integer(this.depth)).append("]");
        buffer.append("[CC ").append(this.degreeModule.getIdInternal()).append("][");
        buffer.append(previousContext.getCurricularSemester().getCurricularYear().getYear()).append("Y,");
        buffer.append(previousContext.getCurricularSemester().getSemester()).append("S] ");
        buffer.append(this.degreeModule.getName());
        System.out.println(buffer.toString());

        this.facesContext = facesContext;
        this.writer = facesContext.getResponseWriter();
        encodeCurricularCourse();
    }

    private void encodeCurricularCourse() throws IOException {
        writer.startElement("tr", this);
        writer.startElement("td", this);
        if (!facesContext.getViewRoot().getLocale().equals(Locale.ENGLISH)) {
            writer.append(this.degreeModule.getName());    
        } else {
            writer.append(this.degreeModule.getNameEn());
        }
        writer.endElement("td");
        writer.startElement("td", this);
        writer.writeAttribute("align", "center", null);
        writer.writeAttribute("width", "100px", null);
        writer.append(previousContext.getCurricularSemester().getCurricularYear().getYear().toString()).append("� ");
        writer.append(this.getBundleValue(facesContext, "ServidorApresentacao/BolonhaManagerResources", "year"));
        writer.append(", ");
        writer.append(previousContext.getCurricularSemester().getSemester().toString()).append("� ");
        writer.append(this.getBundleValue(facesContext, "ServidorApresentacao/BolonhaManagerResources", "semester"));
        writer.endElement("td");
        writer.startElement("td", this);
        writer.writeAttribute("align", "center", null);
        writer.append(((ICurricularCourse)this.degreeModule).computeEctsCredits().toString());
        writer.endElement("td");
        
        if (this.toEdit) {
            encodeCurricularCourseOptions();    
        }
        
        writer.endElement("tr");
    }

    private void encodeCurricularCourseOptions() throws IOException {
        writer.startElement("td", this);
        encodeLink("editCurricularCourse.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("dcpId") + "&contextID=" + this.previousContext.getIdInternal() + "&curricularCourseID=" + this.degreeModule.getIdInternal(), "edit");
        writer.append(" , ");
        encodeLink("deleteCurricularCourse.facesdegreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("dcpId") + "&contextID=" + this.previousContext.getIdInternal() + "&curricularCourseID=" + this.degreeModule.getIdInternal(), "delete");
        writer.endElement("td");
    }
    
}
