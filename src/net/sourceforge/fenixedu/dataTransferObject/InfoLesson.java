package net.sourceforge.fenixedu.dataTransferObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.util.DiaSemana;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public class InfoLesson extends InfoShowOccupation implements Comparable<InfoLesson> {

    private final static ComparatorChain INFO_LESSON_COMPARATOR_CHAIN = new ComparatorChain();
    static {
        INFO_LESSON_COMPARATOR_CHAIN.addComparator(new BeanComparator("diaSemana.diaSemana"));
        INFO_LESSON_COMPARATOR_CHAIN.addComparator(new BeanComparator("inicio"));
        INFO_LESSON_COMPARATOR_CHAIN.addComparator(new BeanComparator("fim"));
        INFO_LESSON_COMPARATOR_CHAIN.addComparator(new BeanComparator("infoSala.nome"));
    }
    
    private DomainReference<Lesson> lesson;    
    private InfoRoom infoSala;
    private InfoShift infoShift;
    private InfoRoomOccupation infoRoomOccupation;

    public InfoLesson(Lesson lesson) {
    	super.copyFromDomain(lesson);
    	this.lesson = new DomainReference<Lesson>(lesson);
    }
    
    @Override
    public ShiftType getTipo() {
	return null;
    }
    
    public DiaSemana getDiaSemana() {
        return getLesson().getDiaSemana();
    }

    public Calendar getFim() {
        return getLesson().getFim();
    }

    public Calendar getInicio() {
        return getLesson().getInicio();
    }   
    
    public FrequencyType getFrequency() {        
        return getLesson().getFrequency();
    }
        
    public YearMonthDay getLessonBegin() {
	return getLesson() != null && getLesson().hasPeriod() ? getLesson().getPeriod().getStartYearMonthDay() : null;
    }
    
    public YearMonthDay getLessonEnd() {
	return getLesson() != null && getLesson().hasPeriod() ? getLesson().getPeriod().getLastOccupationPeriodOfNestedPeriods()
		.getEndYearMonthDay() : null;
    }
    
    public String getWeekDay() {
        final String result = getDiaSemana().getDiaSemana().toString();
        if (result != null && result.equals("7")) {
            return "S";
        }
        if (result != null && result.equals("1")) {
            return "D";
        }
        return result;
    }

    public String getLessonDuration() {
        return getLesson().getUnitHours().toString();
    }
    
    public InfoRoom getInfoSala() {
        return (infoSala == null) ? infoSala = InfoRoom.newInfoFromDomain((AllocatableSpace) getLesson().getSala()) : infoSala;
    }

    public InfoShift getInfoShift() {
        return (infoShift == null) ? infoShift = InfoShift.newInfoFromDomain(getLesson().getShift()) : infoShift;
    }
    
    public InfoRoomOccupation getInfoRoomOccupation() {
	if (infoRoomOccupation == null) {
	    infoRoomOccupation = InfoRoomOccupation.newInfoFromDomain(getLesson().getLessonSpaceOccupation());
	}
	return infoRoomOccupation;
    }

    public static InfoLesson newInfoFromDomain(Lesson lesson) {
    	return (lesson != null) ? new InfoLesson(lesson) : null;
    }
    
    public int compareTo(InfoLesson arg0) {
        return INFO_LESSON_COMPARATOR_CHAIN.compare(this, arg0);
    }
    
    public String getNextPossibleLessonInstanceDate() {
	YearMonthDay day = getLesson().getNextPossibleLessonInstanceDate();
	return day != null ? day.toString("dd/MM/yyyy") : "-";	
    }
    
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoLesson) {
            InfoLesson infoAula = (InfoLesson) obj;
            resultado = (getDiaSemana().equals(infoAula.getDiaSemana()))
                    && (getInicio().get(Calendar.HOUR_OF_DAY) == infoAula.getInicio().get(Calendar.HOUR_OF_DAY))
                    && (getInicio().get(Calendar.MINUTE) == infoAula.getInicio().get(Calendar.MINUTE))
                    && (getFim().get(Calendar.HOUR_OF_DAY) == infoAula.getFim().get(Calendar.HOUR_OF_DAY))
                    && (getFim().get(Calendar.MINUTE) == infoAula.getFim().get(Calendar.MINUTE))
                    && ((getInfoSala() == null && infoAula.getInfoSala() == null) || (getInfoSala() != null && getInfoSala().equals(infoAula.getInfoSala())))
                    && ((getInfoRoomOccupation() == null && infoAula.getInfoRoomOccupation() == null) 
                            || (getInfoRoomOccupation() != null && getInfoRoomOccupation().equals(infoAula.getInfoRoomOccupation())));
        }
        return resultado;
    }

    private Lesson getLesson(){
        return lesson == null ? null : lesson.getObject();
    }
}