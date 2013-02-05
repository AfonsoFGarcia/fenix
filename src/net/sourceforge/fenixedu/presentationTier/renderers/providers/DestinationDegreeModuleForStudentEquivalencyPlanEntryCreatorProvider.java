package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan.StudentEquivalencyPlanEntryCreator;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DestinationDegreeModuleForStudentEquivalencyPlanEntryCreatorProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final StudentEquivalencyPlanEntryCreator studentEquivalencyPlanEntryCreator = (StudentEquivalencyPlanEntryCreator) source;
        final DegreeCurricularPlanEquivalencePlan equivalencePlan =
                studentEquivalencyPlanEntryCreator.getDegreeCurricularPlanEquivalencePlan();
        final DegreeCurricularPlan degreeCurricularPlan = equivalencePlan.getDegreeCurricularPlan();
        final Set<DegreeModule> degreeModules = degreeCurricularPlan.getAllDegreeModules();
        return degreeModules;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
