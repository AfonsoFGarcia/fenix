package net.sourceforge.fenixedu.tools.enrollment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseGroup;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IScientificArea;
import net.sourceforge.fenixedu.domain.precedences.IPrecedence;
import net.sourceforge.fenixedu.domain.precedences.IRestriction;
import net.sourceforge.fenixedu.domain.precedences.RestrictionByNumberOfDoneCurricularCourses;
import net.sourceforge.fenixedu.domain.precedences.RestrictionDoneCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionPeriodToApply;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPrecedence;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.AreaType;
import net.sourceforge.fenixedu.util.BranchType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * @author David Santos in Jan 29, 2004
 */

public class VerifyLEECCurricularPlan {
    public static void main(String args[]) {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentBranch branchDAO = persistentSuport.getIPersistentBranch();
            IPersistentDegreeCurricularPlan degreeCurricularPlanDAO = persistentSuport
                    .getIPersistentDegreeCurricularPlan();

            persistentSuport.iniciarTransaccao();

            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) degreeCurricularPlanDAO
                    .readByOID(DegreeCurricularPlan.class, new Integer("48"));

            List branches = branchDAO.readByDegreeCurricularPlan(degreeCurricularPlan);

            Iterator iterator = branches.iterator();
            while (iterator.hasNext()) {
                IBranch branch = (IBranch) iterator.next();
                if (branch.getBranchType().equals(BranchType.COMMON_BRANCH)) {
                    System.out.println("BASES: [" + branch.getName() + "]");
                    printItForThisAreaWithScopes(branch, AreaType.BASE_OBJ);
                } else {
                    System.out.println("�REA DE ESPECIALIZA��O: [" + branch.getName() + "]");
                    printItForThisAreaWithScopes(branch, AreaType.SPECIALIZATION_OBJ);
                    System.out.println("�REA SECUND�RIA: [" + branch.getName() + "]");
                    printItForThisAreaWithScopes(branch, AreaType.SECONDARY_OBJ);
                }
            }

            printItForPrecedences(degreeCurricularPlan);

