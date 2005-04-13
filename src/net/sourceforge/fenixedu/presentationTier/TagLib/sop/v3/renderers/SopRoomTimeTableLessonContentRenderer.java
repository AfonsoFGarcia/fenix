package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers;

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.domain.RoomOccupation;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlot;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlotContentRenderer;

/**
 * @author Nuno Nunes
 */
public class SopRoomTimeTableLessonContentRenderer implements LessonSlotContentRenderer {

    /**
     * @see ServidorApresentacao.TagLib.sop.v3.LessonSlotContentRenderer#render(ServidorApresentacao.TagLib.sop.v3.LessonSlot)
     */
    public StringBuffer render(LessonSlot lessonSlot) {
        StringBuffer strBuffer = new StringBuffer();
        //InfoLesson lesson =
        // lessonSlot.getInfoLessonWrapper().getInfoLesson();
        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();

        if (showOccupation instanceof InfoLesson) {
            InfoLesson lesson = (InfoLesson) showOccupation;
            strBuffer.append("<a href='manageExecutionCourse.do?method=prepare&amp;page=0").append(
                    "&amp;" + SessionConstants.EXECUTION_PERIOD_OID + "=").append(
                    lesson.getInfoShift().getInfoDisciplinaExecucao().getInfoExecutionPeriod()
                            .getIdInternal()).append("&amp;execution_course_oid=").append(
                    lesson.getInfoShift().getInfoDisciplinaExecucao().getIdInternal()).append("'>")
                    .append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla()).append("</a>");

            // Note : A link to shift cannot be used because within the SOP
            //        interface, shifts are viewed in a curricular year and
            //        an execution degree context. View room occupation does
            //        NOT contain this context, and therefor the jump cannot
            //        be made.
            strBuffer.append("&nbsp;(").append(lesson.getTipo()).append(")");

            //TODO(rspl): Will it stay like this the interface for showing
            // it is a quinzenal lesson?
            if (lesson.getInfoRoomOccupation().getFrequency() != null && lesson.getInfoRoomOccupation().getFrequency().intValue() == RoomOccupation.QUINZENAL) {
                strBuffer.append("&nbsp;&nbsp;[Q]");
            }
        } else {
            System.out.println("the else");
            InfoExam infoExam = (InfoExam) showOccupation;
            for (int iterEC = 0; iterEC < infoExam.getAssociatedExecutionCourse().size(); iterEC++) {
                InfoExecutionCourse infoEC = (InfoExecutionCourse) infoExam
                        .getAssociatedExecutionCourse().get(iterEC);
                if (iterEC != 0) {
                    strBuffer.append(", ");
                }
                strBuffer.append(infoEC.getSigla());

            }
            strBuffer.append(" - ");
            strBuffer.append(infoExam.getSeason().getSeason());
            strBuffer.append("� �poca");
        }

        return strBuffer;
    }
}