/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.domain.projectsManagement;

import java.io.Serializable;

/**
 * @author Susana Fernandes
 * 
 */
public class CabimentosReportLine implements Serializable, ICabimentosReportLine {

    private String projectCode;

    private Double cabimentos;

    private Double justifications;

    private Double total;

    public Double getCabimentos() {
	return cabimentos;
    }

    public void setCabimentos(Double cabimentos) {
	this.cabimentos = cabimentos;
    }

    public Double getJustifications() {
	return justifications;
    }

    public void setJustifications(Double justifications) {
	this.justifications = justifications;
    }

    public String getProjectCode() {
	return projectCode;
    }

    public void setProjectCode(String projectCode) {
	this.projectCode = projectCode;
    }

    public Double getTotal() {
	return total;
    }

    public void setTotal(Double total) {
	this.total = total;
    }
}
