/*
 * Created on 18:52:01,20/Out/2004
 *
 * by gedl@rnl.ist.utl.pt
 */
package ServidorApresentacao.TagLib.sop.v3;

import java.util.List;

import DataBeans.InfoLesson;
import DataBeans.InfoShowOccupation;

/**
 * @author gedl@rnl.ist.utl.pt
 * 
 * 18:52:01,20/Out/2004
 */
public class ClassTimeTableWithLinksLessonContentRenderer implements LessonSlotContentRenderer {
    private String application;

    /**
     * @param application
     */
    public ClassTimeTableWithLinksLessonContentRenderer(String application) {
        setApplication(application);
    }

    public StringBuffer render(LessonSlot lessonSlot) {
        StringBuffer strBuffer = new StringBuffer();
        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();
        if (showOccupation instanceof InfoLesson) {
            InfoLesson lesson = (InfoLesson) showOccupation;
            strBuffer.append("<a href=\"");
            strBuffer.append(getApplication());
            strBuffer.append("/publico/viewSiteExecutionCourse.do?method=firstPage&objectCode=");
            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getIdInternal()).append(
                    "\">");
            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla())
                    .append("</a>");
            ;

            strBuffer.append("&nbsp;");
            List infoShiftList = lesson.getInfoShiftList();
            for (int index = 0; index < infoShiftList.size(); index++) {
                strBuffer.append("&nbsp;(").append(lesson.getTipo()).append(")&nbsp;");
            }

            strBuffer.append(lesson.getInfoRoomOccupation().getInfoRoom().getNome());
            return strBuffer;
        }
        return new StringBuffer("");
    }

    /**
     * @return Returns the application.
     */
    public String getApplication() {
        return application;
    }

    /**
     * @param application
     *            The application to set.
     */
    public void setApplication(String application) {
        this.application = application;
    }
}

