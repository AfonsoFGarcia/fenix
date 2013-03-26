/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.converters;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import pt.ist.fenixWebFramework.renderers.components.converters.ConversionException;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeModuleToEnrolKeyConverter extends Converter {

    @Override
    public Object convert(Class type, Object value) {
        if (value == null) {
            return null;
        }

        final pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter converter =
                new pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter();
        final List<DegreeModuleToEnrol> result = new ArrayList<DegreeModuleToEnrol>();
        final String[] values = (String[]) value;
        for (String key : values) {
            String[] parts = key.split(",");
            if (parts.length < 3) {
                throw new ConversionException("invalid key format: " + key);
            }

            final Context context = (Context) converter.convert(type, parts[0]);
            final CurriculumGroup curriculumGroup = (CurriculumGroup) converter.convert(type, parts[1]);
            final ExecutionSemester executionSemester = (ExecutionSemester) converter.convert(type, parts[2]);
            result.add(new DegreeModuleToEnrol(curriculumGroup, context, executionSemester));
        }

        return result;
    }
}