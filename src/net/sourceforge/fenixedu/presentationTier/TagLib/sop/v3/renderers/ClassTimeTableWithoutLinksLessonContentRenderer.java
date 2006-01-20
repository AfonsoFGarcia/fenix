package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenTest;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlot;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlotContentRenderer;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ClassTimeTableWithoutLinksLessonContentRenderer implements LessonSlotContentRenderer {

    /**
     * @see ServidorApresentacao.TagLib.sop.v3.LessonSlotContentRenderer#render(ServidorApresentacao.TagLib.sop.v3.LessonSlot)
     */
    public StringBuilder render(LessonSlot lessonSlot) {
        StringBuilder strBuffer = new StringBuilder();
        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();

        if (showOccupation instanceof InfoLesson) {
            InfoLesson lesson = (InfoLesson) showOccupation;
            if (lessonSlot.isSinleSlot() || !lessonSlot.getInfoLessonWrapper().isFirstRowAlreadyAppended()) {
                strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());
            }

            if (lessonSlot.isSinleSlot()) {
                strBuffer.append("<br/>");
            }

            if (lessonSlot.isSinleSlot() || (lessonSlot.getInfoLessonWrapper().isFirstRowAlreadyAppended() && !lessonSlot.getInfoLessonWrapper().isSecondRowAlreadyAppended())) {
                
                List infoShiftList = lesson.getInfoShiftList();
                for (int index = 0; index < infoShiftList.size(); index++) {
                    strBuffer.append("(").append(lesson.getTipo().getSiglaTipoAula()).append(")&nbsp;");
                }

                strBuffer.append(lesson.getInfoRoomOccupation().getInfoRoom().getNome());

                if (lesson.getInfoRoomOccupation().getFrequency() != null
                        && lesson.getInfoRoomOccupation().getFrequency().intValue() == RoomOccupation.QUINZENAL) {
                    strBuffer.append("&nbsp;&nbsp;[Q]");
                }
            }

            if (lessonSlot.isSinleSlot() || (lessonSlot.getInfoLessonWrapper().isFirstRowAlreadyAppended() && !lessonSlot.getInfoLessonWrapper().isSecondRowAlreadyAppended())) {
                lessonSlot.getInfoLessonWrapper().setSecondRowAlreadyAppended(true);
            }

            if (lessonSlot.isSinleSlot() || !lessonSlot.getInfoLessonWrapper().isFirstRowAlreadyAppended()) {
                lessonSlot.getInfoLessonWrapper().setFirstRowAlreadyAppended(true);
            }
        } else if (showOccupation instanceof InfoExam) {
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
        } else if (showOccupation instanceof InfoWrittenTest) {
            InfoWrittenTest infoWrittenTest = (InfoWrittenTest) showOccupation;
            for (int iterEC = 0; iterEC < infoWrittenTest.getAssociatedExecutionCourse().size(); iterEC++) {
                InfoExecutionCourse infoEC = (InfoExecutionCourse) infoWrittenTest.getAssociatedExecutionCourse().get(iterEC);
                if (iterEC != 0) {
                    strBuffer.append(", ");
                }
                strBuffer.append(infoEC.getSigla());
            }
            strBuffer.append(" - ");
            strBuffer.append(infoWrittenTest.getDescription());
        }

        return strBuffer;
    }
}