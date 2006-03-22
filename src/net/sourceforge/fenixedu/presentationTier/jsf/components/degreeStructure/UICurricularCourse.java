package net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure;

import java.io.IOException;
import java.util.Locale;

import javax.faces.context.FacesContext;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.util.CurricularPeriodLabelFormatter;

public class UICurricularCourse extends UIDegreeModule {
    public static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UICurricularCourse";

    public static final String COMPONENT_FAMILY = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UICurricularCourse";

    private CurricularCourse curricularCourse;
    private boolean byYears;
    
    public UICurricularCourse() {
        super();
        this.curricularCourse = (CurricularCourse) super.degreeModule;
        this.byYears = false;
    }

    public UICurricularCourse(DegreeModule curricularCourse, Context previousContext, Boolean toEdit, Boolean showRules, int depth, String tabs, ExecutionYear executionYear) {
        super(curricularCourse, previousContext, toEdit, showRules, depth, tabs, executionYear);
        this.curricularCourse = (CurricularCourse) super.degreeModule;
        this.byYears = false;
    }

    public UICurricularCourse(DegreeModule curricularCourse, Context previousContext, Boolean toEdit, Boolean showRules, ExecutionYear executionYear) {
        super(curricularCourse, previousContext, toEdit, showRules, 0, null, executionYear);
        this.curricularCourse = (CurricularCourse) super.degreeModule;
        this.byYears = true;
    }
    
    public String getFamily() {
        return UICurricularCourse.COMPONENT_FAMILY;
    }

    public void encodeBegin(FacesContext facesContext) throws IOException {
        if (!isRendered()) {
            return;
        }
        
        log(false);

        this.facesContext = facesContext;
        this.writer = facesContext.getResponseWriter();
        
        encodeCurricularCourse();
        
        if (this.showRules && this.curricularCourse.hasAnyCurricularRules()) {
            encodeCurricularRules();    
        }
        
        if (!byYears && this.curricularCourse.isAnual()) {
            encodeInNextPeriod(facesContext);
        }
    }

