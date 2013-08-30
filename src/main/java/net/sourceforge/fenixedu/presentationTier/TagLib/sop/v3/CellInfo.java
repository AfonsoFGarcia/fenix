package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3;

import java.util.Iterator;
import java.util.LinkedList;

import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;

public class CellInfo {

    private LinkedList lessonViewList;

    // cuidado ao modificar os constructores, olhar para o getContent
    /**
     * 
     * @deprecated
     */
    @Deprecated
    public CellInfo(int rowSpan, int colSpan, InfoLesson lessonView) {
    }

    /**
     * 
     * @param rowSpan
     * @param lessonView
     * @deprecated
     */
    @Deprecated
    public CellInfo(int rowSpan, InfoLesson lessonView) {
        this(rowSpan, 0, lessonView);
    }

    public CellInfo() {
        this.lessonViewList = new LinkedList();
    }

    public void addLessonView(InfoLesson lessonView) {
        this.lessonViewList.add(lessonView);
    }

    public String getContent() {
        StringBuilder buffer = new StringBuilder("");

        if (lessonViewList.isEmpty()) {
            buffer = buffer.append("&nbsp;");
        } else {
            Iterator iterator = this.lessonViewList.iterator();
            while (iterator.hasNext()) {

                InfoLesson infoLesson = (InfoLesson) iterator.next();

                buffer = buffer.append(infoLesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());
                buffer = buffer.append(" (").append(infoLesson.getInfoShift().getShiftTypesCodePrettyPrint());
                buffer = buffer.append(") ");
                final AllocatableSpace allocatableSpace = infoLesson.getAllocatableSpace();
                if (allocatableSpace != null) {
                    buffer = buffer.append(allocatableSpace.getNome());
                }
                if (iterator.hasNext()) {
                    buffer.append("<br/>");
                }
            }
        }
        return buffer.toString();
    }

}