            persistentSuport.confirmarTransaccao();
        } catch (Throwable e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * @param branch
     * @param areaType
     * @throws ExcepcaoPersistencia
     */
    private static void printItForThisAreaWithScopes(IBranch branch, AreaType areaType)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
        IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport
                .getIPersistentCurricularCourseGroup();

        List groups = curricularCourseGroupDAO.readByBranchAndAreaType(branch, areaType);
        Iterator iterator1 = groups.iterator();
        while (iterator1.hasNext()) {
            ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) iterator1.next();
            List scientificAreas = curricularCourseGroup.getScientificAreas();
            Iterator iterator2 = scientificAreas.iterator();

            System.out.println("");
            System.out.print("\t");
            System.out.println("GRUPO: [" + curricularCourseGroup.getIdInternal()
                    + "] N� M�NIMO DE CR�DITOS: ["
                    + curricularCourseGroup.getMinimumCredits().toString()
                    + "] N� M�XIMO DE CR�DITOS: ["
                    + curricularCourseGroup.getMaximumCredits().toString() + "]");

            while (iterator2.hasNext()) {
                IScientificArea scientificArea = (IScientificArea) iterator2.next();

                System.out.println("");
                System.out.print("\t\t");
                System.out.println("�REA CIENT�FICA: [" + scientificArea.getName() + "]");

                List curricularCourses = getCurricularCourseScopesByScientificAreaInGroup(
                        curricularCourseGroup, scientificArea);
                sortCurricularCourseScopes(curricularCourses);

                Iterator iterator3 = curricularCourses.iterator();
                while (iterator3.hasNext()) {
                    ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator3
                            .next();
                    if (curricularCourseScope.getEndDate() == null) {
                        String name = curricularCourseScope.getCurricularCourse().getName();
                        String year = curricularCourseScope.getCurricularSemester().getCurricularYear()
                                .getYear().toString();
                        String semester = curricularCourseScope.getCurricularSemester().getSemester()
                                .toString();
                        System.out.print("\t\t\t");
                        System.out.println("NOME: [" + name + "], ANO: [" + year + "], SEMESTRE: ["
                                + semester + "]");
                        //						System.out.println("NOME: [" + name + "], ANO: [" +
                        // year + "], SEMESTRE: ["
                        //                                + semester + "], ID ["
                        //                                +
                        // curricularCourseScope.getCurricularCourse().getIdInternal().toString()
                        //                                + "]");
                    }
                }
            }
        }
        System.out.println("");
        System.out.println("");
    }

    /**
     * @param curricularCourseGroup
     * @param scientificArea
     * @return curricular course scopes list from the group and belonging to
     *         that scientific area.
     */
    private static List getCurricularCourseScopesByScientificAreaInGroup(
            ICurricularCourseGroup curricularCourseGroup, IScientificArea scientificArea) {
        List result = new ArrayList();
        List curricularCourses = curricularCourseGroup.getCurricularCourses();
        Iterator iterator1 = curricularCourses.iterator();
        while (iterator1.hasNext()) {
            ICurricularCourse curricularCourse = (ICurricularCourse) iterator1.next();
            if (curricularCourse.getScientificArea().equals(scientificArea)) {
                Iterator iterator2 = curricularCourse.getScopes().iterator();
                while (iterator2.hasNext()) {
                    ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator2
                            .next();
                    result.add(curricularCourseScope);
                }
            }
        }
        return result;
    }

    /**
     * @param curricularCourseScopes
     */
    private static void sortCurricularCourseScopes(List curricularCourseScopes) {
        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("curricularCourse.name"));
        comparatorChain.addComparator(new BeanComparator("curricularSemester.curricularYear.year"));
        comparatorChain.addComparator(new BeanComparator("curricularSemester.semester"));
        Collections.sort(curricularCourseScopes, comparatorChain);
    }

    /**
     * @param degreeCurricularPlan
     * @throws ExcepcaoPersistencia
     */
    private static void printItForPrecedences(IDegreeCurricularPlan degreeCurricularPlan)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
        IPersistentPrecedence precedenceDAO = persistentSuport.getIPersistentPrecedence();
        IPersistentCurricularCourse curricularCourseDAO = persistentSuport
                .getIPersistentCurricularCourse();

        System.out.println("PRECED�NCIAS:");

        List curricularCourses = curricularCourseDAO
                .readCurricularCoursesByDegreeCurricularPlan(degreeCurricularPlan);
        sortCurricularCourses(curricularCourses);
        Iterator iterator1 = curricularCourses.iterator();
        while (iterator1.hasNext()) {
            ICurricularCourse curricularCourse = (ICurricularCourse) iterator1.next();
            List precedences = precedenceDAO.readByCurricularCourse(curricularCourse);
            if (precedences != null && !precedences.isEmpty()) {
                System.out.print("\t");
                System.out.println("DISCIPLINA: [" + curricularCourse.getName()
                        + "] TEM PRECED�NCIA DE:");
                Iterator iterator2 = precedences.iterator();
                while (iterator2.hasNext()) {
                    IPrecedence precedence = (IPrecedence) iterator2.next();
                    List restrictions = precedence.getRestrictions();
                    Iterator iterator3 = restrictions.iterator();
                    while (iterator3.hasNext()) {
                        IRestriction restriction = (IRestriction) iterator3.next();
                        if (restriction instanceof RestrictionByNumberOfDoneCurricularCourses) {
                            RestrictionByNumberOfDoneCurricularCourses actualRestriction = (RestrictionByNumberOfDoneCurricularCourses) restriction;
                            Integer numberOfDoneCurricularCourses = actualRestriction
                                    .getNumberOfCurricularCourses();
                            System.out.print("\t\t");
                            System.out.println(numberOfDoneCurricularCourses + " disciplinas feitas");
                        } else if (restriction instanceof RestrictionDoneCurricularCourse) {
                            RestrictionDoneCurricularCourse actualRestriction = (RestrictionDoneCurricularCourse) restriction;
                            System.out.print("\t\t");
                            System.out.println(actualRestriction.getPrecedentCurricularCourse()
                                    .getName()
                                    + " feita");
                        } else if (restriction instanceof RestrictionPeriodToApply) {
                            RestrictionPeriodToApply actualRestriction = (RestrictionPeriodToApply) restriction;
                            System.out.print("\t\t");
                            System.out.println("no "
                                    + actualRestriction.getPeriodToApplyRestriction().getValue()
                                    + "� semestre");
                        } else {
                            throw new RuntimeException("RESTRI��O DESCONHECIDA!");
                        }
                        if (iterator3.hasNext()) {
                            System.out.print("\t\t\t");
                            System.out.println("E");
                        }
                    }
                    if (iterator2.hasNext()) {
                        System.out.print("\t\t");
                        System.out.println("OU");
                    }
                }
            }
        }
        System.out.println("");
        System.out.println("");
    }

    /**
     * @param curricularCourseList
     */
    private static void sortCurricularCourses(List curricularCourseList) {
        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("name"));

        Collections.sort(curricularCourseList, comparatorChain);
    }

}