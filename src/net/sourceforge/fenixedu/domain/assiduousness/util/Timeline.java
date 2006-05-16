package net.sourceforge.fenixedu.domain.assiduousness.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.joda.time.Duration;
import org.joda.time.TimeOfDay;

public class Timeline {

    private List<TimePoint> timePoints;
    
    public Timeline() {
        this.timePoints = new ArrayList<TimePoint>();
        // adds a starting point at 00:00 with Null Time attribute
      timePoints.add(new TimePoint(DomainConstants.TIMELINE_START, AttributeType.NULL, AttributeType.NULL));
      timePoints.add(new TimePoint(DomainConstants.TIMELINE_END, AttributeType.NULL));
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
    
    
    public void plotList(List<TimePoint> pointList) {
//        int i = 1;
        for (TimePoint point: pointList) {
            plotPoint(point);
 //           i = i +1;
        }
    }
   
    
    // To add directly a point to the timeline - used ONLY by addPoint
    private void addPoint(int position, TimePoint timePoint) {
        getTimePoints().add(position, timePoint);
    }
    
    
    // Adds a point to the time line, sets the openIntervals accordingly
    // TODO must refactor! it's confusing and has some dups...
    public void plotPoint(TimePoint newPoint) {
        int timeLineSize = getNumberOfTimePoints();
        Attributes insideIntervals = new Attributes();
        for (int i = 0; i < timeLineSize; i++) {
            TimePoint currentPoint = getTimeLinePosition(i);
            if (newPoint.isAtSameTime(currentPoint)) {
                if (insideIntervals.contains(newPoint.getPointAttributes())) { // the interval is open and we're going to close it right away
                    pointClosesInterval(insideIntervals, newPoint, i);
                    // TODO verificar isto! - apaguei, em principio e' redundante
//                    currentPoint.getPointAttributes().removeAttributes(newPoint.getPointAttributes().getAttributes()); // remove the newPoint attributes from the current point 
                    currentPoint.getPointAttributes().addAttributes(newPoint.getPointAttributes().getAttributes()); // since the time is the same we will not add the new point but use the current 
                } else { // the interval is not open, let's create it
                    pointOpensInterval(insideIntervals, newPoint, i);
                    insideIntervals.addAttributes(currentPoint.getIntervalAttributes().getAttributes());
                    currentPoint.getPointAttributes().addAttributes(newPoint.getPointAttributes().getAttributes()); // since the time is the same we will not add the new point but use the current 
                }
                break;
            } else if (newPoint.isBefore(currentPoint)) {
                addPoint(i,newPoint); // adds the point in this position and shifts the current point to the next position (i+1)
                // se attrib ponto do novo ponto estao em intervalos abertos => intervalo vai-se fechar
                if (insideIntervals.contains(newPoint.getPointAttributes())) { // inserted point only has 1 attribute
                    pointClosesInterval(insideIntervals, newPoint, i);
                } else { // novo intervalo
                    pointOpensInterval(insideIntervals, newPoint, i);
                }                
                break; // get off the for loop
            } else { // is after
                // caso em q o intervalo ja esta' aberto
            	// faz o update do atributo no ponto
                for (AttributeType attribute: currentPoint.getPointAttributes().getAttributes()) {
                    if (insideIntervals.contains(attribute)) { 
                        newPoint.getIntervalAttributes().removeAttribute(attribute); // removes the current point attribute to new point interval attributes
                        insideIntervals.removeAttribute(attribute);
                    } else {
                        newPoint.getIntervalAttributes().addAttribute(attribute); // adds the current point attribute to new point interval attributes
                        insideIntervals.addAttribute(attribute);
                    }
                }
            }
        }
    }    
    
    
    // Removes an attribute from all timeline's points starting from startPosition
    // part of plotPoint
    private void removeIntervalAttributeFromNextPoints(int startPosition, Attributes attributes) {
        int timeLineSize = getNumberOfTimePoints();
        for (int i = startPosition; i < timeLineSize; i++) {
            TimePoint currentPoint = getTimeLinePosition(i);
            currentPoint.getIntervalAttributes().removeAttributes(attributes.getAttributes());
        }
    }

    // Adds an attribute to all timeline points' interval starting from startPosition. the timeline's last point is excluded 'cause it's the end of all 1 point intervals
    // part of plotPoint
    private void addIntervalAttributeToNextPoints(int startPosition, Attributes attributes) {
        int timeLineSize = getNumberOfTimePoints() - 1; // exclude the last timeline point
        for (int i = startPosition; i < timeLineSize; i++) {
            TimePoint currentPoint = getTimeLinePosition(i);
            currentPoint.getIntervalAttributes().addAttributes(attributes.getAttributes());
        }
    }
    
    // As the point closes the interval, this method removes the point's attribute from the open intervals set and from all subsequent point's attribute sets
    // part of plotPoint
    private void pointClosesInterval(Attributes insideIntervals, TimePoint point, int position) {
        insideIntervals.removeAttributes(point.getPointAttributes().getAttributes()); // remove the attribute from the openIntervals set
        removeIntervalAttributeFromNextPoints(position, point.getPointAttributes()); // remove the attribute from this and the next points
    }
    
    // As the point opens the interval, this method adds the point's attribute to the open intervals set and to all subsequent point's attribute sets
    // part of plotPoint
    private void pointOpensInterval(Attributes insideIntervals, TimePoint point, int position) {
        insideIntervals.addAttributes(point.getPointAttributes().getAttributes()); // adds the point's attribute to the openInterval set
        addIntervalAttributeToNextPoints(position, point.getPointAttributes()); // adds the point's attribute to this and the rest of the following points
    }
 
    
    // Finds the start point of a worked interval before a given TimePoint. The start point must have worked as point attributes and worked must be in its interval attributes 
    private TimePoint findWorkedStartPointBetweenPoints(TimePoint startPoint, TimePoint endPoint) {
        int startPosition = getTimePoints().indexOf(startPoint);
        int timelineSize = getNumberOfTimePoints();
        for (int i = startPosition; i < timelineSize; i++) {
            TimePoint point = getTimeLinePosition(i);
            // worked is never overlapped to another worked, so if it's a worked point it's unique
            AttributeType pointWorkedAttribute = point.getPointAttributes().intersects(DomainConstants.WORKED_ATTRIBUTES);
                if (pointWorkedAttribute != null) {
                    if (isPointStartingAttributeInterval(point, pointWorkedAttribute) && (point.getPoint().isBefore(endPoint.getPoint()) || 
                            point.getPoint().equals(endPoint.getPoint()))) {
                        return point;
                    }
                }
        }
        return null;
    }
    

    // Finds the start point of the interval. The start point must have attribute as point attributes and attribute must be in its interval attributes 
    private TimePoint findIntervalStartPointByAttribute(AttributeType attribute) {
        for (TimePoint point: getTimePoints()) {
            if (isPointStartingAttributeInterval(point, attribute)) {
                return point;
            }
        }
        return null;
    }
    
    // Finds the end point of the interval. The end point must have attribute as point attributes and attribute must not be in its interval attributes 
    private TimePoint findIntervalEndPointByAttribute(AttributeType attribute) { 
        for (TimePoint point: getTimePoints()) {
            if (isPointClosingAttributeInterval(point, attribute)) {
                return point;
            }
        }
        return null;
    }
    
    
    // Finds the end point of the interval before a given TimePoint. The end point must have attribute as point attributes and attribute must not be in its interval attributes 
    private TimePoint findIntervalEndPointBetweenPointsByAttribute(TimePoint startPoint, TimePoint endPoint, AttributeType attribute) { 
        int startPosition = getTimePoints().indexOf(startPoint);
        int timelineSize = getNumberOfTimePoints();
        for (int i = startPosition; i < timelineSize; i++) {
            TimePoint point = getTimeLinePosition(i);
            if (isPointClosingAttributeInterval(point, attribute) && (point.getPoint().isBefore(endPoint.getPoint()) || point.getPoint().equals(endPoint.getPoint()))) {
                return point;
            }
        }
        return null;
    }

    // Returns an TimeInterval of the specified attribute BEWARE that the TimeInterval doesn't have information about the attribute
    // used in calculateAttributesDuration since we need to build and interval to get its duration
    private TimeInterval findIntervalByAttribute(AttributeType attribute) {
        List<TimePoint> listTimePoint = findTimePointsByAttribute(attribute);
        if (listTimePoint.size() == 2) {
            return new TimeInterval(listTimePoint.get(0).getPoint(), listTimePoint.get(1).getPoint());
        }
        return null;
    }
 
    private List<TimePoint> findTimePointsByAttribute(AttributeType attribute) {
        List<TimePoint> timePointList = new ArrayList<TimePoint>();
        TimePoint startPoint = findIntervalStartPointByAttribute(attribute);
        TimePoint endPoint = findIntervalEndPointByAttribute(attribute);
        if ((startPoint != null) && (endPoint != null)) {
            timePointList.add(startPoint);
            timePointList.add(endPoint);
        }
        return timePointList;
    }
    
    // A point starts an AttributeType attribute interval if its attribute contains attribute and if the interval attributes contains attribute
    private boolean isPointStartingAttributeInterval(TimePoint point, AttributeType attribute) {
        return (point.getPointAttributes().contains(attribute) && point.getIntervalAttributes().contains(attribute));
    }

    // A point closes an AttributeType attribute interval if its attribute contains attribute and if the interval attributes does not contain attribute
    private boolean isPointClosingAttributeInterval(TimePoint point, AttributeType attribute) {
        return (point.getPointAttributes().contains(attribute) && (point.getIntervalAttributes().contains(attribute) == false));
    }
    
    // Finds which attributes overlap one interval
    public Attributes findAttributesIntervalThatOverlapFromAttributes(AttributeType attribute, Attributes attributesToCheck) {
        Attributes overlappedAttributes = new Attributes();
        for (TimePoint point: getTimePoints()) {
            // ou o ponto tem o atributo ou o intervalo tem o atributo (caso em que o ponto pertence ao intervalo do attribute) 
            if (point.getPointAttributes().contains(attribute) || point.getIntervalAttributes().contains(attribute))  {
                for (AttributeType attributeCheck: attributesToCheck.getAttributes()) {
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
        for (TimePoint point: getTimePoints()) {
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
        for (TimePoint point: getTimePoints()) {
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
        if (pointList.size() > 2) {
            pointList = normalizeList(pointList);
        }
        Iterator<TimePoint> pointListIt = pointList.iterator();
        while (pointListIt.hasNext()) {
            TimePoint point = pointListIt.next();
            if (pointListIt.hasNext()) {
                TimePoint point2 = pointListIt.next();
                System.out.println("calcular a duracao entre " + point + " e " + point2);
                totalDuration = totalDuration.plus(new TimeInterval(point.getPoint(), point2.getPoint()).getDuration());
            }
        }
        System.out.println("total duration: " + totalDuration.toPeriod().toString());
        return totalDuration;
    }

    // Returns a list with 2 points, the 1st and the last of pointList.
    public List<TimePoint> normalizeList(List pointList) {
        List normalizedPointList = new ArrayList();
        normalizedPointList.add(pointList.get(0));
        normalizedPointList.add(pointList.get(pointList.size() - 1));
        return normalizedPointList;
    }
    
    
    // Calcula a duracao dos intervalos de atributo attributes cujo final seja ate timeOfDay
    // TODO verificar
    public Duration calculateDurationAllIntervalsByAttributesToTime(TimeOfDay timeOfDay, Attributes attributes) {
    		Duration totalDuration = new Duration(0);
        for (AttributeType attribute: attributes.getAttributes()) {
            TimeInterval interval = findIntervalByAttribute(attribute);
            if (interval != null) {
                if (interval.getEndTime().isBefore(timeOfDay) || interval.getEndTime().equals(timeOfDay)) {
                    totalDuration = totalDuration.plus(interval.getDuration());
                }
            } else {
                break;
            }
           
        }
        return totalDuration;
    }

    // Calcula a duracao dos intervalos de atributo attributes a partir de timeof day
    // TODO verificar
    public Duration calculateDurationAllIntervalsByAttributesFromTime(TimeOfDay timeOfDay, Attributes attributes) {
        Duration totalDuration = new Duration(0);
        for (AttributeType attribute: attributes.getAttributes()) {
            TimeInterval interval = findIntervalByAttribute(attribute);
            if (interval != null) {
                if (interval.getStartTime().isAfter(timeOfDay) || interval.getStartTime().equals(timeOfDay)) {
                    totalDuration = totalDuration.plus(interval.getDuration());
                }
            } else {
                break;
            }
           
        }
        return totalDuration;
    }

    // Calcula a duracao dos intervalos de atributo attributes a partir de timeof day
    // TODO verificar
    public Duration calculateDurationAllIntervalsByAttributes(Attributes attributes) {
        return calculateDurationAllIntervalsByAttributesFromTime(DomainConstants.TIMELINE_START, attributes);
    }
    
    // Returns a list will all points that contain the specified attributes
    public List<TimePoint> getAllAttributePoints(Attributes attributes) {
        List<TimePoint> pointList = new ArrayList<TimePoint>();
        for (TimePoint point: getTimePoints()) {
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
        for (TimePoint point: getTimePoints()) {
            // checks if the point is a WORKED in the fixed period or is a BALANCE in the fixed period.
            if (point.hasAttributes(fixedPeriodAttribute, DomainConstants.WORKED_ATTRIBUTES) || point.hasAttributes(fixedPeriodAttribute, AttributeType.BALANCE)) {
                pointList.add(point);
            }
        }
        return this.calculateDurationPointList(pointList);
    }    

    // Calcula o intervalo de refeicao feito pelo funcionario
    public TimeInterval calculateMealBreakInterval(TimeInterval scheduleMealBreakInterval) {
        // find Meal's start and end points
        TimePoint startMealBreakPoint = findStartLunchBreak(scheduleMealBreakInterval);
        TimePoint endMealBreakPoint = findEndLunchBreak(scheduleMealBreakInterval, startMealBreakPoint);
        if (startMealBreakPoint != null) { // ha inicio de refeicao - ie funcionario saiu para almoco
            if (endMealBreakPoint != null) { // ha fim de refeicao - ie funcionario regressou do almoco
                return new TimeInterval(startMealBreakPoint.getPoint(), endMealBreakPoint.getPoint());
            } else { // funcionario nao regressou
                // calcula a duracao do periodo em q o funcionario saiu para o almoco e o fim do almoco definido no horario.
                return calculateBreakPeriod();
            }
        } else { // nao ha inicio de refeicao
            return calculateBreakPeriod();
        }
//            return new TimeInterval(startMealBreakPoint.getPoint(), endMealBreakPoint.getPoint());
//        } else { // nao ha fim de refeicao, employee bazou ou nao fez refeicao
//            System.out.println("nao ha inicio ou fim de ref");
//            // calcula a duracao do periodo em q o funcionario saiu para o almoco e o fim do almoco definido no horario.
//            return calculateBreakPeriod();
//            //return null;
//        }
    }    
    
    // Calcula a duracao do periodo em q o funcionario saiu para almoco durante o periodo de almoco.
    // 3 casos, um em que sai depois do periodo de almoco comecar, e outro q entra ja almocado.
    // e o caso em q entra e sai durante o periodo de almoco
    public TimeInterval calculateBreakPeriod() {

        TimePoint mealStart = findIntervalStartPointByAttribute(AttributeType.MEAL);
        System.out.println("mealStart " + mealStart);
        TimePoint mealEnd = findIntervalEndPointByAttribute(AttributeType.MEAL);
        System.out.println("mealEnd " + mealEnd);
        
        if (mealStart.getIntervalAttributes().contains(DomainConstants.WORKED_ATTRIBUTES)) { // meal esta' dentro de um periodo de trabalho
            // TODO pode haver varios worked dentro de meal...
            AttributeType workedAttribute = mealStart.getIntervalAttributes().intersects(DomainConstants.WORKED_ATTRIBUTES); // saber qual o atributo de worked
            // encontrar o ponto dentro de meal q termina o periodo de trabalho
            TimePoint workedEndPoint = findIntervalEndPointBetweenPointsByAttribute(mealStart, mealEnd, workedAttribute);
            System.out.println("workedEndPoint " + workedEndPoint);
            TimePoint workedStartPoint = findWorkedStartPointBetweenPoints(mealStart, mealEnd); // ver se nao houve marcacao antes do final da refeicao
            System.out.println("workedStartPoint " + workedStartPoint);
            if (workedStartPoint != null) {
                System.out.println("start e' null");
                return (new TimeInterval(workedEndPoint.getPoint(), workedStartPoint.getPoint()));
            } else if (workedEndPoint != null) {
                System.out.println("end e' null");
                return (new TimeInterval(workedEndPoint.getPoint(), mealEnd.getPoint()));
            }
        } else { // mealStart nao esta' dentro dum worked procurar inicio de periodo de trabalho
            System.out.println("mealstart nao ta dentro de worked -> entrada dentro do int de refeicao");
            TimePoint workedStartPoint = findWorkedStartPointBetweenPoints(mealStart, mealEnd);
            System.out.println("workedStartPoint " + workedStartPoint);
            // encontrar ponto a partir de workStartPoint
            AttributeType workedAttribute = workedStartPoint.getPointAttributes().intersects(DomainConstants.WORKED_ATTRIBUTES); // saber qual o atributo de worked
            TimePoint workedEndPoint =  findIntervalEndPointBetweenPointsByAttribute(workedStartPoint, mealEnd, workedAttribute); 
            System.out.println("workedEndPoint " + workedEndPoint);
            // caso em q trabalhou dentro da meal e nao almocou
            if (workedStartPoint != null) {
                if (workedEndPoint != null) { // saiu antes do final do periodo de almoco entao foi almocar...
                    return (new TimeInterval(workedEndPoint.getPoint(), mealEnd.getPoint()));
                } else {
                    return (new TimeInterval(mealStart.getPoint(), workedStartPoint.getPoint()));
                }
            } else {
                if (workedEndPoint != null) {
                    return (new TimeInterval(workedEndPoint.getPoint(), mealEnd.getPoint()));
                } else {
                    return (new TimeInterval(mealStart.getPoint(), mealEnd.getPoint()));
                }
            }
        }
        return null;
    }    
    
    
    // Encontra 1o ponto do intervalo de refeicao feito pelo funcionario.
    public TimePoint findStartLunchBreak(TimeInterval scheduleMealBreakInterval) {
    		List<TimePoint> workedPointsList = getAllAttributePoints(DomainConstants.WORKED_ATTRIBUTES);
    		TimeInterval mealInterval = findIntervalByAttribute(AttributeType.MEAL);
    		TimePoint startMealBreakPoint = null;
    		for (TimePoint point: workedPointsList) {
            // ponto esta dentro do intervalo de refeicao            
            if ((point.getIntervalAttributes().contains(DomainConstants.WORKED_ATTRIBUTES) == false) && point.getIntervalAttributes().contains(AttributeType.MEAL) && point.getPoint().isAfter(mealInterval.getStartTime()) && 
                    scheduleMealBreakInterval.contains(point.getPoint(), false)) {
                System.out.println("ponto" + point.toString());
                // check if the employee nao trabalhou apenas dentro do periodo de almoco
//                TimeInterval workedIntervalBeforeMeal = findIntervalByAttribute(point.getPointAttributes().intersects(DomainConstants.WORKED_ATTRIBUTES));
//                System.out.println(workedIntervalBeforeMeal.toString());
                startMealBreakPoint = point;
                
//                if (workedIntervalBeforeMeal.getStartTime().isBefore(mealInterval.getStartTime())) {
//                    startMealBreakPoint = point;
//                    System.out.println("point" + point.toString());
//                    break;
//                } else {
////                    startMealBreakPoint = 
//                }
            }
        }
        // NAO SE APLICA PQ O FUNCIONARIO TEM DE IR ALMOCAR NO INTERVALO
        //        // Se nao foi encontrado nenhum ponto pode haver a hipotese do funcionario ter ido almocar antes do intervalo de almoco
//        if (startMealBreakPoint == null) {
//            int workedPointsListSize = workedPointsList.size();
//            for (int i = workedPointsListSize; i < 0; i--) {
//                TimePoint point = workedPointsList.get(i);
//                if ((point.getIntervalAttributes().contains(DomainConstants.WORKED_ATTRIBUTES) == false) && (point.getIntervalAttributes().contains(AttributeType.MEAL) == false)
//                        && point.getPoint().isBefore(mealInterval.getStartTime())) {
//                    startMealBreakPoint = point;
//                    break;
//                }
//            }
//        }
        return startMealBreakPoint;
    }
    
    // procura final da refeicao feita pelo funcionario
    public TimePoint findEndLunchBreak(TimeInterval scheduleMealBreakInterval, TimePoint startMealBreakPoint) {
        List<TimePoint> workedPointsList = getAllAttributePoints(DomainConstants.WORKED_ATTRIBUTES);
        TimeInterval mealInterval = findIntervalByAttribute(AttributeType.MEAL);
        // encontrar ponto final
        for  (TimePoint point: workedPointsList) {
            // ponto abre worked e e' depois do inicio da refeicao
            if (point.getIntervalAttributes().contains(DomainConstants.WORKED_ATTRIBUTES) && point.getPoint().isAfter(mealInterval.getStartTime())) { 
                // && scheduleMealBreakInterval.contains(point.getPoint(), false)) {
                // e' este ponto no caso de ser depois de startMealBreakPoint
                if ((startMealBreakPoint != null) && point.getPoint().isAfter(startMealBreakPoint.getPoint())) {
                    return point;
                }
            }
        }
        return null;
    }

    
    // Returns the time the employee worked during Normal Work Period 1 or 2 time interval. The clockings should be done during the Normal Work Period 1 or 2.
    // TODO verify this!
    public Duration calculateNormalWorkPeriod(AttributeType normalWorkPeriodAttribute) {
    		Duration totalDuration = Duration.ZERO;
    		// get the workedAttributes during the normal work period
    		Attributes overlappedAttributes = findAttributesIntervalThatOverlapFromAttributes(normalWorkPeriodAttribute, DomainConstants.WORKED_ATTRIBUTES);
    		// since the worked times are not overlapped lets calculate each duration
    		for (AttributeType attribute: overlappedAttributes.getAttributes()) {
    			TimeInterval attributeInterval = findIntervalByAttribute(attribute);
    			totalDuration = totalDuration.plus(attributeInterval.getDuration());
    		}
    		return totalDuration;
    }


    	// Returns the first point the employee worked (corresponding to the 1st clocking of the day)
    public TimePoint findFirstWorkStart() {
    		for (TimePoint point: getTimePoints()) {
    			if (point.getPointAttributes().equals(AttributeType.WORKED1)) {
    				return point;
    			}
    		}
    		return null;
    }
    

    public void print() {
        for (TimePoint point: getTimePoints()) {
            System.out.println(point.toString());
        }
    }

}
