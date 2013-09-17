/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteCompetenceCourse {

    @Checked("RolePredicates.BOLONHA_MANAGER_PREDICATE")
    @Atomic
    public static void run(final String competenceCourseID) {
        final CompetenceCourse competenceCourse = FenixFramework.getDomainObject(competenceCourseID);
        if (competenceCourse != null) {
            competenceCourse.delete();
        }
    }
}