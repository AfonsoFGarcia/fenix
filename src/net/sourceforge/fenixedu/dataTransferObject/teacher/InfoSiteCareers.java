/*
 * Created on Nov 13, 2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.CareerType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class InfoSiteCareers extends DataTranferObject implements ISiteComponent {

	private List infoCareers;

	private CareerType careerType;

	private InfoTeacher infoTeacher;

	public InfoSiteCareers() {
	}

	/**
	 * @return Returns the careerType.
	 */
	public CareerType getCareerType() {
		return careerType;
	}

	/**
	 * @param careerType
	 *            The careerType to set.
	 */
	public void setCareerType(CareerType careerType) {
		this.careerType = careerType;
	}

	/**
	 * @return Returns the infoCareers.
	 */
	public List getInfoCareers() {
		return infoCareers;
	}

	/**
	 * @param infoCareers
	 *            The infoCareers to set.
	 */
	public void setInfoCareers(List infoCareers) {
		this.infoCareers = infoCareers;
	}

	/**
	 * @return Returns the infoTeacher.
	 */
	public InfoTeacher getInfoTeacher() {
		return infoTeacher;
	}

	/**
	 * @param infoTeacher
	 *            The infoTeacher to set.
	 */
	public void setInfoTeacher(InfoTeacher infoTeacher) {
		this.infoTeacher = infoTeacher;
	}

}