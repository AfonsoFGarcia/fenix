package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

/**
 * @author tfc130
 */
public class PrepararEscolherContextoFormAction extends FenixContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.execute(mapping, form, request, response);

        HttpSession session = request.getSession(false);
        if (session != null) {
            IUserView userView = SessionUtils.getUserView(request);

            InfoExecutionPeriod infoExecutionPeriod = setExecutionContext(request);

            /* Criar o bean de semestres */
            //			List semestres = new ArrayList();
            //			semestres.add(new LabelValueBean("escolher", ""));
            //			semestres.add(new LabelValueBean("1 �", "1"));
            //			semestres.add(new LabelValueBean("2 �", "2"));
            //			session.setAttribute("semestres", semestres);
            /* Criar o bean de anos curricutares */
            List anosCurriculares = new ArrayList();
            anosCurriculares.add(new LabelValueBean("escolher", ""));
            anosCurriculares.add(new LabelValueBean("1 �", "1"));
            anosCurriculares.add(new LabelValueBean("2 �", "2"));
            anosCurriculares.add(new LabelValueBean("3 �", "3"));
            anosCurriculares.add(new LabelValueBean("4 �", "4"));
            anosCurriculares.add(new LabelValueBean("5 �", "5"));
            request.setAttribute("anosCurriculares", anosCurriculares);

            /* Cria o form bean com as licenciaturas em execucao. */
            Object argsLerLicenciaturas[] = { infoExecutionPeriod.getInfoExecutionYear() };

            List executionDegreeList = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionDegreesByExecutionYear", argsLerLicenciaturas);

            Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

            List licenciaturas = new ArrayList();

            licenciaturas.add(new LabelValueBean("escolher", ""));

            Iterator iterator = executionDegreeList.iterator();

            int index = 0;
            while (iterator.hasNext()) {
                InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
                String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree()
                        .getNome();

                name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso()
                        .toString()
                        + " de " + name;

                name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
                        + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

                licenciaturas.add(new LabelValueBean(name, String.valueOf(index++)));
            }

            request.setAttribute(SessionConstants.INFO_EXECUTION_DEGREE_LIST_KEY, executionDegreeList);

            request.setAttribute("licenciaturas", licenciaturas);

            return mapping.findForward("Sucesso");
        }
        throw new Exception();

    }

    /**
     * Method existencesOfInfoDegree.
     * 
     * @param executionDegreeList
     * @param infoExecutionDegree
     * @return int
     */
    private boolean duplicateInfoDegree(List executionDegreeList, InfoExecutionDegree infoExecutionDegree) {
        InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
        Iterator iterator = executionDegreeList.iterator();

        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
            if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
                    && !(infoExecutionDegree.equals(infoExecutionDegree2)))
                return true;

        }
        return false;
    }

    /**
     * Method setExecutionContext.
     * 
     * @param request
     */
    private InfoExecutionPeriod setExecutionContext(HttpServletRequest request) throws Exception {

        //HttpSession session = request.getSession(false);
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);
        if (infoExecutionPeriod == null) {
            IUserView userView = SessionUtils.getUserView(request);
            infoExecutionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(userView,
                    "ReadCurrentExecutionPeriod", new Object[0]);

            request.setAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY, infoExecutionPeriod);
        }
        return infoExecutionPeriod;
    }

}