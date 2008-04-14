package net.sourceforge.fenixedu.domain.assiduousness.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.Meal;
import net.sourceforge.fenixedu.domain.assiduousness.WorkScheduleType;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class Timeline {

    private List<TimePoint> timePoints;

    public Timeline(WorkScheduleType workScheduleType) {
	timePoints = new ArrayList<TimePoint>();
	timePoints.add(new TimePoint(workScheduleType.getWorkTime(), AttributeType.NULL, AttributeType.NULL));
	timePoints
		.add(new TimePoint(workScheduleType.getWorkEndTime(), workScheduleType.isWorkTimeNextDay(), AttributeType.NULL));

	List<TimePoint> pointList = new ArrayList<TimePoint>();
	pointList.addAll((workScheduleType.getNormalWorkPeriod()).toTimePoints(AttributeType.NORMAL_WORK_PERIOD_1,
		AttributeType.NORMAL_WORK_PERIOD_2));
	if (workScheduleType.definedFixedPeriod()) {
	    pointList.addAll((workScheduleType.getFixedWorkPeriod()).toTimePoints(AttributeType.FIXED_PERIOD_1,
		    AttributeType.FIXED_PERIOD_2));
	}
	if (workScheduleType.definedMeal()) {
	    pointList.addAll(((Meal) workScheduleType.getMeal()).toTimePoints());
	}
	plotList(pointList);
    }

    public Timeline(YearMonthDay day, DateTime firstClocking, DateTime lastClocking) {
	timePoints = new ArrayList<TimePoint>();
	timePoints.add(new TimePoint(new TimeOfDay(firstClocking.toTimeOfDay()), AttributeType.NULL, AttributeType.NULL));
	timePoints.add(new TimePoint(lastClocking.toTimeOfDay(), day.equals(lastClocking.toYearMonthDay()) ? false : true,
		AttributeType.NULL));
    }

    public List<TimePoint> getTimePoints() {
	return timePoints;
    }

    public void setTimePoints(List<TimePoint> newTimePoints) {
	timePoints = newTimePoints;
    }

    public int getNumberOfTimePoints() {
	return getTimePoints().size();
    }

    public TimePoint getTimeLinePosition(int position) {
	return getTimePoints().get(position);
    }

    // To add directly a point to the timeline - used ONLY by addPoint
    private void addPoint(int position, TimePoint timePoint) {
	getTimePoints().add(position, timePoint);
    }

    public void plotList(List<TimePoint> pointList) {
	for (int i = 0; i < pointList.size(); i++) {
	    plotPoint(pointList.get(i), i);
	}
    }

    // Adds a point to the time line, sets the openIntervals accordingly
    // TODO must refactor! it's confusing and has some dups...
    public void plotPoint(TimePoint newPoint, int workedPoints) {
	int timeLineSize = getNumberOfTimePoints();
	Attributes insideIntervals = new Attributes();
	for (int i = 0; i < timeLineSize; i++) {
	    TimePoint currentPoint = getTimeLinePosition(i);
	    if (newPoint.isAtSameTime(currentPoint)) {
		for (AttributeType attributeType : newPoint.getPointAttributes().getAttributes()) {
		    if (insideIntervals.contains(attributeType)) {
			pointClosesInterval(insideIntervals, attributeType, i);
			currentPoint.getPointAttributes().addAttribute(attributeType);
		    } else {
			pointOpensInterval(insideIntervals, attributeType, i);
			// insideIntervals.addAttributes(currentPoint.getIntervalAttributes()
			// .getAttributes());
			currentPoint.getPointAttributes().addAttribute(attributeType);
		    }
		}
		return;
	    } else if (newPoint.isBefore(currentPoint)) {
		addPoint(i, newPoint);
		// adds the point in this position and shifts the current point
		// to the next position
		// (i+1) se attrib ponto do novo ponto estao em intervalos
		// abertos => intervalo vai-se fechar
		for (AttributeType attributeType : newPoint.getPointAttributes().getAttributes()) {
		    if (insideIntervals.contains(attributeType)) {
			pointClosesInterval(insideIntervals, attributeType, i);
		    } else {
			pointOpensInterval(insideIntervals, attributeType, i);
		    }
		}
		return; // get off the for loop
	    } else {
		// is after caso em q o intervalo ja esta' aberto faz o update
		// do atributo no ponto
		for (AttributeType attribute : currentPoint.getPointAttributes().getAttributes()) {
		    if (insideIntervals.contains(attribute)) {
			newPoint.getIntervalAttributes().removeAttribute(attribute);
			// removes the current point attribute to new point
			// interval attributes
			insideIntervals.removeAttribute(attribute);
		    } else {
			if (!newPoint.getIntervalAttributes().contains(AttributeType.JUSTIFICATION)
				&& newPoint.getPointAttributes().contains(DomainConstants.WORKED_ATTRIBUTES)
				&& attribute.equals(AttributeType.MEAL) && workedPoints % 2 == 0 && workedPoints != 0) {
			    TimePoint mealStart = findIntervalStartPointByAttribute(AttributeType.MEAL);
			    TimePoint newMealStart = findStartLunchBreak(null);
			    TimePoint mealEnd = findIntervalEndPointByAttribute(AttributeType.MEAL);
			    if (!mealStart.getPointAttributes().contains(DomainConstants.WORKED_ATTRIBUTES)
				    && newMealStart != null && newMealStart.isBefore(mealStart)
				    && !isPointStartingAttributeInterval(newMealStart, AttributeType.MEAL)) {
				int newMealIndex = getTimePointIndex(newMealStart);
				if (newMealIndex != -1) {
				    newMealStart.getPointAttributes().addAttribute(AttributeType.MEAL);
				    newMealStart.getIntervalAttributes().addAttribute(AttributeType.MEAL);
				    addIntervalAttributeToNextPoints(newMealIndex, new Attributes(AttributeType.MEAL));
				}
			    }
			    if (mealEnd != null) {
				TimePoint pointBeforeMeal = findWorkedPointBefore(newPoint);
				if (!mealEnd.getPointAttributes().contains(DomainConstants.WORKED_ATTRIBUTES)
					&& pointBeforeMeal != null
					&& pointBeforeMeal.getIntervalAttributes().contains(AttributeType.MEAL)) {
				    int oldMealIndex = getTimePointIndex(mealEnd);
				    if (oldMealIndex != -1) {
					mealEnd.getPointAttributes().removeAttribute(attribute);
					addIntervalAttributeToNextPoints(oldMealIndex, new Attributes(AttributeType.MEAL));
				    }
				    newPoint.getPointAttributes().addAttribute(attribute);
				    insideIntervals.addAttribute(attribute);
				}
			    } else {
				newPoint.getPointAttributes().addAttribute(attribute);
				insideIntervals.addAttribute(attribute);
			    }
			} else {
			    newPoint.getIntervalAttributes().addAttribute(attribute);
			    // adds the current point attribute to new point
			    // interval attributes
			    insideIntervals.addAttribute(attribute);
			}
		    }
		}
	    }
	}
	TimePoint lastTimelinePoint = getTimeLinePosition(timeLineSize - 1);
	// the newpoint is after the last point in the timeline
	newPoint.setTime(lastTimelinePoint.getTime());
	newPoint.setNextDay(lastTimelinePoint.isNextDay());
	addPoint(timeLineSize - 1, newPoint);
	// adds the point in this position and shifts the current point to the
	// next position
	// (i+1) se attrib ponto do novo ponto estao em intervalos abertos =>
	// intervalo vai-se fechar
	if (insideIntervals.contains(newPoint.getPointAttributes())) {
	    // inserted point only has 1 attribute
	    pointClosesInterval(insideIntervals, newPoint, timeLineSize - 1);
	} else { // novo intervalo
	    pointOpensInterval(insideIntervals, newPoint, timeLineSize - 1);
	}
    }

    private int getTimePointIndex(TimePoint timePoint) {
	for (int i = 0; i < timePoints.size(); i++) {
	    TimePoint timePointList = timePoints.get(i);
	    if (timePointList.equals(timePoint)) {
		return i;
	    }
	}
	return -1;
    }

    // Removes an attribute from all timeline's points starting from
    // startPosition part of plotPoint
    private void removeIntervalAttributeFromNextPoints(int startPosition, Attributes attributes) {
	int timeLineSize = getNumberOfTimePoints();
	for (int i = startPosition; i < timeLineSize; i++) {
	    TimePoint currentPoint = getTimeLinePosition(i);
	    currentPoint.getIntervalAttributes().removeAttributes(attributes.getAttributes());
	}
    }

    // Adds an attribute to all timeline points' interval starting from
    // startPosition. the timeline's last point is excluded 'cause it's the
    // end
    // of all 1 point intervals part of plotPoint
    private void addIntervalAttributeToNextPoints(int startPosition, Attributes attributes) {
	int timeLineSize = getNumberOfTimePoints() - 1; // exclude the last
	// timeline point
	for (int i = startPosition; i < timeLineSize; i++) {
	    TimePoint currentPoint = getTimeLinePosition(i);
	    currentPoint.getIntervalAttributes().addAttributes(attributes.getAttributes());
	}
    }

    // As the point closes the interval, this method removes the point's
    // attribute from the open intervals set and from all subsequent point's
    // attribute sets part of plotPoint
    private void pointClosesInterval(Attributes insideIntervals, TimePoint point, int position) {
	insideIntervals.removeAttributes(point.getPointAttributes().getAttributes()); // remove
	// the attribute from the openIntervals set
	removeIntervalAttributeFromNextPoints(position, point.getPointAttributes()); // remove
	// the attribute from this and the next points
    }

    private void pointClosesInterval(Attributes insideIntervals, AttributeType attributeType, int position) {
	insideIntervals.removeAttributes(new Attributes(attributeType).getAttributes()); // remove
	// the attribute from the openIntervals set
	removeIntervalAttributeFromNextPoints(position, new Attributes(attributeType)); // remove
	// the attribute from this and the next points
    }

    // As the point opens the interval, this method adds the point's
    // attribute
    // to the open intervals set and to all subsequent point's attribute
    // sets
    // part of plotPoint
    private void pointOpensInterval(Attributes insideIntervals, TimePoint point, int position) {
	insideIntervals.addAttributes(point.getPointAttributes().getAttributes()); // adds
	// the point's attribute to the openInterval set
	addIntervalAttributeToNextPoints(position, point.getPointAttributes()); // adds
	// the point's attribute to this and the rest of the following points
    }

    private void pointOpensInterval(Attributes insideIntervals, AttributeType attributeType, int position) {
	insideIntervals.addAttributes(new Attributes(attributeType).getAttributes()); // adds
	// the point's attribute to the openInterval set
	addIntervalAttributeToNextPoints(position, new Attributes(attributeType)); // adds
	// the point's attribute to this and the rest of the following points
    }

    // Finds the start point of a worked interval before a given TimePoint.
    // The
    // start point must have worked as point attributes and worked must be
    // in
    // its interval attributes
    private TimePoint findWorkedStartPointBetweenPoints(TimePoint startPoint, TimePoint endPoint) {
	int startPosition = getTimePoints().indexOf(startPoint);
	int timelineSize = getNumberOfTimePoints();
	for (int i = startPosition; i < timelineSize; i++) {
	    TimePoint point = getTimeLinePosition(i);
	    // worked is never overlapped to another worked, so if it's a
	    // worked
	    // point it's unique
	    AttributeType pointWorkedAttribute = point.getPointAttributes().intersects(DomainConstants.WORKED_ATTRIBUTES);
	    if (pointWorkedAttribute != null) {
		if (isPointStartingAttributeInterval(point, pointWorkedAttribute)
			&& (point.isBefore(endPoint) || point.isAtSameTime(endPoint))) {
		    return point;
		}
	    }
	}
	return null;
    }

    // Finds the start point of the interval. The start point must have
    // attribute as point attributes and attribute must be in its interval
    // attributes
    private TimePoint findIntervalStartPointByAttribute(AttributeType attribute) {
	for (TimePoint point : getTimePoints()) {
	    if (isPointStartingAttributeInterval(point, attribute)) {
		return point;
	    }
	}
	return null;
    }

    // Finds the end point of the interval. The end point must have
    // attribute as
    // point attributes and attribute must not be in its interval attributes
    private TimePoint findIntervalEndPointByAttribute(AttributeType attribute) {
	for (TimePoint point : getTimePoints()) {
	    if (isPointClosingAttributeInterval(point, attribute)) {
		return point;
	    }
	}
	return null;
    }

    public boolean isOpeningAndNotClosingWorkedPeriod(TimePoint point) {
	boolean isPointEndingAnyWorkedPeriod = isPointEndingAnyWorkedPeriod(point);
	for (AttributeType attributeType : point.getPointAttributes().getAttributes()) {
	    if (DomainConstants.WORKED_ATTRIBUTES.contains(attributeType)
		    && point.getIntervalAttributes().contains(attributeType) && !isPointEndingAnyWorkedPeriod) {
		return true;
	    }
	}
	return false;
    }

    public boolean isClosingAndNotOpeningWorkedPeriod(TimePoint point) {
	boolean isPointStartingAnyWorkedPeriod = isPointStartingAnyWorkedPeriod(point);
	for (AttributeType attributeType : point.getPointAttributes().getAttributes()) {
	    if (DomainConstants.WORKED_ATTRIBUTES.contains(attributeType)
		    && !point.getIntervalAttributes().contains(attributeType) && !isPointStartingAnyWorkedPeriod) {
		return true;
	    }
	}
	return false;
    }

    // Finds the end point of the interval before a given TimePoint. The end
    // point must have attribute as point attributes and attribute must not
    // be
    // in its interval attributes
    private TimePoint findIntervalEndPointBetweenPointsByAttribute(TimePoint startPoint, TimePoint endPoint,
	    AttributeType attribute) {
	int startPosition = getTimePoints().indexOf(startPoint);
	int timelineSize = getNumberOfTimePoints();
	for (int i = startPosition; i < timelineSize; i++) {
	    TimePoint point = getTimeLinePosition(i);
	    if (isPointClosingAttributeInterval(point, attribute) && (point.isBefore(endPoint) || point.isAtSameTime(endPoint))) {
		return point;
	    }
	}
	return null;
    }

    // Returns an TimeInterval of the specified attribute BEWARE that the
    // TimeInterval doesn't have information about the attribute used in
    // calculateAttributesDuration since we need to build and interval to
    // get
    // its duration
    private TimePoint[] findWorkedIntervalByAttribute(AttributeType attribute) {
	List<TimePoint> listTimePoint = findFirstTimePointsByAttribute(attribute);
	if (listTimePoint.size() == 2) {
	    return new TimePoint[] { listTimePoint.get(0), listTimePoint.get(1) };
	}
	return null;
    }

    private List<TimePoint> findFirstTimePointsByAttribute(AttributeType attribute) {
	List<TimePoint> timePointList = new ArrayList<TimePoint>();
	TimePoint startPoint = findIntervalStartPointByAttribute(attribute);
	TimePoint endPoint = findIntervalEndPointByAttribute(attribute);
	if ((startPoint != null) && (endPoint != null)) {
	    timePointList.add(startPoint);
	    timePointList.add(endPoint);
	}
	return timePointList;
    }

    private boolean isPointStartingAnyWorkedPeriod(TimePoint point) {
	for (AttributeType attributeType : DomainConstants.WORKED_ATTRIBUTES.getAttributes()) {
	    if (isPointStartingAttributeInterval(point, attributeType)) {
		return true;
	    }
	}
	return false;
    }

    private boolean isPointEndingAnyWorkedPeriod(TimePoint point) {
	for (AttributeType attributeType : DomainConstants.WORKED_ATTRIBUTES.getAttributes()) {
	    if (isPointClosingAttributeInterval(point, attributeType)) {
		return true;
	    }
	}
	return false;
    }

    // A point starts an AttributeType attribute interval if its attribute
    // contains attribute and if the interval attributes contains attribute
    private boolean isPointStartingAttributeInterval(TimePoint point, AttributeType attribute) {
	return (point.getPointAttributes().contains(attribute) && point.getIntervalAttributes().contains(attribute));
    }

    // A point closes an AttributeType attribute interval if its attribute
    // contains attribute and if the interval attributes does not contain
    // attribute
    private boolean isPointClosingAttributeInterval(TimePoint point, AttributeType attribute) {
	return (point.getPointAttributes().contains(attribute) && (point.getIntervalAttributes().contains(attribute) == false));
    }

    // Finds which attributes overlap one interval
    public Attributes findAttributesIntervalThatOverlapFromAttributes(AttributeType attribute, Attributes attributesToCheck) {
	Attributes overlappedAttributes = new Attributes();
	for (TimePoint point : getTimePoints()) {
	    // ou o ponto tem o atributo ou o intervalo tem o atributo (caso
	    // em
	    // que o ponto pertence ao intervalo do attribute)
	    if (point.getPointAttributes().contains(attribute) || point.getIntervalAttributes().contains(attribute)) {
		for (AttributeType attributeCheck : attributesToCheck.getAttributes()) {
		    if (point.hasAttributes(attribute, attributeCheck)) {
			overlappedAttributes.addAttribute(attributeCheck);
		    }
		}
	    }
	}
	return overlappedAttributes;
    }

    public boolean areIntervalsByAttributeOverlapped(AttributeType attribute1, AttributeType attribute2) {
	TimePoint overlappedPoint = null;
	for (TimePoint point : getTimePoints()) {
	    if (point.hasAttributes(attribute1, attribute2)) {
		overlappedPoint = point;
	    }
	}
	if (overlappedPoint != null) {
	    return true;
	}
	return false;
    }

    // Checks if the attribute interval and attributes are overlapped.
    public boolean areIntervalsByAttributeOverlapped(AttributeType attribute1, Attributes attributes) {
	TimePoint overlappedPoint = null;
	for (TimePoint point : getTimePoints()) {
	    if (point.hasAttributes(attribute1, attributes)) {
		overlappedPoint = point;
	    }
	}
	if (overlappedPoint != null) {
	    return true;
	}
	return false;
    }

    // Calculates the duration of a List<TimePoint>
    public Duration calculateDurationPointList(List<TimePoint> pointList) {
	Duration totalDuration = Duration.ZERO;

	Iterator<TimePoint> pointListIt = pointList.iterator();
	while (pointListIt.hasNext()) {
	    TimePoint point = pointListIt.next();
	    TimePoint point2 = null;
	    do {
		if (point2 != null) {
		    point = point2;
		}
		if (pointListIt.hasNext()) {
		    point2 = pointListIt.next();
		    totalDuration = totalDuration.plus(new TimeInterval(point.getTime(), point2.getTime(), point2.isNextDay())
			    .getDuration());
		} else {
		    return totalDuration;
		}
	    } while (countNumberOfWorkedAttributes(point2) > 1 || (countNumberOfWorkedAttributesInInterval(point2) > 1)
		    || (point2.getPointAttributes().contains(AttributeType.JUSTIFICATION)));
	}
	return totalDuration;
    }

    private int countNumberOfWorkedAttributes(TimePoint point2) {
	int result = 0;
	for (AttributeType attributeType : point2.getPointAttributes().getAttributes()) {
	    if (DomainConstants.WORKED_ATTRIBUTES.getAttributes().contains(attributeType)) {
		result++;
	    }
	}
	return result;
    }

    private int countNumberOfWorkedAttributesInInterval(TimePoint point2) {
	int result = 0;
	for (AttributeType attributeType : point2.getIntervalAttributes().getAttributes()) {
	    if (DomainConstants.WORKED_ATTRIBUTES.getAttributes().contains(attributeType)) {
		result++;
	    }
	}
	return result;
    }

    public Duration calculateWorkPeriodDuration(TimePoint firstTimePoint, TimePoint lastTimePoint,
	    TimePoint minimumClockTimePoint, TimePoint maximumClockTimePoint, Duration maximumDuration,
	    WorkScheduleType workScheduleType) {
	Duration totalDuration = Duration.ZERO;
	Duration temp = Duration.ZERO;
	List<Interval> timePointIntervals = new ArrayList<Interval>();
	List<Interval> justificationsIntervals = new ArrayList<Interval>();

	for (AttributeType attributeType : DomainConstants.WORKED_ATTRIBUTES.getAttributes()) {
	    TimePoint[] timePoints = findWorkedIntervalByAttribute(attributeType);
	    if (timePoints != null) {
		if ((firstTimePoint != null && (timePoints[1].isBefore(firstTimePoint) || firstTimePoint
			.isAtSameTime(timePoints[1])))
			|| (lastTimePoint != null && (lastTimePoint.isBefore(timePoints[0]) || lastTimePoint
				.isAtSameTime(timePoints[0])))) {
		    DateTime begin = timePoints[0].getTime().toDateTimeToday();
		    DateTime end = timePoints[1].getTime().toDateTimeToday();
		    if (timePoints[1].isNextDay()) {
			end = end.plusDays(1);
		    }
		    Interval timePointInterval = new Interval(begin, end);
		    if (timePoints[0].getPointAttributes().contains(AttributeType.JUSTIFICATION)
			    && timePoints[1].getPointAttributes().contains(AttributeType.JUSTIFICATION)) {
			justificationsIntervals.add(timePointInterval);

		    } else {
			timePointIntervals.add(timePointInterval);
		    }
		    if (workScheduleType != null
			    && workScheduleType.getScheduleClockingType().equals(ScheduleClockingType.OLD_RIGID_CLOCKING)) {
			Interval overlap = timePointInterval.overlap(workScheduleType.getNormalWorkPeriod()
				.getFirstPeriodInterval().toInterval(begin));
			if (overlap != null) {
			    temp = overlap.toDuration();
			}
			if (workScheduleType.getNormalWorkPeriod().isSecondWorkPeriodDefined()) {
			    overlap = timePointInterval.overlap(workScheduleType.getNormalWorkPeriod().getSecondPeriodInterval()
				    .toInterval(begin));
			    if (overlap != null) {
				temp = temp.plus(overlap.toDuration());
			    }
			}
		    } else {
			temp = getDurationBetweenPoints(timePoints[0], timePoints[1], minimumClockTimePoint,
				maximumClockTimePoint);
		    }
		    if (!(timePoints[0].getPointAttributes().contains(AttributeType.JUSTIFICATION) && timePoints[1]
			    .getPointAttributes().contains(AttributeType.JUSTIFICATION))
			    && (maximumDuration != null && (temp.isLongerThan(maximumDuration)))) {
			totalDuration = totalDuration.plus(maximumDuration);
		    } else {
			totalDuration = totalDuration.plus(temp);
		    }
		}
	    }
	}
	for (Interval justificationInterval : justificationsIntervals) {
	    for (Interval workedInterval : timePointIntervals) {
		Interval overlap = justificationInterval.overlap(workedInterval);
		if (overlap != null) {
		    totalDuration = totalDuration.minus(overlap.toDuration());
		}
	    }
	}
	return totalDuration;
    }

    private Duration getDurationBetweenPoints(TimePoint firstTimePoint, TimePoint lastTimePoint, TimePoint minimumClockTimePoint,
	    TimePoint maximumClockTimePoint) {
	DateTime startDate = firstTimePoint.getTime().toDateTimeToday();
	if (firstTimePoint.isBefore(minimumClockTimePoint)) {
	    startDate = minimumClockTimePoint.getTime().toDateTimeToday();
	    firstTimePoint = minimumClockTimePoint;
	} else if (maximumClockTimePoint.isBefore(firstTimePoint)) {
	    startDate = maximumClockTimePoint.getTime().toDateTimeToday();
	    firstTimePoint = maximumClockTimePoint;
	}

	DateTime endDate = lastTimePoint.getTime().toDateTimeToday();
	if (lastTimePoint.isBefore(minimumClockTimePoint)) {
	    endDate = minimumClockTimePoint.getTime().toDateTimeToday();
	    lastTimePoint = minimumClockTimePoint;
	} else if (maximumClockTimePoint.isBefore(lastTimePoint)) {
	    endDate = maximumClockTimePoint.getTime().toDateTimeToday();
	    lastTimePoint = maximumClockTimePoint;
	}

	if (firstTimePoint.isNextDay() != lastTimePoint.isNextDay()) {
	    endDate = endDate.plusDays(1);
	}

	return new Duration(startDate, endDate);
    }

    public Duration calculateWorkPeriodDurationBetweenDates(DateTime beginDate, DateTime endDate) {
	Duration totalDuration = Duration.ZERO;
	Interval interval = new Interval(beginDate, endDate);
	for (AttributeType attributeType : DomainConstants.WORKED_ATTRIBUTES.getAttributes()) {
	    TimePoint[] timePoints = findWorkedIntervalByAttribute(attributeType);
	    if (timePoints != null) {
		DateTime end = timePoints[1].getTime().toDateTime(beginDate);
		if (timePoints[1].isNextDay()) {
		    end = end.plusDays(1);
		}
		Interval workedInterval = new Interval(timePoints[0].getTime().toDateTime(beginDate), end);
		Interval overlap = interval.overlap(workedInterval);
		if (overlap != null) {
		    totalDuration = totalDuration.plus(overlap.toDuration());
		}
	    }
	}
	return totalDuration;
    }

    // Returns a list will all points that contain the specified attributes
    public List<TimePoint> getAllAttributePoints(Attributes attributes) {
	List<TimePoint> pointList = new ArrayList<TimePoint>();
	for (TimePoint point : getTimePoints()) {
	    if (point.getPointAttributes().contains(attributes)) {
		pointList.add(point);
	    }
	}
	return pointList;
    }

    //
    // METODOS ESPECIFICOS PARA A ASSIDUIDADE
    //

    public Duration calculateFixedPeriod(AttributeType fixedPeriodAttribute) {
	List<TimePoint> pointList = new ArrayList<TimePoint>();
	for (TimePoint point : getTimePoints()) {
	    AttributeType workedAttribute = point.getWorkedAttribute(fixedPeriodAttribute);
	    if (workedAttribute != null && findIntervalEndPointByAttribute(workedAttribute) != null) {
		pointList.add(point);
	    }
	}
	return this.calculateDurationPointList(pointList);
    }

    // Calcula o intervalo de refeicao feito pelo funcionario
    public TimeInterval calculateMealBreakInterval(TimeInterval scheduleMealBreakInterval) {
	if (getNumberOfWorkPoints() <= 2) {
	    return null;
	}
	// find Meal's start and end points
	TimePoint startMealBreakPoint = findStartLunchBreak(scheduleMealBreakInterval);
	TimePoint endMealBreakPoint = findEndLunchBreak(scheduleMealBreakInterval, startMealBreakPoint);
	if (startMealBreakPoint == null && endMealBreakPoint == null) {
	    return null;
	} else if (startMealBreakPoint != null && endMealBreakPoint != null) {
	    return new TimeInterval(startMealBreakPoint.getTime(), endMealBreakPoint.getTime(), false);
	} else if (startMealBreakPoint != null && endMealBreakPoint == null
		&& startMealBreakPoint.getTime().isBefore(scheduleMealBreakInterval.getEndTime())) {
	    return new TimeInterval(startMealBreakPoint.getTime(), scheduleMealBreakInterval.getEndTime(), false);
	} else {
	    return calculateBreakPeriod();
	}
    }

    private int getNumberOfWorkPoints() {
	int numberOfWorkPoints = 0;
	for (TimePoint timePoint : getTimePoints()) {
	    if (timePoint.getPointAttributes().contains(DomainConstants.WORKED_ATTRIBUTES)) {
		numberOfWorkPoints++;
	    }
	}
	return numberOfWorkPoints;
    }

    // Calcula a duracao do periodo em q o funcionario saiu para almoco
    // durante
    // o periodo de almoco.
    // 3 casos, um em que sai depois do periodo de almoco comecar, e outro q
    // entra ja almocado.
    // e o caso em q entra e sai durante o periodo de almoco
    public TimeInterval calculateBreakPeriod() {

	TimePoint mealStart = findIntervalStartPointByAttribute(AttributeType.MEAL);
	TimePoint mealEnd = findIntervalEndPointByAttribute(AttributeType.MEAL);

	if (mealStart.getIntervalAttributes().contains(DomainConstants.WORKED_ATTRIBUTES)) {
	    // meal esta' dentro de um periodo de trabalho
	    // TODO pode haver varios worked dentro de meal...
	    AttributeType workedAttribute = mealStart.getIntervalAttributes().intersects(DomainConstants.WORKED_ATTRIBUTES);
	    // saber qual o atributo de worked encontrar o ponto dentro de
	    // meal
	    // q termina o periodo de trabalho
	    TimePoint workedEndPoint = findIntervalEndPointBetweenPointsByAttribute(mealStart, mealEnd, workedAttribute);
	    TimePoint workedStartPoint = findWorkedStartPointBetweenPoints(mealStart, mealEnd);
	    // ver se nao houve marcacao antes do final da refeicao
	    if (workedStartPoint != null && workedEndPoint != null) {
		return (new TimeInterval(workedEndPoint.getTime(), workedStartPoint.getTime(), workedStartPoint.isNextDay()));
	    } else if (workedEndPoint != null) {
		return (new TimeInterval(workedEndPoint.getTime(), mealEnd.getTime(), mealEnd.isNextDay()));
	    }
	} else { // mealStart nao esta' dentro dum worked procurar inicio
	    // de
	    // periodo de trabalho
	    TimePoint workedStartPoint = findWorkedStartPointBetweenPoints(mealStart, mealEnd);
	    TimePoint workedEndPoint = null;
	    // caso em q trabalhou dentro da meal e nao almocou
	    if (workedStartPoint != null) {
		// encontrar ponto a partir de workStartPoint
		AttributeType workedAttribute = workedStartPoint.getPointAttributes().intersects(
			DomainConstants.WORKED_ATTRIBUTES); // saber qual o
		// atributo de worked
		workedEndPoint = findIntervalEndPointBetweenPointsByAttribute(workedStartPoint, mealEnd, workedAttribute);
		if (workedEndPoint != null) {
		    // saiu antes do final do periodo de almoco entao foi
		    // almocar...
		    return (new TimeInterval(workedEndPoint.getTime(), mealEnd.getTime(), mealEnd.isNextDay()));
		} else {
		    return (new TimeInterval(mealStart.getTime(), workedStartPoint.getTime(), workedStartPoint.isNextDay()));
		}
	    } else {
		if (workedEndPoint != null) {
		    return (new TimeInterval(workedEndPoint.getTime(), mealEnd.getTime(), mealEnd.isNextDay()));
		} else {
		    return (new TimeInterval(mealStart.getTime(), mealEnd.getTime(), mealEnd.isNextDay()));
		}
	    }
	}
	return null;
    }

    // Encontra 1o ponto do intervalo de refeicao feito pelo funcionario.
    public TimePoint findStartLunchBreak(TimeInterval scheduleMealBreakInterval) {
	List<TimePoint> workedPointsList = getAllAttributePoints(DomainConstants.WORKED_ATTRIBUTES);
	TimePoint[] mealInterval = findWorkedIntervalByAttribute(AttributeType.MEAL);
	TimePoint startMealBreakPoint = null;
	TimePoint firstPoint = workedPointsList.get(0);
	for (TimePoint point : workedPointsList) {
	    // ponto esta dentro do intervalo de refeicao
	    if (point.getIntervalAttributes().contains(DomainConstants.WORKED_ATTRIBUTES) == false) {
		if (point.getIntervalAttributes().contains(AttributeType.MEAL)
			|| (point.isBefore(mealInterval[0]) && !point.equals(firstPoint))) {
		    startMealBreakPoint = point;
		}
	    }
	}
	if (startMealBreakPoint != null) {
	    TimePoint nextWorkedPoint = getNextWorkedPoint(startMealBreakPoint);
	    if (nextWorkedPoint != null && nextWorkedPoint.isBefore(mealInterval[0])) {
		return null;
	    }
	}
	return startMealBreakPoint;
    }

    // procura final da refeicao feita pelo funcionario
    public TimePoint findEndLunchBreak(TimeInterval scheduleMealBreakInterval, TimePoint startMealBreakPoint) {
	if (startMealBreakPoint != null) {
	    List<TimePoint> workedPointsList = getAllAttributePoints(DomainConstants.WORKED_ATTRIBUTES);
	    TimePoint[] mealInterval = findWorkedIntervalByAttribute(AttributeType.MEAL);
	    for (TimePoint point : workedPointsList) {
		// ponto abre worked e e' depois do inicio da refeicao
		if (point.getIntervalAttributes().contains(DomainConstants.WORKED_ATTRIBUTES)
			&& (mealInterval[0].isBefore(point) || mealInterval[0].isAtSameTime(point))) {
		    if (point.getTime().isAfter(startMealBreakPoint.getTime())) {
			return point;
		    }
		}
	    }
	}
	return null;
    }

    // Returns the time the employee worked during Normal Work Period 1 or 2
    // time interval. The clockings should be done during the Normal Work
    // Period
    // 1 or 2.
    // TODO verify this!
    public Duration calculateNormalWorkPeriod(AttributeType normalWorkPeriodAttribute) {
	Duration totalDuration = Duration.ZERO;
	// get the workedAttributes during the normal work period
	Attributes overlappedAttributes = findAttributesIntervalThatOverlapFromAttributes(normalWorkPeriodAttribute,
		DomainConstants.WORKED_ATTRIBUTES);
	// since the worked times are not overlapped lets calculate each
	// duration
	for (AttributeType attribute : overlappedAttributes.getAttributes()) {
	    TimePoint[] attributeInterval = findWorkedIntervalByAttribute(attribute);
	    DateTime startDate = attributeInterval[0].getTime().toDateTimeToday();
	    DateTime endDate = attributeInterval[1].getTime().toDateTimeToday();
	    if (attributeInterval[0].isNextDay() != attributeInterval[1].isNextDay()) {
		endDate = endDate.plusDays(1);
	    }
	    totalDuration = totalDuration.plus(new Duration(startDate, endDate));
	}
	return totalDuration;
    }

    public TimePoint getFirstWorkTimePoint() {
	for (TimePoint point : getTimePoints()) {
	    if (point.getPointAttributes().contains(DomainConstants.WORKED_ATTRIBUTES)) {
		return point;
	    }
	}
	return null;
    }

    public TimePoint getLastWorkTimePoint() {
	for (int i = getNumberOfTimePoints() - 1; i >= 0; i--) {
	    TimePoint point = getTimePoints().get(i);
	    if (point.getPointAttributes().contains(DomainConstants.WORKED_ATTRIBUTES)) {
		return point;
	    }
	}
	return null;
    }

    // Plots the pairs of clockings in the timeline
    // Converts pairs of clockings of the clockingList to clockingInterval
    // and
    // adds it to the pointList.
    public void plotListInTimeline(List<AssiduousnessRecord> clockingList, List<Leave> leaveList,
	    Iterator<AttributeType> attributesIt, YearMonthDay day) {
	List<TimePoint> pointList = new ArrayList<TimePoint>();

	Iterator<AssiduousnessRecord> clockingIt = clockingList.iterator();
	DateTime lastClock = null;
	while (clockingIt.hasNext()) {
	    final AssiduousnessRecord clockIn = clockingIt.next();
	    DateTime clockInDate = clockIn.getDate().minusSeconds(clockIn.getDate().getSecondOfMinute());
	    for (Leave leave : leaveList) {
		if ((lastClock == null || leave.getDate().isAfter(lastClock) || leave.getDate().isEqual(lastClock))
			&& leave.getDate().isBefore(clockInDate)) {
		    pointList.addAll(leave.toTimePoints((AttributeType) attributesIt.next()));
		}
	    }
	    final AttributeType attribute = attributesIt.next();
	    final TimePoint timePointIn = constructTimePoint(clockIn, day, attribute);
	    pointList.add(timePointIn);
	    lastClock = timePointIn.getTime().toDateTime(clockInDate);
	    if (clockingIt.hasNext()) {
		final AssiduousnessRecord clockOut = clockingIt.next();
		final TimePoint timePointOut = constructTimePoint(clockOut, day, attribute);
		pointList.add(timePointOut);
	    }
	}
	for (Leave leave : leaveList) {
	    if ((lastClock == null || leave.getDate().isAfter(lastClock) || leave.getDate().isEqual(lastClock))) {
		pointList.addAll(leave.toTimePoints((AttributeType) attributesIt.next()));
	    }
	}
	plotList(pointList);
    }

    private TimePoint constructTimePoint(AssiduousnessRecord clockIn, YearMonthDay day, AttributeType attribute) {
	final TimeOfDay timeIn = new TimeOfDay(clockIn.getDate().getHourOfDay(), clockIn.getDate().getMinuteOfHour(), 0);
	return new TimePoint(timeIn, clockIn.getDate().toYearMonthDay().isAfter(day), attribute);
    }

    public TimePoint getNextWorkedPoint(TimePoint timePoint) {
	int timepointPosition = getTimePointIndex(timePoint);
	for (int iter = timepointPosition + 1; iter < getTimePoints().size(); iter++) {
	    TimePoint tempTimePoint = getTimePoints().get(iter);
	    if (tempTimePoint.getPointAttributes().contains(DomainConstants.WORKED_ATTRIBUTES)) {
		return tempTimePoint;
	    }
	}
	return null;
    }

    public TimePoint getPreviousWorkedPoint(TimePoint timePoint) {
	int timepointPosition = getTimePointIndex(timePoint);
	for (int iter = timepointPosition - 1; iter >= 0; iter--) {
	    TimePoint tempTimePoint = getTimePoints().get(iter);
	    if (tempTimePoint.getPointAttributes().contains(DomainConstants.WORKED_ATTRIBUTES)) {
		return tempTimePoint;
	    }
	}
	return null;
    }

    private TimePoint findWorkedPointBefore(TimePoint pointAfter) {
	TimePoint result = null;
	for (TimePoint point : getTimePoints()) {
	    if (point.isBefore(pointAfter)) {
		if (point.getPointAttributes().contains(DomainConstants.WORKED_ATTRIBUTES)) {
		    result = point;
		}
	    } else {
		return result;
	    }
	}
	return result;
    }

    public TimePoint[] findIntervalByAttribute(AttributeType attribute, TimeOfDay time) {
	TimePoint[] result = null;
	for (TimePoint point : getTimePoints()) {
	    if (isPointStartingAttributeInterval(point, attribute) && point.getTime().isEqual(time)) {
		result = new TimePoint[2];
		result[0] = point;
	    } else if (result != null && isPointClosingAttributeInterval(point, attribute)) {
		result[1] = point;
	    }
	}
	return result;
    }

    public boolean hasWorkingPointBeforeLeave(Leave leave) {
	TimePoint[] result = findIntervalByAttribute(AttributeType.JUSTIFICATION, leave.getDate().toTimeOfDay());
	return (result != null && getPreviousWorkedPoint(result[0]) != null);
    }

    public boolean hasWorkingPointAfterLeave(Leave leave) {
	TimePoint[] result = findIntervalByAttribute(AttributeType.JUSTIFICATION, leave.getDate().toTimeOfDay());
	return (result != null && getNextWorkedPoint(result[1]) != null);
    }
}
