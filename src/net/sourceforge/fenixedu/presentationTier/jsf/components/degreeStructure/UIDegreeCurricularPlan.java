package net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;

public class UIDegreeCurricularPlan extends UIInput {
    public static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UIDegreeCurricularPlan";

    public static final String COMPONENT_FAMILY = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UIDegreeCurricularPlan";

    protected static final int ROOT_DEPTH = -1;

    private Boolean toEdit;

    private FacesContext facesContext;

    private ResponseWriter writer;

    public UIDegreeCurricularPlan() {
        super();
        this.setRendererType(null);
    }

    public String getFamily() {
        return UIDegreeCurricularPlan.COMPONENT_FAMILY;
    }

    public void encodeBegin(FacesContext facesContext) throws IOException {
        if (!isRendered()) {
            return;
        }

        final DegreeCurricularPlan dcp = (DegreeCurricularPlan) this.getAttributes().get("dcp");
        if (!dcp.getCurricularStage().equals(CurricularStage.OLD)) {
            final Boolean onlyStructure = this.getBooleanAttribute("onlyStructure");
            this.toEdit = this.getBooleanAttribute("toEdit");
            final String organizeBy = (String) this.getAttributes().get("organizeBy");

            if (organizeBy != null && organizeBy.equalsIgnoreCase("years")) {
                encodeByYears(facesContext, dcp);
            } else {
                StringBuilder dcpBuffer = new StringBuilder();
                dcpBuffer.append("[DCP ").append(dcp.getIdInternal()).append("] ").append(dcp.getName());
                System.out.println(dcpBuffer);

                new UICourseGroup(dcp.getDegreeModule(), onlyStructure, this.toEdit, ROOT_DEPTH, "").encodeBegin(facesContext);
            }
        }
    }

    private Boolean getBooleanAttribute(String attributeName) {
        if (this.getAttributes().get(attributeName) instanceof Boolean) {
            return (Boolean) this.getAttributes().get(attributeName);
        } else {
            return Boolean.valueOf((String) this.getAttributes().get(attributeName));
        }
    }

    private void encodeByYears(FacesContext facesContext, DegreeCurricularPlan dcp) throws IOException {
        this.facesContext = facesContext;
        this.writer = facesContext.getResponseWriter();

        if (!((CourseGroup) dcp.getDegreeModule()).hasAnyCourseGroupContexts()) {
            encodeEmptyCurricularPlanInfo();
        } else {
            CurricularPeriod degreeStructure = dcp.getDegreeStructure();
            for (CurricularPeriod child : degreeStructure.getSortedChilds()) {
                encodePeriodTable(child);
            }
            encodeSubtitles();
        }
    }

