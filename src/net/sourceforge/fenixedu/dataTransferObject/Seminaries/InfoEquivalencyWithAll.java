package net.sourceforge.fenixedu.dataTransferObject.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency;
import net.sourceforge.fenixedu.domain.Seminaries.Theme;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Fernanda Quitério
 * 
 * 
 *         Created at 25/Jun/2004
 * 
 */
public class InfoEquivalencyWithAll extends InfoEquivalency {
	@Override
	public void copyFromDomain(CourseEquivalency courseEquivalency) {
		super.copyFromDomain(courseEquivalency);
		if (courseEquivalency != null) {
			setCurricularCourse(InfoCurricularCourse.newInfoFromDomain(courseEquivalency.getCurricularCourse()));
			setModality(InfoModality.newInfoFromDomain(courseEquivalency.getModality()));
			setThemes((List) CollectionUtils.collect(courseEquivalency.getThemes(), new Transformer() {

				@Override
				public Object transform(Object arg0) {
					return InfoTheme.newInfoFromDomain((Theme) arg0);
				}
			}));
		}
	}

	public static InfoEquivalency newInfoFromDomain(CourseEquivalency courseEquivalency) {
		InfoEquivalencyWithAll infoEquivalency = null;
		if (courseEquivalency != null) {
			infoEquivalency = new InfoEquivalencyWithAll();
			infoEquivalency.copyFromDomain(courseEquivalency);
		}
		return infoEquivalency;
	}
}