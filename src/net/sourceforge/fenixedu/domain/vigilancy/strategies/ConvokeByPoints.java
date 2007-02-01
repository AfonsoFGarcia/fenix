package net.sourceforge.fenixedu.domain.vigilancy.strategies;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.vigilancy.UnavailableTypes;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.VigilancyWithCredits;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;

import org.apache.commons.collections.comparators.ComparatorChain;

public class ConvokeByPoints extends Strategy {

	public ConvokeByPoints() {
		super();
	}

	public StrategySugestion sugest(List<Vigilant> vigilants, WrittenEvaluation writtenEvaluation) {

		List<Vigilant> teachersSugestion = new ArrayList<Vigilant>();
		List<Vigilant> vigilantSugestion = new ArrayList<Vigilant>();
		Set<Person> incompatiblePersons = new HashSet<Person>();
		List<UnavailableInformation> unavailableVigilants = new ArrayList<UnavailableInformation>();

		if (writtenEvaluation.hasAnyVigilancys()) {
			incompatiblePersons.addAll(getIncompatiblePersons(writtenEvaluation));
		}

		final List<ExecutionCourse> executionCourses = writtenEvaluation.getAssociatedExecutionCourses();
	
		for (Vigilant vigilant : vigilants) {

			if (vigilant.canBeConvokedForWrittenEvaluation(writtenEvaluation)
					&& !incompatiblePersons.contains(vigilant.getIncompatiblePerson())) {
				Teacher teacher = vigilant.getTeacher();
				if (teacher != null && teacher.teachesAny(executionCourses)) {
					teachersSugestion.add(vigilant);
					incompatiblePersons.add(vigilant.getPerson());
				} else {
					vigilantSugestion.add(vigilant);
				}

			} else {
				if (!vigilantIsAlreadyConvokedForThisExam(vigilant, writtenEvaluation)) {
					UnavailableTypes reason;
					if (incompatiblePersons.contains(vigilant.getIncompatiblePerson())) {
						reason = UnavailableTypes.INCOMPATIBLE_PERSON;
					} else {
						reason = vigilant.getWhyIsUnavailabeFor(writtenEvaluation);
					}
					unavailableVigilants.add(new UnavailableInformation(vigilant, reason));

				}
			}
		}

		ComparatorChain comparator = new ComparatorChain();
		comparator.addComparator(new PointsComparator());
		//comparator.addComparator(new ConvokeComparator());
		comparator.addComparator(Vigilant.CATEGORY_COMPARATOR);
		comparator.addComparator(Vigilant.USERNAME_COMPARATOR);

		Collections.sort(vigilantSugestion, comparator);
		Collections.sort(teachersSugestion, comparator);
		return new StrategySugestion(teachersSugestion, vigilantSugestion, unavailableVigilants);
	}

	private boolean vigilantIsAlreadyConvokedForThisExam(Vigilant vigilant,
			WrittenEvaluation writtenEvaluation) {
		List<Vigilancy> convokes = vigilant.getVigilancys();
		for (Vigilancy convoke : convokes) {
			if (convoke.getWrittenEvaluation().equals(writtenEvaluation))
				return true;
		}
		return false;
	}

	private List<Person> getIncompatiblePersons(WrittenEvaluation writtenEvaluation) {
		List<Vigilancy> convokes = writtenEvaluation.getVigilancys();
		List<Person> people = new ArrayList<Person>();
		for (Vigilancy convoke : convokes) {
			Vigilant vigilant = convoke.getVigilant();
			people.add(vigilant.getPerson());
		}
		return people;
	}

	class PointsComparator implements Comparator<Vigilant> {

		public int compare(Vigilant v1, Vigilant v2) {
			return getPointsUsingCriteria(v1).compareTo(getPointsUsingCriteria(v2));
		}
		
		private Double getPointsUsingCriteria(Vigilant vigilant) {
		
			double points = vigilant.getStartPoints();
			BigDecimal weight = vigilant.getPointsWeight();
			
			for(VigilancyWithCredits vigilancy : vigilant.getActiveVigilancyWithCredits()) {
				points += vigilancy.hasPointsAttributed() ? vigilancy.getPoints() * weight.doubleValue() : 1;
			}
			return points;
		}
	}
	
	class ConvokeComparator implements Comparator<Vigilant> {

		public int compare(Vigilant v1, Vigilant v2) {
			return v1.getActiveVigilancyWithCreditsCount().compareTo(v2.getActiveVigilancyWithCreditsCount());
		}
		
	}
}