    private void encodeEmptyCurricularPlanInfo() throws IOException {
        writer.startElement("table", this);
        writer.startElement("tr", this);
        writer.startElement("td", this);
        writer.writeAttribute("align", "center", null);
        writer.startElement("i", this);
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "empty.curricularPlan"));
        writer.endElement("i");
        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
    }
    
    private void encodeSubtitles() throws IOException {
        writer.startElement("ul", this);
        writer.writeAttribute("class", "nobullet", null);
        writer.writeAttribute("style", "padding-left: 0pt;", null);
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "subtitle")).append(":\n");

        writer.startElement("li", this);
        writer.startElement("span", this);
        writer.writeAttribute("style", "color: #888", null);
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "contactLessonHoursAcronym")).append(" - ");
        writer.endElement("span");
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "contactLessonHours"));
        writer.endElement("li");
        
        writer.startElement("li", this);
        writer.startElement("span", this);
        writer.writeAttribute("style", "color: #888", null);
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "autonomousWorkAcronym")).append(" - ");
        writer.endElement("span");
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "autonomousWork"));
        writer.endElement("li");
        
        writer.startElement("li", this);
        writer.startElement("span", this);
        writer.writeAttribute("style", "color: #888", null);
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "totalLoadAcronym")).append(" - ");
        writer.endElement("span");
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "totalLoad")).append(" (");
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "contactLessonHoursAcronym")).append(" + ");
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "autonomousWorkAcronym")).append(")");
        writer.endElement("li");
        
        writer.endElement("ul");
    }

    private void encodePeriodTable(CurricularPeriod curricularPeriod) throws IOException {
        if (curricularPeriod.hasAnyChilds()) {
            for (CurricularPeriod child : curricularPeriod.getSortedChilds()) {
                encodePeriodTable(child);    
            }
        } else {
            writer.startElement("table", this);
            writer.writeAttribute("class", "showinfo1 sp thleft", null);
            writer.writeAttribute("style", "width: 70em;", null);

            encodeHeader(curricularPeriod);
            if (curricularPeriod.hasAnyContexts()) {
                List<Double> sums = encodeCurricularCourses(curricularPeriod.getContexts());
                encodeSumsFooter(sums);
            } else {
                encodeEmptySemesterInfo();
            }
            
            writer.endElement("table");
        }
    }
    
    private void encodeHeader(CurricularPeriod curricularPeriod) throws IOException {
        writer.startElement("tr", this);
        writer.writeAttribute("class", "bgcolor2", null);
        
        writer.startElement("th", this);
        writer.writeAttribute("colspan", (this.toEdit) ? 3 : 5, null);
        writer.startElement("strong", this);
        writer.append(curricularPeriod.getFullLabel());
        writer.endElement("strong");
        writer.endElement("th");

        if (this.toEdit) {
            encodeCourseGroupOptions(curricularPeriod);
        }

        writer.endElement("tr");
    }
    
    private void encodeCourseGroupOptions(CurricularPeriod curricularPeriod) throws IOException {
        writer.startElement("td", this);
        writer.writeAttribute("class", "aright", null);
        writer.writeAttribute("colspan", 3, null);
        encodeLink("createCurricularCourse.faces?degreeCurricularPlanID="
                + this.facesContext.getExternalContext().getRequestParameterMap().get(
                        "degreeCurricularPlanID") + "&curricularYearID=" + curricularPeriod.getParent().getOrder()
                + "&curricularSemesterID=" + curricularPeriod.getOrder(), "create.curricular.course");
        writer.append(" , ");
        encodeLink("associateCurricularCourse.faces?degreeCurricularPlanID=" 
                + this.facesContext.getExternalContext().getRequestParameterMap().get(
                        "degreeCurricularPlanID") + "&curricularYearID=" + curricularPeriod.getParent().getOrder()
                + "&curricularSemesterID=" + curricularPeriod.getOrder(), "associate.curricular.course");
        writer.endElement("td");
    }
    
    private List<Double> encodeCurricularCourses(List<Context> contexts) throws IOException {
        double sumContactLoad = 0.0;
        double sumAutonomousWork = 0.0;
        double sumTotalLoad = 0.0;
        double sumCredits = 0.0;
        for (Context context : contexts) {
            DegreeModule degreeModule = context.getDegreeModule();
            if (degreeModule instanceof CurricularCourse) {
                sumContactLoad += ((CurricularCourse)degreeModule).getContactLoad(context.getCurricularPeriod().getOrder());
                sumAutonomousWork += ((CurricularCourse)degreeModule).getAutonomousWorkHours(context.getCurricularPeriod().getOrder());
                sumTotalLoad += ((CurricularCourse)degreeModule).getTotalLoad(context.getCurricularPeriod().getOrder());
                sumCredits += degreeModule.getEctsCredits();
                new UICurricularCourse((CurricularCourse) degreeModule, this.toEdit, context).encodeBegin(facesContext);
            }
        }
        
        List<Double> result = new ArrayList<Double>(4);
        result.add(sumContactLoad);
        result.add(sumAutonomousWork);
        result.add(sumTotalLoad);
        result.add(sumCredits);
        
        return result;
    }

    private void encodeSumsFooter(List<Double> sums) throws IOException {
        writer.startElement("tr", this);

        writer.startElement("td", this);
        writer.writeAttribute("colspan", 3, null);
        writer.endElement("td");

        writer.startElement("td", this);
        writer.writeAttribute("class", "highlight2 smalltxt", null);
        writer.writeAttribute("align", "center", null);
        writer.writeAttribute("style", "width: 13em;", null);
        writer.startElement("span", this);
        writer.writeAttribute("style", "color: #888", null);
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "contactLessonHoursAcronym")).append("-");
        writer.endElement("span");
        writer.append(String.valueOf(sums.get(0))).append(" ");

        writer.startElement("span", this);
        writer.writeAttribute("style", "color: #888", null);
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "autonomousWorkAcronym")).append("-");
        writer.endElement("span");
        writer.append(String.valueOf(sums.get(1))).append(" ");

        writer.startElement("span", this);
        writer.writeAttribute("style", "color: #888", null);
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "totalLoadAcronym")).append("-");
        writer.endElement("span");
        writer.append(String.valueOf(sums.get(2))).append(" ");
        writer.endElement("td");

        writer.startElement("td", this);
        writer.writeAttribute("class", "aright highlight2", null);
        writer.writeAttribute("style", "width: 7em;", null);
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", "credits")).append(" ");
        writer.append(String.valueOf(sums.get(3)));
        writer.endElement("td");
        
        if (this.toEdit) {
            writer.startElement("td", this);
            writer.append("&nbsp;");
            writer.endElement("td");
        }

        writer.endElement("tr");
    }

    private void encodeEmptySemesterInfo() throws IOException {
        writer.startElement("tr", this);
        writer.startElement("td", this);
        if (this.toEdit) {
            writer.writeAttribute("colspan", 5, null);
        } else {
            writer.writeAttribute("colspan", 3, null);
        }
        writer.writeAttribute("align", "center", null);
        writer.startElement("i", this);
        writer.append(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources",
                "no.associated.curricular.courses.to.year"));
        writer.endElement("i");
        writer.endElement("td");
        writer.endElement("tr");
    }

    protected String getBundleValue(String bundleName, String bundleKey) {
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, facesContext.getViewRoot()
                .getLocale());
        return bundle.getString(bundleKey);
    }

    protected void encodeLink(String href, String bundleKey) throws IOException {
        writer.startElement("a", this);
        writer.writeAttribute("href", href, null);
        writer.write(this.getBundleValue("ServidorApresentacao/BolonhaManagerResources", bundleKey));
        writer.endElement("a");
    }

}
