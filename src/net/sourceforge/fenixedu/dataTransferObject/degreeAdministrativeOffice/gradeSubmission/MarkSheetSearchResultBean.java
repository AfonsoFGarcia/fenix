package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.MarkSheetState;

public class MarkSheetSearchResultBean {
    
    private int totalNumberOfStudents;
    private List<MarkSheet> markSheets;
    
    public MarkSheetSearchResultBean() {
        markSheets = new ArrayList<MarkSheet>();  
    }

    public List<MarkSheet> getMarkSheets() {
        return markSheets;
    }
    
    public Collection<MarkSheet> getMarkSheetsSortedByEvaluationDate() {
        Collections.sort(getMarkSheets(), new Comparator<MarkSheet>() {
            public int compare(MarkSheet o1, MarkSheet o2) {
                return o1.getEvaluationDateDateTime().compareTo(o2.getEvaluationDateDateTime()); 
            }
        });
        return getMarkSheets();
    }

    public void setMarkSheets(List<MarkSheet> markSheets) {
        this.markSheets = markSheets;
    }

    public void addMarkSheet(MarkSheet markSheet) {
        getMarkSheets().add(markSheet);
    }

    public int getNumberOfEnroledStudents() {
        int numberOfEnroledStudents = 0;
        for (MarkSheet markSheet : getMarkSheets()) {
            if (! isRectificationMarkSheet(markSheet.getMarkSheetState())) {
                numberOfEnroledStudents += markSheet.getEnrolmentEvaluationsCount();
            }
        }
        return numberOfEnroledStudents;
    }

    private boolean isRectificationMarkSheet(MarkSheetState markSheetState) {
        return (markSheetState == MarkSheetState.RECTIFICATION || markSheetState == MarkSheetState.RECTIFICATION_NOT_CONFIRMED);
    }

    public int getTotalNumberOfStudents() {
        return totalNumberOfStudents;
    }

    public void setTotalNumberOfStudents(int totalNumberStudents) {
        this.totalNumberOfStudents = totalNumberStudents;
    }

}
