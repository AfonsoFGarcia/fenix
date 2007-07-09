package pt.utl.ist.codeGenerator.database;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.space.Room;

import org.joda.time.DateTime;

public class ExamRoomManager extends HashSet<Room> {

    private final Map<ExecutionPeriod, EvaluationRoomManager> evaluationRoomManagerMap = new HashMap<ExecutionPeriod, EvaluationRoomManager>();

    public DateTime getNextDateTime(final ExecutionPeriod executionPeriod) {
	EvaluationRoomManager evaluationRoomManager = evaluationRoomManagerMap.get(executionPeriod);
	if (evaluationRoomManager == null) {
	    evaluationRoomManager = new EvaluationRoomManager(
		    executionPeriod.getEndDateYearMonthDay().minusDays(31).toDateTimeAtMidnight(),
		    executionPeriod.getEndDateYearMonthDay().toDateTimeAtMidnight(), 180, this);
	    evaluationRoomManagerMap.put(executionPeriod, evaluationRoomManager);
	}
	return evaluationRoomManager.getNextDateTime();
    }

    public Room getNextOldRoom(final ExecutionPeriod executionPeriod) {
	final EvaluationRoomManager evaluationRoomManager = evaluationRoomManagerMap.get(executionPeriod);
	return evaluationRoomManager.getNextOldRoom();
    }

}