    private void log(boolean on) {
        if (on) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(tabs);
            buffer.append("[LEVEL ").append(new Integer(this.depth)).append("]");
            buffer.append("[CC ").append(this.curricularCourse.getIdInternal()).append("][");
            buffer.append(previousContext.getCurricularPeriod().getOrderByType(CurricularPeriodType.YEAR)).append("Y,");
            buffer.append(previousContext.getCurricularPeriod().getOrderByType(CurricularPeriodType.SEMESTER)).append("S] ");
            buffer.append(this.curricularCourse.getName());
            System.out.println(buffer.toString());
        }
    }

    private void encodeCurricularCourse() throws IOException {
        writer.startElement("tr", this);
        
        encodeName(true);
        encodeContext(previousContext.getCurricularPeriod());
        encodeRegime();
        encodeLoadsAndCredits(previousContext.getCurricularPeriod());

        if (this.toEdit) {
            if (this.showRules) {
                encodeCurricularRulesOptions();
            } else {
                encodeCurricularCourseOptions();
            }
        }
        
        writer.endElement("tr");
    }

    private void encodeName(boolean linkable) throws IOException {
        writer.startElement("td", this);
        
        if (linkable) {
            writer.startElement("a", this);
            String action = "&action=" + ((this.toEdit) ? "build" : (String) this.facesContext.getExternalContext().getRequestParameterMap().get("action")); 
            String organizeBy = "&organizeBy=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("organizeBy");
            String showRules = "&showRules=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("showRules");        
            String hideCourses = "&hideCourses=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("hideCourses");
            writer.writeAttribute("href", "viewCurricularCourse.faces?curricularCourseID=" + this.curricularCourse.getIdInternal() + action + organizeBy + showRules + hideCourses, null);
        }
        
        if (!facesContext.getViewRoot().getLocale().equals(Locale.ENGLISH)) {
            writer.append(this.curricularCourse.getName());
        } else {
            writer.append(this.curricularCourse.getNameEn());
        }
        
        if (linkable) {
            writer.endElement("a");
        }
        
        writer.endElement("td");
    }
    
    private void encodeContext(CurricularPeriod curricularPeriod) throws IOException {
        writer.startElement("td", this);
        writer.writeAttribute("class", "smalltxt", null);
        if (!byYears) {
            writer.writeAttribute("align", "center", null);
            writer.append(CurricularPeriodLabelFormatter.getFullLabel(curricularPeriod, getLocale(), true));
        } else {
            writer.append(previousContext.getParentCourseGroup().getName());
        }
        writer.endElement("td");
    }

    private void encodeRegime() throws IOException {
        writer.startElement("td", this);
        if (!this.curricularCourse.isOptional()) {
            writer.writeAttribute("class", "highlight2 smalltxt", null);
            writer.writeAttribute("align", "center", null);
            writer.writeAttribute("style", "width: 1em;", null);
            writer.append(this.getBundleValue("EnumerationResources", this.curricularCourse.getRegime().toString() + ".ACRONYM"));
        } else {
            writer.append("&nbsp;");
        }
        writer.endElement("td");
    }
    
    private void encodeLoadsAndCredits(CurricularPeriod curricularPeriod) throws IOException {
        writer.startElement("td", this);
        if (!this.curricularCourse.isOptional()) {
            writer.writeAttribute("class", "smalltxt", null);
            writer.writeAttribute("class", "aright", null);
            
            writer.startElement("span", this);
            writer.writeAttribute("style", "color: #888", null);
            writer.append(this.getBundleValue("BolonhaManagerResources", "contactLessonHoursAcronym")).append("-");
            writer.endElement("span");
            writer.append(this.curricularCourse.getContactLoad(curricularPeriod).toString()).append(" ");

            writer.startElement("span", this);
            writer.writeAttribute("style", "color: #888", null);
            writer.append(this.getBundleValue("BolonhaManagerResources", "autonomousWorkAcronym")).append("-");
            writer.endElement("span");
            writer.append(this.curricularCourse.getAutonomousWorkHours(curricularPeriod).toString()).append(" ");
            
            writer.startElement("span", this);
            writer.writeAttribute("style", "color: #888", null);
            writer.append(this.getBundleValue("BolonhaManagerResources", "totalLoadAcronym")).append("-");
            writer.endElement("span");
            writer.append(this.curricularCourse.getTotalLoad(curricularPeriod).toString());
            writer.endElement("td");

            writer.startElement("td", this);
            writer.writeAttribute("class", "smalltxt", null);
            writer.writeAttribute("class", "aright", null);
            writer.append(this.getBundleValue("BolonhaManagerResources", "credits.abbreviation")).append(" ");
            writer.append(this.curricularCourse.getEctsCredits(curricularPeriod).toString());
        } else {
            writer.append("&nbsp;");
            writer.endElement("td");
            writer.startElement("td", this);
        }
        writer.endElement("td");
    }

    public void encodeInNextPeriod(FacesContext facesContext) throws IOException {
        if (previousContext.getCurricularPeriod().getNext() != null) {
            this.facesContext = facesContext;
            this.writer = facesContext.getResponseWriter();
            
            writer.startElement("tr", this);
            
            encodeName(false);
            encodeContext(previousContext.getCurricularPeriod().getNext());
            encodeRegime();
            encodeLoadsAndCredits(previousContext.getCurricularPeriod().getNext());
            
            writer.startElement("td", this);
            writer.append("&nbsp;");
            writer.endElement("td");
            
            writer.endElement("tr");
        }
    }

    private void encodeCurricularCourseOptions() throws IOException {
        writer.startElement("td", this);
        writer.writeAttribute("align", "right", null);
        writer.writeAttribute("style", "width: 9em;", null);
        String organizeBy = "&organizeBy=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("organizeBy");
        encodeLink("editCurricularCourse.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&contextID=" + this.previousContext.getIdInternal() + "&curricularCourseID=" + this.curricularCourse.getIdInternal() + organizeBy, "edit");
        writer.append(" , ");
        encodeLink("deleteCurricularCourseContext.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&contextID=" + this.previousContext.getIdInternal() + "&curricularCourseID=" + this.curricularCourse.getIdInternal() + organizeBy, "delete");
        writer.endElement("td");
    }

    private void encodeCurricularRulesOptions() throws IOException {
        writer.startElement("td", this);
        writer.writeAttribute("align", "right", null);
        writer.writeAttribute("style", "width: 9em;", null);
        String organizeBy = "&organizeBy=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("organizeBy");
        String hideCourses = "&hideCourses=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("hideCourses");        
        encodeLink("../curricularRules/createCurricularRule.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&degreeModuleID=" + this.curricularCourse.getIdInternal() + organizeBy + hideCourses, "setCurricularRule");
        writer.endElement("td");
    }

}
