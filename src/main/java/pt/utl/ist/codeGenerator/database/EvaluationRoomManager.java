package pt.utl.ist.codeGenerator.database;

import java.util.Set;
import java.util.Stack;

import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

public class EvaluationRoomManager extends Stack<Space> {

    private final DateTime startDateTime;
    private final DateTime endDateTime;

    private final int evaluationDurationInMinutes;

    private DateTime nextDateTime;

    public EvaluationRoomManager(final DateTime startDateTime, final DateTime endDateTime, final int evaluationDurationInMinutes,
            Set<Space> oldRooms) {
        this.startDateTime = startDateTime.withField(DateTimeFieldType.hourOfDay(), 8);
        this.endDateTime = endDateTime.withField(DateTimeFieldType.hourOfDay(), 20);
        this.nextDateTime = startDateTime;
        this.evaluationDurationInMinutes = evaluationDurationInMinutes;
        addAll(oldRooms);
    }

    public DateTime getNextDateTime() {
        if (nextDateTime.isAfter(endDateTime)) {
            pop();
            nextDateTime = startDateTime;
        } else if (nextDateTime.getHourOfDay() > 20) {
            if (nextDateTime.getDayOfWeek() == 6) {
                nextDateTime = nextDateTime.plusDays(2).withField(DateTimeFieldType.hourOfDay(), 8);
            } else {
                nextDateTime = nextDateTime.plusDays(1).withField(DateTimeFieldType.hourOfDay(), 8);
            }
        } else {
            nextDateTime = nextDateTime.plusMinutes(evaluationDurationInMinutes);
        }
        return nextDateTime;
    }

    public Space getNextOldRoom() {
        return peek();
    }

}
