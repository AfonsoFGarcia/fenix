package net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.util.CurricularRuleLabelFormatter;

public class UIDegreeModule extends UIInput {
    public static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UIDegreeModule";

    public static final String COMPONENT_FAMILY = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UIDegreeModule";

    protected DegreeModule degreeModule;
    protected Context previousContext;
    protected ExecutionYear executionYear;
    protected Boolean toEdit;
    protected Boolean showRules = Boolean.FALSE;
    protected int depth;
    protected String tabs;

    protected FacesContext facesContext;
    protected ResponseWriter writer;

    protected static final int BASE_DEPTH = UIDegreeCurricularPlan.ROOT_DEPTH + 1;
    
    public UIDegreeModule() {
        super();
        this.setRendererType(null);
    }

    public UIDegreeModule(DegreeModule degreeModule, Context previousContext, Boolean toEdit, Boolean showRules, int depth, String tabs, ExecutionYear executionYear) {
        this();
        this.degreeModule = degreeModule;
        this.previousContext = previousContext;
        this.toEdit = toEdit;
        this.showRules = showRules;
        this.depth = depth;
        this.tabs = tabs;
        this.executionYear = executionYear;
    }

    public String getFamily() {
        return UIDegreeModule.COMPONENT_FAMILY;
    }

    public void encodeBegin(FacesContext facesContext) throws IOException {
        if (!isRendered()) {
            return;
        }
        
        setFromAttributes();
        
        if (this.degreeModule.isLeaf()) {
            new UICurricularCourse(this.degreeModule, null, this.toEdit, this.showRules, this.depth, this.tabs, this.executionYear).encodeBegin(facesContext);
        } else if (!this.degreeModule.isLeaf()) {
            new UICourseGroup(this.degreeModule, null, this.toEdit, this.showRules, this.depth, this.tabs, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null).encodeBegin(facesContext);
        }
    }
    
    private void setFromAttributes() {
        if (this.degreeModule == null) {
            this.degreeModule = (DegreeModule) this.getAttributes().get("degreeModule");
        }
        if (this.toEdit == null) {
            this.toEdit = (Boolean) this.getAttributes().get("toEdit");
        }
        if (this.tabs == null) {
            this.depth = UIDegreeModule.BASE_DEPTH;
            this.tabs = "";
        }
    }

    protected String getBundleValue(String bundleName, String bundleKey) {
        ResourceBundle bundle = ResourceBundle.getBundle("resources/" + bundleName, facesContext.getViewRoot().getLocale());
        return bundle.getString(bundleKey);
    }

    protected Locale getLocale() {
        return facesContext.getViewRoot().getLocale();
    }

    protected void encodeLink(String href, String ... bundleKeys) throws IOException {
        writer.startElement("a", this);
        writer.writeAttribute("href", href, null);
        for (String bundleKey : bundleKeys) {
            writer.write(this.getBundleValue("BolonhaManagerResources", bundleKey));    
        }
        writer.endElement("a");
    }

    protected void encodeCurricularRules() throws IOException {
        List<CurricularRule> curricularRulesToEncode = new ArrayList<CurricularRule>();

        for (CurricularRule curricularRule : this.degreeModule.getCurricularRules(this.executionYear)) {
            if (curricularRule.appliesToContext(this.previousContext)) {
                curricularRulesToEncode.add(curricularRule);
            } 
        }

        if (!curricularRulesToEncode.isEmpty()) {
            writer.startElement("tr", this);
        
            writer.startElement("td", this);
            writer.writeAttribute("colspan", (this.toEdit) ? "6" : "5", null);    
            writer.writeAttribute("style", "padding:0; margin: 0;", null);
            
            writer.startElement("table", this);
            writer.writeAttribute("class", "smalltxt noborder", null);
            writer.writeAttribute("style", "width: 100%;", null);
            for (CurricularRule curricularRule : curricularRulesToEncode) {
                writer.startElement("tr", this);
                encodeCurricularRule(curricularRule);
                if(this.toEdit) {
                    encodeCurricularRuleOptions(curricularRule);
                }
                writer.endElement("tr");
            }
            writer.endElement("table");
            writer.endElement("td");
            
            writer.endElement("tr");
        }
    }
    
    private void encodeCurricularRule(CurricularRule curricularRule) throws IOException {
        writer.startElement("td", this);
        if (!this.toEdit) {
            writer.writeAttribute("colspan", "2", null);    
        }
        writer.writeAttribute("style", "color: #888;", null);
        writer.append(CurricularRuleLabelFormatter.getLabel(curricularRule));
        writer.endElement("td");
    }

    private void encodeCurricularRuleOptions(CurricularRule curricularRule) throws IOException {
        writer.startElement("td", this);
        writer.writeAttribute("class", "aright", null);
        String organizeBy = "&organizeBy=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("organizeBy");
        String hideCourses = "&hideCourses=" + (String) this.facesContext.getExternalContext().getRequestParameterMap().get("hideCourses");        
        //encodeLink("../curricularRules/editCurricularRule.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
        //        .get("degreeCurricularPlanID") + "&curricularRuleID=" + curricularRule.getIdInternal() + organizeBy + hideCourses, "edit");
        //writer.append(" , ");
        encodeLink("../curricularRules/deleteCurricularRule.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&curricularRuleID=" + curricularRule.getIdInternal() + organizeBy + hideCourses, "delete");
        writer.endElement("td");
    }

}
