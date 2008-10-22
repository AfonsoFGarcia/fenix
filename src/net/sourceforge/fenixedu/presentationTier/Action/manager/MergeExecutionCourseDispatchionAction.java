/*
 * Created on 3/Dez/2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.text.Collator;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionPeriodByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadExecutionCoursesByDegreeAndExecutionPeriodId;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ReadDegreeByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadAllExecutionPeriods;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">Jo�o Mota </a> 3/Dez/2003
 * @author Fernanda Quit�rio 17/Dez/2003
 * 
 */
public class MergeExecutionCourseDispatchionAction extends FenixDispatchAction {

    public ActionForward chooseDegreesAndExecutionPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {

	DynaActionForm degreesForm = (DynaActionForm) form;
	Integer sourceDegreeId = (Integer) degreesForm.get("sourceDegreeId");
	Integer destinationDegreeId = (Integer) degreesForm.get("destinationDegreeId");
	Integer executionPeriodId = (Integer) degreesForm.get("executionPeriodId");

	getSourceAndDestinationExecutionCourses(request, sourceDegreeId, destinationDegreeId, executionPeriodId);

	getSourceAndDestinationDegrees(request, sourceDegreeId, destinationDegreeId);

	getExecutionPeriod(request, executionPeriodId);

	return mapping.findForward("chooseExecutionCourses");
    }

    protected void getExecutionPeriod(HttpServletRequest request, Integer executionPeriodId) throws FenixServiceException,
	    FenixFilterException {

	InfoExecutionPeriod infoExecutionPeriod = ReadExecutionPeriodByOID.run(executionPeriodId);

	request.setAttribute("infoExecutionPeriod", infoExecutionPeriod);
    }

    protected void getSourceAndDestinationDegrees(HttpServletRequest request, Integer sourceDegreeId, Integer destinationDegreeId)
	    throws FenixServiceException, FenixFilterException {

	InfoDegree sourceInfoDegree = ReadDegreeByOID.run(sourceDegreeId);
	InfoDegree destinationInfoDegree = ReadDegreeByOID.run(destinationDegreeId);

	request.setAttribute("sourceInfoDegree", sourceInfoDegree);
	request.setAttribute("destinationInfoDegree", destinationInfoDegree);
    }

    protected void getSourceAndDestinationExecutionCourses(HttpServletRequest request, Integer sourceDegreeId,
	    Integer destinationDegreeId, Integer executionPeriodId) throws FenixServiceException, FenixFilterException {

	List destinationExecutionCourses = ReadExecutionCoursesByDegreeAndExecutionPeriodId.run(destinationDegreeId,
		executionPeriodId);
	List sourceExecutionCourses = ReadExecutionCoursesByDegreeAndExecutionPeriodId.run(sourceDegreeId, executionPeriodId);

	Collator collator = Collator.getInstance();
	Collections.sort(destinationExecutionCourses, new BeanComparator("nome", collator));
	Collections.sort(sourceExecutionCourses, new BeanComparator("nome", collator));

	request.setAttribute("sourceExecutionCourses", sourceExecutionCourses);
	request.setAttribute("destinationExecutionCourses", destinationExecutionCourses);
    }

    public ActionForward prepareChooseDegreesAndExecutionPeriod(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	SortedSet<Degree> degrees = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
	degrees.addAll(Degree.readNotEmptyDegrees());

	List executionPeriods = ReadAllExecutionPeriods.run();

	ComparatorChain comparator = new ComparatorChain();
	comparator.addComparator(new BeanComparator("infoExecutionYear.year"), true);
	comparator.addComparator(new BeanComparator("name"), true);
	Collections.sort(executionPeriods, comparator);

	request.setAttribute("sourceDegrees", degrees);
	request.setAttribute("destinationDegrees", degrees);
	request.setAttribute("executionPeriods", executionPeriods);

	return mapping.findForward("chooseDegreesAndExecutionPeriod");
    }

    public ActionForward mergeExecutionCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {

	DynaActionForm mergeExecutionCoursesForm = (DynaActionForm) form;
	Integer sourceExecutionCourseId = (Integer) mergeExecutionCoursesForm.get("sourceExecutionCourseId");
	Integer destinationExecutionCourseId = (Integer) mergeExecutionCoursesForm.get("destinationExecutionCourseId");
	Object[] args = { destinationExecutionCourseId, sourceExecutionCourseId };

	ServiceUtils.executeService("MergeExecutionCourses", args);

	return mapping.findForward("sucess");
    }

}