/*
 * Created on Jan 11, 2005
 *
 */
package DataBeans.projectsManagement;

import org.apache.struts.util.LabelValueBean;

import DataBeans.DataTranferObject;
import Dominio.projectsManagement.IProject;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoProject extends DataTranferObject {
    private String projectCode;

    private String title;

    private Integer coordinatorCode;

    private String coordinatorName;

    private String origin;

    private LabelValueBean type;

    private String cost;

    private String coordination;

    private Integer explorationUnit;

    public String getCoordination() {
        return coordination;
    }

    public void setCoordination(String coordination) {
        this.coordination = coordination;
    }

    public String getCoordinatorName() {
        return coordinatorName;
    }

    public void setCoordinatorName(String coordinatorName) {
        this.coordinatorName = coordinatorName;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public Integer getExplorationUnit() {
        return explorationUnit;
    }

    public void setExplorationUnit(Integer explorationUnit) {
        this.explorationUnit = explorationUnit;
    }

    public Integer getCoordinatorCode() {
        return coordinatorCode;
    }

    public void setCoordinatorCode(Integer coordinatorCode) {
        this.coordinatorCode = coordinatorCode;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LabelValueBean getType() {
        return type;
    }

    public void setType(LabelValueBean type) {
        this.type = type;
    }

    public String getProjectIdentification() {
        return this.explorationUnit + this.origin + this.type.getLabel() + this.cost + this.coordination + this.getFourDigitsProjectCode();
    }

    public String getFourDigitsProjectCode() {
        if (projectCode.length() == 1) {
            return "000" + projectCode;
        }
        if (projectCode.length() == 2) {
            return "00" + projectCode;

        }
        if (projectCode.length() == 3) {
            return "0" + projectCode;
        }
        return projectCode;
    }

    public boolean equals(Object obj) {
        if ((obj != null) && (obj instanceof InfoProject))
            return (projectCode != null) && projectCode.equals(((InfoProject) obj).getProjectCode());
        return false;
    }

    public void copyFromDomain(IProject project) {
        if (project != null) {
            setCoordination(project.getCoordination());
            setCoordinatorCode(project.getCoordinatorCode());
            setCoordinatorName(project.getCoordinatorName());
            setCost(project.getCost());
            setExplorationUnit(project.getExplorationUnit());
            setOrigin(project.getOrigin());
            setProjectCode(project.getProjectCode());
            setTitle(project.getTitle());
            setType(project.getType());
        }
    }

    public static InfoProject newInfoFromDomain(IProject project) {
        InfoProject infoProject = null;
        if (project != null) {
            infoProject = new InfoProject();
            infoProject.copyFromDomain(project);
        }
        return infoProject;
    }
}
