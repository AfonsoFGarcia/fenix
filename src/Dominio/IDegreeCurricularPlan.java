package Dominio;

import java.util.Date;

import Util.DegreeCurricularPlanState;

/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public interface IDegreeCurricularPlan {

	public String getName();
	public ICurso getDegree();
	public DegreeCurricularPlanState getState();
	public Date getEndDate();
	public Date getInitialDate();

	public void setName(String name);
	public void setDegree(ICurso curso);
	public void setState(DegreeCurricularPlanState state);
	public void setEndDate(Date endDate);
	public void setInitialDate(Date initialDate);
}